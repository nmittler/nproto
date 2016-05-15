package io.nproto.schema.gen;

import static io.nproto.UnsafeUtil.fieldOffset;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;
import static org.objectweb.asm.Type.LONG_TYPE;
import static org.objectweb.asm.Type.getInternalName;

import io.nproto.FieldType;
import io.nproto.Internal;
import io.nproto.Writer;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;
import io.nproto.schema.SchemaUtil;
import io.nproto.schema.SchemaUtil.FieldInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Modifier;
import java.util.List;

@Internal
public class AsmSchemaFactory implements SchemaFactory {
  private static final String SCHEMA_INTERNAL_NAME = getInternalName(Schema.class);
  private static final String WRITER_INTERNAL_NAME = getInternalName(Writer.class);
  private static final String SCHEMAUTIL_INTERNAL_NAME = getInternalName(SchemaUtil.class);
  private static final Type ENUM_TYPE = Type.getType(Enum.class);
  private static final String WRITETO_DESCRIPTOR = String.format("(Ljava/lang/Object;L%s;)V",
          WRITER_INTERNAL_NAME);

  private static final GeneratedClassLoader CL =
          new GeneratedClassLoader(AsmSchemaFactory.class.getClassLoader());

  @Override
  public <T> Schema<T> createSchema(final Class<T> messageType) {

    try {
      @SuppressWarnings("unchecked")
      Class<Schema<T>> newClass = (Class<Schema<T>>) CL.defineClass(createSchemaClass(messageType));
      return newClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> byte[] createSchemaClass(final Class<T> messageType) {
    if (messageType.isInterface() || Modifier.isAbstract(messageType.getModifiers())) {
      throw new RuntimeException(
              "The root object can neither be an abstract "
                      + "class nor interface: \"" + messageType.getName());
    }

    ClassWriter cv = new ClassWriter(0);
    //ClassVisitor cv = new CheckClassAdapter(writer);
    final String className = getSchemaClassName(messageType);
    cv.visit(V1_6, ACC_PUBLIC + ACC_FINAL, className, null, "java/lang/Object",
            new String[]{SCHEMA_INTERNAL_NAME});
    {
      // Generate the constructor
      MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "<init>",
              "()V", null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
      mv.visitInsn(RETURN);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    }

    // Generate the public writeTo(T, Writer) method.
    MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "writeTo", WRITETO_DESCRIPTOR, null, null);
    mv.visitCode();
    List<FieldInfo> fields = SchemaUtil.getAllFieldInfo(messageType);
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0; i < fields.size(); ++i) {
      FieldInfo f = fields.get(i);
      if (lastFieldNumber == f.fieldNumber) {
        // Disallow duplicate field numbers.
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      lastFieldNumber = f.fieldNumber;

      String offsetFieldName = f.field.getName().toUpperCase() + "_OFFSET";

      // Generate the static variable that holds the unsafe offset for this field.
      generateOffsetField(cv, offsetFieldName, fieldOffset(f.field));

      // Generate the writeTo code for this field.
      writeField(mv, className, offsetFieldName, f.fieldNumber, f.fieldType);
    }
    mv.visitInsn(RETURN);
    mv.visitMaxs(7, 3);
    mv.visitEnd();

    // Complete the generation of the class and return a new instance.
    cv.visitEnd();
    return cv.toByteArray();
  }

  private static void generateOffsetField(ClassVisitor cv, String name, long value) {
    // Generate the static offset field.
    cv.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, name,
            LONG_TYPE.getDescriptor(), null, value);
  }

  private static void writeField(MethodVisitor mv,
                                 String className,
                                 String offsetFieldName,
                                 int fieldNumber,
                                 FieldType fieldType) {
    switch (fieldType) {
      case DOUBLE:
        unsafeWrite(mv, "unsafeWriteDouble", fieldNumber, className, offsetFieldName);
        break;
      case FLOAT:
        unsafeWrite(mv, "unsafeWriteFloat", fieldNumber, className, offsetFieldName);
        break;
      case INT64:
        unsafeWrite(mv, "unsafeWriteInt64", fieldNumber, className, offsetFieldName);
        break;
      case UINT64:
        unsafeWrite(mv, "unsafeWriteUInt64", fieldNumber, className, offsetFieldName);
        break;
      case INT32:
        unsafeWrite(mv, "unsafeWriteInt32", fieldNumber, className, offsetFieldName);
        break;
      case FIXED64:
        unsafeWrite(mv, "unsafeWriteFixed64", fieldNumber, className, offsetFieldName);
        break;
      case FIXED32:
        unsafeWrite(mv, "unsafeWriteFixed32", fieldNumber, className, offsetFieldName);
        break;
      case BOOL:
        unsafeWrite(mv, "unsafeWriteBool", fieldNumber, className, offsetFieldName);
        break;
      case STRING:
        unsafeWrite(mv, "unsafeWriteString", fieldNumber, className, offsetFieldName);
        break;
      case MESSAGE:
        unsafeWrite(mv, "unsafeWriteMessage", fieldNumber, className, offsetFieldName);
        break;
      case BYTES:
        unsafeWrite(mv, "unsafeWriteBytes", fieldNumber, className, offsetFieldName);
        break;
      case UINT32:
        unsafeWrite(mv, "unsafeWriteUInt32", fieldNumber, className, offsetFieldName);
        break;
      case ENUM:
        unsafeWriteEnum(mv, fieldNumber, className, offsetFieldName);
        break;
      case SFIXED32:
        unsafeWrite(mv, "unsafeWriteSFixed32", fieldNumber, className, offsetFieldName);
        break;
      case SFIXED64:
        unsafeWrite(mv, "unsafeWriteSFixed64", fieldNumber, className, offsetFieldName);
        break;
      case SINT32:
        unsafeWrite(mv, "unsafeWriteSInt32", fieldNumber, className, offsetFieldName);
        break;
      case SINT64:
        unsafeWrite(mv, "unsafeWriteSInt64", fieldNumber, className, offsetFieldName);
        break;
      case DOUBLE_LIST:
        unsafeWriteList(mv, "unsafeWriteDoubleList", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_DOUBLE_LIST:
        unsafeWriteList(mv, "unsafeWriteDoubleList", fieldNumber, className, offsetFieldName, true);
        break;
      case FLOAT_LIST:
        unsafeWriteList(mv, "unsafeWriteFloatList", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_FLOAT_LIST:
        unsafeWriteList(mv, "unsafeWriteFloatList", fieldNumber, className, offsetFieldName, true);
        break;
      case INT64_LIST:
        unsafeWriteList(mv, "unsafeWriteInt64List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_INT64_LIST:
        unsafeWriteList(mv, "unsafeWriteInt64List", fieldNumber, className, offsetFieldName, true);
        break;
      case UINT64_LIST:
        unsafeWriteList(mv, "unsafeWriteUInt64List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_UINT64_LIST:
        unsafeWriteList(mv, "unsafeWriteUInt64List", fieldNumber, className, offsetFieldName, true);
        break;
      case INT32_LIST:
        unsafeWriteList(mv, "unsafeWriteInt32List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_INT32_LIST:
        unsafeWriteList(mv, "unsafeWriteInt32List", fieldNumber, className, offsetFieldName, true);
        break;
      case FIXED64_LIST:
        unsafeWriteList(mv, "unsafeWriteFixed64List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_FIXED64_LIST:
        unsafeWriteList(mv, "unsafeWriteFixed64List", fieldNumber, className, offsetFieldName, true);
        break;
      case FIXED32_LIST:
        unsafeWriteList(mv, "unsafeWriteFixed32List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_FIXED32_LIST:
        unsafeWriteList(mv, "unsafeWriteFixed32List", fieldNumber, className, offsetFieldName, true);
        break;
      case BOOL_LIST:
        unsafeWriteList(mv, "unsafeWriteBoolList", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_BOOL_LIST:
        unsafeWriteList(mv, "unsafeWriteBoolList", fieldNumber, className, offsetFieldName, true);
        break;
      case STRING_LIST:
        unsafeWrite(mv, "unsafeWriteStringList", fieldNumber, className, offsetFieldName);
        break;
      case MESSAGE_LIST:
        unsafeWrite(mv, "unsafeWriteMessageList", fieldNumber, className, offsetFieldName);
        break;
      case BYTES_LIST:
        unsafeWrite(mv, "unsafeWriteBytesList", fieldNumber, className, offsetFieldName);
        break;
      case UINT32_LIST:
        unsafeWriteList(mv, "unsafeWriteUInt32List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_UINT32_LIST:
        unsafeWriteList(mv, "unsafeWriteUInt32List", fieldNumber, className, offsetFieldName, true);
        break;
      case ENUM_LIST:
        unsafeWriteEnumList(mv, fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_ENUM_LIST:
        unsafeWriteEnumList(mv, fieldNumber, className, offsetFieldName, true);
        break;
      case SFIXED32_LIST:
        unsafeWriteList(mv, "unsafeWriteSFixed32List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_SFIXED32_LIST:
        unsafeWriteList(mv, "unsafeWriteSFixed32List", fieldNumber, className, offsetFieldName, true);
        break;
      case SFIXED64_LIST:
        unsafeWriteList(mv, "unsafeWriteSFixed64List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_SFIXED64_LIST:
        unsafeWriteList(mv, "unsafeWriteSFixed64List", fieldNumber, className, offsetFieldName, true);
        break;
      case SINT32_LIST:
        unsafeWriteList(mv, "unsafeWriteSInt32List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_SINT32_LIST:
        unsafeWriteList(mv, "unsafeWriteSInt32List", fieldNumber, className, offsetFieldName, true);
        break;
      case SINT64_LIST:
        unsafeWriteList(mv, "unsafeWriteSInt64List", fieldNumber, className, offsetFieldName, false);
        break;
      case PACKED_SINT64_LIST:
        unsafeWriteList(mv, "unsafeWriteSInt64List", fieldNumber, className, offsetFieldName, true);
        break;
    }
  }

  private static void unsafeWrite(MethodVisitor mv, String methodName, int fieldNumber, String className, String offsetFieldName) {
    final int messageIndex = 1;
    final int writerIndex = 2;
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
            "(ILjava/lang/Object;JLio/nproto/Writer;)V", false);
  }

  private static void unsafeWriteEnum(MethodVisitor mv, int fieldNumber, String className, String offsetFieldName) {
    final int messageIndex = 1;
    final int writerIndex = 2;
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(ENUM_TYPE);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, "unsafeWriteEnum",
            "(ILjava/lang/Object;JLio/nproto/Writer;Ljava/lang/Class;)V", false);
  }

  private static void unsafeWriteList(MethodVisitor mv, String methodName, int fieldNumber, String className, String offsetFieldName, boolean packed) {
    final int messageIndex = 1;
    final int writerIndex = 2;
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitInsn(packed ? ICONST_1 : ICONST_0);
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
            "(ILjava/lang/Object;JZLio/nproto/Writer;)V", false);
  }

  private static void unsafeWriteEnumList(MethodVisitor mv, int fieldNumber, String className, String offsetFieldName, boolean packed) {
    final int messageIndex = 1;
    final int writerIndex = 2;
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitInsn(packed ? ICONST_1 : ICONST_0);
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(ENUM_TYPE);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, "unsafeWriteEnumList",
            "(ILjava/lang/Object;JZLio/nproto/Writer;Ljava/lang/Class;)V", false);
  }

  private static String getSchemaClassName(Class<?> messageType) {
    return AsmSchemaFactory.class.getPackage().getName().replace('.', '/') + '/' +
            messageType.getSimpleName() + "Schema";
  }

  private static final class GeneratedClassLoader extends ClassLoader {
    GeneratedClassLoader(ClassLoader parent) {
      super(parent);
    }

    Class<?> defineClass(byte[] bytes) {
      return defineClass(null, bytes, 0, bytes.length);
    }
  }
}
