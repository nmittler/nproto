package com.google.apps.tiktok.protobuf.experimental.util;

import com.google.apps.tiktok.protobuf.experimental.ByteString;
import com.google.apps.tiktok.protobuf.experimental.InternalApi;
import com.google.apps.tiktok.protobuf.experimental.descriptor.FieldDescriptor;
import com.google.apps.tiktok.protobuf.experimental.schema.Reader;
import com.google.apps.tiktok.protobuf.experimental.schema.Writer;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods used by schemas.
 */
@InternalApi
public final class SchemaUtil {
  private SchemaUtil() {}

  public static void writeDouble(int fieldNumber, double value, Writer writer) {
    if (Double.compare(value, 0.0) != 0) {
      writer.writeDouble(fieldNumber, value);
    }
  }

  public static void writeFloat(int fieldNumber, float value, Writer writer) {
    if (Float.compare(value, 0.0f) != 0) {
      writer.writeFloat(fieldNumber, value);
    }
  }

  public static void writeInt64(int fieldNumber, long value, Writer writer) {
    if (value != 0) {
      writer.writeInt64(fieldNumber, value);
    }
  }

  public static void writeUInt64(int fieldNumber, long value, Writer writer) {
    if (value != 0) {
      writer.writeUInt64(fieldNumber, value);
    }
  }

  public static void writeSInt64(int fieldNumber, long value, Writer writer) {
    if (value != 0) {
      writer.writeSInt64(fieldNumber, value);
    }
  }

  public static void writeFixed64(int fieldNumber, long value, Writer writer) {
    if (value != 0) {
      writer.writeFixed64(fieldNumber, value);
    }
  }

  public static void writeSFixed64(int fieldNumber, long value, Writer writer) {
    if (value != 0) {
      writer.writeSFixed64(fieldNumber, value);
    }
  }

  public static void writeInt32(int fieldNumber, int value, Writer writer) {
    if (value != 0) {
      writer.writeInt32(fieldNumber, value);
    }
  }

  public static void writeUInt32(int fieldNumber, int value, Writer writer) {
    if (value != 0) {
      writer.writeUInt32(fieldNumber, value);
    }
  }

  public static void writeSInt32(int fieldNumber, int value, Writer writer) {
    if (value != 0) {
      writer.writeSInt32(fieldNumber, value);
    }
  }

  public static void writeFixed32(int fieldNumber, int value, Writer writer) {
    if (value != 0) {
      writer.writeFixed32(fieldNumber, value);
    }
  }

  public static void writeSFixed32(int fieldNumber, int value, Writer writer) {
    if (value != 0) {
      writer.writeSFixed32(fieldNumber, value);
    }
  }

  public static void writeEnum(int fieldNumber, int value, Writer writer) {
    if (value != 0) {
      writer.writeEnum(fieldNumber, value);
    }
  }

  public static void writeBool(int fieldNumber, boolean value, Writer writer) {
    if (value) {
      writer.writeBool(fieldNumber, true);
    }
  }

  public static void writeString(int fieldNumber, String value, Writer writer) {
    if (value != null) {
      writer.writeString(fieldNumber, value);
    }
  }

  public static void writeBytes(int fieldNumber, ByteString value, Writer writer) {
    if (value != null) {
      writer.writeBytes(fieldNumber, value);
    }
  }

  public static void writeMessage(int fieldNumber, Object value, Writer writer) {
    if (value != null) {
      writer.writeMessage(fieldNumber, value);
    }
  }

  public static void writeDoubleList(
      int fieldNumber, List<Double> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeDoubleList(fieldNumber, value, packed);
    }
  }

  public static void writeFloatList(
      int fieldNumber, List<Float> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeFloatList(fieldNumber, value, packed);
    }
  }

  public static void writeInt64List(
      int fieldNumber, List<Long> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeInt64List(fieldNumber, value, packed);
    }
  }

  public static void writeUInt64List(
      int fieldNumber, List<Long> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeUInt64List(fieldNumber, value, packed);
    }
  }

  public static void writeSInt64List(
      int fieldNumber, List<Long> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeSInt64List(fieldNumber, value, packed);
    }
  }

  public static void writeFixed64List(
      int fieldNumber, List<Long> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeFixed64List(fieldNumber, value, packed);
    }
  }

  public static void writeSFixed64List(
      int fieldNumber, List<Long> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeSFixed64List(fieldNumber, value, packed);
    }
  }

  public static void writeInt32List(
      int fieldNumber, List<Integer> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeInt32List(fieldNumber, value, packed);
    }
  }

  public static void writeUInt32List(
      int fieldNumber, List<Integer> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeUInt32List(fieldNumber, value, packed);
    }
  }

  public static void writeSInt32List(
      int fieldNumber, List<Integer> value, Writer writer, boolean packed) {
    if (value != null) {
      writer.writeSInt32List(fieldNumber, value, packed);
    }
  }

  public static void writeFixed32List(
      int fieldNumber, List<Integer> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeFixed32List(fieldNumber, value, packed);
    }
  }

  public static void writeSFixed32List(
      int fieldNumber, List<Integer> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeSFixed32List(fieldNumber, value, packed);
    }
  }

  public static void writeEnumList(
      int fieldNumber, List<Integer> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeEnumList(fieldNumber, value, packed);
    }
  }

  public static void writeBoolList(
      int fieldNumber, List<Boolean> value, Writer writer, boolean packed) {
    if (value != null && !value.isEmpty()) {
      writer.writeBoolList(fieldNumber, value, packed);
    }
  }

  public static void writeStringList(int fieldNumber, List<String> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeStringList(fieldNumber, value);
    }
  }

  public static void writeBytesList(int fieldNumber, List<ByteString> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeBytesList(fieldNumber, value);
    }
  }

  public static void writeMessageList(int fieldNumber, List<?> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeMessageList(fieldNumber, value);
    }
  }

  public static void unsafeWriteDouble(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeDouble(fieldNumber, UnsafeUtil.getDouble(message, offset), writer);
  }

  public static void unsafeWriteFloat(int fieldNumber, Object message, long offset, Writer writer) {
    writeFloat(fieldNumber, UnsafeUtil.getFloat(message, offset), writer);
  }

  public static void unsafeWriteInt64(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteUInt64(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteSInt64(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteFixed64(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteSFixed64(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteInt32(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteUInt32(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteSInt32(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteFixed32(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteSFixed32(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteEnum(int fieldNumber, Object message, long offset, Writer writer) {
    writeEnum(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteBool(int fieldNumber, Object message, long offset, Writer writer) {
    writeBool(fieldNumber, UnsafeUtil.getBoolean(message, offset), writer);
  }

  public static void unsafeWriteString(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeString(fieldNumber, (String) UnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeWriteBytes(int fieldNumber, Object message, long offset, Writer writer) {
    writeBytes(fieldNumber, (ByteString) UnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeWriteMessage(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeMessage(fieldNumber, UnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteDoubleList(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeDoubleList(
        fieldNumber, (List<Double>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFloatList(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeFloatList(
        fieldNumber, (List<Float>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteInt64List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeInt64List(fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteUInt64List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeUInt64List(
        fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSInt64List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeSInt64List(
        fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFixed64List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeFixed64List(
        fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSFixed64List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeSFixed64List(
        fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteInt32List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeInt32List(
        fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteUInt32List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeUInt32List(
        fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSInt32List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeSInt32List(
        fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFixed32List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeFixed32List(
        fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSFixed32List(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeSFixed32List(
        fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteEnumList(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeEnumList(
        fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteBoolList(
      int fieldNumber, Object message, long offset, Writer writer, boolean packed) {
    writeBoolList(
        fieldNumber, (List<Boolean>) UnsafeUtil.getObject(message, offset), writer, packed);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteStringList(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeStringList(fieldNumber, (List<String>) UnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteBytesList(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeBytesList(fieldNumber, (List<ByteString>) UnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteMessageList(
      int fieldNumber, Object message, long offset, Writer writer) {
    writeMessageList(fieldNumber, (List<?>) UnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeReadDouble(Object message, long offset, Reader reader) {
    UnsafeUtil.putDouble(message, offset, reader.readDouble());
  }

  public static void unsafeReadFloat(Object message, long offset, Reader reader) {
    UnsafeUtil.putFloat(message, offset, reader.readFloat());
  }

  public static void unsafeReadInt64(Object message, long offset, Reader reader) {
    UnsafeUtil.putLong(message, offset, reader.readInt64());
  }

  public static void unsafeReadUInt64(Object message, long offset, Reader reader) {
    UnsafeUtil.putLong(message, offset, reader.readUInt64());
  }

  public static void unsafeReadSInt64(Object message, long offset, Reader reader) {
    UnsafeUtil.putLong(message, offset, reader.readSInt64());
  }

  public static void unsafeReadFixed64(Object message, long offset, Reader reader) {
    UnsafeUtil.putLong(message, offset, reader.readFixed64());
  }

  public static void unsafeReadSFixed64(Object message, long offset, Reader reader) {
    UnsafeUtil.putLong(message, offset, reader.readSFixed64());
  }

  public static void unsafeReadInt32(Object message, long offset, Reader reader) {
    UnsafeUtil.putInt(message, offset, reader.readInt32());
  }

  public static void unsafeReadUInt32(Object message, long offset, Reader reader) {
    UnsafeUtil.putInt(message, offset, reader.readUInt32());
  }

  public static void unsafeReadSInt32(Object message, long offset, Reader reader) {
    UnsafeUtil.putInt(message, offset, reader.readSInt32());
  }

  public static void unsafeReadFixed32(Object message, long offset, Reader reader) {
    UnsafeUtil.putInt(message, offset, reader.readFixed32());
  }

  public static void unsafeReadSFixed32(Object message, long offset, Reader reader) {
    UnsafeUtil.putInt(message, offset, reader.readSFixed32());
  }

  public static void unsafeReadEnum(Object message, long offset, Reader reader) {
    UnsafeUtil.putInt(message, offset, reader.readEnum());
  }

  public static void unsafeReadBool(Object message, long offset, Reader reader) {
    UnsafeUtil.putBoolean(message, offset, reader.readBool());
  }

  public static void unsafeReadString(Object message, long offset, Reader reader) {
    UnsafeUtil.putObject(message, offset, reader.readString());
  }

  public static void unsafeReadBytes(Object message, long offset, Reader reader) {
    UnsafeUtil.putObject(message, offset, reader.readBytes());
  }

  public static void unsafeReadMessage(Object message, long offset, Reader reader) {
    UnsafeUtil.putObject(message, offset, reader.readMessage());
  }

  public static void unsafeReadDoubleList(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readDoubleList(SchemaUtil.<Double>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadFloatList(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readFloatList(SchemaUtil.<Float>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadInt64List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readInt64List(SchemaUtil.<Long>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadUInt64List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readUInt64List(SchemaUtil.<Long>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadSInt64List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readSInt64List(SchemaUtil.<Long>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadFixed64List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readFixed64List(SchemaUtil.<Long>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadSFixed64List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readSFixed64List(SchemaUtil.<Long>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadInt32List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readInt32List(SchemaUtil.<Integer>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadUInt32List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readUInt32List(SchemaUtil.<Integer>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadSInt32List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readSInt32List(SchemaUtil.<Integer>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadFixed32List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readFixed32List(SchemaUtil.<Integer>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadSFixed32List(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readSFixed32List(SchemaUtil.<Integer>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadEnumList(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readEnumList(SchemaUtil.<Integer>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadBoolList(
      Object message, long offset, Reader reader, boolean packed) {
    reader.readBoolList(SchemaUtil.<Boolean>getOrCreateList(message, offset), packed);
  }

  public static void unsafeReadStringList(Object message, long offset, Reader reader) {
    reader.readStringList(SchemaUtil.<String>getOrCreateList(message, offset));
  }

  public static void unsafeReadBytesList(Object message, long offset, Reader reader) {
    reader.readBytesList(SchemaUtil.<ByteString>getOrCreateList(message, offset));
  }

  public static <T> void unsafeReadMessageList(
      Object message, long offset, Reader reader, Class<T> targetType) {
    reader.readMessageList(SchemaUtil.<T>getOrCreateList(message, offset), targetType);
  }

  public static <L> List<L> getOrCreateList(List<L> list) {
    if (list == null) {
      list = new ArrayList<L>();
    }
    return list;
  }

  private static <L> List<L> getOrCreateList(Object message, long offset) {
    @SuppressWarnings("unchecked")
    List<L> list = (List<L>) UnsafeUtil.getObject(message, offset);
    if (list == null) {
      list = new ArrayList<L>();
      UnsafeUtil.putObject(message, offset, list);
    }
    return list;
  }

  /**
   * Determines whether to issue tableswitch or lookupswitch for the mergeFrom method.
   * 
   * @see #shouldUseTableSwitch(int, int, int)
   */
  public static boolean shouldUseTableSwitch(List<FieldDescriptor> fields) {
    // Determine whether to issue a tableswitch or a lookupswitch
    // instruction.
    if (fields.isEmpty()) {
      return false;
    }

    int lo = fields.get(0).getFieldNumber();
    int hi = fields.get(fields.size() - 1).getFieldNumber();
    return shouldUseTableSwitch(lo, hi, fields.size());
  }

  /**
   * Determines whether to issue tableswitch or lookupswitch for the mergeFrom method. This is
   * based on the <a href=
   * "http://hg.openjdk.java.net/jdk8/jdk8/langtools/file/30db5e0aaf83/src/share/classes/com/sun/tools/javac/jvm/Gen.java#l1159">
   * logic in the JDK</a>.
   *
   * @param lo the lowest fieldNumber contained within the message.
   * @param hi the higest fieldNumber contained within the message.
   * @param numFields the total number of fields in the message.
   * @return {@code true} if tableswitch should be used, rather than lookupswitch.
   */
  public static boolean shouldUseTableSwitch(int lo, int hi, int numFields) {
    long tableSpaceCost = 4 + ((long) hi - lo + 1); // words
    long tableTimeCost = 3; // comparisons
    long lookupSpaceCost = 3 + 2 * (long) numFields;
    long lookupTimeCost = numFields;
    return tableSpaceCost + 3 * tableTimeCost <= lookupSpaceCost + 3 * lookupTimeCost;
  }
}
