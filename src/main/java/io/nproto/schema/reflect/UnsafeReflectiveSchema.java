package io.nproto.schema.reflect;

import static io.nproto.util.UnsafeUtil.fieldOffset;

import io.nproto.ByteString;
import io.nproto.descriptor.PropertyType;
import io.nproto.JavaType;
import io.nproto.Reader;
import io.nproto.descriptor.BeanDescriptor;
import io.nproto.util.UnsafeUtil;
import io.nproto.Writer;
import io.nproto.schema.Field;
import io.nproto.schema.Schema;
import io.nproto.util.SchemaUtil;
import io.nproto.descriptor.PropertyDescriptor;

import sun.plugin.dom.exception.InvalidStateException;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

final class UnsafeReflectiveSchema<T> implements Schema<T> {
  private static final int ENTRIES_PER_FIELD = 2;
  private static final int LONG_LENGTH = 8;
  private static final int FIELD_LENGTH = ENTRIES_PER_FIELD * LONG_LENGTH;
  private static final long DATA_OFFSET = UnsafeUtil.arrayBaseOffset(long[].class);
  private final long[] data;
  private final long dataLimit;
  private final FieldMap fieldMap;

  // Array that holds lazy entries for fields used for iteration.
  private WeakReference<Field[]> fields;

  UnsafeReflectiveSchema(BeanDescriptor descriptor) {
    List<PropertyDescriptor> protoProperties = descriptor.getPropertyDescriptors();
    final int numFields = protoProperties.size();
    fieldMap = SchemaUtil.shouldUseTableSwitch(protoProperties) ?
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
      UnsafeUtil.putLong(data, dataPos, (((long) f.type.id()) << 32) | f.fieldNumber);
      UnsafeUtil.putLong(data, dataPos + LONG_LENGTH, fieldOffset(f.field));
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
          SchemaUtil.unsafeWriteDouble(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 2: //FLOAT:
          SchemaUtil.unsafeWriteFloat(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 3: //INT64:
          SchemaUtil.unsafeWriteInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 4: //UINT64:
          SchemaUtil.unsafeWriteUInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 5: //INT32:
          SchemaUtil.unsafeWriteInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 6: //FIXED64:
          SchemaUtil.unsafeWriteFixed64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 7: //FIXED32:
          SchemaUtil.unsafeWriteFixed32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 8: //BOOL:
          SchemaUtil.unsafeWriteBool(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 9: //STRING:
          SchemaUtil.unsafeWriteString(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 10: //MESSAGE:
          SchemaUtil.unsafeWriteMessage(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 11: //BYTES:
          SchemaUtil.unsafeWriteBytes(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 12: //UINT32:
          SchemaUtil.unsafeWriteUInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 13: //ENUM:
          SchemaUtil.unsafeWriteEnum(fieldNumber, message, getLong(pos + LONG_LENGTH), writer, Enum.class);
          break;
        case 14: //SFIXED32:
          SchemaUtil.unsafeWriteSFixed32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 15: //SFIXED64:
          SchemaUtil.unsafeWriteSFixed64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 16: //SINT32:
          SchemaUtil.unsafeWriteSInt32(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 17: //SINT64:
          SchemaUtil.unsafeWriteSInt64(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 18: //DOUBLE_LIST:
          SchemaUtil.unsafeWriteDoubleList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 19: //FLOAT_LIST:
          SchemaUtil.unsafeWriteFloatList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 20: //INT64_LIST:
          SchemaUtil.unsafeWriteInt64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 21: //UINT64_LIST:
          SchemaUtil.unsafeWriteUInt64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 22: //INT32_LIST:
          SchemaUtil.unsafeWriteInt32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 23: //FIXED64_LIST:
          SchemaUtil.unsafeWriteFixed64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 24: //FIXED32_LIST:
          SchemaUtil.unsafeWriteFixed32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 25: //BOOL_LIST:
          SchemaUtil.unsafeWriteBoolList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 26: //STRING_LIST:
          SchemaUtil.unsafeWriteStringList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 27: //MESSAGE_LIST:
          SchemaUtil.unsafeWriteMessageList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 28: //BYTES_LIST:
          SchemaUtil.unsafeWriteBytesList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 29: //UINT32_LIST:
          SchemaUtil.unsafeWriteUInt32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 30: //ENUM_LIST:
          SchemaUtil.unsafeWriteEnumList(fieldNumber, message, getLong(pos + LONG_LENGTH), writer, Enum.class);
          break;
        case 31: //SFIXED32_LIST:
          SchemaUtil.unsafeWriteSFixed32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 32: //SFIXED64_LIST:
          SchemaUtil.unsafeWriteSFixed64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 33: //SINT32_LIST:
          SchemaUtil.unsafeWriteSInt32List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        case 34: //SINT64_LIST:
          SchemaUtil.unsafeWriteSInt64List(fieldNumber, message, getLong(pos + LONG_LENGTH), writer);
          break;
        default:
          throw new IllegalArgumentException("Unsupported fieldType: " + getFieldType(getLong(pos)));
      }
    }
  }

  @Override
  public void mergeFrom(T message, Reader reader) {
    while (true) {
      int fieldNumber = reader.fieldNumber();
      if (fieldNumber == Reader.READ_DONE) {
        // Done reading.
        return;
      }
      long dataPos = fieldMap.getDataPos(fieldNumber);
      if (dataPos >= 0) {
        mergeFieldFrom(message, dataPos, reader);
      } else if (!reader.skipField()) {
        // We're done after skipping the unknown field.
        return;
      }
    }
  }

  private void mergeFieldFrom(T message, long pos, Reader reader) {
    switch(getFieldTypeId(getLong(pos))) {
      case 1: //DOUBLE:
        SchemaUtil.unsafeReadDouble(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 2: //FLOAT:
        SchemaUtil.unsafeReadFloat(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 3: //INT64:
        SchemaUtil.unsafeReadInt64(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 4: //UINT64:
        SchemaUtil.unsafeReadUInt64(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 5: //INT32:
        SchemaUtil.unsafeReadInt32(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 6: //FIXED64:
        SchemaUtil.unsafeReadFixed64(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 7: //FIXED32:
        SchemaUtil.unsafeReadFixed32(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 8: //BOOL:
        SchemaUtil.unsafeReadBool(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 9: //STRING:
        SchemaUtil.unsafeReadString(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 10: //MESSAGE:
        SchemaUtil.unsafeReadMessage(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 11: //BYTES:
        SchemaUtil.unsafeReadBytes(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 12: //UINT32:
        SchemaUtil.unsafeReadUInt32(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 13: //ENUM:
        SchemaUtil.unsafeReadEnum(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 14: //SFIXED32:
        SchemaUtil.unsafeReadSFixed32(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 15: //SFIXED64:
        SchemaUtil.unsafeReadSFixed64(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 16: //SINT32:
        SchemaUtil.unsafeReadSInt32(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 17: //SINT64:
        SchemaUtil.unsafeReadSInt64(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 18: //DOUBLE_LIST:
        SchemaUtil.unsafeReadDoubleList(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 19: //FLOAT_LIST:
        SchemaUtil.unsafeReadFloatList(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 20: //INT64_LIST:
        SchemaUtil.unsafeReadInt64List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 21: //UINT64_LIST:
        SchemaUtil.unsafeReadUInt64List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 22: //INT32_LIST:
        SchemaUtil.unsafeReadInt32List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 23: //FIXED64_LIST:
        SchemaUtil.unsafeReadFixed64List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 24: //FIXED32_LIST:
        SchemaUtil.unsafeReadFixed32List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 25: //BOOL_LIST:
        SchemaUtil.unsafeReadBoolList(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 26: //STRING_LIST:
        SchemaUtil.unsafeReadStringList(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 27: //MESSAGE_LIST:
        SchemaUtil.unsafeReadMessageList(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 28: //BYTES_LIST:
        SchemaUtil.unsafeReadBytesList(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 29: //UINT32_LIST:
        SchemaUtil.unsafeReadUInt32List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 30: //ENUM_LIST:
        SchemaUtil.unsafeReadEnumList(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 31: //SFIXED32_LIST:
        SchemaUtil.unsafeReadSFixed32List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 32: //SFIXED64_LIST:
        SchemaUtil.unsafeReadSFixed64List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 33: //SINT32_LIST:
        SchemaUtil.unsafeReadSInt32List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      case 34: //SINT64_LIST:
        SchemaUtil.unsafeReadSInt64List(message, getLong(pos + LONG_LENGTH), reader);
        break;
      default:
        throw new IllegalArgumentException("Unsupported fieldType: " + getFieldType(getLong(pos)));
    }
  }

  private long getLong(long pos) {
    return UnsafeUtil.getLong(data, pos);
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
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return UnsafeUtil.getInt(message, valueOffset());
    }

    @Override
    public <E extends Enum<E>> Enum<E> enumValue(Object message, Class<E> clazz) {
      if (type().getJavaType() != JavaType.ENUM) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return clazz.cast(UnsafeUtil.getObject(message, valueOffset()));
    }

    @Override
    public long longValue(Object message) {
      if (type().getJavaType() != JavaType.LONG) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return UnsafeUtil.getLong(message, valueOffset());
    }

    @Override
    public double doubleValue(Object message) {
      if (type().getJavaType() != JavaType.DOUBLE) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return UnsafeUtil.getDouble(message, valueOffset());
    }

    @Override
    public float floatValue(Object message) {
      if (type().getJavaType() != JavaType.FLOAT) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return UnsafeUtil.getFloat(message, valueOffset());
    }

    @Override
    public Object messageValue(Object message) {
      if (type().getJavaType() != JavaType.MESSAGE) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return UnsafeUtil.getObject(message, valueOffset());
    }

    @Override
    public String stringValue(Object message) {
      if (type().getJavaType() != JavaType.STRING) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return (String) UnsafeUtil.getObject(message, valueOffset());
    }

    @Override
    public ByteString bytesValue(Object message) {
      if (type().getJavaType() != JavaType.BYTE_STRING) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      return (ByteString) UnsafeUtil.getObject(message, valueOffset());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <L> List<L> values(Object message, Class<? extends List<L>> clazz) {
      if (type().getJavaType() != JavaType.LIST) {
        throw new InvalidStateException("Incorrect java type: " + type().getJavaType());
      }
      // TODO(nathanmittler): check the type parameter before casting.
      return (List<L>) UnsafeUtil.getObject(message, valueOffset());
    }
  }

  private static abstract class FieldMap {
    abstract long getDataPos(int fieldNumber);
    abstract void loadField(PropertyDescriptor field, int fieldIndex, long dataPos);
  }

  private static final class TableFieldMap extends FieldMap {
    private final int min;
    private final long[] positions;

    TableFieldMap(List<PropertyDescriptor> fields) {
      min = fields.get(0).fieldNumber;
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
        positions[i] = UnsafeUtil.arrayBaseOffset(long[].class) + (i * FIELD_LENGTH);
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
