package io.nproto.schema.asm;

import static io.nproto.UnsafeUtil.fieldOffset;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DCMPL;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DSTORE;
import static org.objectweb.asm.Opcodes.FCMPL;
import static org.objectweb.asm.Opcodes.FCONST_0;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.LCMP;
import static org.objectweb.asm.Opcodes.LCONST_0;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;
import static org.objectweb.asm.Type.LONG_TYPE;
import static org.objectweb.asm.Type.getInternalName;

import io.nproto.FieldType;
import io.nproto.Internal;
import io.nproto.UnsafeUtil;
import io.nproto.Writer;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;
import io.nproto.schema.SchemaUtil;
import io.nproto.schema.SchemaUtil.FieldInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Modifier;
import java.util.List;

@Internal
public class AsmSchemaFactory implements SchemaFactory {
  private static final String SCHEMA_INTERNAL_NAME = getInternalName(Schema.class);
  private static final String WRITER_INTERNAL_NAME = getInternalName(Writer.class);
  private static final String UNSAFEUTIL_INTERNAL_NAME = getInternalName(UnsafeUtil.class);
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
      String writeToMethodName = "_write_" + f.field.getName();

      // Generate the static variable that holds the unsafe offset for this field.
      generateOffsetField(cv, offsetFieldName, fieldOffset(f.field));

      // Generate a private writeTo method for this field.
      generateFieldWriteToMethod(cv, className, writeToMethodName, offsetFieldName, f.fieldNumber,
              f.fieldType);

      // Now add a call to this field's writeTo method from the public writeTo method.
      callFieldWriteToMethod(mv, className, writeToMethodName);
    }
    mv.visitInsn(RETURN);
    mv.visitMaxs(3, 3);
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

  private static void generateFieldWriteToMethod(ClassVisitor cv,
                                                 String className,
                                                 String methodName,
                                                 String offsetFieldName,
                                                 int fieldNumber,
                                                 FieldType fieldType) {
    // Generate the writeTo method for this field.
    MethodVisitor mv = cv.visitMethod(ACC_PRIVATE, methodName, WRITETO_DESCRIPTOR, null, null);
    mv.visitCode();

    // Create the start and end labels for the branching logic.
    Label start = new Label();
    Label end = new Label();
    mv.visitLabel(start);

    // Indices for all local variables.
    final int messageIndex = 1;
    final int writerIndex = 2;
    final int valueIndex = 3;

    // Now get and write the value.
    switch (fieldType) {
      case DOUBLE:
        // Get the value from the message and store in a local variable.
        unsafeGetDouble(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_double(mv, end, valueIndex);

        // Write the value.
        prepareWriteDoubleValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeDouble", "(ID)V", true);
        break;
      case FLOAT:
        // Get the value from the message and store in a local variable.
        unsafeGetFloat(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_float(mv, end, valueIndex);

        // Write the value.
        prepareWriteFloatValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeFloat", "(IF)V", true);
        break;
      case INT64:
        // Get the value from the message and store in a local variable.
        unsafeGetLong(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_long(mv, end, valueIndex);

        // Write the value.
        prepareWriteLongValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeInt64", "(IJ)V", true);
        break;
      case UINT64:
        // Get the value from the message and store in a local variable.
        unsafeGetLong(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_long(mv, end, valueIndex);

        // Write the value.
        prepareWriteLongValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeUInt64", "(IJ)V", true);
        break;
      case INT32:
        // Get the value from the message and store in a local variable.
        unsafeGetInt(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_int(mv, end, valueIndex);

        // Write the value.
        prepareWriteIntValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeInt32", "(II)V", true);
        break;
      case FIXED64:
        // Get the value from the message and store in a local variable.
        unsafeGetLong(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_long(mv, end, valueIndex);

        // Write the value.
        prepareWriteLongValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeFixed64", "(IJ)V", true);
        break;
      case FIXED32:
        // Get the value from the message and store in a local variable.
        unsafeGetInt(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_int(mv, end, valueIndex);

        // Write the value.
        prepareWriteIntValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeFixed32", "(II)V", true);
        break;
      case BOOL:
        // Get the value from the message and store in a local variable.
        unsafeGetBool(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_int(mv, end, valueIndex);

        // Write the value.
        prepareWriteIntValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeBool", "(IZ)V", true);
        break;
      case STRING:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/lang/String", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteObjectValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeString", "(ILjava/lang/String;)V", true);
        break;
      case MESSAGE:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/lang/Object", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteObjectValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeMessage", "(ILjava/lang/Object;)V", true);
        break;
      case BYTES:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "io/nproto/ByteString", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteObjectValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeBytes", "(ILio/nproto/ByteString;)V", true);
        break;
      case UINT32:
        // Get the value from the message and store in a local variable.
        unsafeGetInt(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_int(mv, end, valueIndex);

        // Write the value.
        prepareWriteIntValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeUInt32", "(II)V", true);
        break;
      case ENUM:
        // Get the value from the message and store in a local variable.
        unsafeGetInt(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_int(mv, end, valueIndex);

        // Write the value.
        prepareWriteIntValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeEnum", "(II)V", true);
        break;
      case SFIXED32:
        // Get the value from the message and store in a local variable.
        unsafeGetInt(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_int(mv, end, valueIndex);

        // Write the value.
        prepareWriteIntValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSFixed32", "(II)V", true);
        break;
      case SFIXED64:
        // Get the value from the message and store in a local variable.
        unsafeGetLong(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_long(mv, end, valueIndex);

        // Write the value.
        prepareWriteLongValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSFixed64", "(IJ)V", true);
        break;
      case SINT32:
        // Get the value from the message and store in a local variable.
        unsafeGetInt(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_int(mv, end, valueIndex);

        // Write the value.
        prepareWriteIntValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSInt32", "(II)V", true);
        break;
      case SINT64:
        // Get the value from the message and store in a local variable.
        unsafeGetLong(mv, className, offsetFieldName, messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfZero_long(mv, end, valueIndex);

        // Write the value.
        prepareWriteLongValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSInt64", "(IJ)V", true);
        break;
      case DOUBLE_LIST:
      case PACKED_DOUBLE_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeDoubleList", "(IZLjava/util/List;)V", true);
        break;
      case FLOAT_LIST:
      case PACKED_FLOAT_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeFloatList", "(IZLjava/util/List;)V", true);
        break;
      case INT64_LIST:
      case PACKED_INT64_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeInt64List", "(IZLjava/util/List;)V", true);
        break;
      case UINT64_LIST:
      case PACKED_UINT64_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeUInt64List", "(IZLjava/util/List;)V", true);
        break;
      case INT32_LIST:
      case PACKED_INT32_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeInt32List", "(IZLjava/util/List;)V", true);
        break;
      case FIXED64_LIST:
      case PACKED_FIXED64_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeFixed64List", "(IZLjava/util/List;)V", true);
        break;
      case FIXED32_LIST:
      case PACKED_FIXED32_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeFixed32List", "(IZLjava/util/List;)V", true);
        break;
      case BOOL_LIST:
      case PACKED_BOOL_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeBoolList", "(IZLjava/util/List;)V", true);
        break;
      case STRING_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value (never packed).
        prepareWriteObjectValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeStringList", "(ILjava/util/List;)V", true);
        break;
      case MESSAGE_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value (never packed).
        prepareWriteObjectValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeMessageList", "(ILjava/util/List;)V", true);
        break;
      case BYTES_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value (never packed).
        prepareWriteObjectValue(mv, fieldNumber, writerIndex, valueIndex);
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeBytesList", "(ILjava/util/List;)V", true);
        break;
      case UINT32_LIST:
      case PACKED_UINT32_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeUInt32List", "(IZLjava/util/List;)V", true);
        break;
      case ENUM_LIST:
      case PACKED_ENUM_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeEnumList", "(IZLjava/util/List;)V", true);
        break;
      case SFIXED32_LIST:
      case PACKED_SFIXED32_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSFixed32List", "(IZLjava/util/List;)V", true);
        break;
      case SFIXED64_LIST:
      case PACKED_SFIXED64_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSFixed64List", "(IZLjava/util/List;)V", true);
        break;
      case SINT32_LIST:
      case PACKED_SINT32_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSInt32List", "(IZLjava/util/List;)V", true);
        break;
      case SINT64_LIST:
      case PACKED_SINT64_LIST:
        // Get the value from the message and store in a local variable.
        unsafeGetObject(mv, className, offsetFieldName, "java/util/List", messageIndex, valueIndex);

        // If the value is the default, don't write - just go to the end.
        jumpIfNull(mv, end, valueIndex);

        // Write the value.
        prepareWriteListValue(mv, fieldNumber, writerIndex, valueIndex, fieldType.isPacked());
        mv.visitMethodInsn(INVOKEINTERFACE, WRITER_INTERNAL_NAME, "writeSInt64List", "(IZLjava/util/List;)V", true);
        break;
    }
    mv.visitLabel(end);
    mv.visitInsn(RETURN);
    mv.visitMaxs(4, 5);
    mv.visitEnd();
  }

  private static void callFieldWriteToMethod(MethodVisitor mv, String className, String methodName) {
    mv.visitVarInsn(ALOAD, 0); // Push 'this'
    mv.visitVarInsn(ALOAD, 1); // Push the message
    mv.visitVarInsn(ALOAD, 2); // Push the writer
    mv.visitMethodInsn(INVOKESPECIAL, className, methodName, WRITETO_DESCRIPTOR, false);
  }

  private static void jumpIfZero_double(MethodVisitor mv, Label jumpTo, int valueIndex) {
    mv.visitInsn(DCONST_0);
    mv.visitVarInsn(DLOAD, valueIndex);
    mv.visitInsn(DCMPL);
    mv.visitJumpInsn(IFEQ, jumpTo);
  }

  private static void jumpIfZero_float(MethodVisitor mv, Label jumpTo, int valueIndex) {
    mv.visitInsn(FCONST_0);
    mv.visitVarInsn(FLOAD, valueIndex);
    mv.visitInsn(FCMPL);
    mv.visitJumpInsn(IFEQ, jumpTo);
  }

  private static void jumpIfZero_long(MethodVisitor mv, Label jumpTo, int valueIndex) {
    mv.visitInsn(LCONST_0);
    mv.visitVarInsn(LLOAD, valueIndex);
    mv.visitInsn(LCMP);
    mv.visitJumpInsn(IFEQ, jumpTo);
  }

  private static void jumpIfZero_int(MethodVisitor mv, Label jumpTo, int valueIndex) {
    mv.visitVarInsn(ILOAD, valueIndex);
    mv.visitJumpInsn(IFEQ, jumpTo);
  }

  private static void jumpIfNull(MethodVisitor mv, Label jumpTo, int valueIndex) {
    mv.visitVarInsn(ALOAD, valueIndex);
    mv.visitJumpInsn(IFNULL, jumpTo);
  }

  private static void unsafeGetDouble(MethodVisitor mv, String className, String offsetFieldName, int messageIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitMethodInsn(INVOKESTATIC, UNSAFEUTIL_INTERNAL_NAME, "getDouble", "(Ljava/lang/Object;J)D", false);
    mv.visitVarInsn(DSTORE, valueIndex);
  }

  private static void unsafeGetFloat(MethodVisitor mv, String className, String offsetFieldName, int messageIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitMethodInsn(INVOKESTATIC, UNSAFEUTIL_INTERNAL_NAME, "getFloat", "(Ljava/lang/Object;J)F", false);
    mv.visitVarInsn(FSTORE, valueIndex);
  }

  private static void unsafeGetLong(MethodVisitor mv, String className, String offsetFieldName, int messageIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitMethodInsn(INVOKESTATIC, UNSAFEUTIL_INTERNAL_NAME, "getLong", "(Ljava/lang/Object;J)J", false);
    mv.visitVarInsn(LSTORE, valueIndex);
  }

  private static void unsafeGetInt(MethodVisitor mv, String className, String offsetFieldName, int messageIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitMethodInsn(INVOKESTATIC, UNSAFEUTIL_INTERNAL_NAME, "getInt", "(Ljava/lang/Object;J)I", false);
    mv.visitVarInsn(ISTORE, valueIndex);
  }

  private static void unsafeGetBool(MethodVisitor mv, String className, String offsetFieldName, int messageIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitMethodInsn(INVOKESTATIC, UNSAFEUTIL_INTERNAL_NAME, "getBoolean", "(Ljava/lang/Object;J)Z", false);
    mv.visitVarInsn(ISTORE, valueIndex);
  }

  private static void unsafeGetObject(MethodVisitor mv, String className, String offsetFieldName, String fieldDescriptor, int messageIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, messageIndex);
    mv.visitFieldInsn(GETSTATIC, className, offsetFieldName, LONG_TYPE.getDescriptor());
    mv.visitMethodInsn(INVOKESTATIC, UNSAFEUTIL_INTERNAL_NAME, "getObject", "(Ljava/lang/Object;J)Ljava/lang/Object;", false);
    mv.visitTypeInsn(CHECKCAST, fieldDescriptor);
    mv.visitVarInsn(ASTORE, valueIndex);
  }

  private static void prepareWriteDoubleValue(MethodVisitor mv, int fieldNumber, int writerIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(DLOAD, valueIndex);
  }

  private static void prepareWriteFloatValue(MethodVisitor mv, int fieldNumber, int writerIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(FLOAD, valueIndex);
  }

  private static void prepareWriteIntValue(MethodVisitor mv, int fieldNumber, int writerIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ILOAD, valueIndex);
  }

  private static void prepareWriteLongValue(MethodVisitor mv, int fieldNumber, int writerIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(LLOAD, valueIndex);
  }

  private static void prepareWriteObjectValue(MethodVisitor mv, int fieldNumber, int writerIndex, int valueIndex) {
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, valueIndex);
  }

  private static void prepareWriteListValue(MethodVisitor mv, int fieldNumber, int writerIndex, int valueIndex, boolean packed) {
    mv.visitVarInsn(ALOAD, writerIndex);
    mv.visitLdcInsn(fieldNumber);
    mv.visitInsn(packed ? ICONST_1 : ICONST_0);
    mv.visitVarInsn(ALOAD, valueIndex);
  }

  /*private static void println(String value, MethodVisitor mv) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
    mv.visitLdcInsn(value);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
  }*/

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
