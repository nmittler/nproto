package com.google.protobuf.experimental.util;

import com.google.protobuf.experimental.JavaType;
import com.google.protobuf.experimental.example.PojoMessage;
import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.BeanDescriptor;
import com.google.protobuf.experimental.descriptor.BeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.PropertyDescriptor;
import com.google.protobuf.experimental.ByteString;
import com.google.protobuf.experimental.Internal;
import com.google.protobuf.experimental.Reader;
import com.google.protobuf.experimental.WireFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Internal
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

  public static final class PojoDescriptorFactory implements BeanDescriptorFactory {
    private static final PojoDescriptorFactory INSTANCE = new PojoDescriptorFactory();
    //private static final BeanDescriptor DESCRIPTOR = newDescriptor();

    private PojoDescriptorFactory() {
    }

    public static PojoDescriptorFactory getInstance() {
      return INSTANCE;
    }

    private static Field pojoField(String name) {
      try {
        return PojoMessage.class.getDeclaredField(name);
      } catch (NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
    }

    private static BeanDescriptor newDescriptor() {
      final Field ENUM_FIELD = pojoField("enumField");
      final Field BOOL_FIELD = pojoField("boolField");
      final Field UINT32_FIELD = pojoField("uint32Field");
      final Field INT32_FIELD = pojoField("int32Field");
      final Field SINT32_FIELD = pojoField("sInt32Field");
      final Field FIXED32_FIELD = pojoField("fixedInt32Field");
      final Field SFIXED32_FIELD = pojoField("sFixedInt32Field");
      final Field UINT64_FIELD = pojoField("uint64Field");
      final Field INT64_FIELD = pojoField("int64Field");
      final Field SINT64_FIELD = pojoField("sInt64Field");
      final Field FIXED64_FIELD = pojoField("fixedInt64Field");
      final Field SFIXED64_FIELD = pojoField("sFixedInt64Field");
      final Field STRING_FIELD = pojoField("stringField");
      final Field BYTES_FIELD = pojoField("bytesField");
      final Field MESSAGE_FIELD = pojoField("messageField");
      final Field ENUM_LIST_FIELD = pojoField("enumListField");
      final Field BOOL_LIST_FIELD = pojoField("boolListField");
      final Field UINT32_LIST_FIELD = pojoField("uint32ListField");
      final Field INT32_LIST_FIELD = pojoField("int32ListField");
      final Field SINT32_LIST_FIELD = pojoField("sInt32ListField");
      final Field FIXED32_LIST_FIELD = pojoField("fixedInt32ListField");
      final Field SFIXED32_LIST_FIELD = pojoField("sFixedInt32ListField");
      final Field UINT64_LIST_FIELD = pojoField("uint64ListField");
      final Field INT64_LIST_FIELD = pojoField("int64ListField");
      final Field SINT64_LIST_FIELD = pojoField("sInt64ListField");
      final Field FIXED64_LIST_FIELD = pojoField("fixedInt64ListField");
      final Field SFIXED64_LIST_FIELD = pojoField("sFixedInt64ListField");
      final Field STRING_LIST_FIELD = pojoField("stringListField");
      final Field BYTES_LIST_FIELD = pojoField("bytesListField");
      final Field MESSAGE_LIST_FIELD = pojoField("messageListField");

      List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>(30);
      properties.add(new PropertyDescriptor(
              ENUM_FIELD, 1, WireFormat.FieldType.ENUM));
      properties.add(new PropertyDescriptor(
              BOOL_FIELD, 2, WireFormat.FieldType.BOOL));
      properties.add(new PropertyDescriptor(
              UINT32_FIELD, 3, WireFormat.FieldType.UINT32));
      properties.add(new PropertyDescriptor(
              INT32_FIELD, 4, WireFormat.FieldType.INT32));
      properties.add(new PropertyDescriptor(
              SINT32_FIELD, 5, WireFormat.FieldType.SINT32));
      properties.add(new PropertyDescriptor(
              FIXED32_FIELD, 6, WireFormat.FieldType.FIXED32));
      properties.add(new PropertyDescriptor(
              SFIXED32_FIELD, 7, WireFormat.FieldType.SFIXED32));
      properties.add(new PropertyDescriptor(
              UINT64_FIELD, 8, WireFormat.FieldType.UINT64));
      properties.add(new PropertyDescriptor(
              INT64_FIELD, 9, WireFormat.FieldType.INT64));
      properties.add(new PropertyDescriptor(
              SINT64_FIELD, 10, WireFormat.FieldType.SINT64));
      properties.add(new PropertyDescriptor(
              FIXED64_FIELD, 11, WireFormat.FieldType.FIXED64));
      properties.add(new PropertyDescriptor(
              SFIXED64_FIELD, 12, WireFormat.FieldType.SFIXED64));
      properties.add(new PropertyDescriptor(
              STRING_FIELD, 13, WireFormat.FieldType.STRING));
      properties.add(new PropertyDescriptor(
              BYTES_FIELD, 14, WireFormat.FieldType.BYTES));
      properties.add(new PropertyDescriptor(
              MESSAGE_FIELD, 15, WireFormat.FieldType.MESSAGE));
      properties.add(new PropertyDescriptor(
              ENUM_LIST_FIELD, 16, WireFormat.FieldType.ENUM));
      properties.add(new PropertyDescriptor(
              BOOL_LIST_FIELD, 17, WireFormat.FieldType.BOOL));
      properties.add(new PropertyDescriptor(
              UINT32_LIST_FIELD, 18, WireFormat.FieldType.UINT32));
      properties.add(new PropertyDescriptor(
              INT32_LIST_FIELD, 19, WireFormat.FieldType.INT32));
      properties.add(new PropertyDescriptor(
              SINT32_LIST_FIELD, 20, WireFormat.FieldType.SINT32));
      properties.add(new PropertyDescriptor(
              FIXED32_LIST_FIELD, 21, WireFormat.FieldType.FIXED32));
      properties.add(new PropertyDescriptor(
              SFIXED32_LIST_FIELD, 22, WireFormat.FieldType.SFIXED32));
      properties.add(new PropertyDescriptor(
              UINT64_LIST_FIELD, 23, WireFormat.FieldType.UINT64));
      properties.add(new PropertyDescriptor(
              INT64_LIST_FIELD, 24, WireFormat.FieldType.INT64));
      properties.add(new PropertyDescriptor(
              SINT64_LIST_FIELD, 25, WireFormat.FieldType.SINT64));
      properties.add(new PropertyDescriptor(
              FIXED64_LIST_FIELD, 26, WireFormat.FieldType.FIXED64));
      properties.add(new PropertyDescriptor(
              SFIXED64_LIST_FIELD, 27, WireFormat.FieldType.SFIXED64));
      properties.add(new PropertyDescriptor(
              STRING_LIST_FIELD, 28, WireFormat.FieldType.STRING));
      properties.add(new PropertyDescriptor(
              BYTES_LIST_FIELD, 29, WireFormat.FieldType.BYTES));
      properties.add(new PropertyDescriptor(
              MESSAGE_LIST_FIELD, 30, WireFormat.FieldType.MESSAGE));
      return new BeanDescriptor(properties);
    }

    @Override
    public BeanDescriptor descriptorFor(Class<?> clazz) {
      if (!PojoMessage.class.isAssignableFrom(clazz)) {
        throw new IllegalArgumentException("Unsupported class: " + clazz.getName());
      }
      return newDescriptor();
    }
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
    List<PropertyDescriptor> protoProperties =
            AnnotationBeanDescriptorFactory.getInstance().descriptorFor(msg.getClass())
                    .getPropertyDescriptors();
    for (PropertyDescriptor info : protoProperties) {
      final Object value;
      try {
        if (isAccessible(info.field)) {
          value = info.field.get(msg);
        } else {
          // Call the getter.
          Method accessor = accessorFor(info.field);
          value = accessor.invoke(msg);
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      }
      if (value instanceof List) {
        List<?> entries = (List<?>) value;
        for (Object entry : entries) {
          addFieldValue(info, entry, fieldValues);
        }
      } else if (value != null) {
        addFieldValue(info, value, fieldValues);
      }
    }
    return fieldValues.toArray(new FieldValue[fieldValues.size()]);
  }

  private static boolean isAccessible(Field field) {
    int mod = field.getModifiers();
    return Modifier.isPublic(mod);
  }

  private static Method accessorFor(Field field) {
    String name = field.getName();
    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
    if (field.getType() == boolean.class) {
      name = "is" + name;
    } else {
      name = "get" + name;
    }
    try {
      return field.getDeclaringClass().getMethod(name);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static void addFieldValue(PropertyDescriptor info, Object value, List<FieldValue> fieldValues) {
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
