package io.nproto.schema;

import io.nproto.ByteString;
import io.nproto.JavaType;
import io.nproto.PojoMessage;
import io.nproto.Reader;
import io.nproto.schema.SchemaUtil.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TestUtil {
  private TestUtil() {
  }

  public static PojoMessage newTestMessage() {
    PojoMessage msg = new PojoMessage();
    msg.uint32Field = 1;
    msg.int32Field = 2;
    msg.fixedInt32Field = 3;
    msg.sInt32Field = 4;
    msg.sFixedInt32Field = 5;

    msg.uint64Field = 5;
    msg.int64Field = 6;
    msg.fixedInt64Field = 7;
    msg.sInt64Field = 8;
    msg.sFixedInt64Field = 9;

    msg.stringField = "hello world";
    msg.bytesField = ByteString.copyFromUtf8("here are some bytes");
    msg.messageField = new Object();

    msg.uint32ListField = Arrays.asList(1, 2);
    msg.int32ListField = Arrays.asList(3, 4);
    msg.fixedInt32ListField = Arrays.asList(5, 6);
    msg.sInt32ListField = Arrays.asList(7, 8);
    msg.sFixedInt32ListField = Arrays.asList(9, 10);

    msg.uint64ListField = Arrays.asList(1L, 2L);
    msg.int64ListField = Arrays.asList(3L, 4L);
    msg.fixedInt64ListField = Arrays.asList(5L, 6L);
    msg.sInt64ListField = Arrays.asList(7L, 8L);
    msg.sFixedInt64ListField = Arrays.asList(9L, 10L);

    msg.stringListField = Arrays.asList("ab", "cd");
    msg.bytesListField = Arrays.asList(ByteString.copyFromUtf8("ab"), ByteString.copyFromUtf8("cd"));
    msg.messageListField = Arrays.asList(new Object(), new Object());
    return msg;
  }

  public static final class PojoReader implements Reader {
    private final FieldValue[] fieldValues;
    private int index;

    public PojoReader(PojoMessage msg) {
      fieldValues = fieldValuesFor(msg);
    }

    public void reset() {
      index = 0;
    }

    @Override
    public int fieldNumber() {
      if (index >= fieldValues.length) {
        return READ_DONE;
      }
      return fieldValues[index].fieldNumber;
    }

    @Override
    public boolean skipField() {
      return ++index < fieldValues.length;
    }

    @Override
    public double readDouble() {
      return fieldValues[index++].getDouble();
    }

    @Override
    public float readFloat() {
      return fieldValues[index++].getFloat();
    }

    @Override
    public long readUInt64() {
      return fieldValues[index++].getLong();
    }

    @Override
    public long readInt64() {
      return fieldValues[index++].getLong();
    }

    @Override
    public int readInt32() {
      return fieldValues[index++].getInt();
    }

    @Override
    public long readFixed64() {
      return fieldValues[index++].getLong();
    }

    @Override
    public int readFixed32() {
      return fieldValues[index++].getInt();
    }

    @Override
    public boolean readBool() {
      return fieldValues[index++].getBool();
    }

    @Override
    public String readString() {
      return (String) fieldValues[index++].value;
    }

    @Override
    public Object readMessage() {
      return fieldValues[index++].value;
    }

    @Override
    public ByteString readBytes() {
      return (ByteString) fieldValues[index++].value;
    }

    @Override
    public int readUInt32() {
      return fieldValues[index++].getInt();
    }

    @Override
    public Enum<?> readEnum() {
      return (Enum<?>) fieldValues[index++].value;
    }

    @Override
    public int readSFixed32() {
      return fieldValues[index++].getInt();
    }

    @Override
    public long readSFixed64() {
      return fieldValues[index++].getLong();
    }

    @Override
    public int readSInt32() {
      return fieldValues[index++].getInt();
    }

    @Override
    public long readSInt64() {
      return fieldValues[index++].getLong();
    }
  }

  private static FieldValue[] fieldValuesFor(Object msg) {
    List<FieldValue> fieldValues = new ArrayList<FieldValue>();
    List<FieldInfo> fieldInfos = SchemaUtil.getAllFieldInfo(msg.getClass());
    for (FieldInfo info : fieldInfos) {
      Object value;
      try {
        value = info.field.get(msg);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
      if (value instanceof List) {
        List<?> entries = (List<?>) value;
        for (Object entry : entries) {
          addFieldValue(info, entry, fieldValues);
        }
      } else if (value != null){
        addFieldValue(info, value, fieldValues);
      }
    }
    return fieldValues.toArray(new FieldValue[fieldValues.size()]);
  }

  private static void addFieldValue(FieldInfo info, Object value, List<FieldValue> fieldValues) {
    if (value instanceof Integer && value != Integer.valueOf(0)) {
      fieldValues.add(new FieldValue(info.fieldNumber, JavaType.INT, value));
    } else if (value instanceof Long && value != Long.valueOf(0)) {
      fieldValues.add(new FieldValue(info.fieldNumber, JavaType.LONG, value));
    } else if (value instanceof Double && Double.compare(0.0, (Double) value) != 0) {
      fieldValues.add(new FieldValue(info.fieldNumber, JavaType.DOUBLE, value));
    } else if (value instanceof Float && Float.compare(0.0f, (Float) value) != 0) {
      fieldValues.add(new FieldValue(info.fieldNumber, JavaType.FLOAT, value));
    } else if (value instanceof String && !((String) value).isEmpty()) {
      fieldValues.add(new FieldValue(info.fieldNumber, JavaType.STRING, value));
    } else if (value instanceof ByteString && !((ByteString) value).isEmpty()) {
      fieldValues.add(new FieldValue(info.fieldNumber, JavaType.BYTE_STRING, value));
    } else if (value != null) {
      fieldValues.add(new FieldValue(info.fieldNumber, JavaType.MESSAGE, value));
    }
  }

  private static final class FieldValue {
    final int fieldNumber;
    final JavaType javaType;
    final Object value;

    FieldValue(int fieldNumber, JavaType javaType, Object value) {
      this.fieldNumber = fieldNumber;
      this.javaType = javaType;
      this.value = value;
    }

    double getDouble() {
      return (Double) value;
    }

    float getFloat() {
      return (Float) value;
    }

    int getInt() {
      return (Integer) value;
    }

    long getLong() {
      return (Long) value;
    }

    boolean getBool() {
      return (Boolean) value;
    }
  }
}
