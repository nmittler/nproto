package com.google.apps.tiktok.protobuf.experimental.schema.asm;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DCMPG;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DSTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.DUP2;
import static org.objectweb.asm.Opcodes.DUP_X1;
import static org.objectweb.asm.Opcodes.FCMPG;
import static org.objectweb.asm.Opcodes.FCONST_0;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.LCMP;
import static org.objectweb.asm.Opcodes.LCONST_0;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;
import static org.objectweb.asm.Type.getInternalName;

import com.google.apps.tiktok.protobuf.experimental.ByteString;
import com.google.apps.tiktok.protobuf.experimental.FieldType;
import com.google.apps.tiktok.protobuf.experimental.InternalApi;
import com.google.apps.tiktok.protobuf.experimental.JavaType;
import com.google.apps.tiktok.protobuf.experimental.descriptor.AnnotationMessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.descriptor.FieldDescriptor;
import com.google.apps.tiktok.protobuf.experimental.descriptor.MessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.Reader;
import com.google.apps.tiktok.protobuf.experimental.schema.Schema;
import com.google.apps.tiktok.protobuf.experimental.schema.SchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.Writer;
import com.google.apps.tiktok.protobuf.experimental.util.SchemaUtil;
import com.google.apps.tiktok.protobuf.experimental.util.UnsafeUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory that dynamically generates schema bytecode for a given message class. Creation of
 * these schemas are generally slow, but the schema itself will generally outperform a generic
 * schema.
 */
// TODO(nathanmittler): Generate writeTo/mergeFrom separately to support lazy code generation.
@InternalApi
public final class AsmSchemaFactory implements SchemaFactory {
  private static final int MESSAGE_INDEX = 1;

  // writeTo variables
  private static final int WRITER_INDEX = 2;
  private static final int WRITE_VALUE_INDEX = 3;

  // mergeFrom variables.
  private static final int READER_INDEX = 2;
  // These variables are not used at the same time, so re-using the index.
  private static final int READ_FIELD_NUMBER_INDEX = 3;
  private static final int READ_VALUE_LIST_INDEX = 3;

  private static final Type CLASS_TYPE = Type.getType(Class.class);
  private static final String OBJECT_NAME = getInternalName(Object.class);
  private static final String SCHEMA_NAME = getInternalName(Schema.class);
  private static final String READER_NAME = getInternalName(Reader.class);
  private static final String SCHEMA_UTIL_NAME = getInternalName(SchemaUtil.class);
  private static final String UNSAFE_UTIL_NAME = getInternalName(UnsafeUtil.class);
  private static final String WRITER_NAME = getInternalName(Writer.class);
  private static final String ARRAY_LIST_NAME = getInternalName(ArrayList.class);

  // Schema methods.
  private static final String WRITE_TO_NAME;
  private static final String WRITE_TO_DESCRIPTOR;
  private static final String MERGE_FROM_NAME;
  private static final String MERGE_FROM_DESCRIPTOR;

  // Reader methods.
  private static final String FIELD_NUMBER_NAME;
  private static final String FIELD_NUMBER_DESCRIPTOR;
  private static final String SKIP_FIELD_NAME;
  private static final String SKIP_FIELD_DESCRIPTOR;

  // Note that we don't hardcode the method names since proguard can change them. Instead
  // we look up the method by name and then get the method's name.
  static {
    try {
      // Schema methods.
      Method method = Schema.class.getDeclaredMethod("writeTo", Object.class, Writer.class);
      WRITE_TO_NAME = method.getName();
      WRITE_TO_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = Schema.class.getDeclaredMethod("mergeFrom", Object.class, Reader.class);
      MERGE_FROM_NAME = method.getName();
      MERGE_FROM_DESCRIPTOR = Type.getMethodDescriptor(method);

      // Reader methods.
      method = Reader.class.getDeclaredMethod("getFieldNumber");
      FIELD_NUMBER_NAME = method.getName();
      FIELD_NUMBER_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = Reader.class.getDeclaredMethod("skipField");
      SKIP_FIELD_NAME = method.getName();
      SKIP_FIELD_DESCRIPTOR = Type.getMethodDescriptor(method);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static final InlineFieldProcessor[] INLINE_FIELD_PROCESSORS;
  private static final MinimalCodeFieldProcessor[] MINIMAL_CODE_FIELD_PROCESSORS;

  static {
    FieldType[] propertyTypes = FieldType.values();
    INLINE_FIELD_PROCESSORS = new InlineFieldProcessor[propertyTypes.length];
    MINIMAL_CODE_FIELD_PROCESSORS = new MinimalCodeFieldProcessor[propertyTypes.length];
    for (int i = 0; i < propertyTypes.length; ++i) {
      FieldType propertyType = propertyTypes[i];
      INLINE_FIELD_PROCESSORS[i] = new InlineFieldProcessor(propertyType);
      MINIMAL_CODE_FIELD_PROCESSORS[i] = new MinimalCodeFieldProcessor(propertyType);
    }
  }

  private final ClassLoadingStrategy classLoadingStrategy;
  private final MessageDescriptorFactory beanDescriptorFactory;
  private final SchemaNamingStrategy schemaNamingStrategy;
  private final boolean minimizeGeneratedCode;
  private final boolean preferUnsafeAccess;

  /**
   * Constructs the factory with default settings.
   */
  public AsmSchemaFactory() {
    this(
        new InjectionClassLoadingStrategy(),
        AnnotationMessageDescriptorFactory.getValidatingInstance(),
        DefaultSchemaNamingStrategy.getInstance(),
        false,
        false);
  }

  /**
   * Constructs the factory.
   *
   * @param classLoadingStrategy the strategy used for loading new schema classes from bytecode.
   * @param messageDescriptorFactory a factory for generating descriptors for message classes.
   * @param schemaNamingStrategy a strategy for naming dynamically-generated schema classes.
   * @param minimizeGeneratedCode if {@code true}, this factory will attempt to minimize the amount
   * of generated code by using utility methods where possible. If {@code false}, all code will be
   * generated inline which may perform better on some platforms.
   * @param preferUnsafeAccess if {@code true}, this factory will prefer the use of
   * {@code sun.misc.Unsafe} for accessing fields in the message class even if the field is
   * accessible directly.
   */
  public AsmSchemaFactory(
      ClassLoadingStrategy classLoadingStrategy,
      MessageDescriptorFactory messageDescriptorFactory,
      SchemaNamingStrategy schemaNamingStrategy,
      boolean minimizeGeneratedCode,
      boolean preferUnsafeAccess) {
    if (classLoadingStrategy == null) {
      throw new NullPointerException("classLoadingStrategy");
    }
    if (messageDescriptorFactory == null) {
      throw new NullPointerException("beanDescriptorFactory");
    }
    if (schemaNamingStrategy == null) {
      throw new NullPointerException("schemaNamingStrategy");
    }
    this.classLoadingStrategy = classLoadingStrategy;
    this.beanDescriptorFactory = messageDescriptorFactory;
    this.schemaNamingStrategy = schemaNamingStrategy;
    this.minimizeGeneratedCode = minimizeGeneratedCode;
    this.preferUnsafeAccess = preferUnsafeAccess;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    try {
      @SuppressWarnings("unchecked")
      Class<Schema<T>> newClass =
          (Class<Schema<T>>)
              classLoadingStrategy.loadSchemaClass(
                  messageType,
                  schemaNamingStrategy.schemaNameFor(messageType),
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
              + "class nor interface: \""
              + messageType.getName());
    }

    ClassWriter writer = new ClassWriter(0);

    // To aid in debugging, swap over to CheckClassAdapter.
    //ClassVisitor cv = new CheckClassAdapter(writer);
    ClassVisitor cv = writer;

    final String messageClassName = getInternalName(messageType);
    final String schemaClassName =
        schemaNamingStrategy.schemaNameFor(messageType).replace('.', '/');
    cv.visit(
        V1_6,
        ACC_PUBLIC + ACC_FINAL,
        schemaClassName,
        null,
        OBJECT_NAME,
        new String[] {SCHEMA_NAME});
    generateConstructor(cv);

    final boolean hasPackageAccess = classLoadingStrategy.isPackagePrivateAccessSupported();
    List<FieldDescriptor> fields =
        beanDescriptorFactory.descriptorFor(messageType).getFieldDescriptors();
    WriteToGenerator writeTo = new WriteToGenerator(cv, messageClassName);
    MergeFromGenerator mergeFrom = new MergeFromGenerator(cv, messageClassName, fields);
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0; i < fields.size(); ++i) {
      FieldDescriptor f = fields.get(i);
      if (lastFieldNumber == f.getFieldNumber()) {
        // Disallow duplicate field numbers.
        throw new RuntimeException("Duplicate field number: " + f.getFieldNumber());
      }
      lastFieldNumber = f.getFieldNumber();

      writeTo.addField(f, hasPackageAccess);
      mergeFrom.addField(f, i, hasPackageAccess);
    }
    writeTo.end();
    mergeFrom.end();

    // Complete the generation of the class and return a new instance.
    cv.visitEnd();
    return writer.toByteArray();
  }

  private static void generateConstructor(ClassVisitor cv) {
    MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, OBJECT_NAME, "<init>", "()V", false);
    mv.visitInsn(RETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }

  private final class WriteToGenerator {
    private final MethodVisitor mv;
    private final String messageClassName;

    WriteToGenerator(ClassVisitor cv, String messageClassName) {
      mv = cv.visitMethod(ACC_PUBLIC, WRITE_TO_NAME, WRITE_TO_DESCRIPTOR, null, null);
      mv.visitCode();

      // Cast the message to the concrete type.
      this.messageClassName = messageClassName;
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitTypeInsn(CHECKCAST, messageClassName);
      mv.visitVarInsn(ASTORE, MESSAGE_INDEX);
    }

    void addField(FieldDescriptor fieldDescriptor, boolean hasPackageAccess) {
      if (minimizeGeneratedCode) {
        MINIMAL_CODE_FIELD_PROCESSORS[fieldDescriptor.getType().ordinal()]
            .write(messageClassName, mv, fieldDescriptor, hasPackageAccess, preferUnsafeAccess);
      } else {
        INLINE_FIELD_PROCESSORS[fieldDescriptor.getType().ordinal()]
            .write(messageClassName, mv, fieldDescriptor, hasPackageAccess, preferUnsafeAccess);
      }
    }

    void end() {
      mv.visitInsn(RETURN);
      mv.visitMaxs(6, 5);
      mv.visitEnd();
    }
  }

  private final class MergeFromGenerator {
    private final String messageClassName;
    private final MethodVisitor mv;
    private final Label startLabel;
    private final Label endLabel;
    private final Label defaultLabel;
    private final Label[] labels;
    private final boolean tableSwitch;
    private final int lo;

    MergeFromGenerator(ClassVisitor cv, String messageClassName, List<FieldDescriptor> fields) {
      mv = cv.visitMethod(ACC_PUBLIC, MERGE_FROM_NAME, MERGE_FROM_DESCRIPTOR, null, null);
      mv.visitCode();

      // Cast the message to the concrete type.
      this.messageClassName = messageClassName;
      // TODO: why doesn't this help to avoid cast later?
      /*mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitTypeInsn(CHECKCAST, messageClassName);
      mv.visitVarInsn(ASTORE, MESSAGE_INDEX);*/

      // Create the main labels and visit the start.
      startLabel = new Label();
      endLabel = new Label();
      defaultLabel = new Label();
      visitLabel(mv, startLabel);

      // Get the field number form the reader.
      mv.visitVarInsn(ALOAD, READER_INDEX);
      mv.visitMethodInsn(
          INVOKEINTERFACE, READER_NAME, FIELD_NUMBER_NAME, FIELD_NUMBER_DESCRIPTOR, true);

      // Make a copy of the field number and store to a local variable. The first check is against
      // MAXINT since looking for that value in the switch statement would mean that we couldn't use
      // a tableswitch (rather than lookupswitch).
      mv.visitInsn(DUP);
      mv.visitVarInsn(ISTORE, READ_FIELD_NUMBER_INDEX);
      mv.visitLdcInsn(Reader.READ_DONE);
      mv.visitJumpInsn(IF_ICMPEQ, endLabel);

      // Load the field number again for the switch.
      mv.visitVarInsn(ILOAD, READ_FIELD_NUMBER_INDEX);
      tableSwitch = SchemaUtil.shouldUseTableSwitch(fields);
      final int numFields = fields.size();
      if (tableSwitch) {
        // Tableswitch...

        // Determine the number of labels (i.e. cases).
        lo = fields.get(0).getFieldNumber();
        int hi = fields.get(numFields - 1).getFieldNumber();
        int numLabels = (hi - lo) + 1;

        // Create the labels
        labels = new Label[numLabels];
        for (int labelIndex = 0, fieldIndex = 0; fieldIndex < numFields; ++fieldIndex) {
          while (labelIndex < fields.get(fieldIndex).getFieldNumber() - lo) {
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
          keys[i] = fields.get(i).getFieldNumber();
          Label label = new Label();
          labels[i] = label;
        }

        // Create the switch statement.
        mv.visitLookupSwitchInsn(defaultLabel, keys, labels);
      }
    }

    void addField(FieldDescriptor field, int fieldIndex, boolean hasPackageAccess) {
      if (tableSwitch) {
        addTableSwitchCase(field, hasPackageAccess);
      } else {
        addLookupSwitchCase(field, fieldIndex, hasPackageAccess);
      }
    }

    private void addTableSwitchCase(FieldDescriptor fieldDescriptor, boolean hasPackageAccess) {
      // Tableswitch: Label index is the field number.
      visitLabel(mv, labels[fieldDescriptor.getFieldNumber() - lo]);
      readField(fieldDescriptor, hasPackageAccess);
      mv.visitJumpInsn(GOTO, startLabel);
    }

    private void addLookupSwitchCase(
        FieldDescriptor fieldDescriptor, int fieldIndex, boolean hasPackageAccess) {
      // Lookupswitch: Label index is field index.
      visitLabel(mv, labels[fieldIndex]);
      readField(fieldDescriptor, hasPackageAccess);
      mv.visitJumpInsn(GOTO, startLabel);
    }

    private void readField(FieldDescriptor fieldDescriptor, boolean hasPackageAccess) {
      if (minimizeGeneratedCode) {
        MINIMAL_CODE_FIELD_PROCESSORS[fieldDescriptor.getType().ordinal()]
            .read(messageClassName, mv, fieldDescriptor, hasPackageAccess, preferUnsafeAccess);
      } else {
        INLINE_FIELD_PROCESSORS[fieldDescriptor.getType().ordinal()]
            .read(messageClassName, mv, fieldDescriptor, hasPackageAccess, preferUnsafeAccess);
      }
    }

    void end() {
      // Default case: skip the unknown field and check for done.
      visitLabel(mv, defaultLabel);
      mv.visitVarInsn(ALOAD, READER_INDEX);
      mv.visitMethodInsn(
          INVOKEINTERFACE, READER_NAME, SKIP_FIELD_NAME, SKIP_FIELD_DESCRIPTOR, true);
      mv.visitJumpInsn(IFNE, startLabel);

      visitLabel(mv, endLabel);
      mv.visitInsn(RETURN);
      mv.visitMaxs(6, 4);
      mv.visitEnd();
    }
  }

  private static void visitLabel(MethodVisitor mv, Label label) {
    mv.visitLabel(label);
    mv.visitFrame(F_SAME, 0, null, 0, null);
  }

  private static final class InlineFieldProcessor {
    private final String writeName;
    private final String writeDescriptor;
    private final String unsafeGetName;
    private final String unsafeGetDescriptor;
    private final String unsafePutName;
    private final String unsafePutDescriptor;
    private final String readMethodName;
    private final String readMethodDescriptor;
    private final boolean packed;
    private final boolean needPacked;
    private final boolean needObjectClass;

    InlineFieldProcessor(FieldType fieldType) {
      try {
        Method writeMethod;
        Method unsafeGetMethod;
        Method unsafePutMethod;
        Method readMethod;
        boolean needPacked = false;
        boolean needObjectClass = false;
        switch (fieldType) {
          case DOUBLE:
            writeMethod = Writer.class.getDeclaredMethod("writeDouble", int.class, double.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getDouble", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putDouble", Object.class, long.class, double.class);
            readMethod = Reader.class.getDeclaredMethod("readDouble");
            break;
          case FLOAT:
            writeMethod = Writer.class.getDeclaredMethod("writeFloat", int.class, float.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getFloat", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putFloat", Object.class, long.class, float.class);
            readMethod = Reader.class.getDeclaredMethod("readFloat");
            break;
          case INT64:
            writeMethod = Writer.class.getDeclaredMethod("writeInt64", int.class, long.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getLong", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putLong", Object.class, long.class, long.class);
            readMethod = Reader.class.getDeclaredMethod("readInt64");
            break;
          case UINT64:
            writeMethod = Writer.class.getDeclaredMethod("writeUInt64", int.class, long.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getLong", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putLong", Object.class, long.class, long.class);
            readMethod = Reader.class.getDeclaredMethod("readUInt64");
            break;
          case INT32:
            writeMethod = Writer.class.getDeclaredMethod("writeInt32", int.class, int.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getInt", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putInt", Object.class, long.class, int.class);
            readMethod = Reader.class.getDeclaredMethod("readInt32");
            break;
          case FIXED64:
            writeMethod = Writer.class.getDeclaredMethod("writeFixed64", int.class, long.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getLong", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putLong", Object.class, long.class, long.class);
            readMethod = Reader.class.getDeclaredMethod("readFixed64");
            break;
          case FIXED32:
            writeMethod = Writer.class.getDeclaredMethod("writeFixed32", int.class, int.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getInt", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putInt", Object.class, long.class, int.class);
            readMethod = Reader.class.getDeclaredMethod("readFixed32");
            break;
          case BOOL:
            writeMethod = Writer.class.getDeclaredMethod("writeBool", int.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getBoolean", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putBoolean", Object.class, long.class, boolean.class);
            readMethod = Reader.class.getDeclaredMethod("readBool");
            break;
          case STRING:
            writeMethod = Writer.class.getDeclaredMethod("writeString", int.class, String.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readString");
            break;
          case MESSAGE:
            writeMethod = Writer.class.getDeclaredMethod("writeMessage", int.class, Object.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readMessage");
            break;
          case BYTES:
            writeMethod = Writer.class.getDeclaredMethod("writeBytes", int.class, ByteString.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readBytes");
            break;
          case UINT32:
            writeMethod = Writer.class.getDeclaredMethod("writeUInt32", int.class, int.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getInt", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putInt", Object.class, long.class, int.class);
            readMethod = Reader.class.getDeclaredMethod("readUInt32");
            break;
          case ENUM:
            writeMethod = Writer.class.getDeclaredMethod("writeEnum", int.class, int.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getInt", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putInt", Object.class, long.class, int.class);
            readMethod = Reader.class.getDeclaredMethod("readEnum");
            break;
          case SFIXED32:
            writeMethod = Writer.class.getDeclaredMethod("writeSFixed32", int.class, int.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getInt", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putInt", Object.class, long.class, int.class);
            readMethod = Reader.class.getDeclaredMethod("readSFixed32");
            break;
          case SFIXED64:
            writeMethod = Writer.class.getDeclaredMethod("writeSFixed64", int.class, long.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getLong", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putLong", Object.class, long.class, long.class);
            readMethod = Reader.class.getDeclaredMethod("readSFixed64");
            break;
          case SINT32:
            writeMethod = Writer.class.getDeclaredMethod("writeSInt32", int.class, int.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getInt", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putInt", Object.class, long.class, int.class);
            readMethod = Reader.class.getDeclaredMethod("readSInt32");
            break;
          case SINT64:
            writeMethod = Writer.class.getDeclaredMethod("writeSInt64", int.class, long.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getLong", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class.getDeclaredMethod("putLong", Object.class, long.class, long.class);
            readMethod = Reader.class.getDeclaredMethod("readSInt64");
            break;
          case DOUBLE_LIST:
          case DOUBLE_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeDoubleList", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readDoubleList", List.class, boolean.class);
            break;
          case FLOAT_LIST:
          case FLOAT_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeFloatList", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readFloatList", List.class, boolean.class);
            break;
          case INT64_LIST:
          case INT64_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeInt64List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readInt64List", List.class, boolean.class);
            break;
          case UINT64_LIST:
          case UINT64_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeUInt64List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readUInt64List", List.class, boolean.class);
            break;
          case INT32_LIST:
          case INT32_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeInt32List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readInt32List", List.class, boolean.class);
            break;
          case FIXED64_LIST:
          case FIXED64_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeFixed64List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readFixed64List", List.class, boolean.class);
            break;
          case FIXED32_LIST:
          case FIXED32_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeFixed32List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readFixed32List", List.class, boolean.class);
            break;
          case BOOL_LIST:
          case BOOL_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeBoolList", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readBoolList", List.class, boolean.class);
            break;
          case STRING_LIST:
            writeMethod = Writer.class.getDeclaredMethod("writeStringList", int.class, List.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readStringList", List.class);
            break;
          case MESSAGE_LIST:
            needObjectClass = true;
            writeMethod = Writer.class.getDeclaredMethod("writeMessageList", int.class, List.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readMessageList", List.class, Class.class);
            break;
          case BYTES_LIST:
            writeMethod = Writer.class.getDeclaredMethod("writeBytesList", int.class, List.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readBytesList", List.class);
            break;
          case UINT32_LIST:
          case UINT32_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeUInt32List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readUInt32List", List.class, boolean.class);
            break;
          case ENUM_LIST:
          case ENUM_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeEnumList", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod = Reader.class.getDeclaredMethod("readEnumList", List.class, boolean.class);
            break;
          case SFIXED32_LIST:
          case SFIXED32_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeSFixed32List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readSFixed32List", List.class, boolean.class);
            break;
          case SFIXED64_LIST:
          case SFIXED64_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeSFixed64List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readSFixed64List", List.class, boolean.class);
            break;
          case SINT32_LIST:
          case SINT32_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeSInt32List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readSInt32List", List.class, boolean.class);
            break;
          case SINT64_LIST:
          case SINT64_LIST_PACKED:
            needPacked = true;
            writeMethod =
                Writer.class
                    .getDeclaredMethod("writeSInt64List", int.class, List.class, boolean.class);
            unsafeGetMethod =
                UnsafeUtil.class.getDeclaredMethod("getObject", Object.class, long.class);
            unsafePutMethod =
                UnsafeUtil.class
                    .getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            readMethod =
                Reader.class.getDeclaredMethod("readSInt64List", List.class, boolean.class);
            break;
          default:
            throw new IllegalArgumentException("Unsupported PropertyType: " + fieldType);
        }
        writeName = writeMethod.getName();
        writeDescriptor = Type.getMethodDescriptor(writeMethod);
        unsafeGetName = unsafeGetMethod.getName();
        unsafeGetDescriptor = Type.getMethodDescriptor(unsafeGetMethod);
        unsafePutName = unsafePutMethod.getName();
        unsafePutDescriptor = Type.getMethodDescriptor(unsafePutMethod);
        readMethodName = readMethod.getName();
        readMethodDescriptor = Type.getMethodDescriptor(readMethod);
        packed = fieldType.isPacked();
        this.needPacked = needPacked;
        this.needObjectClass = needObjectClass;
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }

    void write(
        String messageClassName,
        MethodVisitor mv,
        FieldDescriptor fieldDescriptor,
        boolean hasPackageAccess,
        boolean preferUnsafeAccess) {
      Label label = new Label();

      getValue(messageClassName, mv, fieldDescriptor, hasPackageAccess, preferUnsafeAccess);
      storeValueAndJumpIfDefault(mv, fieldDescriptor, label);
      writeValue(mv, fieldDescriptor);

      visitLabel(mv, label);
    }

    void read(
        String messageClassName,
        MethodVisitor mv,
        FieldDescriptor fieldDescriptor,
        boolean hasPackageAccess,
        boolean preferUnsafeAccess) {
      if (preferUnsafeAccess && UnsafeUtil.isSupported()) {
        unsafeRead(mv, fieldDescriptor);
      } else if (isAccessible(hasPackageAccess, fieldDescriptor)) {
        safeRead(messageClassName, mv, fieldDescriptor);
      } else if (UnsafeUtil.isSupported()) {
        unsafeRead(mv, fieldDescriptor);
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Unable to access field %s in class %s",
                fieldDescriptor.getField().getName(), messageClassName));
      }
    }

    private void getValue(
        String messageClassName,
        MethodVisitor mv,
        FieldDescriptor fieldDescriptor,
        boolean hasPackageAccess,
        boolean preferUnsafeAccess) {
      if (preferUnsafeAccess && UnsafeUtil.isSupported()) {
        unsafeGetValue(mv, fieldDescriptor);
      } else if (isAccessible(hasPackageAccess, fieldDescriptor)) {
        safeGetValue(messageClassName, mv, fieldDescriptor);
      } else if (UnsafeUtil.isSupported()) {
        unsafeGetValue(mv, fieldDescriptor);
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Unable to access field %s in class %s",
                fieldDescriptor.getField().getName(), messageClassName));
      }
    }

    private void safeGetValue(
        String messageClassName, MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitFieldInsn(
          GETFIELD,
          messageClassName,
          fieldDescriptor.getField().getName(),
          Type.getDescriptor(fieldDescriptor.getField().getType()));
    }

    private void unsafeGetValue(MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      long fieldOffset = UnsafeUtil.objectFieldOffset(fieldDescriptor.getField());
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(fieldOffset);
      mv.visitMethodInsn(INVOKESTATIC, UNSAFE_UTIL_NAME, unsafeGetName, unsafeGetDescriptor, false);
      // Make sure object types are cast properly.
      if (fieldDescriptor.getType().isList()) {
        mv.visitTypeInsn(CHECKCAST, getInternalName(fieldDescriptor.getField().getType()));
      } else {
        switch (fieldDescriptor.getType().getJavaType()) {
          case BYTE_STRING:
          case STRING:
          case MESSAGE:
            mv.visitTypeInsn(CHECKCAST, getInternalName(fieldDescriptor.getField().getType()));
            break;
          default:
            break;
        }
      }
    }

    private void writeValue(MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(fieldDescriptor.getFieldNumber());
      loadValue(mv, fieldDescriptor);
      if (needPacked) {
        mv.visitLdcInsn(packed ? 1 : 0);
      }
      mv.visitMethodInsn(INVOKEINTERFACE, WRITER_NAME, writeName, writeDescriptor, true);
    }

    private void storeValueAndJumpIfDefault(
        MethodVisitor mv, FieldDescriptor fieldDescriptor, Label label) {
      if (fieldDescriptor.getType().isList()) {
        mv.visitInsn(DUP);
        mv.visitVarInsn(ASTORE, WRITE_VALUE_INDEX);
        mv.visitJumpInsn(IFNULL, label);
      } else {
        switch (fieldDescriptor.getType().getJavaType()) {
          case INT:
          case BOOLEAN:
          case ENUM:
            mv.visitInsn(DUP);
            mv.visitVarInsn(ISTORE, WRITE_VALUE_INDEX);
            mv.visitJumpInsn(IFEQ, label);
            break;
          case LONG:
            mv.visitInsn(DUP2);
            mv.visitVarInsn(LSTORE, WRITE_VALUE_INDEX);
            mv.visitInsn(LCONST_0);
            mv.visitInsn(LCMP);
            mv.visitJumpInsn(IFEQ, label);
            break;
          case FLOAT:
            mv.visitInsn(DUP);
            mv.visitVarInsn(FSTORE, WRITE_VALUE_INDEX);
            mv.visitInsn(FCONST_0);
            mv.visitInsn(FCMPG);
            mv.visitJumpInsn(IFEQ, label);
            break;
          case DOUBLE:
            mv.visitInsn(DUP2);
            mv.visitVarInsn(DSTORE, WRITE_VALUE_INDEX);
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DCMPG);
            mv.visitJumpInsn(IFEQ, label);
            break;
          default:
            // Object types.
            mv.visitInsn(DUP);
            mv.visitVarInsn(ASTORE, WRITE_VALUE_INDEX);
            mv.visitJumpInsn(IFNULL, label);
            break;
        }
      }
    }

    private void loadValue(MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      if (fieldDescriptor.getType().isList()) {
        mv.visitVarInsn(ALOAD, WRITE_VALUE_INDEX);
      } else {
        switch (fieldDescriptor.getType().getJavaType()) {
          case INT:
          case BOOLEAN:
          case ENUM:
            mv.visitVarInsn(ILOAD, WRITE_VALUE_INDEX);
            break;
          case LONG:
            mv.visitVarInsn(LLOAD, WRITE_VALUE_INDEX);
            break;
          case FLOAT:
            mv.visitVarInsn(FLOAD, WRITE_VALUE_INDEX);
            break;
          case DOUBLE:
            mv.visitVarInsn(DLOAD, WRITE_VALUE_INDEX);
            break;
          default:
            // Object types.
            mv.visitVarInsn(ALOAD, WRITE_VALUE_INDEX);
            break;
        }
      }
    }

    private void safeRead(
        String messageClassName, MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      if (fieldDescriptor.getType().isList()) {
        String name = fieldDescriptor.getField().getName();
        String descriptor = Type.getDescriptor(fieldDescriptor.getField().getType());

        // Get or create the value list.
        Label endCreateList = new Label();
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX); // ()->(m)
        // TODO: why can't this be done earlier?
        mv.visitTypeInsn(CHECKCAST, messageClassName);
        mv.visitFieldInsn(GETFIELD, messageClassName, name, descriptor); // (m)->(l)
        mv.visitInsn(DUP); // (l)->(l,l)
        mv.visitVarInsn(ASTORE, READ_VALUE_LIST_INDEX); // (l,l)->(l)
        mv.visitJumpInsn(IFNONNULL, endCreateList); // (l)->()
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX); // ()->(m)
        // TODO: why can't this be done earlier?
        mv.visitTypeInsn(CHECKCAST, messageClassName);
        mv.visitTypeInsn(NEW, ARRAY_LIST_NAME); // Create the new list. (m)->(m,l)
        mv.visitInsn(DUP); // (m,l)->(m,l,l)
        mv.visitInsn(DUP); // (m,l,l)->(m,l,l,l)
        mv.visitMethodInsn(
            INVOKESPECIAL, ARRAY_LIST_NAME, "<init>", "()V", false); // (m,l,l,l)->(m,l,l)
        mv.visitVarInsn(ASTORE, READ_VALUE_LIST_INDEX); // (m,l,l)->(m,l)
        mv.visitFieldInsn(PUTFIELD, messageClassName, name, descriptor); // (m,l)->()
        visitLabel(mv, endCreateList);

        // Read the values from the reader.
        mv.visitVarInsn(ALOAD, READER_INDEX);
        mv.visitVarInsn(ALOAD, READ_VALUE_LIST_INDEX);
        if (needPacked) {
          mv.visitLdcInsn(packed ? 1 : 0);
        }
        if (needObjectClass) {
          mv.visitLdcInsn(CLASS_TYPE);
        }
        mv.visitMethodInsn(
            INVOKEINTERFACE, READER_NAME, readMethodName, readMethodDescriptor, true);
      } else {
        Type fieldType = Type.getType(fieldDescriptor.getField().getType());
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
        // TODO: why can't we do this earlier?
        mv.visitTypeInsn(CHECKCAST, messageClassName);

        // Get the value.
        mv.visitVarInsn(ALOAD, READER_INDEX);
        mv.visitMethodInsn(
            INVOKEINTERFACE, READER_NAME, readMethodName, readMethodDescriptor, true);
        if (fieldDescriptor.getType().getJavaType() == JavaType.MESSAGE) {
          mv.visitTypeInsn(CHECKCAST, fieldType.getInternalName());
        }

        // Set the value on the field.
        mv.visitFieldInsn(
            PUTFIELD,
            messageClassName,
            fieldDescriptor.getField().getName(),
            fieldType.getDescriptor());
      }
    }

    private void unsafeRead(MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      final long fieldOffset = UnsafeUtil.objectFieldOffset(fieldDescriptor.getField());

      if (fieldDescriptor.getType().isList()) {
        // Get or create the value list.
        Label endCreateList = new Label();
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX); // ()->(m)
        mv.visitLdcInsn(fieldOffset); // (m)->(m,o)
        mv.visitMethodInsn(
            INVOKESTATIC,
            UNSAFE_UTIL_NAME,
            unsafeGetName,
            unsafeGetDescriptor,
            false); // (m,o)->(l)
        mv.visitInsn(DUP); // (l)->(l,l)
        mv.visitVarInsn(ASTORE, READ_VALUE_LIST_INDEX); // (l,l)->(l)
        mv.visitJumpInsn(IFNONNULL, endCreateList); // (l)->()
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX); // ()->(m)
        mv.visitLdcInsn(fieldOffset); // (m)->(m,o)
        mv.visitTypeInsn(NEW, ARRAY_LIST_NAME); // Create the new list. (m,o)->(m,o,v)
        mv.visitInsn(DUP); // (m,o,l)->(m,o,l,l)
        mv.visitInsn(DUP); // (m,o,l,l)->(m,o,l,l,l)
        mv.visitMethodInsn(
            INVOKESPECIAL, ARRAY_LIST_NAME, "<init>", "()V", false); // (m,o,l,l,l)->(m,o,l,l)
        mv.visitVarInsn(ASTORE, READ_VALUE_LIST_INDEX); // (m,o,l,l)->(m,o,l)
        mv.visitMethodInsn(
            INVOKESTATIC,
            UNSAFE_UTIL_NAME,
            unsafePutName,
            unsafePutDescriptor,
            false); // (m,o,l)->()
        visitLabel(mv, endCreateList);

        // Read the values from the reader.
        mv.visitVarInsn(ALOAD, READER_INDEX);
        mv.visitVarInsn(ALOAD, READ_VALUE_LIST_INDEX);
        if (needPacked) {
          mv.visitLdcInsn(packed ? 1 : 0);
        }
        if (needObjectClass) {
          mv.visitLdcInsn(CLASS_TYPE);
        }
        mv.visitMethodInsn(
            INVOKEINTERFACE, READER_NAME, readMethodName, readMethodDescriptor, true);
      } else {
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
        mv.visitLdcInsn(fieldOffset);
        mv.visitVarInsn(ALOAD, READER_INDEX);
        mv.visitMethodInsn(
            INVOKEINTERFACE, READER_NAME, readMethodName, readMethodDescriptor, true);

        mv.visitMethodInsn(
            INVOKESTATIC, UNSAFE_UTIL_NAME, unsafePutName, unsafePutDescriptor, false);
      }
    }
  }

  private static final class MinimalCodeFieldProcessor {
    private final String unsafeWriteName;
    private final String unsafeWriteDescriptor;
    private final String safeWriteName;
    private final String safeWriteDescriptor;
    private final String unsafeReadName;
    private final String unsafeReadDescriptor;
    private final String safeReadName;
    private final String safeReadDescriptor;
    private final String getOrCreateListName;
    private final String getOrCreateListDescriptor;
    private final boolean packed;
    private final boolean needPacked;
    private final boolean needObjectClass;

    MinimalCodeFieldProcessor(FieldType fieldType) {
      try {
        Method unsafeWriteMethod;
        Method safeWriteMethod;
        Method unsafeReadMethod;
        Method safeReadMethod;
        boolean needPacked = false;
        boolean needObjectClass = false;
        switch (fieldType) {
          case DOUBLE:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteDouble", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeDouble", int.class, double.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadDouble", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readDouble");
            break;
          case FLOAT:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteFloat", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeFloat", int.class, float.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadFloat", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readFloat");
            break;
          case INT64:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteInt64", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeInt64", int.class, long.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadInt64", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readInt64");
            break;
          case UINT64:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteUInt64", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeUInt64", int.class, long.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadUInt64", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readUInt64");
            break;
          case INT32:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteInt32", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeInt32", int.class, int.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadInt32", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readInt32");
            break;
          case FIXED64:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteFixed64", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeFixed64", int.class, long.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadFixed64", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readFixed64");
            break;
          case FIXED32:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteFixed32", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeFixed32", int.class, int.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadFixed32", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readFixed32");
            break;
          case BOOL:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteBool", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeBool", int.class, boolean.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadBool", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readBool");
            break;
          case STRING:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteString", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeString", int.class, String.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadString", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readString");
            break;
          case MESSAGE:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteMessage", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeMessage", int.class, Object.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadMessage", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readMessage");
            break;
          case BYTES:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteBytes", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeBytes", int.class, ByteString.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadBytes", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readBytes");
            break;
          case UINT32:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteUInt32", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeUInt32", int.class, int.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadUInt32", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readUInt32");
            break;
          case ENUM:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteEnum", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class.getDeclaredMethod("writeEnum", int.class, int.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadEnum", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readEnum");
            break;
          case SFIXED32:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSFixed32", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeSFixed32", int.class, int.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadSFixed32", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readSFixed32");
            break;
          case SFIXED64:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSFixed64", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeSFixed64", int.class, long.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadSFixed64", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readSFixed64");
            break;
          case SINT32:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSInt32", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeSInt32", int.class, int.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadSInt32", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readSInt32");
            break;
          case SINT64:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSInt64", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeSInt64", int.class, long.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod("unsafeReadSInt64", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readSInt64");
            break;
          case DOUBLE_LIST:
          case DOUBLE_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteDoubleList",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeDoubleList", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadDoubleList",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readDoubleList", List.class, boolean.class);
            break;
          case FLOAT_LIST:
          case FLOAT_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteFloatList",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeFloatList", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadFloatList",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readFloatList", List.class, boolean.class);
            break;
          case INT64_LIST:
          case INT64_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteInt64List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeInt64List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadInt64List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readInt64List", List.class, boolean.class);
            break;
          case UINT64_LIST:
          case UINT64_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteUInt64List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeUInt64List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadUInt64List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readUInt64List", List.class, boolean.class);
            break;
          case INT32_LIST:
          case INT32_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteInt32List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeInt32List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadInt32List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readInt32List", List.class, boolean.class);
            break;
          case FIXED64_LIST:
          case FIXED64_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteFixed64List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeFixed64List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadFixed64List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readFixed64List", List.class, boolean.class);
            break;
          case FIXED32_LIST:
          case FIXED32_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteFixed32List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeFixed32List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadFixed32List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readFixed32List", List.class, boolean.class);
            break;
          case BOOL_LIST:
          case BOOL_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteBoolList",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeBoolList", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadBoolList",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readBoolList", List.class, boolean.class);
            break;
          case STRING_LIST:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteStringList", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeStringList", int.class, List.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadStringList", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readStringList", List.class);
            break;
          case MESSAGE_LIST:
            needObjectClass = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteMessageList",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeMessageList", int.class, List.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadMessageList",
                        Object.class,
                        long.class,
                        Reader.class,
                        Class.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readMessageList", List.class, Class.class);
            break;
          case BYTES_LIST:
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteBytesList", int.class, Object.class, long.class, Writer.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod("writeBytesList", int.class, List.class, Writer.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadBytesList", Object.class, long.class, Reader.class);
            safeReadMethod = Reader.class.getDeclaredMethod("readBytesList", List.class);
            break;
          case UINT32_LIST:
          case UINT32_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteUInt32List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeUInt32List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadUInt32List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readUInt32List", List.class, boolean.class);
            break;
          case ENUM_LIST:
          case ENUM_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteEnumList",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeEnumList", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadEnumList",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readEnumList", List.class, boolean.class);
            break;
          case SFIXED32_LIST:
          case SFIXED32_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSFixed32List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeSFixed32List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadSFixed32List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readSFixed32List", List.class, boolean.class);
            break;
          case SFIXED64_LIST:
          case SFIXED64_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSFixed64List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeSFixed64List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadSFixed64List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readSFixed64List", List.class, boolean.class);
            break;
          case SINT32_LIST:
          case SINT32_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSInt32List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeSInt32List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadSInt32List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readSInt32List", List.class, boolean.class);
            break;
          case SINT64_LIST:
          case SINT64_LIST_PACKED:
            needPacked = true;
            unsafeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeWriteSInt64List",
                        int.class,
                        Object.class,
                        long.class,
                        Writer.class,
                        boolean.class);
            safeWriteMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "writeSInt64List", int.class, List.class, Writer.class, boolean.class);
            unsafeReadMethod =
                SchemaUtil.class
                    .getDeclaredMethod(
                        "unsafeReadSInt64List",
                        Object.class,
                        long.class,
                        Reader.class,
                        boolean.class);
            safeReadMethod =
                Reader.class.getDeclaredMethod("readSInt64List", List.class, boolean.class);
            break;
          default:
            throw new IllegalArgumentException("Unsupported FieldType: " + fieldType);
        }
        unsafeWriteName = unsafeWriteMethod.getName();
        unsafeWriteDescriptor = Type.getMethodDescriptor(unsafeWriteMethod);
        safeWriteName = safeWriteMethod.getName();
        safeWriteDescriptor = Type.getMethodDescriptor(safeWriteMethod);
        unsafeReadName = unsafeReadMethod.getName();
        unsafeReadDescriptor = Type.getMethodDescriptor(unsafeReadMethod);
        safeReadName = safeReadMethod.getName();
        safeReadDescriptor = Type.getMethodDescriptor(safeReadMethod);

        Method getOrCreateListMethod =
            SchemaUtil.class.getDeclaredMethod("getOrCreateList", List.class);
        getOrCreateListName = getOrCreateListMethod.getName();
        getOrCreateListDescriptor = Type.getMethodDescriptor(getOrCreateListMethod);
        packed = fieldType.isPacked();
        this.needPacked = needPacked;
        this.needObjectClass = needObjectClass;
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }

    void write(
        String messageClassName,
        MethodVisitor mv,
        FieldDescriptor fieldDescriptor,
        boolean hasPackageAccess,
        boolean preferUnsafeAccess) {
      if (preferUnsafeAccess && UnsafeUtil.isSupported()) {
        unsafeWrite(mv, fieldDescriptor);
      } else if (isAccessible(hasPackageAccess, fieldDescriptor)) {
        safeWrite(messageClassName, mv, fieldDescriptor);
      } else if (UnsafeUtil.isSupported()) {
        unsafeWrite(mv, fieldDescriptor);
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Unable to access field %s in class %s",
                fieldDescriptor.getField().getName(), messageClassName));
      }
    }

    void read(
        String messageClassName,
        MethodVisitor mv,
        FieldDescriptor fieldDescriptor,
        boolean hasPackageAccess,
        boolean preferUnsafeAccess) {
      if (preferUnsafeAccess && UnsafeUtil.isSupported()) {
        unsafeRead(mv, fieldDescriptor);
      } else if (isAccessible(hasPackageAccess, fieldDescriptor)) {
        safeRead(messageClassName, mv, fieldDescriptor);
      } else if (UnsafeUtil.isSupported()) {
        unsafeRead(mv, fieldDescriptor);
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Unable to access field %s in class %s",
                fieldDescriptor.getField().getName(), messageClassName));
      }
    }

    private void safeWrite(
        String messageClassName, MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      mv.visitLdcInsn(fieldDescriptor.getFieldNumber());

      // Get the field value.
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitFieldInsn(
          GETFIELD,
          messageClassName,
          fieldDescriptor.getField().getName(),
          Type.getDescriptor(fieldDescriptor.getField().getType()));

      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      if (needPacked) {
        mv.visitLdcInsn(packed ? 1 : 0);
      }
      mv.visitMethodInsn(INVOKESTATIC, SCHEMA_UTIL_NAME, safeWriteName, safeWriteDescriptor, false);
    }

    private void unsafeWrite(MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      mv.visitLdcInsn(fieldDescriptor.getFieldNumber());
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(UnsafeUtil.objectFieldOffset(fieldDescriptor.getField()));
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      if (needPacked) {
        mv.visitLdcInsn(packed ? 1 : 0);
      }
      mv.visitMethodInsn(
          INVOKESTATIC, SCHEMA_UTIL_NAME, unsafeWriteName, unsafeWriteDescriptor, false);
    }

    private void safeRead(
        String messageClassName, MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      if (fieldDescriptor.getType().isList()) {
        // Get or create the list.
        String name = fieldDescriptor.getField().getName();
        String descriptor = Type.getDescriptor(fieldDescriptor.getField().getType());

        mv.visitVarInsn(ALOAD, READER_INDEX);
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
        mv.visitTypeInsn(CHECKCAST, messageClassName);
        mv.visitInsn(DUP);

        // Get or create the value list.
        mv.visitFieldInsn(GETFIELD, messageClassName, name, descriptor);
        mv.visitMethodInsn(
            INVOKESTATIC, SCHEMA_UTIL_NAME, getOrCreateListName, getOrCreateListDescriptor, false);
        mv.visitInsn(
            DUP_X1); // Duplicate the list before the message on the stack (list, message, list)
        mv.visitFieldInsn(PUTFIELD, messageClassName, name, descriptor);

        // Read the values from the reader.
        if (needPacked) {
          mv.visitLdcInsn(packed ? 1 : 0);
        }
        if (needObjectClass) {
          mv.visitLdcInsn(CLASS_TYPE);
        }
        mv.visitMethodInsn(INVOKEINTERFACE, READER_NAME, safeReadName, safeReadDescriptor, true);
      } else {
        Type fieldType = Type.getType(fieldDescriptor.getField().getType());
        mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
        mv.visitTypeInsn(CHECKCAST, messageClassName);

        // Get the value.
        mv.visitVarInsn(ALOAD, READER_INDEX);
        mv.visitMethodInsn(INVOKEINTERFACE, READER_NAME, safeReadName, safeReadDescriptor, true);
        if (fieldDescriptor.getType().getJavaType() == JavaType.MESSAGE) {
          mv.visitTypeInsn(CHECKCAST, fieldType.getInternalName());
        }

        // Set the value on the field.
        mv.visitFieldInsn(
            PUTFIELD,
            messageClassName,
            fieldDescriptor.getField().getName(),
            fieldType.getDescriptor());
      }
    }

    private void unsafeRead(MethodVisitor mv, FieldDescriptor fieldDescriptor) {
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(UnsafeUtil.objectFieldOffset(fieldDescriptor.getField()));
      mv.visitVarInsn(ALOAD, READER_INDEX);
      if (needPacked) {
        mv.visitLdcInsn(packed ? 1 : 0);
      }
      if (needObjectClass) {
        mv.visitLdcInsn(CLASS_TYPE);
      }
      mv.visitMethodInsn(
          INVOKESTATIC, SCHEMA_UTIL_NAME, unsafeReadName, unsafeReadDescriptor, false);
    }
  }

  private static boolean isAccessible(
      boolean packagePrivateAccessSupported, FieldDescriptor fieldDescriptor) {
    int mod = fieldDescriptor.getField().getModifiers();
    return Modifier.isPublic(mod) || (packagePrivateAccessSupported && !Modifier.isPrivate(mod));
  }
}
