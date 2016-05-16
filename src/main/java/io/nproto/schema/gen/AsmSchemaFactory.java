package io.nproto.schema.gen;

import static io.nproto.UnsafeUtil.fieldOffset;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;
import static org.objectweb.asm.Type.getInternalName;

import io.nproto.FieldType;
import io.nproto.Internal;
import io.nproto.JavaType;
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
public final class AsmSchemaFactory implements SchemaFactory {
  private static final String SCHEMA_INTERNAL_NAME = getInternalName(Schema.class);
  private static final String WRITER_INTERNAL_NAME = getInternalName(Writer.class);
  private static final String SCHEMAUTIL_INTERNAL_NAME = getInternalName(SchemaUtil.class);
  private static final Type ENUM_TYPE = Type.getType(Enum.class);
  private static final String WRITETO_DESCRIPTOR = String.format("(Ljava/lang/Object;L%s;)V",
          WRITER_INTERNAL_NAME);
  private static final int MESSAGE_INDEX = 1;
  private static final int WRITER_INDEX = 2;
  private static final FieldWriter[] FIELD_WRITERS;
  static {
    FieldType[] fieldTypes = FieldType.values();
    FIELD_WRITERS = new FieldWriter[fieldTypes.length];
    for (int i = 0; i < fieldTypes.length; ++i) {
      FIELD_WRITERS[i] = new FieldWriter(fieldTypes[i]);
    }
  }

  private static final GeneratedClassLoader CL =
          new GeneratedClassLoader(AsmSchemaFactory.class.getClassLoader());

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {

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

  public static <T> byte[] createSchemaClass(Class<T> messageType) {
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
    generateConstructor(cv);
    generateWriteTo(cv, messageType);

    // Complete the generation of the class and return a new instance.
    cv.visitEnd();
    return cv.toByteArray();
  }

  private static void generateConstructor(ClassVisitor cv) {
    MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    mv.visitInsn(RETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }

  private static <T> void generateWriteTo(ClassVisitor cv, Class<T> messageType) {
    final List<FieldInfo> fields = SchemaUtil.getAllFieldInfo(messageType);
    final int numFields = fields.size();

    final MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "writeTo", WRITETO_DESCRIPTOR, null, null);
    mv.visitCode();
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0; i < numFields; ++i) {
      FieldInfo f = fields.get(i);
      if (lastFieldNumber == f.fieldNumber) {
        // Disallow duplicate field numbers.
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      lastFieldNumber = f.fieldNumber;

      // Generate the writeTo code for this field.
      FIELD_WRITERS[f.fieldType.ordinal()].write(mv, f.fieldNumber, fieldOffset(f.field));
      //writeField(mv, f.fieldNumber, f.fieldType, fieldOffset(f.field));
    }
    mv.visitInsn(RETURN);
    mv.visitMaxs(7, 3);
    mv.visitEnd();
  }

  /*private static void writeField(MethodVisitor mv, int fieldNumber, FieldType fieldType, long offset) {
    switch (fieldType) {
      case DOUBLE:
        unsafeWrite(mv, "unsafeWriteDouble", fieldNumber, offset);
        break;
      case FLOAT:
        unsafeWrite(mv, "unsafeWriteFloat", fieldNumber, offset);
        break;
      case INT64:
        unsafeWrite(mv, "unsafeWriteInt64", fieldNumber, offset);
        break;
      case UINT64:
        unsafeWrite(mv, "unsafeWriteUInt64", fieldNumber, offset);
        break;
      case INT32:
        unsafeWrite(mv, "unsafeWriteInt32", fieldNumber, offset);
        break;
      case FIXED64:
        unsafeWrite(mv, "unsafeWriteFixed64", fieldNumber, offset);
        break;
      case FIXED32:
        unsafeWrite(mv, "unsafeWriteFixed32", fieldNumber, offset);
        break;
      case BOOL:
        unsafeWrite(mv, "unsafeWriteBool", fieldNumber, offset);
        break;
      case STRING:
        unsafeWrite(mv, "unsafeWriteString", fieldNumber, offset);
        break;
      case MESSAGE:
        unsafeWrite(mv, "unsafeWriteMessage", fieldNumber, offset);
        break;
      case BYTES:
        unsafeWrite(mv, "unsafeWriteBytes", fieldNumber, offset);
        break;
      case UINT32:
        unsafeWrite(mv, "unsafeWriteUInt32", fieldNumber, offset);
        break;
      case ENUM:
        unsafeWriteEnum(mv, fieldNumber, offset);
        break;
      case SFIXED32:
        unsafeWrite(mv, "unsafeWriteSFixed32", fieldNumber, offset);
        break;
      case SFIXED64:
        unsafeWrite(mv, "unsafeWriteSFixed64", fieldNumber, offset);
        break;
      case SINT32:
        unsafeWrite(mv, "unsafeWriteSInt32", fieldNumber, offset);
        break;
      case SINT64:
        unsafeWrite(mv, "unsafeWriteSInt64", fieldNumber, offset);
        break;
      case DOUBLE_LIST:
        unsafeWriteList(mv, "unsafeWriteDoubleList", fieldNumber, offset);
        break;
      case PACKED_DOUBLE_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteDoubleList", fieldNumber, offset);
        break;
      case FLOAT_LIST:
        unsafeWriteList(mv, "unsafeWriteFloatList", fieldNumber, offset);
        break;
      case PACKED_FLOAT_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteFloatList", fieldNumber, offset);
        break;
      case INT64_LIST:
        unsafeWriteList(mv, "unsafeWriteInt64List", fieldNumber, offset);
        break;
      case PACKED_INT64_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteInt64List", fieldNumber, offset);
        break;
      case UINT64_LIST:
        unsafeWriteList(mv, "unsafeWriteUInt64List", fieldNumber, offset);
        break;
      case PACKED_UINT64_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteUInt64List", fieldNumber, offset);
        break;
      case INT32_LIST:
        unsafeWriteList(mv, "unsafeWriteInt32List", fieldNumber, offset);
        break;
      case PACKED_INT32_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteInt32List", fieldNumber, offset);
        break;
      case FIXED64_LIST:
        unsafeWriteList(mv, "unsafeWriteFixed64List", fieldNumber, offset);
        break;
      case PACKED_FIXED64_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteFixed64List", fieldNumber, offset);
        break;
      case FIXED32_LIST:
        unsafeWriteList(mv, "unsafeWriteFixed32List", fieldNumber, offset);
        break;
      case PACKED_FIXED32_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteFixed32List", fieldNumber, offset);
        break;
      case BOOL_LIST:
        unsafeWriteList(mv, "unsafeWriteBoolList", fieldNumber, offset);
        break;
      case PACKED_BOOL_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteBoolList", fieldNumber, offset);
        break;
      case STRING_LIST:
        unsafeWrite(mv, "unsafeWriteStringList", fieldNumber, offset);
        break;
      case MESSAGE_LIST:
        unsafeWrite(mv, "unsafeWriteMessageList", fieldNumber, offset);
        break;
      case BYTES_LIST:
        unsafeWrite(mv, "unsafeWriteBytesList", fieldNumber, offset);
        break;
      case UINT32_LIST:
        unsafeWriteList(mv, "unsafeWriteUInt32List", fieldNumber, offset);
        break;
      case PACKED_UINT32_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteUInt32List", fieldNumber, offset);
        break;
      case ENUM_LIST:
        unsafeWriteEnumList(mv, fieldNumber, offset);
        break;
      case PACKED_ENUM_LIST:
        unsafeWriteEnumListPacked(mv, fieldNumber, offset);
        break;
      case SFIXED32_LIST:
        unsafeWriteList(mv, "unsafeWriteSFixed32List", fieldNumber, offset);
        break;
      case PACKED_SFIXED32_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteSFixed32List", fieldNumber, offset);
        break;
      case SFIXED64_LIST:
        unsafeWriteList(mv, "unsafeWriteSFixed64List", fieldNumber, offset);
        break;
      case PACKED_SFIXED64_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteSFixed64List", fieldNumber, offset);
        break;
      case SINT32_LIST:
        unsafeWriteList(mv, "unsafeWriteSInt32List", fieldNumber, offset);
        break;
      case PACKED_SINT32_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteSInt32List", fieldNumber, offset);
        break;
      case SINT64_LIST:
        unsafeWriteList(mv, "unsafeWriteSInt64List", fieldNumber, offset);
        break;
      case PACKED_SINT64_LIST:
        unsafeWriteListPacked(mv, "unsafeWriteSInt64List", fieldNumber, offset);
        break;
    }
  }

  private static void unsafeWrite(MethodVisitor mv, String methodName, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
            "(ILjava/lang/Object;JLio/nproto/Writer;)V", false);
  }

  private static void unsafeWriteEnum(MethodVisitor mv, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitLdcInsn(ENUM_TYPE);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, "unsafeWriteEnum",
            "(ILjava/lang/Object;JLio/nproto/Writer;Ljava/lang/Class;)V", false);
  }

  private static void unsafeWriteList(MethodVisitor mv, String methodName, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitInsn(ICONST_0);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
            "(ILjava/lang/Object;JZLio/nproto/Writer;)V", false);
  }

  private static void unsafeWriteListPacked(MethodVisitor mv, String methodName, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitInsn(ICONST_1);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
            "(ILjava/lang/Object;JZLio/nproto/Writer;)V", false);
  }

  private static void unsafeWriteEnumList(MethodVisitor mv, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitInsn(ICONST_0);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitLdcInsn(ENUM_TYPE);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, "unsafeWriteEnumList",
            "(ILjava/lang/Object;JZLio/nproto/Writer;Ljava/lang/Class;)V", false);
  }

  private static void unsafeWriteEnumListPacked(MethodVisitor mv, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitInsn(ICONST_1);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitLdcInsn(ENUM_TYPE);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, "unsafeWriteEnumList",
            "(ILjava/lang/Object;JZLio/nproto/Writer;Ljava/lang/Class;)V", false);
  }*/

  private static String getSchemaClassName(Class<?> messageType) {
    return messageType.getName().replace('.', '/') + "Schema";
  }

  private static final class GeneratedClassLoader extends ClassLoader {
    GeneratedClassLoader(ClassLoader parent) {
      super(parent);
    }

    Class<?> defineClass(byte[] bytes) {
      return defineClass(null, bytes, 0, bytes.length);
    }
  }

  private static final class FieldWriter {
    private final FieldType fieldType;
    private final String methodName;
    private final WriteType writeType;

    private enum WriteType {
      STANDARD,
      ENUM,
      ENUM_LIST,
      LIST
    }

    FieldWriter(FieldType fieldType) {
      this.fieldType = fieldType;
      JavaType jtype = fieldType.getJavaType();
      WriteType writeType = (jtype == JavaType.LIST) ?
              WriteType.LIST : (jtype == JavaType.ENUM) ? WriteType.ENUM : WriteType.STANDARD;
      switch (fieldType) {
        case DOUBLE:
          methodName = "unsafeWriteDouble";
          break;
        case FLOAT:
          methodName = "unsafeWriteFloat";
          break;
        case INT64:
          methodName = "unsafeWriteInt64";
          break;
        case UINT64:
          methodName = "unsafeWriteUInt64";
          break;
        case INT32:
          methodName = "unsafeWriteInt32";
          break;
        case FIXED64:
          methodName = "unsafeWriteFixed64";
          break;
        case FIXED32:
          methodName = "unsafeWriteFixed32";
          break;
        case BOOL:
          methodName = "unsafeWriteBool";
          break;
        case STRING:
          methodName = "unsafeWriteString";
          break;
        case MESSAGE:
          methodName = "unsafeWriteMessage";
          break;
        case BYTES:
          methodName = "unsafeWriteBytes";
          break;
        case UINT32:
          methodName = "unsafeWriteUInt32";
          break;
        case ENUM:
          methodName = "unsafeWriteEnum";
          break;
        case SFIXED32:
          methodName = "unsafeWriteSFixed32";
          break;
        case SFIXED64:
          methodName = "unsafeWriteSFixed64";
          break;
        case SINT32:
          methodName = "unsafeWriteSInt32";
          break;
        case SINT64:
          methodName = "unsafeWriteSInt64";
          break;
        case DOUBLE_LIST:
          methodName = "unsafeWriteDoubleList";
          break;
        case PACKED_DOUBLE_LIST:
          methodName = "unsafeWriteDoubleList";
          break;
        case FLOAT_LIST:
          methodName = "unsafeWriteFloatList";
          break;
        case PACKED_FLOAT_LIST:
          methodName = "unsafeWriteFloatList";
          break;
        case INT64_LIST:
          methodName = "unsafeWriteInt64List";
          break;
        case PACKED_INT64_LIST:
          methodName = "unsafeWriteInt64List";
          break;
        case UINT64_LIST:
          methodName = "unsafeWriteUInt64List";
          break;
        case PACKED_UINT64_LIST:
          methodName = "unsafeWriteUInt64List";
          break;
        case INT32_LIST:
          methodName = "unsafeWriteInt32List";
          break;
        case PACKED_INT32_LIST:
          methodName = "unsafeWriteInt32List";
          break;
        case FIXED64_LIST:
          methodName = "unsafeWriteFixed64List";
          break;
        case PACKED_FIXED64_LIST:
          methodName = "unsafeWriteFixed64List";
          break;
        case FIXED32_LIST:
          methodName = "unsafeWriteFixed32List";
          break;
        case PACKED_FIXED32_LIST:
          methodName = "unsafeWriteFixed32List";
          break;
        case BOOL_LIST:
          methodName = "unsafeWriteBoolList";
          break;
        case PACKED_BOOL_LIST:
          methodName = "unsafeWriteBoolList";
          break;
        case STRING_LIST:
          methodName = "unsafeWriteStringList";
          break;
        case MESSAGE_LIST:
          methodName = "unsafeWriteMessageList";
          break;
        case BYTES_LIST:
          methodName = "unsafeWriteBytesList";
          break;
        case UINT32_LIST:
          methodName = "unsafeWriteUInt32List";
          break;
        case PACKED_UINT32_LIST:
          methodName = "unsafeWriteUInt32List";
          break;
        case ENUM_LIST:
          methodName = "unsafeWriteEnumList";
          writeType = WriteType.ENUM_LIST;
          break;
        case PACKED_ENUM_LIST:
          methodName = "unsafeWriteEnumList";
          writeType = WriteType.ENUM_LIST;
          break;
        case SFIXED32_LIST:
          methodName = "unsafeWriteSFixed32List";
          break;
        case PACKED_SFIXED32_LIST:
          methodName = "unsafeWriteSFixed32List";
          break;
        case SFIXED64_LIST:
          methodName = "unsafeWriteSFixed64List";
          break;
        case PACKED_SFIXED64_LIST:
          methodName = "unsafeWriteSFixed64List";
          break;
        case SINT32_LIST:
          methodName = "unsafeWriteSInt32List";
          break;
        case PACKED_SINT32_LIST:
          methodName = "unsafeWriteSInt32List";
          break;
        case SINT64_LIST:
          methodName = "unsafeWriteSInt64List";
          break;
        case PACKED_SINT64_LIST:
          methodName = "unsafeWriteSInt64List";
          break;
        default:
          throw new IllegalArgumentException("Unsupported FieldType: " + fieldType);
      }
      this.writeType = writeType;
    }

    private void write(MethodVisitor mv, int fieldNumber, long offset) {
      switch (writeType) {
        case STANDARD:
          unsafeWrite(mv, fieldNumber, offset);
          break;
        case ENUM:
          unsafeWriteEnum(mv, fieldNumber, offset);
          break;
        case ENUM_LIST:
          unsafeWriteEnumList(mv, fieldNumber, offset);
          break;
        case LIST:
          unsafeWriteList(mv, fieldNumber, offset);
          break;
      }
    }

    private void unsafeWriteEnum(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
              "(ILjava/lang/Object;JLio/nproto/Writer;Ljava/lang/Class;)V", false);
    }

    private void unsafeWrite(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
              "(ILjava/lang/Object;JLio/nproto/Writer;)V", false);
    }

    private void unsafeWriteList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitInsn(fieldType.isPacked() ? ICONST_1 : ICONST_0);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
              "(ILjava/lang/Object;JZLio/nproto/Writer;)V", false);
    }

    private void unsafeWriteEnumList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitInsn(fieldType.isPacked() ? ICONST_1 : ICONST_0);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, methodName,
              "(ILjava/lang/Object;JZLio/nproto/Writer;Ljava/lang/Class;)V", false);
    }
  }
}
