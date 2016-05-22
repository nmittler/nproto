package io.nproto.util;


import io.nproto.ByteString;
import io.nproto.Internal;
import io.nproto.Reader;
import io.nproto.Writer;
import io.nproto.descriptor.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

@Internal
public final class AndroidSchemaUtil {
  private AndroidSchemaUtil() {
  }

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

  private static <E extends Enum<E>> void writeEnum(int fieldNumber, E value, Writer writer) {
    if (value != null) {
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

  public static void writeDoubleList(int fieldNumber, List<Double> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeDoubleList(fieldNumber, value);
    }
  }

  public static void writeFloatList(int fieldNumber, List<Float> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeFloatList(fieldNumber, value);
    }
  }

  public static void writeInt64List(int fieldNumber, List<Long> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeInt64List(fieldNumber, value);
    }
  }

  public static void writeUInt64List(int fieldNumber, List<Long> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeUInt64List(fieldNumber, value);
    }
  }

  public static void writeSInt64List(int fieldNumber, List<Long> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeSInt64List(fieldNumber, value);
    }
  }

  public static void writeFixed64List(int fieldNumber, List<Long> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeFixed64List(fieldNumber, value);
    }
  }

  public static void writeSFixed64List(int fieldNumber, List<Long> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeSFixed64List(fieldNumber, value);
    }
  }

  public static void writeInt32List(int fieldNumber, List<Integer> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeInt32List(fieldNumber, value);
    }
  }

  public static void writeUInt32List(int fieldNumber, List<Integer> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeUInt32List(fieldNumber, value);
    }
  }

  public static void writeSInt32List(int fieldNumber, List<Integer> value, Writer writer) {
    if (value != null) {
      writer.writeSInt32List(fieldNumber, value);
    }
  }

  public static void writeFixed32List(int fieldNumber, List<Integer> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeFixed32List(fieldNumber, value);
    }
  }

  public static void writeSFixed32List(int fieldNumber, List<Integer> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeSFixed32List(fieldNumber, value);
    }
  }

  private static <E extends Enum<E>> void writeEnumList(int fieldNumber, List<E> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeEnumList(fieldNumber, value);
    }
  }

  public static void writeBoolList(int fieldNumber, List<Boolean> value, Writer writer) {
    if (value != null && !value.isEmpty()) {
      writer.writeBoolList(fieldNumber, value);
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

  public static void unsafeWriteDouble(int fieldNumber, Object message, long offset, Writer writer) {
    writeDouble(fieldNumber, AndroidUnsafeUtil.getDouble(message, offset), writer);
  }

  public static void unsafeWriteFloat(int fieldNumber, Object message, long offset, Writer writer) {
    writeFloat(fieldNumber, AndroidUnsafeUtil.getFloat(message, offset), writer);
  }

  public static void unsafeWriteInt64(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt64(fieldNumber, AndroidUnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteUInt64(int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt64(fieldNumber, AndroidUnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteSInt64(int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt64(fieldNumber, AndroidUnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteFixed64(int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed64(fieldNumber, AndroidUnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteSFixed64(int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed64(fieldNumber, AndroidUnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteInt32(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt32(fieldNumber, AndroidUnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteUInt32(int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt32(fieldNumber, AndroidUnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteSInt32(int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt32(fieldNumber, AndroidUnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteFixed32(int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed32(fieldNumber, AndroidUnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteSFixed32(int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed32(fieldNumber, AndroidUnsafeUtil.getInt(message, offset), writer);
  }

  public static <E extends Enum<E>> void unsafeWriteEnum(int fieldNumber, Object message, long offset, Writer writer, Class<E> enumClass) {
    writeEnum(fieldNumber, enumClass.cast(AndroidUnsafeUtil.getObject(message, offset)), writer);
  }

  public static void unsafeWriteBool(int fieldNumber, Object message, long offset, Writer writer) {
    writeBool(fieldNumber, AndroidUnsafeUtil.getBoolean(message, offset), writer);
  }

  public static void unsafeWriteString(int fieldNumber, Object message, long offset, Writer writer) {
    writeString(fieldNumber, (String) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeWriteBytes(int fieldNumber, Object message, long offset, Writer writer) {
    writeBytes(fieldNumber, (ByteString) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeWriteMessage(int fieldNumber, Object message, long offset, Writer writer) {
    writeMessage(fieldNumber, AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteDoubleList(int fieldNumber, Object message, long offset, Writer writer) {
    writeDoubleList(fieldNumber, (List<Double>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFloatList(int fieldNumber, Object message, long offset, Writer writer) {
    writeFloatList(fieldNumber, (List<Float>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteInt64List(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt64List(fieldNumber, (List<Long>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteUInt64List(int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt64List(fieldNumber, (List<Long>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSInt64List(int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt64List(fieldNumber, (List<Long>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFixed64List(int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed64List(fieldNumber, (List<Long>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSFixed64List(int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed64List(fieldNumber, (List<Long>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteInt32List(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt32List(fieldNumber, (List<Integer>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteUInt32List(int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt32List(fieldNumber, (List<Integer>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSInt32List(int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt32List(fieldNumber, (List<Integer>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFixed32List(int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed32List(fieldNumber, (List<Integer>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSFixed32List(int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed32List(fieldNumber, (List<Integer>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> void unsafeWriteEnumList(int fieldNumber, Object message, long offset, Writer writer, Class<E> enumClasss) {
    writeEnumList(fieldNumber, (List<E>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteBoolList(int fieldNumber, Object message, long offset, Writer writer) {
    writeBoolList(fieldNumber, (List<Boolean>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteStringList(int fieldNumber, Object message, long offset, Writer writer) {
    writeStringList(fieldNumber, (List<String>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteBytesList(int fieldNumber, Object message, long offset, Writer writer) {
    writeBytesList(fieldNumber, (List<ByteString>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteMessageList(int fieldNumber, Object message, long offset, Writer writer) {
    writeMessageList(fieldNumber, (List<?>) AndroidUnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeReadDouble(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putDouble(message, offset, reader.readDouble());
  }

  public static void unsafeReadFloat(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putFloat(message, offset, reader.readFloat());
  }

  public static void unsafeReadInt64(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putLong(message, offset, reader.readInt64());
  }

  public static void unsafeReadUInt64(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putLong(message, offset, reader.readUInt64());
  }

  public static void unsafeReadSInt64(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putLong(message, offset, reader.readSInt64());
  }

  public static void unsafeReadFixed64(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putLong(message, offset, reader.readFixed64());
  }

  public static void unsafeReadSFixed64(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putLong(message, offset, reader.readSFixed64());
  }

  public static void unsafeReadInt32(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putInt(message, offset, reader.readInt32());
  }

  public static void unsafeReadUInt32(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putInt(message, offset, reader.readUInt32());
  }

  public static void unsafeReadSInt32(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putInt(message, offset, reader.readSInt32());
  }

  public static void unsafeReadFixed32(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putInt(message, offset, reader.readFixed32());
  }

  public static void unsafeReadSFixed32(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putInt(message, offset, reader.readSFixed32());
  }

  public static <E extends Enum<E>> void unsafeReadEnum(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putObject(message, offset, reader.readEnum());
  }

  public static void unsafeReadBool(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putBoolean(message, offset, reader.readBool());
  }

  public static void unsafeReadString(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putObject(message, offset, reader.readString());
  }

  public static void unsafeReadBytes(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putObject(message, offset, reader.readBytes());
  }

  public static void unsafeReadMessage(Object message, long offset, Reader reader) {
    AndroidUnsafeUtil.putObject(message, offset, reader.readMessage());
  }

  public static void unsafeReadDoubleList(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readDouble());
  }

  public static void unsafeReadFloatList(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readFloat());
  }

  public static void unsafeReadInt64List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readInt64());
  }

  public static void unsafeReadUInt64List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readUInt64());
  }

  public static void unsafeReadSInt64List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readSInt64());
  }

  public static void unsafeReadFixed64List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readFixed64());
  }

  public static void unsafeReadSFixed64List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readSFixed64());
  }

  public static void unsafeReadInt32List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readInt32());
  }

  public static void unsafeReadUInt32List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readUInt32());
  }

  public static void unsafeReadSInt32List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readSInt32());
  }

  public static void unsafeReadFixed32List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readFixed32());
  }

  public static void unsafeReadSFixed32List(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readSFixed32());
  }

  public static <E extends Enum<E>> void unsafeReadEnumList(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readEnum());
  }

  public static void unsafeReadBoolList(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readBool());
  }

  public static void unsafeReadStringList(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readString());
  }

  public static void unsafeReadBytesList(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readBytes());
  }

  public static void unsafeReadMessageList(Object message, long offset, Reader reader) {
    getOrCreateList(message, offset).add(reader.readMessage());
  }

  private static <L> List<L> getOrCreateList(Object message, long offset) {
    @SuppressWarnings("unchecked")
    List<L> list = (List<L>) AndroidUnsafeUtil.getObject(message, offset);
    if (list == null) {
      list = new ArrayList<L>();
      AndroidUnsafeUtil.putObject(message, offset, list);
    }
    return list;
  }

  /**
   * Determines whether to issue tableswitch or lookupswitch for the mergeFrom method.
   */
  public static boolean shouldUseTableSwitch(List<PropertyDescriptor> fields) {
    // Determine whether to issue a tableswitch or a lookupswitch
    // instruction.
    if (fields.isEmpty()) {
      return false;
    }
    int lo = fields.get(0).fieldNumber;
    int hi = fields.get(fields.size() - 1).fieldNumber;
    long table_space_cost = 4 + ((long) hi - lo + 1); // words
    long table_time_cost = 3; // comparisons
    long lookup_space_cost = 3 + 2 * (long) fields.size();
    long lookup_time_cost = fields.size();
    return table_space_cost + 3 * table_time_cost <= lookup_space_cost + 3 * lookup_time_cost;
  }
}

