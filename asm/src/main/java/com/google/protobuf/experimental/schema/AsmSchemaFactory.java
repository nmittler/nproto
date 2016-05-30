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

import com.google.protobuf.experimental.ByteString;
import com.google.protobuf.experimental.Internal;
import com.google.protobuf.experimental.JavaType;
import com.google.protobuf.experimental.Reader;
import com.google.protobuf.experimental.Writer;
import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.BeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.PropertyDescriptor;
import com.google.protobuf.experimental.descriptor.PropertyType;
import com.google.protobuf.experimental.util.SchemaUtil;
import com.google.protobuf.experimental.util.UnsafeUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

@Internal
public final class AsmSchemaFactory implements SchemaFactory {
  private static final int MESSAGE_INDEX = 1;
  private static final int WRITER_INDEX = 2;
  private static final int READER_INDEX = 2;

  private static final String OBJECT_NAME = getInternalName(Object.class);
  private static final String SCHEMA_NAME = getInternalName(Schema.class);
  private static final String READER_NAME = getInternalName(Reader.class);
  private static final String SCHEMA_UTIL_NAME = getInternalName(SchemaUtil.class);
  private static final Type ENUM_TYPE = Type.getType(Enum.class);

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

  // SchemaUtil methods.
  private static final String WRITE_DOUBLE_NAME;
  private static final String WRITE_DOUBLE_DESCRIPTOR;
  private static final String WRITE_FLOAT_NAME;
  private static final String WRITE_FLOAT_DESCRIPTOR;
  private static final String WRITE_INT64_NAME;
  private static final String WRITE_INT64_DESCRIPTOR;
  private static final String WRITE_UINT64_NAME;
  private static final String WRITE_UINT64_DESCRIPTOR;
  private static final String WRITE_SINT64_NAME;
  private static final String WRITE_SINT64_DESCRIPTOR;
  private static final String WRITE_FIXED64_NAME;
  private static final String WRITE_FIXED64_DESCRIPTOR;
  private static final String WRITE_SFIXED64_NAME;
  private static final String WRITE_SFIXED64_DESCRIPTOR;
  private static final String WRITE_INT32_NAME;
  private static final String WRITE_INT32_DESCRIPTOR;
  private static final String WRITE_UINT32_NAME;
  private static final String WRITE_UINT32_DESCRIPTOR;
  private static final String WRITE_SINT32_NAME;
  private static final String WRITE_SINT32_DESCRIPTOR;
  private static final String WRITE_FIXED32_NAME;
  private static final String WRITE_FIXED32_DESCRIPTOR;
  private static final String WRITE_SFIXED32_NAME;
  private static final String WRITE_SFIXED32_DESCRIPTOR;
  private static final String WRITE_ENUM_DESCRIPTOR;
  private static final String WRITE_ENUM_NAME;
  private static final String WRITE_BOOL_DESCRIPTOR;
  private static final String WRITE_BOOL_NAME;
  private static final String WRITE_STRING_DESCRIPTOR;
  private static final String WRITE_STRING_NAME;
  private static final String WRITE_BYTES_DESCRIPTOR;
  private static final String WRITE_BYTES_NAME;
  private static final String WRITE_MESSAGE_DESCRIPTOR;
  private static final String WRITE_MESSAGE_NAME;
  private static final String WRITE_DOUBLE_LIST_DESCRIPTOR;
  private static final String WRITE_DOUBLE_LIST_NAME;
  private static final String WRITE_FLOAT_LIST_DESCRIPTOR;
  private static final String WRITE_FLOAT_LIST_NAME;
  private static final String WRITE_INT64_LIST_DESCRIPTOR;
  private static final String WRITE_INT64_LIST_NAME;
  private static final String WRITE_UINT64_LIST_DESCRIPTOR;
  private static final String WRITE_UINT64_LIST_NAME;
  private static final String WRITE_SINT64_LIST_DESCRIPTOR;
  private static final String WRITE_SINT64_LIST_NAME;
  private static final String WRITE_FIXED64_LIST_DESCRIPTOR;
  private static final String WRITE_FIXED64_LIST_NAME;
  private static final String WRITE_SFIXED64_LIST_DESCRIPTOR;
  private static final String WRITE_SFIXED64_LIST_NAME;
  private static final String WRITE_INT32_LIST_DESCRIPTOR;
  private static final String WRITE_INT32_LIST_NAME;
  private static final String WRITE_UINT32_LIST_DESCRIPTOR;
  private static final String WRITE_UINT32_LIST_NAME;
  private static final String WRITE_SINT32_LIST_DESCRIPTOR;
  private static final String WRITE_SINT32_LIST_NAME;
  private static final String WRITE_FIXED32_LIST_DESCRIPTOR;
  private static final String WRITE_FIXED32_LIST_NAME;
  private static final String WRITE_SFIXED32_LIST_DESCRIPTOR;
  private static final String WRITE_SFIXED32_LIST_NAME;
  private static final String WRITE_ENUM_LIST_DESCRIPTOR;
  private static final String WRITE_ENUM_LIST_NAME;
  private static final String WRITE_BOOL_LIST_DESCRIPTOR;
  private static final String WRITE_BOOL_LIST_NAME;
  private static final String WRITE_STRING_LIST_DESCRIPTOR;
  private static final String WRITE_STRING_LIST_NAME;
  private static final String WRITE_BYTES_LIST_DESCRIPTOR;
  private static final String WRITE_BYTES_LIST_NAME;
  private static final String WRITE_MESSAGE_LIST_DESCRIPTOR;
  private static final String WRITE_MESSAGE_LIST_NAME;

  private static final String UNSAFE_WRITE_DOUBLE_NAME;
  private static final String UNSAFE_WRITE_DOUBLE_DESCRIPTOR;
  private static final String UNSAFE_WRITE_FLOAT_NAME;
  private static final String UNSAFE_WRITE_FLOAT_DESCRIPTOR;
  private static final String UNSAFE_WRITE_INT64_NAME;
  private static final String UNSAFE_WRITE_INT64_DESCRIPTOR;
  private static final String UNSAFE_WRITE_UINT64_NAME;
  private static final String UNSAFE_WRITE_UINT64_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SINT64_NAME;
  private static final String UNSAFE_WRITE_SINT64_DESCRIPTOR;
  private static final String UNSAFE_WRITE_FIXED64_NAME;
  private static final String UNSAFE_WRITE_FIXED64_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SFIXED64_NAME;
  private static final String UNSAFE_WRITE_SFIXED64_DESCRIPTOR;
  private static final String UNSAFE_WRITE_INT32_NAME;
  private static final String UNSAFE_WRITE_INT32_DESCRIPTOR;
  private static final String UNSAFE_WRITE_UINT32_NAME;
  private static final String UNSAFE_WRITE_UINT32_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SINT32_NAME;
  private static final String UNSAFE_WRITE_SINT32_DESCRIPTOR;
  private static final String UNSAFE_WRITE_FIXED32_NAME;
  private static final String UNSAFE_WRITE_FIXED32_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SFIXED32_NAME;
  private static final String UNSAFE_WRITE_SFIXED32_DESCRIPTOR;
  private static final String UNSAFE_WRITE_ENUM_DESCRIPTOR;
  private static final String UNSAFE_WRITE_ENUM_NAME;
  private static final String UNSAFE_WRITE_BOOL_DESCRIPTOR;
  private static final String UNSAFE_WRITE_BOOL_NAME;
  private static final String UNSAFE_WRITE_STRING_DESCRIPTOR;
  private static final String UNSAFE_WRITE_STRING_NAME;
  private static final String UNSAFE_WRITE_BYTES_DESCRIPTOR;
  private static final String UNSAFE_WRITE_BYTES_NAME;
  private static final String UNSAFE_WRITE_MESSAGE_DESCRIPTOR;
  private static final String UNSAFE_WRITE_MESSAGE_NAME;

  private static final String UNSAFE_WRITE_DOUBLE_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_DOUBLE_LIST_NAME;
  private static final String UNSAFE_WRITE_FLOAT_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_FLOAT_LIST_NAME;
  private static final String UNSAFE_WRITE_INT64_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_INT64_LIST_NAME;
  private static final String UNSAFE_WRITE_UINT64_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_UINT64_LIST_NAME;
  private static final String UNSAFE_WRITE_SINT64_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SINT64_LIST_NAME;
  private static final String UNSAFE_WRITE_FIXED64_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_FIXED64_LIST_NAME;
  private static final String UNSAFE_WRITE_SFIXED64_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SFIXED64_LIST_NAME;
  private static final String UNSAFE_WRITE_INT32_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_INT32_LIST_NAME;
  private static final String UNSAFE_WRITE_UINT32_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_UINT32_LIST_NAME;
  private static final String UNSAFE_WRITE_SINT32_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SINT32_LIST_NAME;
  private static final String UNSAFE_WRITE_FIXED32_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_FIXED32_LIST_NAME;
  private static final String UNSAFE_WRITE_SFIXED32_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_SFIXED32_LIST_NAME;
  private static final String UNSAFE_WRITE_ENUM_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_ENUM_LIST_NAME;
  private static final String UNSAFE_WRITE_BOOL_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_BOOL_LIST_NAME;
  private static final String UNSAFE_WRITE_STRING_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_STRING_LIST_NAME;
  private static final String UNSAFE_WRITE_BYTES_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_BYTES_LIST_NAME;
  private static final String UNSAFE_WRITE_MESSAGE_LIST_DESCRIPTOR;
  private static final String UNSAFE_WRITE_MESSAGE_LIST_NAME;

  private static final String UNSAFE_READ_DOUBLE_NAME;
  private static final String UNSAFE_READ_DOUBLE_DESCRIPTOR;
  private static final String UNSAFE_READ_FLOAT_NAME;
  private static final String UNSAFE_READ_FLOAT_DESCRIPTOR;
  private static final String UNSAFE_READ_INT64_NAME;
  private static final String UNSAFE_READ_INT64_DESCRIPTOR;
  private static final String UNSAFE_READ_UINT64_NAME;
  private static final String UNSAFE_READ_UINT64_DESCRIPTOR;
  private static final String UNSAFE_READ_SINT64_NAME;
  private static final String UNSAFE_READ_SINT64_DESCRIPTOR;
  private static final String UNSAFE_READ_FIXED64_NAME;
  private static final String UNSAFE_READ_FIXED64_DESCRIPTOR;
  private static final String UNSAFE_READ_SFIXED64_NAME;
  private static final String UNSAFE_READ_SFIXED64_DESCRIPTOR;
  private static final String UNSAFE_READ_INT32_NAME;
  private static final String UNSAFE_READ_INT32_DESCRIPTOR;
  private static final String UNSAFE_READ_UINT32_NAME;
  private static final String UNSAFE_READ_UINT32_DESCRIPTOR;
  private static final String UNSAFE_READ_SINT32_NAME;
  private static final String UNSAFE_READ_SINT32_DESCRIPTOR;
  private static final String UNSAFE_READ_FIXED32_NAME;
  private static final String UNSAFE_READ_FIXED32_DESCRIPTOR;
  private static final String UNSAFE_READ_SFIXED32_NAME;
  private static final String UNSAFE_READ_SFIXED32_DESCRIPTOR;
  private static final String UNSAFE_READ_ENUM_DESCRIPTOR;
  private static final String UNSAFE_READ_ENUM_NAME;
  private static final String UNSAFE_READ_BOOL_DESCRIPTOR;
  private static final String UNSAFE_READ_BOOL_NAME;
  private static final String UNSAFE_READ_STRING_DESCRIPTOR;
  private static final String UNSAFE_READ_STRING_NAME;
  private static final String UNSAFE_READ_BYTES_DESCRIPTOR;
  private static final String UNSAFE_READ_BYTES_NAME;
  private static final String UNSAFE_READ_MESSAGE_DESCRIPTOR;
  private static final String UNSAFE_READ_MESSAGE_NAME;

  private static final String UNSAFE_READ_DOUBLE_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_DOUBLE_LIST_NAME;
  private static final String UNSAFE_READ_FLOAT_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_FLOAT_LIST_NAME;
  private static final String UNSAFE_READ_INT64_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_INT64_LIST_NAME;
  private static final String UNSAFE_READ_UINT64_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_UINT64_LIST_NAME;
  private static final String UNSAFE_READ_SINT64_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_SINT64_LIST_NAME;
  private static final String UNSAFE_READ_FIXED64_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_FIXED64_LIST_NAME;
  private static final String UNSAFE_READ_SFIXED64_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_SFIXED64_LIST_NAME;
  private static final String UNSAFE_READ_INT32_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_INT32_LIST_NAME;
  private static final String UNSAFE_READ_UINT32_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_UINT32_LIST_NAME;
  private static final String UNSAFE_READ_SINT32_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_SINT32_LIST_NAME;
  private static final String UNSAFE_READ_FIXED32_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_FIXED32_LIST_NAME;
  private static final String UNSAFE_READ_SFIXED32_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_SFIXED32_LIST_NAME;
  private static final String UNSAFE_READ_ENUM_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_ENUM_LIST_NAME;
  private static final String UNSAFE_READ_BOOL_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_BOOL_LIST_NAME;
  private static final String UNSAFE_READ_STRING_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_STRING_LIST_NAME;
  private static final String UNSAFE_READ_BYTES_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_BYTES_LIST_NAME;
  private static final String UNSAFE_READ_MESSAGE_LIST_DESCRIPTOR;
  private static final String UNSAFE_READ_MESSAGE_LIST_NAME;

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
      method = Reader.class.getDeclaredMethod("fieldNumber");
      FIELD_NUMBER_NAME = method.getName();
      FIELD_NUMBER_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = Reader.class.getDeclaredMethod("skipField");
      SKIP_FIELD_NAME = method.getName();
      SKIP_FIELD_DESCRIPTOR = Type.getMethodDescriptor(method);

      // SchemaUtil methods.
      method = SchemaUtil.class.getDeclaredMethod("writeDouble", int.class, double.class, Writer.class);
      WRITE_DOUBLE_NAME = method.getName();
      WRITE_DOUBLE_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeFloat", int.class, float.class, Writer.class);
      WRITE_FLOAT_NAME = method.getName();
      WRITE_FLOAT_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeInt64", int.class, long.class, Writer.class);
      WRITE_INT64_NAME = method.getName();
      WRITE_INT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeUInt64", int.class, long.class, Writer.class);
      WRITE_UINT64_NAME = method.getName();
      WRITE_UINT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSInt64", int.class, long.class, Writer.class);
      WRITE_SINT64_NAME = method.getName();
      WRITE_SINT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeFixed64", int.class, long.class, Writer.class);
      WRITE_FIXED64_NAME = method.getName();
      WRITE_FIXED64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSFixed64", int.class, long.class, Writer.class);
      WRITE_SFIXED64_NAME = method.getName();
      WRITE_SFIXED64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeInt32", int.class, int.class, Writer.class);
      WRITE_INT32_NAME = method.getName();
      WRITE_INT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeUInt32", int.class, int.class, Writer.class);
      WRITE_UINT32_NAME = method.getName();
      WRITE_UINT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSInt32", int.class, int.class, Writer.class);
      WRITE_SINT32_NAME = method.getName();
      WRITE_SINT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeFixed32", int.class, int.class, Writer.class);
      WRITE_FIXED32_NAME = method.getName();
      WRITE_FIXED32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSFixed32", int.class, int.class, Writer.class);
      WRITE_SFIXED32_NAME = method.getName();
      WRITE_SFIXED32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeEnum", int.class, Enum.class, Writer.class);
      WRITE_ENUM_NAME = method.getName();
      WRITE_ENUM_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeBool", int.class, boolean.class, Writer.class);
      WRITE_BOOL_NAME = method.getName();
      WRITE_BOOL_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeString", int.class, String.class, Writer.class);
      WRITE_STRING_NAME = method.getName();
      WRITE_STRING_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeBytes", int.class, ByteString.class, Writer.class);
      WRITE_BYTES_NAME = method.getName();
      WRITE_BYTES_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeMessage", int.class, Object.class, Writer.class);
      WRITE_MESSAGE_NAME = method.getName();
      WRITE_MESSAGE_DESCRIPTOR = Type.getMethodDescriptor(method);

      method = SchemaUtil.class.getDeclaredMethod("writeDoubleList", int.class, List.class, Writer.class);
      WRITE_DOUBLE_LIST_NAME = method.getName();
      WRITE_DOUBLE_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeFloatList", int.class, List.class, Writer.class);
      WRITE_FLOAT_LIST_NAME = method.getName();
      WRITE_FLOAT_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeInt64List", int.class, List.class, Writer.class);
      WRITE_INT64_LIST_NAME = method.getName();
      WRITE_INT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeUInt64List", int.class, List.class, Writer.class);
      WRITE_UINT64_LIST_NAME = method.getName();
      WRITE_UINT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSInt64List", int.class, List.class, Writer.class);
      WRITE_SINT64_LIST_NAME = method.getName();
      WRITE_SINT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeFixed64List", int.class, List.class, Writer.class);
      WRITE_FIXED64_LIST_NAME = method.getName();
      WRITE_FIXED64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSFixed64List", int.class, List.class, Writer.class);
      WRITE_SFIXED64_LIST_NAME = method.getName();
      WRITE_SFIXED64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeInt32List", int.class, List.class, Writer.class);
      WRITE_INT32_LIST_NAME = method.getName();
      WRITE_INT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeUInt32List", int.class, List.class, Writer.class);
      WRITE_UINT32_LIST_NAME = method.getName();
      WRITE_UINT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSInt32List", int.class, List.class, Writer.class);
      WRITE_SINT32_LIST_NAME = method.getName();
      WRITE_SINT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeFixed32List", int.class, List.class, Writer.class);
      WRITE_FIXED32_LIST_NAME = method.getName();
      WRITE_FIXED32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeSFixed32List", int.class, List.class, Writer.class);
      WRITE_SFIXED32_LIST_NAME = method.getName();
      WRITE_SFIXED32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeEnumList", int.class, List.class, Writer.class);
      WRITE_ENUM_LIST_NAME = method.getName();
      WRITE_ENUM_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeBoolList", int.class, List.class, Writer.class);
      WRITE_BOOL_LIST_NAME = method.getName();
      WRITE_BOOL_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeStringList", int.class, List.class, Writer.class);
      WRITE_STRING_LIST_NAME = method.getName();
      WRITE_STRING_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeBytesList", int.class, List.class, Writer.class);
      WRITE_BYTES_LIST_NAME = method.getName();
      WRITE_BYTES_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("writeMessageList", int.class, List.class, Writer.class);
      WRITE_MESSAGE_LIST_NAME = method.getName();
      WRITE_MESSAGE_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);

      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteDouble", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_DOUBLE_NAME = method.getName();
      UNSAFE_WRITE_DOUBLE_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteFloat", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_FLOAT_NAME = method.getName();
      UNSAFE_WRITE_FLOAT_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteInt64", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_INT64_NAME = method.getName();
      UNSAFE_WRITE_INT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteUInt64", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_UINT64_NAME = method.getName();
      UNSAFE_WRITE_UINT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSInt64", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SINT64_NAME = method.getName();
      UNSAFE_WRITE_SINT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteFixed64", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_FIXED64_NAME = method.getName();
      UNSAFE_WRITE_FIXED64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSFixed64", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SFIXED64_NAME = method.getName();
      UNSAFE_WRITE_SFIXED64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteInt32", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_INT32_NAME = method.getName();
      UNSAFE_WRITE_INT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteUInt32", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_UINT32_NAME = method.getName();
      UNSAFE_WRITE_UINT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSInt32", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SINT32_NAME = method.getName();
      UNSAFE_WRITE_SINT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteFixed32", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_FIXED32_NAME = method.getName();
      UNSAFE_WRITE_FIXED32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSFixed32", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SFIXED32_NAME = method.getName();
      UNSAFE_WRITE_SFIXED32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteEnum", int.class, Object.class, long.class, Writer.class, Class.class);
      UNSAFE_WRITE_ENUM_NAME = method.getName();
      UNSAFE_WRITE_ENUM_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteBool", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_BOOL_NAME = method.getName();
      UNSAFE_WRITE_BOOL_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteString", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_STRING_NAME = method.getName();
      UNSAFE_WRITE_STRING_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteBytes", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_BYTES_NAME = method.getName();
      UNSAFE_WRITE_BYTES_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteMessage", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_MESSAGE_NAME = method.getName();
      UNSAFE_WRITE_MESSAGE_DESCRIPTOR = Type.getMethodDescriptor(method);

      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteDoubleList", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_DOUBLE_LIST_NAME = method.getName();
      UNSAFE_WRITE_DOUBLE_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteFloatList", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_FLOAT_LIST_NAME = method.getName();
      UNSAFE_WRITE_FLOAT_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteInt64List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_INT64_LIST_NAME = method.getName();
      UNSAFE_WRITE_INT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteUInt64List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_UINT64_LIST_NAME = method.getName();
      UNSAFE_WRITE_UINT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSInt64List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SINT64_LIST_NAME = method.getName();
      UNSAFE_WRITE_SINT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteFixed64List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_FIXED64_LIST_NAME = method.getName();
      UNSAFE_WRITE_FIXED64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSFixed64List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SFIXED64_LIST_NAME = method.getName();
      UNSAFE_WRITE_SFIXED64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteInt32List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_INT32_LIST_NAME = method.getName();
      UNSAFE_WRITE_INT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteUInt32List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_UINT32_LIST_NAME = method.getName();
      UNSAFE_WRITE_UINT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSInt32List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SINT32_LIST_NAME = method.getName();
      UNSAFE_WRITE_SINT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteFixed32List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_FIXED32_LIST_NAME = method.getName();
      UNSAFE_WRITE_FIXED32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteSFixed32List", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_SFIXED32_LIST_NAME = method.getName();
      UNSAFE_WRITE_SFIXED32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteEnumList", int.class, Object.class, long.class, Writer.class, Class.class);
      UNSAFE_WRITE_ENUM_LIST_NAME = method.getName();
      UNSAFE_WRITE_ENUM_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteBoolList", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_BOOL_LIST_NAME = method.getName();
      UNSAFE_WRITE_BOOL_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteStringList", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_STRING_LIST_NAME = method.getName();
      UNSAFE_WRITE_STRING_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteBytesList", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_BYTES_LIST_NAME = method.getName();
      UNSAFE_WRITE_BYTES_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeWriteMessageList", int.class, Object.class, long.class, Writer.class);
      UNSAFE_WRITE_MESSAGE_LIST_NAME = method.getName();
      UNSAFE_WRITE_MESSAGE_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);

      method = SchemaUtil.class.getDeclaredMethod("unsafeReadDouble", Object.class, long.class, Reader.class);
      UNSAFE_READ_DOUBLE_NAME = method.getName();
      UNSAFE_READ_DOUBLE_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadFloat", Object.class, long.class, Reader.class);
      UNSAFE_READ_FLOAT_NAME = method.getName();
      UNSAFE_READ_FLOAT_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadInt64", Object.class, long.class, Reader.class);
      UNSAFE_READ_INT64_NAME = method.getName();
      UNSAFE_READ_INT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadUInt64", Object.class, long.class, Reader.class);
      UNSAFE_READ_UINT64_NAME = method.getName();
      UNSAFE_READ_UINT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSInt64", Object.class, long.class, Reader.class);
      UNSAFE_READ_SINT64_NAME = method.getName();
      UNSAFE_READ_SINT64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadFixed64", Object.class, long.class, Reader.class);
      UNSAFE_READ_FIXED64_NAME = method.getName();
      UNSAFE_READ_FIXED64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSFixed64", Object.class, long.class, Reader.class);
      UNSAFE_READ_SFIXED64_NAME = method.getName();
      UNSAFE_READ_SFIXED64_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadInt32", Object.class, long.class, Reader.class);
      UNSAFE_READ_INT32_NAME = method.getName();
      UNSAFE_READ_INT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadUInt32", Object.class, long.class, Reader.class);
      UNSAFE_READ_UINT32_NAME = method.getName();
      UNSAFE_READ_UINT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSInt32", Object.class, long.class, Reader.class);
      UNSAFE_READ_SINT32_NAME = method.getName();
      UNSAFE_READ_SINT32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadFixed32", Object.class, long.class, Reader.class);
      UNSAFE_READ_FIXED32_NAME = method.getName();
      UNSAFE_READ_FIXED32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSFixed32", Object.class, long.class, Reader.class);
      UNSAFE_READ_SFIXED32_NAME = method.getName();
      UNSAFE_READ_SFIXED32_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadEnum", Object.class, long.class, Reader.class);
      UNSAFE_READ_ENUM_NAME = method.getName();
      UNSAFE_READ_ENUM_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadBool", Object.class, long.class, Reader.class);
      UNSAFE_READ_BOOL_NAME = method.getName();
      UNSAFE_READ_BOOL_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadString", Object.class, long.class, Reader.class);
      UNSAFE_READ_STRING_NAME = method.getName();
      UNSAFE_READ_STRING_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadBytes", Object.class, long.class, Reader.class);
      UNSAFE_READ_BYTES_NAME = method.getName();
      UNSAFE_READ_BYTES_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadMessage", Object.class, long.class, Reader.class);
      UNSAFE_READ_MESSAGE_NAME = method.getName();
      UNSAFE_READ_MESSAGE_DESCRIPTOR = Type.getMethodDescriptor(method);

      method = SchemaUtil.class.getDeclaredMethod("unsafeReadDoubleList", Object.class, long.class, Reader.class);
      UNSAFE_READ_DOUBLE_LIST_NAME = method.getName();
      UNSAFE_READ_DOUBLE_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadFloatList", Object.class, long.class, Reader.class);
      UNSAFE_READ_FLOAT_LIST_NAME = method.getName();
      UNSAFE_READ_FLOAT_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadInt64List", Object.class, long.class, Reader.class);
      UNSAFE_READ_INT64_LIST_NAME = method.getName();
      UNSAFE_READ_INT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadUInt64List", Object.class, long.class, Reader.class);
      UNSAFE_READ_UINT64_LIST_NAME = method.getName();
      UNSAFE_READ_UINT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSInt64List", Object.class, long.class, Reader.class);
      UNSAFE_READ_SINT64_LIST_NAME = method.getName();
      UNSAFE_READ_SINT64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadFixed64List", Object.class, long.class, Reader.class);
      UNSAFE_READ_FIXED64_LIST_NAME = method.getName();
      UNSAFE_READ_FIXED64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSFixed64List", Object.class, long.class, Reader.class);
      UNSAFE_READ_SFIXED64_LIST_NAME = method.getName();
      UNSAFE_READ_SFIXED64_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadInt32List", Object.class, long.class, Reader.class);
      UNSAFE_READ_INT32_LIST_NAME = method.getName();
      UNSAFE_READ_INT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadUInt32List", Object.class, long.class, Reader.class);
      UNSAFE_READ_UINT32_LIST_NAME = method.getName();
      UNSAFE_READ_UINT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSInt32List", Object.class, long.class, Reader.class);
      UNSAFE_READ_SINT32_LIST_NAME = method.getName();
      UNSAFE_READ_SINT32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadFixed32List", Object.class, long.class, Reader.class);
      UNSAFE_READ_FIXED32_LIST_NAME = method.getName();
      UNSAFE_READ_FIXED32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadSFixed32List", Object.class, long.class, Reader.class);
      UNSAFE_READ_SFIXED32_LIST_NAME = method.getName();
      UNSAFE_READ_SFIXED32_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadEnumList", Object.class, long.class, Reader.class);
      UNSAFE_READ_ENUM_LIST_NAME = method.getName();
      UNSAFE_READ_ENUM_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadBoolList", Object.class, long.class, Reader.class);
      UNSAFE_READ_BOOL_LIST_NAME = method.getName();
      UNSAFE_READ_BOOL_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadStringList", Object.class, long.class, Reader.class);
      UNSAFE_READ_STRING_LIST_NAME = method.getName();
      UNSAFE_READ_STRING_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadBytesList", Object.class, long.class, Reader.class);
      UNSAFE_READ_BYTES_LIST_NAME = method.getName();
      UNSAFE_READ_BYTES_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
      method = SchemaUtil.class.getDeclaredMethod("unsafeReadMessageList", Object.class, long.class, Reader.class);
      UNSAFE_READ_MESSAGE_LIST_NAME = method.getName();
      UNSAFE_READ_MESSAGE_LIST_DESCRIPTOR = Type.getMethodDescriptor(method);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

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
    cv.visit(V1_6, ACC_PUBLIC + ACC_FINAL, schemaClassName, null, OBJECT_NAME,
            new String[]{SCHEMA_NAME});
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
    mv.visitMethodInsn(INVOKESPECIAL, OBJECT_NAME, "<init>", "()V", false);
    mv.visitInsn(RETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }

  private static final class WriteToGenerator {
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
      mv = cv.visitMethod(ACC_PUBLIC, MERGE_FROM_NAME, MERGE_FROM_DESCRIPTOR, null, null);
      mv.visitCode();

      // Create the main labels and visit the start.
      startLabel = new Label();
      endLabel = new Label();
      defaultLabel = new Label();
      visitLabel(startLabel);

      // Get the field number form the reader.
      callReader(mv, FIELD_NUMBER_NAME, FIELD_NUMBER_DESCRIPTOR);

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
      callReader(mv, SKIP_FIELD_NAME, SKIP_FIELD_DESCRIPTOR);
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
    mv.visitMethodInsn(INVOKEINTERFACE, READER_NAME, methodName, methodDescriptor, true);
  }

  private static String getSchemaClassName(Class<?> messageType) {
    return messageType.getName() + "Schema";
  }

  private static final class FieldProcessor {
    private final String unsafeWriteName;
    private final String unsafeWriteDescriptor;
    private final String safeWriteName;
    private final String safeWriteDescriptor;
    private final String unsafeReadName;
    private final String unsafeReadDescriptor;
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
          unsafeWriteName = UNSAFE_WRITE_DOUBLE_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_DOUBLE_DESCRIPTOR;
          safeWriteName = WRITE_DOUBLE_NAME;
          safeWriteDescriptor = WRITE_DOUBLE_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_DOUBLE_NAME;
          unsafeReadDescriptor = UNSAFE_READ_DOUBLE_DESCRIPTOR;
          break;
        case FLOAT:
          unsafeWriteName = UNSAFE_WRITE_FLOAT_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_FLOAT_DESCRIPTOR;
          safeWriteName = WRITE_FLOAT_NAME;
          safeWriteDescriptor = WRITE_FLOAT_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_FLOAT_NAME;
          unsafeReadDescriptor = UNSAFE_READ_FLOAT_DESCRIPTOR;
          break;
        case INT64:
          unsafeWriteName = UNSAFE_WRITE_INT64_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_INT64_DESCRIPTOR;
          safeWriteName = WRITE_INT64_NAME;
          safeWriteDescriptor = WRITE_INT64_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_INT64_NAME;
          unsafeReadDescriptor = UNSAFE_READ_INT64_DESCRIPTOR;
          break;
        case UINT64:
          unsafeWriteName = UNSAFE_WRITE_UINT64_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_UINT64_DESCRIPTOR;
          safeWriteName = WRITE_UINT64_NAME;
          safeWriteDescriptor = WRITE_UINT64_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_UINT64_NAME;
          unsafeReadDescriptor = UNSAFE_READ_UINT64_DESCRIPTOR;
          break;
        case INT32:
          unsafeWriteName = UNSAFE_WRITE_INT32_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_INT32_DESCRIPTOR;
          safeWriteName = WRITE_INT32_NAME;
          safeWriteDescriptor = WRITE_INT32_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_INT32_NAME;
          unsafeReadDescriptor = UNSAFE_READ_INT32_DESCRIPTOR;
          break;
        case FIXED64:
          unsafeWriteName = UNSAFE_WRITE_FIXED64_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_FIXED64_DESCRIPTOR;
          safeWriteName = WRITE_FIXED64_NAME;
          safeWriteDescriptor = WRITE_FIXED64_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_FIXED64_NAME;
          unsafeReadDescriptor = UNSAFE_READ_FIXED64_DESCRIPTOR;
          break;
        case FIXED32:
          unsafeWriteName = UNSAFE_WRITE_FIXED32_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_FIXED32_DESCRIPTOR;
          safeWriteName = WRITE_FIXED32_NAME;
          safeWriteDescriptor = WRITE_FIXED32_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_FIXED32_NAME;
          unsafeReadDescriptor = UNSAFE_READ_FIXED32_DESCRIPTOR;
          break;
        case BOOL:
          unsafeWriteName = UNSAFE_WRITE_BOOL_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_BOOL_DESCRIPTOR;
          safeWriteName = WRITE_BOOL_NAME;
          safeWriteDescriptor = WRITE_BOOL_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_BOOL_NAME;
          unsafeReadDescriptor = UNSAFE_READ_BOOL_DESCRIPTOR;
          break;
        case STRING:
          unsafeWriteName = UNSAFE_WRITE_STRING_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_STRING_DESCRIPTOR;
          safeWriteName = WRITE_STRING_NAME;
          safeWriteDescriptor = WRITE_STRING_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_STRING_NAME;
          unsafeReadDescriptor = UNSAFE_READ_STRING_DESCRIPTOR;
          break;
        case MESSAGE:
          unsafeWriteName = UNSAFE_WRITE_MESSAGE_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_MESSAGE_DESCRIPTOR;
          safeWriteName = WRITE_MESSAGE_NAME;
          safeWriteDescriptor = WRITE_MESSAGE_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_MESSAGE_NAME;
          unsafeReadDescriptor = UNSAFE_READ_MESSAGE_DESCRIPTOR;
          break;
        case BYTES:
          unsafeWriteName = UNSAFE_WRITE_BYTES_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_BYTES_DESCRIPTOR;
          safeWriteName = WRITE_BYTES_NAME;
          safeWriteDescriptor = WRITE_BYTES_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_BYTES_NAME;
          unsafeReadDescriptor = UNSAFE_READ_BYTES_DESCRIPTOR;
          break;
        case UINT32:
          unsafeWriteName = UNSAFE_WRITE_UINT32_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_UINT32_DESCRIPTOR;
          safeWriteName = WRITE_UINT32_NAME;
          safeWriteDescriptor = WRITE_UINT32_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_UINT32_NAME;
          unsafeReadDescriptor = UNSAFE_READ_UINT32_DESCRIPTOR;
          break;
        case ENUM:
          unsafeWriteName = UNSAFE_WRITE_ENUM_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_ENUM_DESCRIPTOR;
          safeWriteName = WRITE_ENUM_NAME;
          safeWriteDescriptor = WRITE_ENUM_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_ENUM_NAME;
          unsafeReadDescriptor = UNSAFE_READ_ENUM_DESCRIPTOR;
          break;
        case SFIXED32:
          unsafeWriteName = UNSAFE_WRITE_SFIXED32_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SFIXED32_DESCRIPTOR;
          safeWriteName = WRITE_SFIXED32_NAME;
          safeWriteDescriptor = WRITE_SFIXED32_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SFIXED32_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SFIXED32_DESCRIPTOR;
          break;
        case SFIXED64:
          unsafeWriteName = UNSAFE_WRITE_SFIXED64_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SFIXED64_DESCRIPTOR;
          safeWriteName = WRITE_SFIXED64_NAME;
          safeWriteDescriptor = WRITE_SFIXED64_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SFIXED64_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SFIXED64_DESCRIPTOR;
          break;
        case SINT32:
          unsafeWriteName = UNSAFE_WRITE_SINT32_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SINT32_DESCRIPTOR;
          safeWriteName = WRITE_SINT32_NAME;
          safeWriteDescriptor = WRITE_SINT32_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SINT32_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SINT32_DESCRIPTOR;
          break;
        case SINT64:
          unsafeWriteName = UNSAFE_WRITE_SINT64_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SINT64_DESCRIPTOR;
          safeWriteName = WRITE_SINT64_NAME;
          safeWriteDescriptor = WRITE_SINT64_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SINT64_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SINT64_DESCRIPTOR;
          break;
        case DOUBLE_LIST:
          unsafeWriteName = UNSAFE_WRITE_DOUBLE_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_DOUBLE_LIST_DESCRIPTOR;
          safeWriteName = WRITE_DOUBLE_LIST_NAME;
          safeWriteDescriptor = WRITE_DOUBLE_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_DOUBLE_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_DOUBLE_LIST_DESCRIPTOR;
          break;
        case FLOAT_LIST:
          unsafeWriteName = UNSAFE_WRITE_FLOAT_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_FLOAT_LIST_DESCRIPTOR;
          safeWriteName = WRITE_FLOAT_LIST_NAME;
          safeWriteDescriptor = WRITE_FLOAT_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_FLOAT_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_FLOAT_LIST_DESCRIPTOR;
          break;
        case INT64_LIST:
          unsafeWriteName = UNSAFE_WRITE_INT64_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_INT64_LIST_DESCRIPTOR;
          safeWriteName = WRITE_INT64_LIST_NAME;
          safeWriteDescriptor = WRITE_INT64_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_INT64_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_INT64_LIST_DESCRIPTOR;
          break;
        case UINT64_LIST:
          unsafeWriteName = UNSAFE_WRITE_UINT64_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_UINT64_LIST_DESCRIPTOR;
          safeWriteName = WRITE_UINT64_LIST_NAME;
          safeWriteDescriptor = WRITE_UINT64_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_UINT64_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_UINT64_LIST_DESCRIPTOR;
          break;
        case INT32_LIST:
          unsafeWriteName = UNSAFE_WRITE_INT32_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_INT32_LIST_DESCRIPTOR;
          safeWriteName = WRITE_INT32_LIST_NAME;
          safeWriteDescriptor = WRITE_INT32_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_INT32_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_INT32_LIST_DESCRIPTOR;
          break;
        case FIXED64_LIST:
          unsafeWriteName = UNSAFE_WRITE_FIXED64_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_FIXED64_LIST_DESCRIPTOR;
          safeWriteName = WRITE_FIXED64_LIST_NAME;
          safeWriteDescriptor = WRITE_FIXED64_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_FIXED64_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_FIXED64_LIST_DESCRIPTOR;
          break;
        case FIXED32_LIST:
          unsafeWriteName = UNSAFE_WRITE_FIXED32_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_FIXED32_LIST_DESCRIPTOR;
          safeWriteName = WRITE_FIXED32_LIST_NAME;
          safeWriteDescriptor = WRITE_FIXED32_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_FIXED32_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_FIXED32_LIST_DESCRIPTOR;
          break;
        case BOOL_LIST:
          unsafeWriteName = UNSAFE_WRITE_BOOL_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_BOOL_LIST_DESCRIPTOR;
          safeWriteName = WRITE_BOOL_LIST_NAME;
          safeWriteDescriptor = WRITE_BOOL_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_BOOL_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_BOOL_LIST_DESCRIPTOR;
          break;
        case STRING_LIST:
          unsafeWriteName = UNSAFE_WRITE_STRING_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_STRING_LIST_DESCRIPTOR;
          safeWriteName = WRITE_STRING_LIST_NAME;
          safeWriteDescriptor = WRITE_STRING_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_STRING_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_STRING_LIST_DESCRIPTOR;
          writeType = WriteType.STANDARD;
          break;
        case MESSAGE_LIST:
          unsafeWriteName = UNSAFE_WRITE_MESSAGE_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_MESSAGE_LIST_DESCRIPTOR;
          safeWriteName = WRITE_MESSAGE_LIST_NAME;
          safeWriteDescriptor = WRITE_MESSAGE_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_MESSAGE_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_MESSAGE_LIST_DESCRIPTOR;
          writeType = WriteType.STANDARD;
          break;
        case BYTES_LIST:
          unsafeWriteName = UNSAFE_WRITE_BYTES_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_BYTES_LIST_DESCRIPTOR;
          safeWriteName = WRITE_BYTES_LIST_NAME;
          safeWriteDescriptor = WRITE_BYTES_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_BYTES_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_BYTES_LIST_DESCRIPTOR;
          writeType = WriteType.STANDARD;
          break;
        case UINT32_LIST:
          unsafeWriteName = UNSAFE_WRITE_UINT32_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_UINT32_LIST_DESCRIPTOR;
          safeWriteName = WRITE_UINT32_LIST_NAME;
          safeWriteDescriptor = WRITE_UINT32_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_UINT32_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_UINT32_LIST_DESCRIPTOR;
          break;
        case ENUM_LIST:
          unsafeWriteName = UNSAFE_WRITE_ENUM_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_ENUM_LIST_DESCRIPTOR;
          safeWriteName = WRITE_ENUM_LIST_NAME;
          safeWriteDescriptor = WRITE_ENUM_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_ENUM_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_ENUM_LIST_DESCRIPTOR;
          writeType = WriteType.ENUM_LIST;
          break;
        case SFIXED32_LIST:
          unsafeWriteName = UNSAFE_WRITE_SFIXED32_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SFIXED32_LIST_DESCRIPTOR;
          safeWriteName = WRITE_SFIXED32_LIST_NAME;
          safeWriteDescriptor = WRITE_SFIXED32_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SFIXED32_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SFIXED32_LIST_DESCRIPTOR;
          break;
        case SFIXED64_LIST:
          unsafeWriteName = UNSAFE_WRITE_SFIXED64_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SFIXED64_LIST_DESCRIPTOR;
          safeWriteName = WRITE_SFIXED64_LIST_NAME;
          safeWriteDescriptor = WRITE_SFIXED64_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SFIXED64_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SFIXED64_LIST_DESCRIPTOR;
          break;
        case SINT32_LIST:
          unsafeWriteName = UNSAFE_WRITE_SINT32_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SINT32_LIST_DESCRIPTOR;
          safeWriteName = WRITE_SINT32_LIST_NAME;
          safeWriteDescriptor = WRITE_SINT32_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SINT32_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SINT32_LIST_DESCRIPTOR;
          break;
        case SINT64_LIST:
          unsafeWriteName = UNSAFE_WRITE_SINT64_LIST_NAME;
          unsafeWriteDescriptor = UNSAFE_WRITE_SINT64_LIST_DESCRIPTOR;
          safeWriteName = WRITE_SINT64_LIST_NAME;
          safeWriteDescriptor = WRITE_SINT64_LIST_DESCRIPTOR;
          unsafeReadName = UNSAFE_READ_SINT64_LIST_NAME;
          unsafeReadDescriptor = UNSAFE_READ_SINT64_LIST_DESCRIPTOR;
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
      mv.visitMethodInsn(INVOKESTATIC, SCHEMA_UTIL_NAME, unsafeReadName, unsafeReadDescriptor, false);
    }

    private void unsafeWriteEnum(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMA_UTIL_NAME, unsafeWriteName, unsafeWriteDescriptor, false);
    }

    private void unsafeWrite(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMA_UTIL_NAME, unsafeWriteName, unsafeWriteDescriptor, false);
    }

    private void safeWrite(String messageClassName, MethodVisitor mv, PropertyDescriptor property) {
      mv.visitLdcInsn(property.fieldNumber);

      // Get the field value.
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitFieldInsn(GETFIELD, messageClassName, property.field.getName(),
              Type.getDescriptor(property.field.getType()));

      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMA_UTIL_NAME, safeWriteName, safeWriteDescriptor, false);
    }

    private void unsafeWriteList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMA_UTIL_NAME, unsafeWriteName, unsafeWriteDescriptor, false);
    }

    private void unsafeWriteEnumList(MethodVisitor mv, int fieldNumber, long offset) {
      mv.visitLdcInsn(fieldNumber);
      mv.visitVarInsn(ALOAD, MESSAGE_INDEX);
      mv.visitLdcInsn(offset);
      mv.visitVarInsn(ALOAD, WRITER_INDEX);
      mv.visitLdcInsn(ENUM_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, SCHEMA_UTIL_NAME, unsafeWriteName, unsafeWriteDescriptor, false);
    }
  }
}
