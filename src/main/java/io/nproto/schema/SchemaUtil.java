package io.nproto.schema;

import io.nproto.ByteString;
import io.nproto.FieldType;
import io.nproto.Internal;
import io.nproto.ProtoField;
import io.nproto.Reader;
import io.nproto.UnsafeUtil;
import io.nproto.Writer;
import io.nproto.util.IntToIntHashMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Internal
public final class SchemaUtil {
  private SchemaUtil() {
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

  public static void writeDoubleList(int fieldNumber, List<Double> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeDoubleList(fieldNumber, packed, value);
    }
  }

  public static void writeFloatList(int fieldNumber, List<Float> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeFloatList(fieldNumber, packed, value);
    }
  }

  public static void writeInt64List(int fieldNumber, List<Long> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeInt64List(fieldNumber, packed, value);
    }
  }

  public static void writeUInt64List(int fieldNumber, List<Long> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeUInt64List(fieldNumber, packed, value);
    }
  }

  public static void writeSInt64List(int fieldNumber, List<Long> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeSInt64List(fieldNumber, packed, value);
    }
  }

  public static void writeFixed64List(int fieldNumber, List<Long> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeFixed64List(fieldNumber, packed, value);
    }
  }

  public static void writeSFixed64List(int fieldNumber, List<Long> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeSFixed64List(fieldNumber, packed, value);
    }
  }

  public static void writeInt32List(int fieldNumber, List<Integer> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeInt32List(fieldNumber, packed, value);
    }
  }

  public static void writeUInt32List(int fieldNumber, List<Integer> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeUInt32List(fieldNumber, packed, value);
    }
  }

  public static void writeSInt32List(int fieldNumber, List<Integer> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeSInt32List(fieldNumber, packed, value);
    }
  }

  public static void writeFixed32List(int fieldNumber, List<Integer> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeFixed32List(fieldNumber, packed, value);
    }
  }

  public static void writeSFixed32List(int fieldNumber, List<Integer> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeSFixed32List(fieldNumber, packed, value);
    }
  }

  private static <E extends Enum<E>> void writeEnumList(int fieldNumber, List<E> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeEnumList(fieldNumber, packed, value);
    }
  }

  public static void writeBoolList(int fieldNumber, List<Boolean> value, boolean packed, Writer writer) {
    if (value != null) {
      writer.writeBoolList(fieldNumber, packed, value);
    }
  }

  public static void writeStringList(int fieldNumber, List<String> value, Writer writer) {
    if (value != null) {
      writer.writeStringList(fieldNumber, value);
    }
  }

  public static void writeBytesList(int fieldNumber, List<ByteString> value, Writer writer) {
    if (value != null) {
      writer.writeBytesList(fieldNumber, value);
    }
  }

  public static void writeMessageList(int fieldNumber, List<?> value, Writer writer) {
    if (value != null) {
      writer.writeMessageList(fieldNumber, value);
    }
  }

  public static void unsafeWriteDouble(int fieldNumber, Object message, long offset, Writer writer) {
    writeDouble(fieldNumber, UnsafeUtil.getDouble(message, offset), writer);
  }

  public static void unsafeWriteFloat(int fieldNumber, Object message, long offset, Writer writer) {
    writeFloat(fieldNumber, UnsafeUtil.getFloat(message, offset), writer);
  }

  public static void unsafeWriteInt64(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteUInt64(int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteSInt64(int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteFixed64(int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteSFixed64(int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed64(fieldNumber, UnsafeUtil.getLong(message, offset), writer);
  }

  public static void unsafeWriteInt32(int fieldNumber, Object message, long offset, Writer writer) {
    writeInt32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteUInt32(int fieldNumber, Object message, long offset, Writer writer) {
    writeUInt32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteSInt32(int fieldNumber, Object message, long offset, Writer writer) {
    writeSInt32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteFixed32(int fieldNumber, Object message, long offset, Writer writer) {
    writeFixed32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static void unsafeWriteSFixed32(int fieldNumber, Object message, long offset, Writer writer) {
    writeSFixed32(fieldNumber, UnsafeUtil.getInt(message, offset), writer);
  }

  public static <E extends Enum<E>> void unsafeWriteEnum(int fieldNumber, Object message, long offset, Writer writer, Class<E> enumClass) {
    writeEnum(fieldNumber, enumClass.cast(UnsafeUtil.getObject(message, offset)), writer);
  }

  public static void unsafeWriteBool(int fieldNumber, Object message, long offset, Writer writer) {
    writeBool(fieldNumber, UnsafeUtil.getBoolean(message, offset), writer);
  }

  public static void unsafeWriteString(int fieldNumber, Object message, long offset, Writer writer) {
    writeString(fieldNumber, (String) UnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeWriteBytes(int fieldNumber, Object message, long offset, Writer writer) {
    writeBytes(fieldNumber, (ByteString) UnsafeUtil.getObject(message, offset), writer);
  }

  public static void unsafeWriteMessage(int fieldNumber, Object message, long offset, Writer writer) {
    writeMessage(fieldNumber, UnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteDoubleList(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeDoubleList(fieldNumber, (List<Double>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFloatList(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeFloatList(fieldNumber, (List<Float>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteInt64List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeInt64List(fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteUInt64List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeUInt64List(fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSInt64List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeSInt64List(fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFixed64List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeFixed64List(fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSFixed64List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeSFixed64List(fieldNumber, (List<Long>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteInt32List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeInt32List(fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteUInt32List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeUInt32List(fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSInt32List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeSInt32List(fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteFixed32List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeFixed32List(fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteSFixed32List(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeSFixed32List(fieldNumber, (List<Integer>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> void unsafeWriteEnumList(int fieldNumber, Object message, long offset, boolean packed, Writer writer, Class<E> enumClasss) {
    writeEnumList(fieldNumber, (List<E>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteBoolList(int fieldNumber, Object message, long offset, boolean packed, Writer writer) {
    writeBoolList(fieldNumber, (List<Boolean>) UnsafeUtil.getObject(message, offset), packed, writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteStringList(int fieldNumber, Object message, long offset, Writer writer) {
    writeStringList(fieldNumber, (List<String>) UnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteBytesList(int fieldNumber, Object message, long offset, Writer writer) {
    writeBytesList(fieldNumber, (List<ByteString>) UnsafeUtil.getObject(message, offset), writer);
  }

  @SuppressWarnings("unchecked")
  public static void unsafeWriteMessageList(int fieldNumber, Object message, long offset, Writer writer) {
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

  public static <E extends Enum<E>> void unsafeReadEnum(Object message, long offset, Reader reader) {
    UnsafeUtil.putObject(message, offset, reader.readEnum());
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
    List<L> list = (List<L>) UnsafeUtil.getObject(message, offset);
    if (list == null) {
      list = new ArrayList<L>();
      UnsafeUtil.putObject(message, offset, list);
    }
    return list;
  }

  public static final class FieldInfo implements Comparable<FieldInfo> {
    public final Field field;
    public final FieldType fieldType;
    public final int fieldNumber;

    FieldInfo(Field field, ProtoField protoField) {
      this.field = field;
      this.fieldType = FieldType.forField(field, protoField);
      this.fieldNumber = protoField.number();
    }

    @Override
    public int compareTo(FieldInfo o) {
      return fieldNumber - o.fieldNumber;
    }
  }

  public static List<FieldInfo> getAllFieldInfo(Class<?> clazz) {
    List<FieldInfo> fields = new ArrayList<FieldInfo>();
    getAllFieldInfo(clazz, fields);
    // Now order them in ascending order by their field number.
    Collections.sort(fields);
    return fields;
  }

  private static void getAllFieldInfo(Class<?> clazz, List<FieldInfo> fields) {
    if (Object.class != clazz.getSuperclass()) {
      getAllFieldInfo(clazz.getSuperclass(), fields);
    }

    for (Field f : clazz.getDeclaredFields()) {
      int mod = f.getModifiers();
      ProtoField protoField = f.getAnnotation(ProtoField.class);
      if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && protoField != null) {
        fields.add(new FieldInfo(f, protoField));
      }
    }
  }
}
