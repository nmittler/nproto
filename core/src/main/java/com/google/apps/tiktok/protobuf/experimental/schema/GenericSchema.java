package com.google.apps.tiktok.protobuf.experimental.schema;

import com.google.apps.tiktok.protobuf.experimental.FieldType;
import com.google.apps.tiktok.protobuf.experimental.descriptor.FieldDescriptor;
import com.google.apps.tiktok.protobuf.experimental.descriptor.MessageDescriptor;
import com.google.apps.tiktok.protobuf.experimental.util.SchemaUtil;
import com.google.apps.tiktok.protobuf.experimental.util.UnsafeUtil;

import java.util.Arrays;
import java.util.List;

/**
 * A generic schema that can be used with any protobuf message class. This class requires the use
 * of {@code sun.misc.Unsafe} for unsafe access to field values in the message.
 */
final class GenericSchema<T> implements Schema<T> {
  private static final int ENTRIES_PER_FIELD = 2;
  private static final int LONG_LENGTH = 8;
  private static final int FIELD_LENGTH = ENTRIES_PER_FIELD * LONG_LENGTH;
  private static final long DATA_OFFSET = UnsafeUtil.arrayBaseOffset(long[].class);
  private static final int FIELD_NUMBER_BITS = 29;
  private static final int FIELD_NUMBER_MASK = 0x1FFFFFFF;

  /**
   * Holds all information for accessing the message fields. Each field is represented by two
   * elements in this array, where the first is a concatenation of {@code fieldType} and
   * {@code fieldNumber}, and the second is the unsafe offset of the field within the message.
   *
   * <p>{@code [(fieldType.id() << 29) | fieldNumber][unsafeFieldOffset]}
   */
  private final long[] data;

  /**
   * The limit of the {@code data} array expressed as an unsafe offset from the beginning of the
   * array.
   */
  private final long dataLimit;

  /**
   * Map for looking up the position in the {@code data} array for a given {@code fieldNumber}.
   */
  private final FieldMap fieldMap;

  GenericSchema(MessageDescriptor descriptor) {
    List<FieldDescriptor> fieldDescriptors = descriptor.getFieldDescriptors();
    final int numFields = fieldDescriptors.size();
    fieldMap = FieldMap.newFieldMap(fieldDescriptors);
    data = new long[numFields * ENTRIES_PER_FIELD];
    int lastFieldNumber = Integer.MAX_VALUE;
    long dataPos = DATA_OFFSET;
    for (int i = 0; i < numFields; ++i) {
      FieldDescriptor f = fieldDescriptors.get(i);
      if (f.getFieldNumber() == lastFieldNumber) {
        throw new RuntimeException("Duplicate field number: " + f.getFieldNumber());
      }
      lastFieldNumber = f.getFieldNumber();
      fieldMap.setDataPos(f, i, dataPos);
      UnsafeUtil.putLong(
          data, dataPos, (((long) f.getType().id()) << FIELD_NUMBER_BITS) | f.getFieldNumber());
      UnsafeUtil.putLong(data, dataPos + LONG_LENGTH, UnsafeUtil.objectFieldOffset(f.getField()));
      dataPos += FIELD_LENGTH;
    }
    dataLimit = DATA_OFFSET + (data.length * LONG_LENGTH);
  }

  @Override
  public void writeTo(T message, Writer writer) {
    for (long pos = DATA_OFFSET; pos < dataLimit; pos += FIELD_LENGTH) {
      // Switching on the field type ID to avoid the lookup of FieldType.
      final long numberAndType = getLong(pos);
      final int fieldNumber = getFieldNumber(numberAndType);

      // Benchmarks have shown that switching on a byte is faster than an enum.
      switch (getFieldTypeId(numberAndType)) {
        case 0: //DOUBLE:
          SchemaUtil.unsafeWriteDouble(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 1: //FLOAT:
          SchemaUtil.unsafeWriteFloat(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 2: //INT64:
          SchemaUtil.unsafeWriteInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 3: //UINT64:
          SchemaUtil.unsafeWriteUInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 4: //INT32:
          SchemaUtil.unsafeWriteInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 5: //FIXED64:
          SchemaUtil.unsafeWriteFixed64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 6: //FIXED32:
          SchemaUtil.unsafeWriteFixed32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 7: //BOOL:
          SchemaUtil.unsafeWriteBool(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 8: //STRING:
          SchemaUtil.unsafeWriteString(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 9: //MESSAGE:
          SchemaUtil.unsafeWriteMessage(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 10: //BYTES:
          SchemaUtil.unsafeWriteBytes(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 11: //UINT32:
          SchemaUtil.unsafeWriteUInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 12: //ENUM:
          SchemaUtil.unsafeWriteEnum(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 13: //SFIXED32:
          SchemaUtil.unsafeWriteSFixed32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 14: //SFIXED64:
          SchemaUtil.unsafeWriteSFixed64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 15: //SINT32:
          SchemaUtil.unsafeWriteSInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 16: //SINT64:
          SchemaUtil.unsafeWriteSInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 17: //DOUBLE_LIST:
          SchemaUtil.unsafeWriteDoubleList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 18: //FLOAT_LIST:
          SchemaUtil.unsafeWriteFloatList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 19: //INT64_LIST:
          SchemaUtil.unsafeWriteInt64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 20: //UINT64_LIST:
          SchemaUtil.unsafeWriteUInt64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 21: //INT32_LIST:
          SchemaUtil.unsafeWriteInt32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 22: //FIXED64_LIST:
          SchemaUtil.unsafeWriteFixed64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 23: //FIXED32_LIST:
          SchemaUtil.unsafeWriteFixed32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 24: //BOOL_LIST:
          SchemaUtil.unsafeWriteBoolList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 25: //STRING_LIST:
          SchemaUtil.unsafeWriteStringList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 26: //MESSAGE_LIST:
          SchemaUtil.unsafeWriteMessageList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 27: //BYTES_LIST:
          SchemaUtil.unsafeWriteBytesList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 28: //UINT32_LIST:
          SchemaUtil.unsafeWriteUInt32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 29: //ENUM_LIST:
          SchemaUtil.unsafeWriteEnumList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 30: //SFIXED32_LIST:
          SchemaUtil.unsafeWriteSFixed32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 31: //SFIXED64_LIST:
          SchemaUtil.unsafeWriteSFixed64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 32: //SINT32_LIST:
          SchemaUtil.unsafeWriteSInt32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 33: //SINT64_LIST:
          SchemaUtil.unsafeWriteSInt64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, false);
          break;
        case 34: //DOUBLE_LIST_PACKED:
          SchemaUtil.unsafeWriteDoubleList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 35: //FLOAT_LIST_PACKED:
          SchemaUtil.unsafeWriteFloatList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 36: //INT64_LIST_PACKED:
          SchemaUtil.unsafeWriteInt64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 37: //UINT64_LIST_PACKED:
          SchemaUtil.unsafeWriteUInt64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 38: //INT32_LIST_PACKED:
          SchemaUtil.unsafeWriteInt32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 39: //FIXED64_LIST_PACKED:
          SchemaUtil.unsafeWriteFixed64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 40: //FIXED32_LIST_PACKED:
          SchemaUtil.unsafeWriteFixed32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 41: //BOOL_LIST_PACKED:
          SchemaUtil.unsafeWriteBoolList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 42: //UINT32_LIST_PACKED:
          SchemaUtil.unsafeWriteUInt32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 43: //ENUM_LIST_PACKED:
          SchemaUtil.unsafeWriteEnumList(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 44: //SFIXED32_LIST_PACKED:
          SchemaUtil.unsafeWriteSFixed32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 45: //SFIXED64_LIST_PACKED:
          SchemaUtil.unsafeWriteSFixed64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 46: //SINT32_LIST_PACKED:
          SchemaUtil.unsafeWriteSInt32List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        case 47: //SINT64_LIST_PACKED:
          SchemaUtil.unsafeWriteSInt64List(
              fieldNumber, message, getLong(pos + LONG_LENGTH), writer, true);
          break;
        default:
          throw new IllegalArgumentException(
              "Unsupported fieldType: " + getFieldType(getLong(pos)));
      }
    }
  }

  @Override
  public void mergeFrom(T message, Reader reader) {
    while (true) {
      final long pos = fieldMap.getDataPos(reader.getFieldNumber());
      if (pos < 0L) {
        // Unknown field.
        if (reader.skipField()) {
          continue;
        }
        // Done reading.
        return;
      }

      // Benchmarks have shown that switching on a byte is faster than an enum.
      switch (getFieldTypeId(getLong(pos))) {
        case 0: //DOUBLE:
          SchemaUtil.unsafeReadDouble(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 1: //FLOAT:
          SchemaUtil.unsafeReadFloat(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 2: //INT64:
          SchemaUtil.unsafeReadInt64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 3: //UINT64:
          SchemaUtil.unsafeReadUInt64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 4: //INT32:
          SchemaUtil.unsafeReadInt32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 5: //FIXED64:
          SchemaUtil.unsafeReadFixed64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 6: //FIXED32:
          SchemaUtil.unsafeReadFixed32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 7: //BOOL:
          SchemaUtil.unsafeReadBool(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 8: //STRING:
          SchemaUtil.unsafeReadString(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 9: //MESSAGE:
          SchemaUtil.unsafeReadMessage(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 10: //BYTES:
          SchemaUtil.unsafeReadBytes(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 11: //UINT32:
          SchemaUtil.unsafeReadUInt32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 12: //ENUM:
          SchemaUtil.unsafeReadEnum(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 13: //SFIXED32:
          SchemaUtil.unsafeReadSFixed32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 14: //SFIXED64:
          SchemaUtil.unsafeReadSFixed64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 15: //SINT32:
          SchemaUtil.unsafeReadSInt32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 16: //SINT64:
          SchemaUtil.unsafeReadSInt64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 17: //DOUBLE_LIST:
          SchemaUtil.unsafeReadDoubleList(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 18: //FLOAT_LIST:
          SchemaUtil.unsafeReadFloatList(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 19: //INT64_LIST:
          SchemaUtil.unsafeReadInt64List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 20: //UINT64_LIST:
          SchemaUtil.unsafeReadUInt64List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 21: //INT32_LIST:
          SchemaUtil.unsafeReadInt32List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 22: //FIXED64_LIST:
          SchemaUtil.unsafeReadFixed64List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 23: //FIXED32_LIST:
          SchemaUtil.unsafeReadFixed32List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 24: //BOOL_LIST:
          SchemaUtil.unsafeReadBoolList(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 25: //STRING_LIST:
          SchemaUtil.unsafeReadStringList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 26: //MESSAGE_LIST:
          SchemaUtil.unsafeReadMessageList(
              message, getLong(pos + LONG_LENGTH), reader, Object.class);
          break;
        case 27: //BYTES_LIST:
          SchemaUtil.unsafeReadBytesList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 28: //UINT32_LIST:
          SchemaUtil.unsafeReadUInt32List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 29: //ENUM_LIST:
          SchemaUtil.unsafeReadEnumList(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 30: //SFIXED32_LIST:
          SchemaUtil.unsafeReadSFixed32List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 31: //SFIXED64_LIST:
          SchemaUtil.unsafeReadSFixed64List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 32: //SINT32_LIST:
          SchemaUtil.unsafeReadSInt32List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 33: //SINT64_LIST:
          SchemaUtil.unsafeReadSInt64List(message, getLong(pos + LONG_LENGTH), reader, false);
          break;
        case 34: //DOUBLE_LIST_PACKED:
          SchemaUtil.unsafeReadDoubleList(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 35: //FLOAT_LIST_PACKED:
          SchemaUtil.unsafeReadFloatList(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 36: //INT64_LIST_PACKED:
          SchemaUtil.unsafeReadInt64List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 37: //UINT64_LIST_PACKED:
          SchemaUtil.unsafeReadUInt64List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 38: //INT32_LIST_PACKED:
          SchemaUtil.unsafeReadInt32List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 39: //FIXED64_LIST_PACKED:
          SchemaUtil.unsafeReadFixed64List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 40: //FIXED32_LIST_PACKED:
          SchemaUtil.unsafeReadFixed32List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 41: //BOOL_LIST_PACKED:
          SchemaUtil.unsafeReadBoolList(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 42: //UINT32_LIST_PACKED:
          SchemaUtil.unsafeReadUInt32List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 43: //ENUM_LIST_PACKED:
          SchemaUtil.unsafeReadEnumList(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 44: //SFIXED32_LIST_PACKED:
          SchemaUtil.unsafeReadSFixed32List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 45: //SFIXED64_LIST_PACKED:
          SchemaUtil.unsafeReadSFixed64List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 46: //SINT32_LIST_PACKED:
          SchemaUtil.unsafeReadSInt32List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        case 47: //SINT64_LIST_PACKED:
          SchemaUtil.unsafeReadSInt64List(message, getLong(pos + LONG_LENGTH), reader, true);
          break;
        default:
          throw new IllegalArgumentException(
              "Unsupported fieldType: " + getFieldType(getLong(pos)));
      }
    }
  }

  private long getLong(long pos) {
    return UnsafeUtil.getLong(data, pos);
  }

  private static FieldType getFieldType(long data) {
    return FieldType.forId(getFieldTypeId(data));
  }

  private static int getFieldNumber(long data) {
    return (int) (data & FIELD_NUMBER_MASK);
  }

  private static byte getFieldTypeId(long data) {
    return (byte) (data >> FIELD_NUMBER_BITS);
  }

  /**
   * Map used for looking up the position in the {@code data} array for a field. There are two
   * choices based on the JVM's options for switch statement generation: table and lookup. The
   * {@link TableFieldMap} is used when the array of field numbers is dense and direct indexing
   * into an array can be used without being overly memory intensive. Otherwise,
   * {@link LookupFieldMap} is used, which performs a binary search over an array to identify the
   * position by field number.
   *
   * @see SchemaUtil#shouldUseTableSwitch(List)
   */
  private abstract static class FieldMap {
    /**
     * Gets the position in the {@code data} array for the field.
     *
     * @param fieldNumber the field number for the field.
     * @return the position or {@code -1} if not found.
     */
    abstract long getDataPos(int fieldNumber);

    /**
     * Sets the position in the {@code data} array for a field.
     *
     * @param field the descriptor for the field.
     * @param fieldIndex the index into the descriptor array for this field.
     * @param dataPos the position in the {@code data} array for this field.
     */
    abstract void setDataPos(FieldDescriptor field, int fieldIndex, long dataPos);

    /**
     * Creates the appropriate type of {@link FieldMap} for the message.
     */
    static FieldMap newFieldMap(List<FieldDescriptor> fieldDescriptors) {
      return SchemaUtil.shouldUseTableSwitch(fieldDescriptors)
          ? new TableFieldMap(fieldDescriptors)
          : new LookupFieldMap(fieldDescriptors);
    }
  }

  /**
   * Map that uses direct indexing when looking up field positions.
   */
  private static final class TableFieldMap extends FieldMap {
    private final int min;
    private final int max;
    private final long[] positions;

    TableFieldMap(List<FieldDescriptor> fields) {
      min = fields.get(0).getFieldNumber();
      max = fields.get(fields.size() - 1).getFieldNumber();
      int numPositions = (max - min) + 1;
      positions = new long[numPositions];
    }

    @Override
    void setDataPos(FieldDescriptor field, int fieldIndex, long dataPos) {
      positions[field.getFieldNumber() - min] = dataPos;
    }

    @Override
    long getDataPos(int fieldNumber) {
      if (fieldNumber < min || fieldNumber > max) {
        return -1;
      }
      return positions[fieldNumber - min];
    }
  }

  /**
   * Map that performs a binary search over the field numbers to find the field position.
   */
  private static final class LookupFieldMap extends FieldMap {
    private final int[] fieldNumbers;
    private final long[] positions;

    LookupFieldMap(List<FieldDescriptor> fields) {
      int numFields = fields.size();
      fieldNumbers = new int[numFields];
      positions = new long[numFields];
      for (int i = 0; i < numFields; ++i) {
        fieldNumbers[i] = fields.get(i).getFieldNumber();
        positions[i] = UnsafeUtil.arrayBaseOffset(long[].class) + (i * FIELD_LENGTH);
      }
    }

    @Override
    void setDataPos(FieldDescriptor field, int fieldIndex, long dataPos) {
      fieldNumbers[fieldIndex] = field.getFieldNumber();
      positions[fieldIndex] = dataPos;
    }

    @Override
    long getDataPos(int fieldNumber) {
      int i = Arrays.binarySearch(fieldNumbers, fieldNumber);
      if (i < 0) {
        return -1;
      }
      // TODO: Consider avoiding allocation of the positions[] by computing dataPos from i.
      // Need to benchmark to see if there is any loss in performance with doing this.
      return positions[i];
    }
  }
}
