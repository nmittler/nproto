package com.google.apps.tiktok.protobuf.experimental.testing;

import com.google.apps.tiktok.protobuf.experimental.FieldType;
import com.google.apps.tiktok.protobuf.experimental.descriptor.FieldDescriptor;
import com.google.apps.tiktok.protobuf.experimental.descriptor.MessageDescriptor;
import com.google.apps.tiktok.protobuf.experimental.descriptor.MessageDescriptorFactory;

/**
 * A descriptor factory that generates a hard-coded descriptor for {@link TestMessage}.
 */
public final class TestMessageDescriptorFactory implements MessageDescriptorFactory {
  private static final TestMessageDescriptorFactory INSTANCE = new TestMessageDescriptorFactory();

  private TestMessageDescriptorFactory() {
  }

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
   * manually go through the entire process of what a message would do if it self-registered its own
   * descriptor, including looking up each field by name. This is done for benchmarking purposes, so
   * that we get a more accurate representation of the time it takes to perform this process.
   */
  private static MessageDescriptor newDescriptor() {
    MessageDescriptor.Builder builder = MessageDescriptor.newBuilder(48);
    lookupFieldsByName(builder);
    return builder.build();
  }

  private static void lookupFieldsByName(MessageDescriptor.Builder builder) {
    try {
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("doubleField"), 1, FieldType.DOUBLE));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("floatField"), 2, FieldType.FLOAT));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("int64Field"), 3, FieldType.INT64));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("uint64Field"), 4, FieldType.UINT64));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("int32Field"), 5, FieldType.INT32));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("fixed64Field"), 6, FieldType.FIXED64));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("fixed32Field"), 7, FieldType.FIXED32));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("boolField"), 8, FieldType.BOOL));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("stringField"), 9, FieldType.STRING));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("messageField"), 10, FieldType.MESSAGE));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("bytesField"), 11, FieldType.BYTES));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("uint32Field"), 12, FieldType.UINT32));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("enumField"), 13, FieldType.ENUM));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sfixed32Field"), 14, FieldType.SFIXED32));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sfixed64Field"), 15, FieldType.SFIXED64));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sint32Field"), 16, FieldType.SINT32));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sint64Field"), 17, FieldType.SINT64));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("doubleListField"), 18, FieldType.DOUBLE_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("floatListField"), 19, FieldType.FLOAT_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("int64ListField"), 20, FieldType.INT64_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("uint64ListField"), 21, FieldType.UINT64_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("int32ListField"), 22, FieldType.INT32_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("fixed64ListField"), 23, FieldType.FIXED64_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("fixed32ListField"), 24, FieldType.FIXED32_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("boolListField"), 25, FieldType.BOOL_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("stringListField"), 26, FieldType.STRING_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("messageListField"), 27, FieldType.MESSAGE_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("bytesListField"), 28, FieldType.BYTES_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("uint32ListField"), 29, FieldType.UINT32_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("enumListField"), 30, FieldType.ENUM_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sfixed32ListField"), 31, FieldType.SFIXED32_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sfixed64ListField"), 32, FieldType.SFIXED64_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sint32ListField"), 33, FieldType.SINT32_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sint64ListField"), 34, FieldType.SINT64_LIST));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("doublePackedListField"), 35, FieldType.DOUBLE_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("floatPackedListField"), 36, FieldType.FLOAT_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("int64PackedListField"), 37, FieldType.INT64_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("uint64PackedListField"), 38, FieldType.UINT64_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("int32PackedListField"), 39, FieldType.INT32_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("fixed64PackedListField"), 40, FieldType.FIXED64_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("fixed32PackedListField"), 41, FieldType.FIXED32_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("boolPackedListField"), 42, FieldType.BOOL_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("uint32PackedListField"), 43, FieldType.UINT32_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("enumPackedListField"), 44, FieldType.ENUM_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sfixed32PackedListField"), 45, FieldType.SFIXED32_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sfixed64PackedListField"), 46, FieldType.SFIXED64_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sint32PackedListField"), 47, FieldType.SINT32_LIST_PACKED));
      builder.add(new FieldDescriptor(TestMessage.class.getDeclaredField("sint64PackedListField"), 48, FieldType.SINT64_LIST_PACKED));
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
}
