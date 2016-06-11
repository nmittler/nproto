package com.google.apps.tiktok.protobuf.experimental.testing;

import com.google.apps.tiktok.protobuf.experimental.ByteString;

import java.util.Arrays;

/**
 * A utility for generating populated {@link TestMessage} instances.
 */
public final class TestMessageFactory {
  private TestMessageFactory() {}

  /**
   * Creates a new message with all fields set.
   */
  public static TestMessage newTestMessage() {
    TestMessage msg = new TestMessage();

    // Unary fields.
    msg.doubleField = 1.0;
    msg.floatField = 2.0f;
    msg.enumField = 3;
    msg.setBoolField(true);
    msg.uint32Field = 4;
    msg.int32Field = 5;
    msg.fixed32Field = 6;
    msg.sint32Field = 7;
    msg.sfixed32Field = 8;
    msg.uint64Field = 9;
    msg.int64Field = 10;
    msg.fixed64Field = 11;
    msg.sint64Field = 12;
    msg.sfixed64Field = 13;
    msg.stringField = "hello world";
    msg.bytesField = ByteString.copyFromUtf8("here are some bytes");
    msg.messageField = new TestMessage.InnerMessage(true);

    // Non-packed lists.
    msg.doubleListField = Arrays.asList(14.0, 15.0);
    msg.floatListField = Arrays.asList(16.0f, 17.0f);
    msg.enumListField = Arrays.asList(18, 19);
    msg.setBoolListField(Arrays.asList(true, false));
    msg.uint32ListField = Arrays.asList(20, 21);
    msg.int32ListField = Arrays.asList(22, 23);
    msg.fixed32ListField = Arrays.asList(24, 25);
    msg.sint32ListField = Arrays.asList(26, 27);
    msg.sfixed32ListField = Arrays.asList(28, 29);
    msg.uint64ListField = Arrays.asList(30L, 31L);
    msg.int64ListField = Arrays.asList(32L, 33L);
    msg.fixed64ListField = Arrays.asList(34L, 35L);
    msg.sint64ListField = Arrays.asList(36L, 37L);
    msg.sfixed64ListField = Arrays.asList(38L, 39L);
    msg.stringListField = Arrays.asList("ab", "cd");
    msg.bytesListField =
        Arrays.asList(ByteString.copyFromUtf8("ef"), ByteString.copyFromUtf8("gh"));
    msg.messageListField =
        Arrays.asList(new TestMessage.InnerMessage(true), new TestMessage.InnerMessage(false));

    // Packed lists.
    msg.doublePackedListField = Arrays.asList(40.0, 41.0);
    msg.floatPackedListField = Arrays.asList(42.0f, 43.0f);
    msg.enumPackedListField = Arrays.asList(44, 45);
    msg.setBoolPackedListField(Arrays.asList(true, false));
    msg.uint32PackedListField = Arrays.asList(46, 47);
    msg.int32PackedListField = Arrays.asList(48, 49);
    msg.fixed32PackedListField = Arrays.asList(50, 51);
    msg.sint32PackedListField = Arrays.asList(52, 53);
    msg.sfixed32PackedListField = Arrays.asList(54, 55);
    msg.uint64PackedListField = Arrays.asList(56L, 57L);
    msg.int64PackedListField = Arrays.asList(58L, 59L);
    msg.fixed64PackedListField = Arrays.asList(60L, 61L);
    msg.sint64PackedListField = Arrays.asList(62L, 63L);
    msg.sfixed64PackedListField = Arrays.asList(64L, 65L);
    return msg;
  }
}
