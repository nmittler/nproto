package com.google.protobuf.experimental.schema;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
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

import com.google.protobuf.experimental.JavaType;
import com.google.protobuf.experimental.Writer;
import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.BeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.PropertyDescriptor;
import com.google.protobuf.experimental.descriptor.PropertyType;
import com.google.protobuf.experimental.util.SchemaUtil;
import com.google.protobuf.experimental.util.UnsafeUtil;
import com.google.protobuf.experimental.Internal;
import com.google.protobuf.experimental.Reader;

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
    PropertyType[] propertyTypes = PropertyType.values();
    FIELD_PROCESSORS = new FieldProcessor[propertyTypes.length];
    for (int i = 0; i < propertyTypes.length; ++i) {
      FIELD_PROCESSORS[i] = new FieldProcessor(propertyTypes[i]);
    }
  }

  private final ClassLoadingStrategy classLoadingStrategy;
  private final BeanDescriptorFactory beanDescriptorFactory;

  public AsmSchemaFactory() {
    this(AnnotationBeanDescriptorFactory.getInstance());
  }

  public AsmSchemaFactory(BeanDescriptorFactory beanDescriptorFactory) {
    this(new InjectionClassLoadingStrategy(), beanDescriptorFactory);
  }

  public AsmSchemaFactory(ClassLoadingStrategy classLoadingStrategy, BeanDescriptorFactory beanDescriptorFactory) {
    if (classLoadingStrategy == null) {
      throw new NullPointerException("classLoadingStrategy");
    }
    if (beanDescriptorFactory == null) {
      throw new NullPointerException("beanDescriptorFactory");
    }
    this.classLoadingStrategy = classLoadingStrategy;
    this.beanDescriptorFactory = beanDescriptorFactory;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    try {
      @SuppressWarnings("unchecked")
      Class<Schema<T>> newClass = (Class<Schema<T>>)
              classLoadingStrategy.loadSchemaClass(messageType,
                      getSchemaClassName(messageType),
                      createSchemaClass(messageType));
      return newClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> byte[] createSchemaClass(Class<T> messageType) {
    if (messageType.isInterface() || Modifier.isAbstract(messageType.getModifiers())) {
      throw new RuntimeException(
              "The root object can neither be an abstract "
                      + "class nor interface: \"" + messageType.getName());
    }

    ClassWriter cv = new ClassWriter(0);
    //ClassVisitor cv = new CheckClassAdapter(writer);
    final String messageClassName = getInternalName(messageType);
    final String schemaClassName = getSchemaClassName(messageType).replace('.', '/');
    cv.visit(V1_6, ACC_PUBLIC + ACC_FINAL, schemaClassName, null, "java/lang/Object",
            new String[]{SCHEMA_INTERNAL_NAME});
    generateConstructor(cv);

    final boolean packagePrivateAccessSupported = classLoadingStrategy.isPackagePrivateAccessSupported();
    List<PropertyDescriptor> fields = beanDescriptorFactory.descriptorFor(messageType).getPropertyDescriptors();
    WriteToGenerator writeTo = new WriteToGenerator(cv, messageClassName);
    MergeFromGenerator mergeFrom = new MergeFromGenerator(cv, fields);
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0; i < fields.size(); ++i) {
      PropertyDescriptor f = fields.get(i);
      if (lastFieldNumber == f.fieldNumber) {
        // Disallow duplicate field numbers.
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      lastFieldNumber = f.fieldNumber;

      long offset = UnsafeUtil.objectFieldOffset(f.field);
      writeTo.addField(f, offset, packagePrivateAccessSupported);
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
    private final String messageClassName;

    WriteToGenerator(ClassVisitor cv, String messageClassName) {
      mv = cv.visitMethod(ACC_PUBLIC, "writeTo", WRITE_TO_DESCRIPTOR, null, null);
      mv.visitCode();

      // Cast the message to the concrete type.
      this.messageClassName = messageClassName;
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitTypeInsn(CHECKCAST, messageClassName);
      mv.visitVarInsn(ASTORE, MESSAGE_INDEX);
    }

    void addField(PropertyDescriptor property, long offset, boolean packagePrivateAccessSupported) {
      FIELD_PROCESSORS[property.type.ordinal()].write(messageClassName, mv, property, offset, packagePrivateAccessSupported);
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

    MergeFromGenerator(ClassVisitor cv, List<PropertyDescriptor> fields) {
      mv = cv.visitMethod(ACC_PUBLIC, "mergeFrom", MERGE_FROM_DESCRIPTOR, null, null);
      mv.visitCode();

      // Create the main labels and visit the start.
      startLabel = new Label();
      endLabel = new Label();
      defaultLabel = new Label();
      visitLabel(startLabel);

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
      tableSwitch = SchemaUtil.shouldUseTableSwitch(fields);
      final int numFields = fields.size();
      if (tableSwitch) {
        // Tableswitch...

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
        // Lookupswitch...

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

    void addField(PropertyDescriptor field, int fieldIndex, long offset) {
      if (tableSwitch) {
        addTableSwitchCase(field, offset);
      } else {
        addLookupSwitchCase(field, fieldIndex, offset);
      }
    }

    void addTableSwitchCase(PropertyDescriptor field, long offset) {
      // Tableswitch: Label index is the field number.
      visitLabel(labels[field.fieldNumber - lo]);
      FIELD_PROCESSORS[field.type.ordinal()].read(mv, offset);
      mv.visitJumpInsn(GOTO, startLabel);
    }

    void addLookupSwitchCase(PropertyDescriptor field, int fieldIndex, long offset) {
      // Lookupswitch: Label index is field index.
      visitLabel(labels[fieldIndex]);
      FIELD_PROCESSORS[field.type.ordinal()].read(mv, offset);
      mv.visitJumpInsn(GOTO, startLabel);
    }

    void end() {
      // Default case: skip the unknown field and check for done.
      visitLabel(defaultLabel);
      callReader(mv, "skipField", "()Z");
      mv.visitJumpInsn(IFNE, startLabel);

      visitLabel(endLabel);
      mv.visitInsn(RETURN);
      mv.visitMaxs(4, 4);
      mv.visitEnd();
    }

    private void visitLabel(Label label) {
      mv.visitLabel(label);
      mv.visitFrame(F_SAME, 0, null, 0, null);
    }
  }

  private static void callReader(MethodVisitor mv, String methodName, String methodDescriptor) {
    mv.visitVarInsn(ALOAD, READER_INDEX);
    mv.visitMethodInsn(INVOKEINTERFACE, READER_INTERNAL_NAME, methodName, methodDescriptor, true);
  }

  private static String getSchemaClassName(Class<?> messageType) {
    return messageType.getName() + "Schema";
  }

  private static final class FieldProcessor {
    private final String unsafeWriteMethodName;
    private final String safeWriteMethodName;
    private final String safeWriteMethodDescriptor;
    private final String unsafeReadMethodName;
    private final WriteType writeType;

    private enum WriteType {
      STANDARD,
      ENUM,
      ENUM_LIST,
      LIST
    }

    FieldProcessor(PropertyType propertyType) {
      JavaType jtype = propertyType.getJavaType();
      WriteType writeType = (jtype == JavaType.LIST) ?
              WriteType.LIST : (jtype == JavaType.ENUM) ? WriteType.ENUM : WriteType.STANDARD;
      switch (propertyType) {
        case DOUBLE:
          unsafeWriteMethodName = "unsafeWriteDouble";
          safeWriteMethodName = "writeDouble";
          safeWriteMethodDescriptor = "(IDL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadDouble";
          break;
        case FLOAT:
          unsafeWriteMethodName = "unsafeWriteFloat";
          safeWriteMethodName = "writeFloat";
          safeWriteMethodDescriptor = "(IFL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadFloat";
          break;
        case INT64:
          unsafeWriteMethodName = "unsafeWriteInt64";
          safeWriteMethodName = "writeInt64";
          safeWriteMethodDescriptor = "(IJL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadInt64";
          break;
        case UINT64:
          unsafeWriteMethodName = "unsafeWriteUInt64";
          safeWriteMethodName = "writeUInt64";
          safeWriteMethodDescriptor = "(IJL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadUInt64";
          break;
        case INT32:
          unsafeWriteMethodName = "unsafeWriteInt32";
          safeWriteMethodName = "writeInt32";
          safeWriteMethodDescriptor = "(IIL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadInt32";
          break;
        case FIXED64:
          unsafeWriteMethodName = "unsafeWriteFixed64";
          safeWriteMethodName = "writeFixed64";
          safeWriteMethodDescriptor = "(IJL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadFixed64";
          break;
        case FIXED32:
          unsafeWriteMethodName = "unsafeWriteFixed32";
          safeWriteMethodName = "writeFixed32";
          safeWriteMethodDescriptor = "(IIL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadFixed32";
          break;
        case BOOL:
          unsafeWriteMethodName = "unsafeWriteBool";
          safeWriteMethodName = "writeBool";
          safeWriteMethodDescriptor = "(IZL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadBool";
          break;
        case STRING:
          unsafeWriteMethodName = "unsafeWriteString";
          safeWriteMethodName = "writeString";
          safeWriteMethodDescriptor = "(ILjava/lang/String;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadString";
          break;
        case MESSAGE:
          unsafeWriteMethodName = "unsafeWriteMessage";
          safeWriteMethodName = "writeMessage";
          safeWriteMethodDescriptor = "(ILjava/lang/Object;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadMessage";
          break;
        case BYTES:
          unsafeWriteMethodName = "unsafeWriteBytes";
          safeWriteMethodName = "writeBytes";
          safeWriteMethodDescriptor = "(ILcom/google/protobuf/experimental/ByteString;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadBytes";
          break;
        case UINT32:
          unsafeWriteMethodName = "unsafeWriteUInt32";
          safeWriteMethodName = "writeUInt32";
          safeWriteMethodDescriptor = "(IIL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadUInt32";
          break;
        case ENUM:
          unsafeWriteMethodName = "unsafeWriteEnum";
          safeWriteMethodName = "writeEnum";
          safeWriteMethodDescriptor = "(ILjava/lang/Enum;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadEnum";
          break;
        case SFIXED32:
          unsafeWriteMethodName = "unsafeWriteSFixed32";
          safeWriteMethodName = "writeSFixed32";
          safeWriteMethodDescriptor = "(IIL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSFixed32";
          break;
        case SFIXED64:
          unsafeWriteMethodName = "unsafeWriteSFixed64";
          safeWriteMethodName = "writeSFixed64";
          safeWriteMethodDescriptor = "(IJL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSFixed64";
          break;
        case SINT32:
          unsafeWriteMethodName = "unsafeWriteSInt32";
          safeWriteMethodName = "writeSInt32";
          safeWriteMethodDescriptor = "(IIL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSInt32";
          break;
        case SINT64:
          unsafeWriteMethodName = "unsafeWriteSInt64";
          safeWriteMethodName = "writeSInt64";
          safeWriteMethodDescriptor = "(IJL" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSInt64";
          break;
        case DOUBLE_LIST:
          unsafeWriteMethodName = "unsafeWriteDoubleList";
          safeWriteMethodName = "writeDoubleList";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadDoubleList";
          break;
        case FLOAT_LIST:
          unsafeWriteMethodName = "unsafeWriteFloatList";
          safeWriteMethodName = "writeFloatList";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadFloatList";
          break;
        case INT64_LIST:
          unsafeWriteMethodName = "unsafeWriteInt64List";
          safeWriteMethodName = "writeInt64List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadInt64List";
          break;
        case UINT64_LIST:
          unsafeWriteMethodName = "unsafeWriteUInt64List";
          safeWriteMethodName = "writeUInt64List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadUInt64List";
          break;
        case INT32_LIST:
          unsafeWriteMethodName = "unsafeWriteInt32List";
          safeWriteMethodName = "writeInt32List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadInt32List";
          break;
        case FIXED64_LIST:
          unsafeWriteMethodName = "unsafeWriteFixed64List";
          safeWriteMethodName = "writeFixed64List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadFixed64List";
          break;
        case FIXED32_LIST:
          unsafeWriteMethodName = "unsafeWriteFixed32List";
          safeWriteMethodName = "writeFixed32List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadFixed32List";
          break;
        case BOOL_LIST:
          unsafeWriteMethodName = "unsafeWriteBoolList";
          safeWriteMethodName = "writeBoolList";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadBoolList";
          break;
        case STRING_LIST:
          unsafeWriteMethodName = "unsafeWriteStringList";
          safeWriteMethodName = "writeStringList";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadStringList";
          writeType = WriteType.STANDARD;
          break;
        case MESSAGE_LIST:
          unsafeWriteMethodName = "unsafeWriteMessageList";
          safeWriteMethodName = "writeMessageList";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadMessageList";
          writeType = WriteType.STANDARD;
          break;
        case BYTES_LIST:
          unsafeWriteMethodName = "unsafeWriteBytesList";
          safeWriteMethodName = "writeBytesList";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadBytesList";
          writeType = WriteType.STANDARD;
          break;
        case UINT32_LIST:
          unsafeWriteMethodName = "unsafeWriteUInt32List";
          safeWriteMethodName = "writeUInt32List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadUInt32List";
          break;
        case ENUM_LIST:
          unsafeWriteMethodName = "unsafeWriteEnumList";
          safeWriteMethodName = "writeEnumList";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadEnumList";
          writeType = WriteType.ENUM_LIST;
          break;
        case SFIXED32_LIST:
          unsafeWriteMethodName = "unsafeWriteSFixed32List";
          safeWriteMethodName = "writeSFixed32List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSFixed32List";
          break;
        case SFIXED64_LIST:
          unsafeWriteMethodName = "unsafeWriteSFixed64List";
          safeWriteMethodName = "writeSFixed64List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSFixed64List";
          break;
        case SINT32_LIST:
          unsafeWriteMethodName = "unsafeWriteSInt32List";
          safeWriteMethodName = "writeSInt32List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSInt32List";
          break;
        case SINT64_LIST:
          unsafeWriteMethodName = "unsafeWriteSInt64List";
          safeWriteMethodName = "writeSInt64List";
          safeWriteMethodDescriptor = "(ILjava/util/List;L" + WRITER_INTERNAL_NAME + ";)V";
          unsafeReadMethodName = "unsafeReadSInt64List";
          break;
        default:
          throw new IllegalArgumentException("Unsupported FieldType: " + propertyType);
      }
      this.writeType = writeType;
    }

    void write(String messageClassName, MethodVisitor mv, PropertyDescriptor property, long offset, boolean packagePrivateAccessSupported) {
      switch (writeType) {
        case STANDARD:
          if (isReadable(packagePrivateAccessSupported, property)) {
            safeWrite(messageClassName, mv, property);
          } else {
            unsafeWrite(mv, property.fieldNumber, offset);
          }
          break;
        case ENUM:
          if (isReadable(packagePrivateAccessSupported, property)) {
            safeWrite(messageClassName, mv, property);
          } else {
            unsafeWriteEnum(mv, property.fieldNumber, offset);
          }
          break;
        case ENUM_LIST:
          if (isReadable(packagePrivateAccessSupported, property)) {
            safeWrite(messageClassName, mv, property);
          } else {
            unsafeWriteEnumList(mv, property.fieldNumber, offset);
          }
          break;
        case LIST:
          if (isReadable(packagePrivateAccessSupported, property)) {
            safeWrite(messageClassName, mv, property);
          } else {
            unsafeWriteList(mv, property.fieldNumber, offset);
          }
          break;
      }
    }

    private static boolean isReadable(boolean packagePrivateAccessSupported, PropertyDescriptor property) {
      int mod = property.field.getModifiers();
      return Modifier.isPublic(mod) ||
              (packagePrivateAccessSupported && !Modifier.isPrivate(mod));
    }

    void read(MethodVisitor mv, long offset) {
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, READER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, unsafeReadMethodName,
              "(Ljava/lang/Object;JLcom/google/protobuf/experimental/Reader;)V", false);
    }

    private void unsafeWriteEnum(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, unsafeWriteMethodName,
              "(ILjava/lang/Object;JLcom/google/protobuf/experimental/Writer;Ljava/lang/Class;)V", false);
    }

    private void unsafeWrite(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, unsafeWriteMethodName,
              "(ILjava/lang/Object;JLcom/google/protobuf/experimental/Writer;)V", false);
    }

    private void safeWrite(String messageClassName, MethodVisitor mv, PropertyDescriptor property) {
      mv.visitLdcInsn(property.fieldNumber);

      // Get the field value.
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitFieldInsn(GETFIELD, messageClassName, property.field.getName(),
              Type.getDescriptor(property.field.getType()));

      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, safeWriteMethodName,
              safeWriteMethodDescriptor, false);
    }

    private void unsafeWriteList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, unsafeWriteMethodName,
              "(ILjava/lang/Object;JLcom/google/protobuf/experimental/Writer;)V", false);
    }

    private void unsafeWriteEnumList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMAUTIL_INTERNAL_NAME, unsafeWriteMethodName,
              "(ILjava/lang/Object;JLcom/google/protobuf/experimental/Writer;Ljava/lang/Class;)V", false);
    }
  }
}
