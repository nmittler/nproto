package com.google.apps.tiktok.protobuf.experimental.testing;

import com.google.apps.tiktok.protobuf.experimental.ByteString;
import com.google.apps.tiktok.protobuf.experimental.descriptor.AnnotationMessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.descriptor.FieldDescriptor;
import com.google.apps.tiktok.protobuf.experimental.schema.Reader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * A reader of fields directly from a {@link TestMessage}.
 */
public final class TestMessageReader implements Reader {
  private final FieldValue[] fieldValues;
  private int index;

  public TestMessageReader(TestMessage msg) {
    fieldValues = fieldValuesFor(msg);
  }

  public void reset() {
    index = 0;
  }

  @Override
  public int getFieldNumber() {
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
    return fieldValues[index++].getString();
  }

  @Override
  public Object readMessage() {
    return fieldValues[index++].value;
  }

  @Override
  public ByteString readBytes() {
    return fieldValues[index++].getByteString();
  }

  @Override
  public int readUInt32() {
    return fieldValues[index++].getInt();
  }

  @Override
  public int readEnum() {
    return fieldValues[index++].getInt();
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

  @Override
  public void readDoubleList(List<Double> target, boolean packed) {
    target.addAll(fieldValues[index++].getDoubleList());
  }

  @Override
  public void readFloatList(List<Float> target, boolean packed) {
    target.addAll(fieldValues[index++].getFloatList());
  }

  @Override
  public void readUInt64List(List<Long> target, boolean packed) {
    target.addAll(fieldValues[index++].getLongList());
  }

  @Override
  public void readInt64List(List<Long> target, boolean packed) {
    target.addAll(fieldValues[index++].getLongList());
  }

  @Override
  public void readInt32List(List<Integer> target, boolean packed) {
    target.addAll(fieldValues[index++].getIntegerList());
  }

  @Override
  public void readFixed64List(List<Long> target, boolean packed) {
    target.addAll(fieldValues[index++].getLongList());
  }

  @Override
  public void readFixed32List(List<Integer> target, boolean packed) {
    target.addAll(fieldValues[index++].getIntegerList());
  }

  @Override
  public void readBoolList(List<Boolean> target, boolean packed) {
    target.addAll(fieldValues[index++].getBoolList());
  }

  @Override
  public void readStringList(List<String> target) {
    target.addAll(fieldValues[index++].getStringList());
  }

  @Override
  public <T> void readMessageList(List<T> target, Class<T> targetType) {
    target.addAll(fieldValues[index++].<T>getObjectList());
  }

  @Override
  public void readBytesList(List<ByteString> target) {
    target.addAll(fieldValues[index++].getByteStringList());
  }

  @Override
  public void readUInt32List(List<Integer> target, boolean packed) {
    target.addAll(fieldValues[index++].getIntegerList());
  }

  @Override
  public void readEnumList(List<Integer> target, boolean packed) {
    target.addAll(fieldValues[index++].getIntegerList());
  }

  @Override
  public void readSFixed32List(List<Integer> target, boolean packed) {
    target.addAll(fieldValues[index++].getIntegerList());
  }

  @Override
  public void readSFixed64List(List<Long> target, boolean packed) {
    target.addAll(fieldValues[index++].getLongList());
  }

  @Override
  public void readSInt32List(List<Integer> target, boolean packed) {
    target.addAll(fieldValues[index++].getIntegerList());
  }

  @Override
  public void readSInt64List(List<Long> target, boolean packed) {
    target.addAll(fieldValues[index++].getLongList());
  }

  private static FieldValue[] fieldValuesFor(TestMessage msg) {
    List<FieldValue> fieldValues = new ArrayList<FieldValue>();
    List<FieldDescriptor> protoProperties =
        AnnotationMessageDescriptorFactory.getValidatingInstance()
            .descriptorFor(msg.getClass())
            .getFieldDescriptors();
    for (FieldDescriptor info : protoProperties) {
      final Object value;
      try {
        if (isAccessible(info.getField())) {
          value = info.getField().get(msg);
        } else {
          // Call the appropriate getter.
          if (info.getField().equals(TestMessage.class.getDeclaredField("boolField"))) {
            value = msg.isBoolField();
          } else if (info.getField().equals(TestMessage.class.getDeclaredField("enumField"))) {
            value = msg.getEnumField();
          } else if (info.getField().equals(TestMessage.class.getDeclaredField("boolListField"))) {
            value = msg.getBoolListField();
          } else if (info.getField().equals(TestMessage.class.getDeclaredField("enumListField"))) {
            value = msg.getEnumListField();
          } else if (info.getField()
              .equals(TestMessage.class.getDeclaredField("boolPackedListField"))) {
            value = msg.getBoolPackedListField();
          } else if (info.getField()
              .equals(TestMessage.class.getDeclaredField("enumPackedListField"))) {
            value = msg.getEnumPackedListField();
          } else {
            throw new RuntimeException(
                "Unable to find accessor for field " + info.getField().getName());
          }
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
      addFieldValue(info, value, fieldValues);
    }
    return fieldValues.toArray(new FieldValue[fieldValues.size()]);
  }

  private static boolean isAccessible(Field field) {
    int mod = field.getModifiers();
    return Modifier.isPublic(mod);
  }

  private static void addFieldValue(
      FieldDescriptor info, Object value, List<FieldValue> fieldValues) {
    if (value instanceof Integer && value != Integer.valueOf(0)) {
      fieldValues.add(new FieldValue(info.getFieldNumber(), value));
    } else if (value instanceof Long && value != Long.valueOf(0)) {
      fieldValues.add(new FieldValue(info.getFieldNumber(), value));
    } else if (value instanceof Double && Double.compare(0.0, (Double) value) != 0) {
      fieldValues.add(new FieldValue(info.getFieldNumber(), value));
    } else if (value instanceof Float && Float.compare(0.0f, (Float) value) != 0) {
      fieldValues.add(new FieldValue(info.getFieldNumber(), value));
    } else if (value instanceof String && !((String) value).isEmpty()) {
      fieldValues.add(new FieldValue(info.getFieldNumber(), value));
    } else if (value instanceof ByteString && !((ByteString) value).isEmpty()) {
      fieldValues.add(new FieldValue(info.getFieldNumber(), value));
    } else if (value != null) {
      fieldValues.add(new FieldValue(info.getFieldNumber(), value));
    }
  }

  private static final class FieldValue {
    final int fieldNumber;
    final Object value;

    FieldValue(int fieldNumber, Object value) {
      this.fieldNumber = fieldNumber;
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

    String getString() {
      return (String) value;
    }

    ByteString getByteString() {
      return (ByteString) value;
    }

    @SuppressWarnings("unchecked")
    List<Double> getDoubleList() {
      return (List<Double>) value;
    }

    @SuppressWarnings("unchecked")
    List<Float> getFloatList() {
      return (List<Float>) value;
    }

    @SuppressWarnings("unchecked")
    List<Integer> getIntegerList() {
      return (List<Integer>) value;
    }

    @SuppressWarnings("unchecked")
    List<Long> getLongList() {
      return (List<Long>) value;
    }

    @SuppressWarnings("unchecked")
    List<Boolean> getBoolList() {
      return (List<Boolean>) value;
    }

    @SuppressWarnings("unchecked")
    List<String> getStringList() {
      return (List<String>) value;
    }

    @SuppressWarnings("unchecked")
    List<ByteString> getByteStringList() {
      return (List<ByteString>) value;
    }

    @SuppressWarnings("unchecked")
    <T> List<T> getObjectList() {
      return (List<T>) value;
    }
  }
}
