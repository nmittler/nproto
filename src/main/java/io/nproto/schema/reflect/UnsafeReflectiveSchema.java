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

final class UnsafeReflectiveSchema<T> implements Schema<T> {
  private static final int ENTRIES_PER_FIELD = 2;
  private static final int LONG_LENGTH = 8;
  private static final int FIELD_LENGTH = ENTRIES_PER_FIELD * LONG_LENGTH;

  private final long[] data;
  private final long dataOffset;
  private final long dataLimit;
  //private final int[] fieldNumbers;
  //private final FieldType[] fieldTypes;
  //private final byte[] fieldTypes;

  static <T> UnsafeReflectiveSchema<T> newInstance(Class<T> messageType) {
    return new UnsafeReflectiveSchema<T>(messageType);
  }

  private UnsafeReflectiveSchema(Class<T> messageType) {
    List<FieldInfo> fields = SchemaUtil.getAllFieldInfo(messageType);
    final int numFields = fields.size();
    //data = new long[numFields];
    //fieldNumbers = new int[numFields];
    //fieldTypes = new byte[numFields];
    data = new long[numFields * ENTRIES_PER_FIELD];
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0, dataPos = 0; i < numFields; ++i) {
      FieldInfo f = fields.get(i);
      if (f.fieldNumber == lastFieldNumber) {
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      data[dataPos++] = (((long) f.fieldType.id()) << 32) | f.fieldNumber;
      data[dataPos++] = fieldOffset(f.field);
      //data[i] = fieldOffset(f.field);
      //fieldNumbers[i] = f.fieldNumber;
      //fieldTypes[i] = (byte) f.fieldType.id();
    }
    dataOffset = UnsafeUtil.arrayBaseOffset(long[].class);
    dataLimit = dataOffset + (data.length * LONG_LENGTH);
  }

  @Override
  public void writeTo(T message, Writer writer) {
    //for(int i = 0; i < data.length; ++i) {
    for(long pos = dataOffset; pos < dataLimit; pos += FIELD_LENGTH) {
      int fieldNumber = getFieldNumber(getLong(pos));
      // Switching on the field type ID to avoid the lookup of FieldType.
      switch (getFieldTypeId(getLong(pos))) {
        case 1: //DOUBLE:
          writeDouble(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 2: //FLOAT:
          writeFloat(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 3: //INT64:
          writeInt64(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 4: //UINT64:
          writeUInt64(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 5: //INT32:
          writeInt32(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 6: //FIXED64:
          writeFixed64(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 7: //FIXED32:
          writeFixed32(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 8: //BOOL:
          writeBool(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 9: //STRING:
          writeString(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 10: //MESSAGE:
          writeMessage(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 11: //BYTES:
          writeBytes(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 12: //UINT32:
          writeUInt32(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 13: //ENUM:
          writeEnum(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 14: //SFIXED32:
          writeSFixed32(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 15: //SFIXED64:
          writeSFixed64(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 16: //SINT32:
          writeSInt32(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 17: //SINT64:
          writeSInt64(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 18: //DOUBLE_LIST:
          writeDoubleList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 35: //PACKED_DOUBLE_LIST:
          writeDoubleList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 19: //FLOAT_LIST:
          writeFloatList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 36: //PACKED_FLOAT_LIST:
          writeFloatList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 20: //INT64_LIST:
          writeInt64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 37: //PACKED_INT64_LIST:
          writeInt64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 21: //UINT64_LIST:
          writeUInt64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 38: //PACKED_UINT64_LIST:
          writeUInt64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 22: //INT32_LIST:
          writeInt32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 39: //PACKED_INT32_LIST:
          writeInt32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 23: //FIXED64_LIST:
          writeFixed64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 40: //PACKED_FIXED64_LIST:
          writeFixed64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 24: //FIXED32_LIST:
          writeFixed32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 41: //PACKED_FIXED32_LIST:
          writeFixed32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 25: //BOOL_LIST:
          writeBoolList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 42: //PACKED_BOOL_LIST:
          writeBoolList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 26: //STRING_LIST:
          writeStringList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 27: //MESSAGE_LIST:
          writeMessageList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 28: //BYTES_LIST:
          writeBytesList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH));
          break;
        case 29: //UINT32_LIST:
          writeUInt32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 43: //PACKED_UINT32_LIST:
          writeUInt32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 30: //ENUM_LIST:
          writeEnumList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 44: //PACKED_ENUM_LIST:
          writeEnumList(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 31: //SFIXED32_LIST:
          writeSFixed32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 45: //PACKED_SFIXED32_LIST:
          writeSFixed32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 32: //SFIXED64_LIST:
          writeSFixed64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 46: //PACKED_SFIXED64_LIST:
          writeSFixed64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 33: //SINT32_LIST:
          writeSInt32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 47: //PACKED_SINT32_LIST:
          writeSInt32List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        case 34: //SINT64_LIST:
          writeSInt64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), false);
          break;
        case 48: //PACKED_SINT64_LIST:
          writeSInt64List(message, writer, fieldNumber, getLong(pos + LONG_LENGTH), true);
          break;
        default:
          throw new IllegalArgumentException("Unsupported fieldType: " + getFieldType(getLong(pos)));
      }
      /*switch (getFieldType(getLong(pos))) {
        case DOUBLE:
          writeDouble(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case FLOAT:
          writeFloat(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case INT64:
          writeInt64(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case UINT64:
          writeUInt64(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case INT32:
          writeInt32(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case FIXED64:
          writeFixed64(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case FIXED32:
          writeFixed32(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case BOOL:
          writeBool(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case STRING:
          writeString(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case MESSAGE:
          writeMessage(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case BYTES:
          writeBytes(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case UINT32:
          writeUInt32(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case ENUM:
          writeEnum(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case SFIXED32:
          writeSFixed32(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case SFIXED64:
          writeSFixed64(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case SINT32:
          writeSInt32(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case SINT64:
          writeSInt64(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case DOUBLE_LIST:
          writeDoubleList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_DOUBLE_LIST:
          writeDoubleList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case FLOAT_LIST:
          writeFloatList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_FLOAT_LIST:
          writeFloatList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case INT64_LIST:
          writeInt64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_INT64_LIST:
          writeInt64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case UINT64_LIST:
          writeUInt64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_UINT64_LIST:
          writeUInt64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case INT32_LIST:
          writeInt32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_INT32_LIST:
          writeInt32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case FIXED64_LIST:
          writeFixed64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_FIXED64_LIST:
          writeFixed64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case FIXED32_LIST:
          writeFixed32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_FIXED32_LIST:
          writeFixed32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case BOOL_LIST:
          writeBoolList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_BOOL_LIST:
          writeBoolList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case STRING_LIST:
          writeStringList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case MESSAGE_LIST:
          writeMessageList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case BYTES_LIST:
          writeBytesList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH));
          break;
        case UINT32_LIST:
          writeUInt32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_UINT32_LIST:
          writeUInt32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case ENUM_LIST:
          writeEnumList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_ENUM_LIST:
          writeEnumList(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case SFIXED32_LIST:
          writeSFixed32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_SFIXED32_LIST:
          writeSFixed32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case SFIXED64_LIST:
          writeSFixed64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_SFIXED64_LIST:
          writeSFixed64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case SINT32_LIST:
          writeSInt32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_SINT32_LIST:
          writeSInt32List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        case SINT64_LIST:
          writeSInt64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), false);
          break;
        case PACKED_SINT64_LIST:
          writeSInt64List(message, writer, getFieldNumber(getLong(pos)), getLong(pos + LONG_LENGTH), true);
          break;
        default:
          throw new IllegalArgumentException("Unsupported fieldType: " + getFieldType(getLong(pos)));
      }*/
    }
  }

  private long getLong(long pos) {
    return UnsafeUtil.getLong(data, pos);
  }

  private static void writeDouble(Object message, Writer writer, int fieldNumber, long offset) {
    double value = UnsafeUtil.getDouble(message, offset);
    if (Double.compare(value, 0.0) != 0) {
      writer.writeDouble(fieldNumber, value);
    }
  }

  private static void writeFloat(Object message, Writer writer, int fieldNumber, long offset) {
    float value = UnsafeUtil.getFloat(message, offset);
    if (Float.compare(value, 0.0f) != 0) {
      writer.writeFloat(fieldNumber, value);
    }
  }

  private static void writeInt64(Object message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeInt64(fieldNumber, value);
    }
  }

  private static void writeUInt64(Object message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeUInt64(fieldNumber, value);
    }
  }

  private static void writeSInt64(Object message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeSInt64(fieldNumber, value);
    }
  }

  private static void writeFixed64(Object message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeFixed64(fieldNumber, value);
    }
  }

  private static void writeSFixed64(Object message, Writer writer, int fieldNumber, long offset) {
    long value = UnsafeUtil.getLong(message, offset);
    if (value != 0) {
      writer.writeSFixed64(fieldNumber, value);
    }
  }

  private static void writeInt32(Object message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeInt32(fieldNumber, value);
    }
  }

  private static void writeUInt32(Object message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeUInt32(fieldNumber, value);
    }
  }

  private static void writeSInt32(Object message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeSInt32(fieldNumber, value);
    }
  }

  private static void writeFixed32(Object message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeFixed32(fieldNumber, value);
    }
  }

  private static void writeSFixed32(Object message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeSFixed32(fieldNumber, value);
    }
  }

  private static void writeEnum(Object message, Writer writer, int fieldNumber, long offset) {
    int value = UnsafeUtil.getInt(message, offset);
    if (value != 0) {
      writer.writeEnum(fieldNumber, value);
    }
  }

  private static void writeBool(Object message, Writer writer, int fieldNumber, long offset) {
    if (UnsafeUtil.getBoolean(message, offset)) {
      writer.writeBool(fieldNumber, true);
    }
  }

  private static void writeString(Object message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    String value = (String) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeString(fieldNumber, value);
    }
  }

  private static void writeBytes(Object message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    ByteString value = (ByteString) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeBytes(fieldNumber, value);
    }
  }

  private static void writeMessage(Object message, Writer writer, int fieldNumber, long offset) {
    Object value = UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeMessage(fieldNumber, value);
    }
  }

  private static void writeDoubleList(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Double> value = (List<Double>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeDoubleList(fieldNumber, packed, value);
    }
  }

  private static void writeFloatList(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Float> value = (List<Float>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeFloatList(fieldNumber, packed, value);
    }
  }

  private static void writeInt64List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeInt64List(fieldNumber, packed, value);
    }
  }

  private static void writeUInt64List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeUInt64List(fieldNumber, packed, value);
    }
  }

  private static void writeSInt64List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSInt64List(fieldNumber, packed, value);
    }
  }

  private static void writeFixed64List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeFixed64List(fieldNumber, packed, value);
    }
  }

  private static void writeSFixed64List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Long> value = (List<Long>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSFixed64List(fieldNumber, packed, value);
    }
  }

  private static void writeInt32List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeInt32List(fieldNumber, packed, value);
    }
  }

  private static void writeUInt32List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeUInt32List(fieldNumber, packed, value);
    }
  }

  private static void writeSInt32List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSInt32List(fieldNumber, packed, value);
    }
  }

  private static void writeFixed32List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeFixed32List(fieldNumber, packed, value);
    }
  }

  private static void writeSFixed32List(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeSFixed32List(fieldNumber, packed, value);
    }
  }

  private static void writeEnumList(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Integer> value = (List<Integer>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeEnumList(fieldNumber, packed, value);
    }
  }

  private static void writeBoolList(Object message, Writer writer, int fieldNumber, long offset, boolean packed) {
    @SuppressWarnings("unchecked")
    List<Boolean> value = (List<Boolean>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeBoolList(fieldNumber, packed, value);
    }
  }

  private static void writeStringList(Object message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    List<String> value = (List<String>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeStringList(fieldNumber, value);
    }
  }

  private static void writeBytesList(Object message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    List<ByteString> value = (List<ByteString>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeBytesList(fieldNumber, value);
    }
  }

  private static void writeMessageList(Object message, Writer writer, int fieldNumber, long offset) {
    @SuppressWarnings("unchecked")
    List<?> value = (List<?>) UnsafeUtil.getObject(message, offset);
    if (value != null) {
      writer.writeMessageList(fieldNumber, value);
    }
  }

  private static FieldType getFieldType(long data) {
    return FieldType.forId(getFieldTypeId(data));
  }

  private static int getFieldNumber(long data) {
    return (int) data;
  }

  private static byte getFieldTypeId(long data) {
    return (byte) (data >> 32);
  }
}
