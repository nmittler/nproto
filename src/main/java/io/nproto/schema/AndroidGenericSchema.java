package io.nproto.schema;

import io.nproto.ByteString;
import io.nproto.JavaType;
import io.nproto.Reader;
import io.nproto.Writer;
import io.nproto.descriptor.BeanDescriptor;
import io.nproto.descriptor.PropertyDescriptor;
import io.nproto.descriptor.PropertyType;
import io.nproto.util.AndroidSchemaUtil;
import io.nproto.util.AndroidUnsafeUtil;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

final class AndroidGenericSchema<T> implements Schema<T> {
  private static final int ENTRIES_PER_FIELD = 2;
  private static final int LONG_LENGTH = 8;
  private static final int FIELD_LENGTH = ENTRIES_PER_FIELD * LONG_LENGTH;
  private static final long DATA_OFFSET = AndroidUnsafeUtil.arrayBaseOffset(long[].class);
  private final long[] data;
  private final long dataLimit;
  private final FieldMap fieldMap;

  // Array that holds lazy entries for fields used for iteration.
  private WeakReference<Field[]> fields;

  AndroidGenericSchema(BeanDescriptor descriptor) {
    List<PropertyDescriptor> protoProperties = descriptor.getPropertyDescriptors();
    final int numFields = protoProperties.size();
    fieldMap = AndroidSchemaUtil.shouldUseTableSwitch(protoProperties) ?
            new TableFieldMap(protoProperties) : new LookupFieldMap(protoProperties);
    data = new long[numFields * ENTRIES_PER_FIELD];
    int lastFieldNumber = Integer.MAX_VALUE;
    long dataPos = DATA_OFFSET;
    for (int i = 0; i < numFields; ++i) {
      PropertyDescriptor f = protoProperties.get(i);
      if (f.fieldNumber == lastFieldNumber) {
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      fieldMap.loadField(f, i, dataPos);
      AndroidUnsafeUtil.putLong(data, dataPos, (((long) f.type.id()) << 32) | f.fieldNumber);
      AndroidUnsafeUtil.putLong(data, dataPos + LONG_LENGTH, AndroidUnsafeUtil.objectFieldOffset(f.field));
      dataPos += FIELD_LENGTH;
    }
    dataLimit = DATA_OFFSET + (data.length * LONG_LENGTH);
  }

  @Override
  public Iterator<Field> iterator() {
    Field[] fields = getOrCreateFields();
    return new FieldIterator(fields);
  }

  @Override
  public void writeTo(T message, Writer writer) {
    for(long pos = DATA_OFFSET; pos < dataLimit; pos += FIELD_LENGTH) {
      // Switching on the field type ID to avoid the lookup of FieldType.
      final long numberAndType = getLong(pos);
      final int fieldNumber = getFieldNumber(numberAndType);
      switch (getFieldTypeId(numberAndType)) {
        case 1: //DOUBLE:
          AndroidSchemaUtil.unsafeWriteDouble(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 2: //FLOAT:
          AndroidSchemaUtil.unsafeWriteFloat(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 3: //INT64:
          AndroidSchemaUtil.unsafeWriteInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 4: //UINT64:
          AndroidSchemaUtil.unsafeWriteUInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 5: //INT32:
          AndroidSchemaUtil.unsafeWriteInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 6: //FIXED64:
          AndroidSchemaUtil.unsafeWriteFixed64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 7: //FIXED32:
          AndroidSchemaUtil.unsafeWriteFixed32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 8: //BOOL:
          AndroidSchemaUtil.unsafeWriteBool(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 9: //STRING:
          AndroidSchemaUtil.unsafeWriteString(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 10: //MESSAGE:
          AndroidSchemaUtil.unsafeWriteMessage(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 11: //BYTES:
          AndroidSchemaUtil.unsafeWriteBytes(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 12: //UINT32:
          AndroidSchemaUtil.unsafeWriteUInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 13: //ENUM:
          AndroidSchemaUtil.unsafeWriteEnum(fieldNumber, message, getLong(pos + LONG_LENGTH), writer, Enum.class);
          break;
        case 14: //SFIXED32:
          AndroidSchemaUtil.unsafeWriteSFixed32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 15: //SFIXED64:
          AndroidSchemaUtil.unsafeWriteSFixed64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 16: //SINT32:
          AndroidSchemaUtil.unsafeWriteSInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 17: //SINT64:
          AndroidSchemaUtil.unsafeWriteSInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 18: //DOUBLE_LIST:
          AndroidSchemaUtil.unsafeWriteDoubleList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 19: //FLOAT_LIST:
          AndroidSchemaUtil.unsafeWriteFloatList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 20: //INT64_LIST:
          AndroidSchemaUtil.unsafeWriteInt64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 21: //UINT64_LIST:
          AndroidSchemaUtil.unsafeWriteUInt64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 22: //INT32_LIST:
          AndroidSchemaUtil.unsafeWriteInt32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 23: //FIXED64_LIST:
          AndroidSchemaUtil.unsafeWriteFixed64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 24: //FIXED32_LIST:
          AndroidSchemaUtil.unsafeWriteFixed32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 25: //BOOL_LIST:
          AndroidSchemaUtil.unsafeWriteBoolList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 26: //STRING_LIST:
          AndroidSchemaUtil.unsafeWriteStringList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 27: //MESSAGE_LIST:
          AndroidSchemaUtil.unsafeWriteMessageList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 28: //BYTES_LIST:
          AndroidSchemaUtil.unsafeWriteBytesList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 29: //UINT32_LIST:
          AndroidSchemaUtil.unsafeWriteUInt32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 30: //ENUM_LIST:
          AndroidSchemaUtil.unsafeWriteEnumList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer, Enum.class);
          break;
        case 31: //SFIXED32_LIST:
          AndroidSchemaUtil.unsafeWriteSFixed32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 32: //SFIXED64_LIST:
          AndroidSchemaUtil.unsafeWriteSFixed64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 33: //SINT32_LIST:
          AndroidSchemaUtil.unsafeWriteSInt32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 34: //SINT64_LIST:
          AndroidSchemaUtil.unsafeWriteSInt64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        default:
          throw new IllegalArgumentException("Unsupported fieldType: " + getFieldType(getLong(pos)));
      }
    }
  }

  @Override
  public void mergeFrom(T message, Reader reader) {
    while (true) {
      final long pos = fieldMap.getDataPos(reader.fieldNumber());
      if (pos < 0L) {
        // Unknown field.
        if (reader.skipField()) {
          continue;
        }
        // Done reading.
        return;
      }

      switch(getFieldTypeId(getLong(pos))) {
        case 1: //DOUBLE:
          AndroidSchemaUtil.unsafeReadDouble(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 2: //FLOAT:
          AndroidSchemaUtil.unsafeReadFloat(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 3: //INT64:
          AndroidSchemaUtil.unsafeReadInt64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 4: //UINT64:
          AndroidSchemaUtil.unsafeReadUInt64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 5: //INT32:
          AndroidSchemaUtil.unsafeReadInt32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 6: //FIXED64:
          AndroidSchemaUtil.unsafeReadFixed64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 7: //FIXED32:
          AndroidSchemaUtil.unsafeReadFixed32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 8: //BOOL:
          AndroidSchemaUtil.unsafeReadBool(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 9: //STRING:
          AndroidSchemaUtil.unsafeReadString(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 10: //MESSAGE:
          AndroidSchemaUtil.unsafeReadMessage(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 11: //BYTES:
          AndroidSchemaUtil.unsafeReadBytes(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 12: //UINT32:
          AndroidSchemaUtil.unsafeReadUInt32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 13: //ENUM:
          AndroidSchemaUtil.unsafeReadEnum(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 14: //SFIXED32:
          AndroidSchemaUtil.unsafeReadSFixed32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 15: //SFIXED64:
          AndroidSchemaUtil.unsafeReadSFixed64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 16: //SINT32:
          AndroidSchemaUtil.unsafeReadSInt32(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 17: //SINT64:
          AndroidSchemaUtil.unsafeReadSInt64(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 18: //DOUBLE_LIST:
          AndroidSchemaUtil.unsafeReadDoubleList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 19: //FLOAT_LIST:
          AndroidSchemaUtil.unsafeReadFloatList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 20: //INT64_LIST:
          AndroidSchemaUtil.unsafeReadInt64List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 21: //UINT64_LIST:
          AndroidSchemaUtil.unsafeReadUInt64List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 22: //INT32_LIST:
          AndroidSchemaUtil.unsafeReadInt32List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 23: //FIXED64_LIST:
          AndroidSchemaUtil.unsafeReadFixed64List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 24: //FIXED32_LIST:
          AndroidSchemaUtil.unsafeReadFixed32List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 25: //BOOL_LIST:
          AndroidSchemaUtil.unsafeReadBoolList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 26: //STRING_LIST:
          AndroidSchemaUtil.unsafeReadStringList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 27: //MESSAGE_LIST:
          AndroidSchemaUtil.unsafeReadMessageList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 28: //BYTES_LIST:
          AndroidSchemaUtil.unsafeReadBytesList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 29: //UINT32_LIST:
          AndroidSchemaUtil.unsafeReadUInt32List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 30: //ENUM_LIST:
          AndroidSchemaUtil.unsafeReadEnumList(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 31: //SFIXED32_LIST:
          AndroidSchemaUtil.unsafeReadSFixed32List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 32: //SFIXED64_LIST:
          AndroidSchemaUtil.unsafeReadSFixed64List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 33: //SINT32_LIST:
          AndroidSchemaUtil.unsafeReadSInt32List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        case 34: //SINT64_LIST:
          AndroidSchemaUtil.unsafeReadSInt64List(message, getLong(pos + LONG_LENGTH), reader);
          break;
        default:
          throw new IllegalArgumentException("Unsupported fieldType: " + getFieldType(getLong(pos)));
      }
    }
  }

  private long getLong(long pos) {
    return AndroidUnsafeUtil.getLong(data, pos);
  }

  private Field[] getOrCreateFields() {
    Field[] temp = fields != null ? fields.get() : null;
    if (temp == null) {
      int numFields = data.length / ENTRIES_PER_FIELD;
      temp = new Field[numFields];
      for(int i = 0; i < numFields; ++i) {
        temp[i] = new FieldImpl(data, i * ENTRIES_PER_FIELD);
      }
      fields = new WeakReference<Field[]>(temp);
    }
    return temp;
  }

  private static PropertyType getFieldType(long data) {
    return PropertyType.forId(getFieldTypeId(data));
  }

  private static int getFieldNumber(long data) {
    return (int) data;
  }

  private static byte getFieldTypeId(long data) {
    return (byte) (data >> 32);
  }

  private static final class FieldIterator implements Iterator<Field> {
    private int fieldIndex;
    private final Field[] fields;

    FieldIterator(Field[] fields) {
      this.fields = fields;
    }

    @Override
    public boolean hasNext() {
      return fieldIndex < fields.length;
    }

    @Override
    public Field next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      return fields[fieldIndex++];
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private static final class FieldImpl implements Field {
    private final long[] data;
    private final int dataPos;

    FieldImpl(long[] data, int dataPos) {
      this.data = data;
      this.dataPos = dataPos;
    }

    @Override
    public int number() {
      return getFieldNumber(data[dataPos]);
    }

    @Override
    public PropertyType type() {
      return getFieldType(data[dataPos]);
    }

    private long valueOffset() {
      return data[dataPos + 1];
    }

    @Override
    public int intValue(Object message) {
      if (type().getJavaType() != JavaType.INT) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return AndroidUnsafeUtil.getInt(message, valueOffset());
    }

    @Override
    public <E extends Enum<E>> Enum<E> enumValue(Object message, Class<E> clazz) {
      if (type().getJavaType() != JavaType.ENUM) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return clazz.cast(AndroidUnsafeUtil.getObject(message, valueOffset()));
    }

    @Override
    public long longValue(Object message) {
      if (type().getJavaType() != JavaType.LONG) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return AndroidUnsafeUtil.getLong(message, valueOffset());
    }

    @Override
    public double doubleValue(Object message) {
      if (type().getJavaType() != JavaType.DOUBLE) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return AndroidUnsafeUtil.getDouble(message, valueOffset());
    }

    @Override
    public float floatValue(Object message) {
      if (type().getJavaType() != JavaType.FLOAT) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return AndroidUnsafeUtil.getFloat(message, valueOffset());
    }

    @Override
    public Object messageValue(Object message) {
      if (type().getJavaType() != JavaType.MESSAGE) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return AndroidUnsafeUtil.getObject(message, valueOffset());
    }

    @Override
    public String stringValue(Object message) {
      if (type().getJavaType() != JavaType.STRING) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return (String) AndroidUnsafeUtil.getObject(message, valueOffset());
    }

    @Override
    public ByteString bytesValue(Object message) {
      if (type().getJavaType() != JavaType.BYTE_STRING) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      return (ByteString) AndroidUnsafeUtil.getObject(message, valueOffset());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <L> List<L> values(Object message, Class<? extends List<L>> clazz) {
      if (type().getJavaType() != JavaType.LIST) {
        throw new IllegalStateException("Incorrect java type: " + type().getJavaType());
      }
      // TODO(nathanmittler): check the type parameter before casting.
      return (List<L>) AndroidUnsafeUtil.getObject(message, valueOffset());
    }
  }

  private static abstract class FieldMap {
    abstract long getDataPos(int fieldNumber);
    abstract void loadField(PropertyDescriptor field, int fieldIndex, long dataPos);
  }

  private static final class TableFieldMap extends FieldMap {
    private final int min;
    private final int max;
    private final long[] positions;

    TableFieldMap(List<PropertyDescriptor> fields) {
      min = fields.get(0).fieldNumber;
      max = fields.get(fields.size() - 1).fieldNumber;
      int max = fields.get(fields.size() - 1).fieldNumber;
      int numPositions = (max - min) + 1;
      positions = new long[numPositions];
    }

    @Override
    void loadField(PropertyDescriptor field, int fieldIndex, long dataPos) {
      positions[field.fieldNumber - min] = dataPos;
    }

    @Override
    long getDataPos(int fieldNumber) {
      if (fieldNumber < min || fieldNumber > max) {
        return -1;
      }
      return positions[fieldNumber - min];
    }
  }

  private static final class LookupFieldMap extends FieldMap {
    private final int[] fieldNumbers;
    private final long[] positions;

    LookupFieldMap(List<PropertyDescriptor> fields) {
      int numFields = fields.size();
      fieldNumbers = new int[numFields];
      positions = new long[numFields];
      for (int i = 0; i < numFields; ++i) {
        fieldNumbers[i] = fields.get(i).fieldNumber;
        positions[i] = AndroidUnsafeUtil.arrayBaseOffset(long[].class) + (i * FIELD_LENGTH);
      }
    }

    @Override
    void loadField(PropertyDescriptor field, int fieldIndex, long dataPos) {
      fieldNumbers[fieldIndex] = field.fieldNumber;
      positions[fieldIndex] = dataPos;
    }

    @Override
    long getDataPos(int fieldNumber) {
      int i = Arrays.binarySearch(fieldNumbers, fieldNumber);
      if (i < 0) {
        return -1;
      }
      // TODO: Consider avoiding allocation of the positions[] by computing dataPos from i.
      return positions[i];
    }
  }
}
