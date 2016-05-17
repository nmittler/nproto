package io.nproto.schema.gen;

import static io.nproto.UnsafeUtil.fieldOffset;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;
import static org.objectweb.asm.Type.getInternalName;

import io.nproto.FieldType;
import io.nproto.Internal;
import io.nproto.JavaType;
import io.nproto.Reader;
import io.nproto.Writer;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;
import io.nproto.schema.SchemaUtil;
import io.nproto.schema.SchemaUtil.FieldInfo;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Modifier;
import java.util.List;

@Internal
public final class AsmSchemaFactory implements SchemaFactory {
  private static final String SCHEMA_INTERNAL_NAME = getInternalName(Schema.class);
  private static final String WRITER_INTERNAL_NAME = getInternalName(Writer.class);
  private static final String READER_INTERNAL_NAME = getInternalName(Reader.class);
  private static final String SCHEMAUTIL_INTERNAL_NAME = getInternalName(SchemaUtil.class);
  private static final Type ENUM_TYPE = Type.getType(Enum.class);
  private static final String WRITE_TO_DESCRIPTOR = String.format("(Ljava/lang/Object;L%s;)V",
          WRITER_INTERNAL_NAME);
  private static final String MERGE_FROM_DESCRIPTOR = String.format("(Ljava/lang/Object;L%s;)V",
          READER_INTERNAL_NAME);
  private static final int MESSAGE_INDEX = 1;
  private static final int WRITER_INDEX = 2;
  private static final int READER_INDEX = 2;
  private static final int FIELD_NUMBER_INDEX = 3;
  private static final FieldProcessor[] FIELD_PROCESSORS;
  static {
    FieldType[] fieldTypes = FieldType.values();
    FIELD_PROCESSORS = new FieldProcessor[fieldTypes.length];
    for (int i = 0; i < fieldTypes.length; ++i) {
      FIELD_PROCESSORS[i] = new FieldProcessor(fieldTypes[i]);
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

    List<FieldInfo> fields = SchemaUtil.getAllFieldInfo(messageType);
    WriteToGenerator writeTo = new WriteToGenerator(cv);
    MergeFromGenerator mergeFrom = new MergeFromGenerator(cv, fields);
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0; i < fields.size(); ++i) {
      FieldInfo f = fields.get(i);
      if (lastFieldNumber == f.fieldNumber) {
        // Disallow duplicate field numbers.
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      lastFieldNumber = f.fieldNumber;

      long offset = fieldOffset(f.field);
      writeTo.addField(f, offset);
      mergeFrom.addField(f, i, offset);
    }
    writeTo.end();
    mergeFrom.end();

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

  private static final class WriteToGenerator {
    private final MethodVisitor mv;
    WriteToGenerator(ClassVisitor cv) {
      mv = cv.visitMethod(ACC_PUBLIC, "writeTo", WRITE_TO_DESCRIPTOR, null, null);
      mv.visitCode();
    }

    void addField(FieldInfo field, long offset) {
      FIELD_PROCESSORS[field.fieldType.ordinal()].write(mv, field.fieldNumber, offset);
    }

    void end() {
      mv.visitInsn(RETURN);
      mv.visitMaxs(7, 3);
      mv.visitEnd();
    }
  }

  private static final class MergeFromGenerator {
    private final MethodVisitor mv;
    private final Label startLabel;
    private final Label endLabel;
    private final Label defaultLabel;
    private final Label[] labels;
    private final boolean tableSwitch;
    private final int lo;

    MergeFromGenerator(ClassVisitor cv, List<FieldInfo> fields) {
      mv = cv.visitMethod(ACC_PUBLIC, "mergeFrom", MERGE_FROM_DESCRIPTOR, null, null);
      mv.visitCode();

      // Create the main labels and visit the start.
      startLabel = new Label();
      endLabel = new Label();
      defaultLabel = new Label();
      mv.visitLabel(startLabel);
      mv.visitFrame(F_SAME, 0, null, 0, null);

      // Get the field number form the reader.
      callReader(mv, "fieldNumber", "()I");

      // Make a copy of the field number and store to a local variable. The first check is against
      // MAXINT since looking for that value in the switch statement would mean that we couldn't use a
      // tableswitch (rather than lookupswitch).
      mv.visitInsn(DUP);
      mv.visitVarInsn(ISTORE, FIELD_NUMBER_INDEX);
      mv.visitLdcInsn(Reader.READ_DONE);
      mv.visitJumpInsn(IF_ICMPEQ, endLabel);

      // Load the field number again for the switch.
      mv.visitVarInsn(ILOAD, FIELD_NUMBER_INDEX);
      final int numFields = fields.size();
      tableSwitch = SchemaUtil.shouldUseTableSwitch(fields);
      if (tableSwitch) {
        // Tableswitch

        // Determine the number of labels (i.e. cases).
        lo = fields.get(0).fieldNumber;
        int hi = fields.get(numFields - 1).fieldNumber;
        int numLabels = (hi - lo) + 1;

        // Create the labels
        labels = new Label[numLabels];
        for (int labelIndex = 0, fieldIndex = 0; fieldIndex < numFields; ++fieldIndex) {
          while (labelIndex < fields.get(fieldIndex).fieldNumber - lo) {
            // Unused entries in the table drop down to the default case.
            labels[labelIndex++] = defaultLabel;
          }
          labels[labelIndex++] = new Label();
        }

        // Create the switch statement.
        mv.visitTableSwitchInsn(lo, hi, defaultLabel, labels);
      } else {
        // Lookupswitch

        // Create the keys and labels.
        lo = -1;
        int[] keys = new int[numFields];
        labels = new Label[numFields];
        for (int i = 0; i < numFields; ++i) {
          keys[i] = fields.get(i).fieldNumber;
          Label label = new Label();
          labels[i] = label;
        }

        // Create the switch statement.
        mv.visitLookupSwitchInsn(defaultLabel, keys, labels);
      }
    }

    void addField(FieldInfo field, int fieldIndex, long offset) {
      if (tableSwitch) {
          // Tableswitch: Label index is the field number.
          mv.visitLabel(labels[field.fieldNumber - lo]);
      } else {
          // Lookupswitch: Label index is field index.
          mv.visitLabel(labels[fieldIndex]);
      }
      mv.visitFrame(F_SAME, 0, null, 0, null);
      int processorIndex = field.fieldType.ordinal();
      FIELD_PROCESSORS[processorIndex].read(mv, offset);
      mv.visitJumpInsn(GOTO, startLabel);
    }

    void end() {
      // Default case: skip the unknown field and check for done.
      mv.visitLabel(defaultLabel);
      mv.visitFrame(F_SAME, 0, null, 0, null);
      callReader(mv, "skipField", "()Z");
      mv.visitJumpInsn(IFNE, startLabel);

      // Finish the method.
      mv.visitLabel(endLabel);
      mv.visitFrame(F_SAME, 0, null, 0, null);
      mv.visitInsn(RETURN);
      mv.visitMaxs(4, 4);
      mv.visitEnd();
    }
  }

  private static void callReader(MethodVisitor mv, String methodName, String methodDescriptor) {
    mv.visitVarInsn(ALOAD, READER_INDEX);
    mv.visitMethodInsn(INVOKEINTERFACE, READER_INTERNAL_NAME, methodName, methodDescriptor, true);
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

  private static void unsafeWrite(MethodVisitor mv, String writeMethodName, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, writeMethodName,
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

  private static void unsafeWriteList(MethodVisitor mv, String writeMethodName, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitInsn(ICONST_0);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, writeMethodName,
            "(ILjava/lang/Object;JZLio/nproto/Writer;)V", false);
  }

  private static void unsafeWriteListPacked(MethodVisitor mv, String writeMethodName, int fieldNumber, long offset) {
    mv.visitLdcInsn(fieldNumber);
    mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
    mv.visitLdcInsn(offset);
    mv.visitInsn(ICONST_1);
    mv.visitVarInsn(ALOAD, WRITER_INDEX);
    mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, writeMethodName,
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

  private static final class FieldProcessor {
    private final FieldType fieldType;
    private final String writeMethodName;
    private final String readMethodName;
    private final WriteType writeType;

    private enum WriteType {
      STANDARD,
      ENUM,
      ENUM_LIST,
      LIST
    }

    FieldProcessor(FieldType fieldType) {
      this.fieldType = fieldType;
      JavaType jtype = fieldType.getJavaType();
      WriteType writeType = (jtype == JavaType.LIST) ?
              WriteType.LIST : (jtype == JavaType.ENUM) ? WriteType.ENUM : WriteType.STANDARD;
      switch (fieldType) {
        case DOUBLE:
          writeMethodName = "unsafeWriteDouble";
          readMethodName = "unsafeReadDouble";
          break;
        case FLOAT:
          writeMethodName = "unsafeWriteFloat";
          readMethodName = "unsafeReadFloat";
          break;
        case INT64:
          writeMethodName = "unsafeWriteInt64";
          readMethodName = "unsafeReadInt64";
          break;
        case UINT64:
          writeMethodName = "unsafeWriteUInt64";
          readMethodName = "unsafeReadUInt64";
          break;
        case INT32:
          writeMethodName = "unsafeWriteInt32";
          readMethodName = "unsafeReadInt32";
          break;
        case FIXED64:
          writeMethodName = "unsafeWriteFixed64";
          readMethodName = "unsafeReadFixed64";
          break;
        case FIXED32:
          writeMethodName = "unsafeWriteFixed32";
          readMethodName = "unsafeReadFixed32";
          break;
        case BOOL:
          writeMethodName = "unsafeWriteBool";
          readMethodName = "unsafeReadBool";
          break;
        case STRING:
          writeMethodName = "unsafeWriteString";
          readMethodName = "unsafeReadString";
          break;
        case MESSAGE:
          writeMethodName = "unsafeWriteMessage";
          readMethodName = "unsafeReadMessage";
          break;
        case BYTES:
          writeMethodName = "unsafeWriteBytes";
          readMethodName = "unsafeReadBytes";
          break;
        case UINT32:
          writeMethodName = "unsafeWriteUInt32";
          readMethodName = "unsafeReadUInt32";
          break;
        case ENUM:
          writeMethodName = "unsafeWriteEnum";
          readMethodName = "unsafeReadEnum";
          break;
        case SFIXED32:
          writeMethodName = "unsafeWriteSFixed32";
          readMethodName = "unsafeReadSFixed32";
          break;
        case SFIXED64:
          writeMethodName = "unsafeWriteSFixed64";
          readMethodName = "unsafeReadSFixed64";
          break;
        case SINT32:
          writeMethodName = "unsafeWriteSInt32";
          readMethodName = "unsafeReadSInt32";
          break;
        case SINT64:
          writeMethodName = "unsafeWriteSInt64";
          readMethodName = "unsafeReadSInt64";
          break;
        case DOUBLE_LIST:
          writeMethodName = "unsafeWriteDoubleList";
          readMethodName = "unsafeReadDoubleList";
          break;
        case PACKED_DOUBLE_LIST:
          writeMethodName = "unsafeWriteDoubleList";
          readMethodName = "unsafeReadDoubleList";
          break;
        case FLOAT_LIST:
          writeMethodName = "unsafeWriteFloatList";
          readMethodName = "unsafeReadFloatList";
          break;
        case PACKED_FLOAT_LIST:
          writeMethodName = "unsafeWriteFloatList";
          readMethodName = "unsafeReadFloatList";
          break;
        case INT64_LIST:
          writeMethodName = "unsafeWriteInt64List";
          readMethodName = "unsafeReadInt64List";
          break;
        case PACKED_INT64_LIST:
          writeMethodName = "unsafeWriteInt64List";
          readMethodName = "unsafeReadInt64List";
          break;
        case UINT64_LIST:
          writeMethodName = "unsafeWriteUInt64List";
          readMethodName = "unsafeReadUInt64List";
          break;
        case PACKED_UINT64_LIST:
          writeMethodName = "unsafeWriteUInt64List";
          readMethodName = "unsafeReadUInt64List";
          break;
        case INT32_LIST:
          writeMethodName = "unsafeWriteInt32List";
          readMethodName = "unsafeReadInt32List";
          break;
        case PACKED_INT32_LIST:
          writeMethodName = "unsafeWriteInt32List";
          readMethodName = "unsafeReadInt32List";
          break;
        case FIXED64_LIST:
          writeMethodName = "unsafeWriteFixed64List";
          readMethodName = "unsafeReadFixed64List";
          break;
        case PACKED_FIXED64_LIST:
          writeMethodName = "unsafeWriteFixed64List";
          readMethodName = "unsafeReadFixed64List";
          break;
        case FIXED32_LIST:
          writeMethodName = "unsafeWriteFixed32List";
          readMethodName = "unsafeReadFixed32List";
          break;
        case PACKED_FIXED32_LIST:
          writeMethodName = "unsafeWriteFixed32List";
          readMethodName = "unsafeReadFixed32List";
          break;
        case BOOL_LIST:
          writeMethodName = "unsafeWriteBoolList";
          readMethodName = "unsafeReadBoolList";
          break;
        case PACKED_BOOL_LIST:
          writeMethodName = "unsafeWriteBoolList";
          readMethodName = "unsafeReadBoolList";
          break;
        case STRING_LIST:
          writeMethodName = "unsafeWriteStringList";
          readMethodName = "unsafeReadStringList";
          writeType = WriteType.STANDARD;
          break;
        case MESSAGE_LIST:
          writeMethodName = "unsafeWriteMessageList";
          readMethodName = "unsafeReadMessageList";
          writeType = WriteType.STANDARD;
          break;
        case BYTES_LIST:
          writeMethodName = "unsafeWriteBytesList";
          readMethodName = "unsafeReadBytesList";
          writeType = WriteType.STANDARD;
          break;
        case UINT32_LIST:
          writeMethodName = "unsafeWriteUInt32List";
          readMethodName = "unsafeReadUInt32List";
          break;
        case PACKED_UINT32_LIST:
          writeMethodName = "unsafeWriteUInt32List";
          readMethodName = "unsafeReadUInt32List";
          break;
        case ENUM_LIST:
          writeMethodName = "unsafeWriteEnumList";
          readMethodName = "unsafeReadEnumList";
          writeType = WriteType.ENUM_LIST;
          break;
        case PACKED_ENUM_LIST:
          writeMethodName = "unsafeWriteEnumList";
          readMethodName = "unsafeReadEnumList";
          writeType = WriteType.ENUM_LIST;
          break;
        case SFIXED32_LIST:
          writeMethodName = "unsafeWriteSFixed32List";
          readMethodName = "unsafeReadSFixed32List";
          break;
        case PACKED_SFIXED32_LIST:
          writeMethodName = "unsafeWriteSFixed32List";
          readMethodName = "unsafeReadSFixed32List";
          break;
        case SFIXED64_LIST:
          writeMethodName = "unsafeWriteSFixed64List";
          readMethodName = "unsafeReadSFixed64List";
          break;
        case PACKED_SFIXED64_LIST:
          writeMethodName = "unsafeWriteSFixed64List";
          readMethodName = "unsafeReadSFixed64List";
          break;
        case SINT32_LIST:
          writeMethodName = "unsafeWriteSInt32List";
          readMethodName = "unsafeReadSInt32List";
          break;
        case PACKED_SINT32_LIST:
          writeMethodName = "unsafeWriteSInt32List";
          readMethodName = "unsafeReadSInt32List";
          break;
        case SINT64_LIST:
          writeMethodName = "unsafeWriteSInt64List";
          readMethodName = "unsafeReadSInt64List";
          break;
        case PACKED_SINT64_LIST:
          writeMethodName = "unsafeWriteSInt64List";
          readMethodName = "unsafeReadSInt64List";
          break;
        default:
          throw new IllegalArgumentException("Unsupported FieldType: " + fieldType);
      }
      this.writeType = writeType;
    }

    void write(MethodVisitor mv, int fieldNumber, long offset) {
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

    void read(MethodVisitor mv, long offset) {
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, READER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, readMethodName,
              "(Ljava/lang/Object;JLio/nproto/Reader;)V", false);
    }

    private void unsafeWriteEnum(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, writeMethodName,
              "(ILjava/lang/Object;JLio/nproto/Writer;Ljava/lang/Class;)V", false);
    }

    private void unsafeWrite(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, writeMethodName,
              "(ILjava/lang/Object;JLio/nproto/Writer;)V", false);
    }

    private void unsafeWriteList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitInsn(fieldType.isPacked() ? ICONST_1 : ICONST_0);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, writeMethodName,
              "(ILjava/lang/Object;JZLio/nproto/Writer;)V", false);
    }

    private void unsafeWriteEnumList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitInsn(fieldType.isPacked() ? ICONST_1 : ICONST_0);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, writeMethodName,
              "(ILjava/lang/Object;JZLio/nproto/Writer;Ljava/lang/Class;)V", false);
    }
  }
}
