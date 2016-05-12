package io.nproto.schema.reflect;

import static io.nproto.UnsafeUtil.fieldOffset;

import io.nproto.ByteString;
import io.nproto.FieldType;
import io.nproto.UnsafeUtil;
import io.nproto.Writer;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaUtil;
import io.nproto.schema.SchemaUtil.FieldInfo;

import java.util.List;

class ReflectiveSchema<T> implements Schema<T> {
  private static final int ENTRIES_PER_FIELD = 2;

  private final long[] data;
  //private final int[] fieldNumbers;
  //private final FieldType[] fieldTypes;

  static <T> ReflectiveSchema<T> newInstance(Class<T> messageType) {
    return new ReflectiveSchema<T>(messageType);
  }

  private ReflectiveSchema(Class<T> messageType) {
    //data = getDataForType(messageType);
    List<FieldInfo> fields = SchemaUtil.getAllFieldInfo(messageType);
    final int numFields = fields.size();
    //data = new long[numFields];
    //fieldNumbers = new int[numFields];
    //fieldTypes = new FieldType[numFields];
    data = new long[numFields * ENTRIES_PER_FIELD];
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0, dataPos = 0; i < numFields; ++i) {
      FieldInfo f = fields.get(i);
      if (f.fieldNumber == lastFieldNumber) {
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      data[dataPos++] = (((long) f.fieldType.ordinal()) << 32) | f.fieldNumber;
      data[dataPos++] = fieldOffset(f.field);
    }
  }

  @Override
  public void writeTo(T message, Writer writer) {
    for(int i = 0; i < data.length; ++i) {
      final FieldType fieldType = getFieldType(data[i]);
      switch (fieldType) {
        case DOUBLE:
          writeDouble(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case FLOAT:
          writeFloat(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case INT64:
          writeInt64(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case UINT64:
          writeUInt64(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case INT32:
          writeInt32(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case FIXED64:
          writeFixed64(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case FIXED32:
          writeFixed32(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case BOOL:
          writeBool(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case STRING:
          writeString(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case MESSAGE:
          writeMessage(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case BYTES:
          writeBytes(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case UINT32:
          writeUInt32(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case ENUM:
          writeEnum(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case SFIXED32:
          writeSFixed32(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case SFIXED64:
          writeSFixed64(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case SINT32:
          writeSInt32(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case SINT64:
          writeSInt64(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case DOUBLE_LIST:
        case PACKED_DOUBLE_LIST:
          writeDoubleList(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case FLOAT_LIST:
        case PACKED_FLOAT_LIST:
          writeFloatList(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case INT64_LIST:
        case PACKED_INT64_LIST:
          writeInt64List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case UINT64_LIST:
        case PACKED_UINT64_LIST:
          writeUInt64List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case INT32_LIST:
        case PACKED_INT32_LIST:
          writeInt32List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case FIXED64_LIST:
        case PACKED_FIXED64_LIST:
          writeFixed64List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case FIXED32_LIST:
        case PACKED_FIXED32_LIST:
          writeFixed32List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case BOOL_LIST:
        case PACKED_BOOL_LIST:
          writeBoolList(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case STRING_LIST:
          writeStringList(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case MESSAGE_LIST:
          writeMessageList(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case BYTES_LIST:
          writeBytesList(message, writer, getFieldNumber(data[i++]), data[i]);
          break;
        case UINT32_LIST:
        case PACKED_UINT32_LIST:
          writeUInt32List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case ENUM_LIST:
        case PACKED_ENUM_LIST:
          writeEnumList(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case SFIXED32_LIST:
        case PACKED_SFIXED32_LIST:
          writeSFixed32List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case SFIXED64_LIST:
        case PACKED_SFIXED64_LIST:
          writeSFixed64List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case SINT32_LIST:
        case PACKED_SINT32_LIST:
          writeSInt32List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        case SINT64_LIST:
        case PACKED_SINT64_LIST:
          writeSInt64List(message, writer, getFieldNumber(data[i++]), data[i], fieldType.isPacked());
          break;
        default:
          throw new IllegalArgumentException("Unsupported fieldType: " + fieldType);
      }
    }
  }

  private void writeDouble(T message, Writer writer, int fieldNumber, long offset) {
    double value = UnsafeUtil.getDouble(message, offset);
    if (Double.compare(value, 0.0) != 0) {
      writer.writeDouble(fieldNumber, value);
    }
  }

  private void writeFloat(T message, Writer writer, int fieldNumber, long offset) {
    float value = UnsafeUtil.getFloat(message, offset);
    if (Float.compare(value, 0.0f) != 0) {
      writer.writeFloat(fieldNumber, value);
    }
  }

  private void writeInt64(T message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeInt64(fieldNumber, value);
    }
  }

  private void writeUInt64(T message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeUInt64(fieldNumber, value);
    }
  }

  private void writeSInt64(T message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeSInt64(fieldNumber, value);
    }
  }

  private void writeFixed64(T message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeFixed64(fieldNumber, value);
    }
  }

  private void writeSFixed64(T message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeSFixed64(fieldNumber, value);
    }
  }

  private void writeInt32(T message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeInt32(fieldNumber, value);
    }
  }

  private void writeUInt32(T message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeUInt32(fieldNumber, value);
    }
  }

  private void writeSInt32(T message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeSInt32(fieldNumber, value);
    }
  }

  private void writeFixed32(T message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeFixed32(fieldNumber, value);
    }
  }

  private void writeSFixed32(T message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeSFixed32(fieldNumber, value);
    }
  }

  private void writeEnum(T message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeEnum(fieldNumber, value);
    }
  }

  private void writeBool(T message, Writer writer, int fieldNumber, long offset) {
    if (UnsafeUtil.getBoolean(message, offset)) {
      writer.writeBool(fieldNumber, true);
    }
  }

  private void writeString(T message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    String value = (String) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeString(fieldNumber, value);
    }
  }

  private void writeBytes(T message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    ByteString value = (ByteString) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeBytes(fieldNumber, value);
    }
  }

  private void writeMessage(T message, Writer writer, int fieldNumber, long offset) {
    Object value = UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeMessage(fieldNumber, value);
    }
  }

  private void writeDoubleList(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Double> value = (List<Double>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeDoubleList(fieldNumber, packed, value);
    }
  }

  private void writeFloatList(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Float> value = (List<Float>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeFloatList(fieldNumber, packed, value);
    }
  }

  private void writeInt64List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeInt64List(fieldNumber, packed, value);
    }
  }

  private void writeUInt64List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeUInt64List(fieldNumber, packed, value);
    }
  }

  private void writeSInt64List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSInt64List(fieldNumber, packed, value);
    }
  }

  private void writeFixed64List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeFixed64List(fieldNumber, packed, value);
    }
  }

  private void writeSFixed64List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSFixed64List(fieldNumber, packed, value);
    }
  }

  private void writeInt32List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeInt32List(fieldNumber, packed, value);
    }
  }

  private void writeUInt32List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeUInt32List(fieldNumber, packed, value);
    }
  }

  private void writeSInt32List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSInt32List(fieldNumber, packed, value);
    }
  }

  private void writeFixed32List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeFixed32List(fieldNumber, packed, value);
    }
  }

  private void writeSFixed32List(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSFixed32List(fieldNumber, packed, value);
    }
  }

  private void writeEnumList(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeEnumList(fieldNumber, packed, value);
    }
  }

  private void writeBoolList(T message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Boolean> value = (List<Boolean>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeBoolList(fieldNumber, packed, value);
    }
  }

  private void writeStringList(T message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    List<String> value = (List<String>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeStringList(fieldNumber, value);
    }
  }

  private void writeBytesList(T message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    List<ByteString> value = (List<ByteString>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeBytesList(fieldNumber, value);
    }
  }

  private void writeMessageList(T message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    List<?> value = (List<?>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeMessageList(fieldNumber, value);
    }
  }

  private static int getFieldNumber(long data) {
    return (int) data;
  }

  private static FieldType getFieldType(long data) {
    return FieldType.forOrdinal((int) (data >> 32));
  }
}
