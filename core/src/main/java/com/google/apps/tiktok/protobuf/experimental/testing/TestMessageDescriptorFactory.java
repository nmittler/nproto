package com.google.apps.tiktok.protobuf.experimental.testing;

import com.google.apps.tiktok.protobuf.experimental.FieldType;
import com.google.apps.tiktok.protobuf.experimental.descriptor.FieldDescriptor;
import com.google.apps.tiktok.protobuf.experimental.descriptor.MessageDescriptor;
import com.google.apps.tiktok.protobuf.experimental.descriptor.MessageDescriptorFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A descriptor factory that generates a hard-coded descriptor for {@link TestMessage}.
 */
public final class TestMessageDescriptorFactory implements MessageDescriptorFactory {
  private static final TestMessageDescriptorFactory INSTANCE = new TestMessageDescriptorFactory();

  private TestMessageDescriptorFactory() {}

  public static TestMessageDescriptorFactory getInstance() {
    return INSTANCE;
  }

  @Override
  public MessageDescriptor descriptorFor(Class<?> clazz) {
    if (!TestMessage.class.isAssignableFrom(clazz)) {
      throw new IllegalArgumentException("Unsupported class: " + clazz.getName());
    }
    return newDescriptor();
  }

  /**
   * Creates a new hard-coded descriptor for {@link TestMessage}. Each time this is called, we
   * manually go through the entire process of what a message would do if it self-registered its
   * own descriptor, including looking up each field by name. This is done for benchmarking
   * purposes, so that we get a more accurate representation of the time it takes to perform this
   * process.
   */
  private static MessageDescriptor newDescriptor() {
    try {
      Field enumField = TestMessage.class.getDeclaredField("enumField");
      Field boolField = TestMessage.class.getDeclaredField("boolField");
      Field uint32Field = TestMessage.class.getDeclaredField("uint32Field");
      Field int32Field = TestMessage.class.getDeclaredField("int32Field");
      Field sint32Field = TestMessage.class.getDeclaredField("sint32Field");
      Field fixed32Field = TestMessage.class.getDeclaredField("fixed32Field");
      Field sfixed32Field = TestMessage.class.getDeclaredField("sfixed32Field");
      Field uint64Field = TestMessage.class.getDeclaredField("uint64Field");
      Field int64Field = TestMessage.class.getDeclaredField("int64Field");
      Field sint64Field = TestMessage.class.getDeclaredField("sint64Field");
      Field fixed64Field = TestMessage.class.getDeclaredField("fixed64Field");
      Field sfixed64Field = TestMessage.class.getDeclaredField("sfixed64Field");
      Field stringField = TestMessage.class.getDeclaredField("stringField");
      Field bytesField = TestMessage.class.getDeclaredField("bytesField");
      Field messageField = TestMessage.class.getDeclaredField("messageField");
      Field enumListField = TestMessage.class.getDeclaredField("enumListField");
      Field boolListField = TestMessage.class.getDeclaredField("boolListField");
      Field uint32ListField = TestMessage.class.getDeclaredField("uint32ListField");
      Field int32ListField = TestMessage.class.getDeclaredField("int32ListField");
      Field sint32ListField = TestMessage.class.getDeclaredField("sint32ListField");
      Field fixed32ListField = TestMessage.class.getDeclaredField("fixed32ListField");
      Field sfixed32ListField = TestMessage.class.getDeclaredField("sfixed32ListField");
      Field uint64ListField = TestMessage.class.getDeclaredField("uint64ListField");
      Field int64ListField = TestMessage.class.getDeclaredField("int64ListField");
      Field sint64ListField = TestMessage.class.getDeclaredField("sint64ListField");
      Field fixed64ListField = TestMessage.class.getDeclaredField("fixed64ListField");
      Field sfixed64ListField = TestMessage.class.getDeclaredField("sfixed64ListField");
      Field stringListField = TestMessage.class.getDeclaredField("stringListField");
      Field bytesListField = TestMessage.class.getDeclaredField("bytesListField");
      Field messageListField = TestMessage.class.getDeclaredField("messageListField");

      List<FieldDescriptor> properties = new ArrayList<FieldDescriptor>(30);
      properties.add(new FieldDescriptor(enumField, 1, FieldType.ENUM));
      properties.add(new FieldDescriptor(boolField, 2, FieldType.BOOL));
      properties.add(new FieldDescriptor(uint32Field, 3, FieldType.UINT32));
      properties.add(new FieldDescriptor(int32Field, 4, FieldType.INT32));
      properties.add(new FieldDescriptor(sint32Field, 5, FieldType.SINT32));
      properties.add(new FieldDescriptor(fixed32Field, 6, FieldType.FIXED32));
      properties.add(new FieldDescriptor(sfixed32Field, 7, FieldType.SFIXED32));
      properties.add(new FieldDescriptor(uint64Field, 8, FieldType.UINT64));
      properties.add(new FieldDescriptor(int64Field, 9, FieldType.INT64));
      properties.add(new FieldDescriptor(sint64Field, 10, FieldType.SINT64));
      properties.add(new FieldDescriptor(fixed64Field, 11, FieldType.FIXED64));
      properties.add(new FieldDescriptor(sfixed64Field, 12, FieldType.SFIXED64));
      properties.add(new FieldDescriptor(stringField, 13, FieldType.STRING));
      properties.add(new FieldDescriptor(bytesField, 14, FieldType.BYTES));
      properties.add(new FieldDescriptor(messageField, 15, FieldType.MESSAGE));
      properties.add(new FieldDescriptor(enumListField, 16, FieldType.ENUM_LIST_PACKED));
      properties.add(new FieldDescriptor(boolListField, 17, FieldType.BOOL_LIST_PACKED));
      properties.add(new FieldDescriptor(uint32ListField, 18, FieldType.UINT32_LIST_PACKED));
      properties.add(new FieldDescriptor(int32ListField, 19, FieldType.INT32_LIST_PACKED));
      properties.add(new FieldDescriptor(sint32ListField, 20, FieldType.SINT32_LIST_PACKED));
      properties.add(new FieldDescriptor(fixed32ListField, 21, FieldType.FIXED32_LIST_PACKED));
      properties.add(new FieldDescriptor(sfixed32ListField, 22, FieldType.SFIXED32_LIST_PACKED));
      properties.add(new FieldDescriptor(uint64ListField, 23, FieldType.UINT64_LIST_PACKED));
      properties.add(new FieldDescriptor(int64ListField, 24, FieldType.INT64_LIST_PACKED));
      properties.add(new FieldDescriptor(sint64ListField, 25, FieldType.SINT64_LIST_PACKED));
      properties.add(new FieldDescriptor(fixed64ListField, 26, FieldType.FIXED64_LIST_PACKED));
      properties.add(new FieldDescriptor(sfixed64ListField, 27, FieldType.SFIXED64_LIST_PACKED));
      properties.add(new FieldDescriptor(stringListField, 28, FieldType.STRING_LIST));
      properties.add(new FieldDescriptor(bytesListField, 29, FieldType.BYTES_LIST));
      properties.add(new FieldDescriptor(messageListField, 30, FieldType.MESSAGE_LIST));
      return new MessageDescriptor(properties);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
}
