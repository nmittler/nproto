package com.google.apps.tiktok.protobuf.experimental.schema;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.google.apps.tiktok.protobuf.experimental.testing.TestMessage;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessageFactory;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessageReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public abstract class AbstractSchemaFactoryTest {
  private TestMessage msg;
  private Schema<TestMessage> schema;

  @Mock private Writer writer;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    msg = TestMessageFactory.newTestMessage();
    schema = schema();
  }

  protected abstract Schema<TestMessage> schema();

  @Test
  public void defaultsAreNotWritten() {
    schema.writeTo(new TestMessage(), writer);
    verifyNoMoreInteractions(writer);
  }

  @Test
  public void writeToShouldWriteAllFieldsInOrder() {
    schema.writeTo(msg, writer);
    InOrder inorder = inOrder(writer);
    inorder.verify(writer).writeDouble(eq(1), eq(msg.doubleField));
    inorder.verify(writer).writeFloat(eq(2), eq(msg.floatField));
    inorder.verify(writer).writeInt64(eq(3), eq(msg.int64Field));
    inorder.verify(writer).writeUInt64(eq(4), eq(msg.uint64Field));
    inorder.verify(writer).writeInt32(eq(5), eq(msg.int32Field));
    inorder.verify(writer).writeFixed64(eq(6), eq(msg.fixed64Field));
    inorder.verify(writer).writeFixed32(eq(7), eq(msg.fixed32Field));
    inorder.verify(writer).writeBool(eq(8), eq(msg.isBoolField()));
    inorder.verify(writer).writeString(eq(9), eq(msg.stringField));
    inorder.verify(writer).writeMessage(eq(10), eq(msg.messageField));
    inorder.verify(writer).writeBytes(eq(11), eq(msg.bytesField));
    inorder.verify(writer).writeUInt32(eq(12), eq(msg.uint32Field));
    inorder.verify(writer).writeEnum(eq(13), eq(msg.getEnumField()));
    inorder.verify(writer).writeSFixed32(eq(14), eq(msg.sfixed32Field));
    inorder.verify(writer).writeSFixed64(eq(15), eq(msg.sfixed64Field));
    inorder.verify(writer).writeSInt32(eq(16), eq(msg.sint32Field));
    inorder.verify(writer).writeSInt64(eq(17), eq(msg.sint64Field));

    inorder.verify(writer).writeDoubleList(eq(18), same(msg.doubleListField), eq(false));
    inorder.verify(writer).writeFloatList(eq(19), same(msg.floatListField), eq(false));
    inorder.verify(writer).writeInt64List(eq(20), same(msg.int64ListField), eq(false));
    inorder.verify(writer).writeUInt64List(eq(21), same(msg.uint64ListField), eq(false));
    inorder.verify(writer).writeInt32List(eq(22), same(msg.int32ListField), eq(false));
    inorder.verify(writer).writeFixed64List(eq(23), same(msg.fixed64ListField), eq(false));
    inorder.verify(writer).writeFixed32List(eq(24), same(msg.fixed32ListField), eq(false));
    inorder.verify(writer).writeBoolList(eq(25), same(msg.getBoolListField()), eq(false));
    inorder.verify(writer).writeStringList(eq(26), same(msg.stringListField));
    inorder.verify(writer).writeMessageList(eq(27), same(msg.messageListField));
    inorder.verify(writer).writeBytesList(eq(28), same(msg.bytesListField));
    inorder.verify(writer).writeUInt32List(eq(29), same(msg.uint32ListField), eq(false));
    inorder.verify(writer).writeEnumList(eq(30), same(msg.getEnumListField()), eq(false));
    inorder.verify(writer).writeSFixed32List(eq(31), same(msg.sfixed32ListField), eq(false));
    inorder.verify(writer).writeSFixed64List(eq(32), same(msg.sfixed64ListField), eq(false));
    inorder.verify(writer).writeSInt32List(eq(33), same(msg.sint32ListField), eq(false));
    inorder.verify(writer).writeSInt64List(eq(34), same(msg.sint64ListField), eq(false));

    inorder.verify(writer).writeDoubleList(eq(35), same(msg.doublePackedListField), eq(true));
    inorder.verify(writer).writeFloatList(eq(36), same(msg.floatPackedListField), eq(true));
    inorder.verify(writer).writeInt64List(eq(37), same(msg.int64PackedListField), eq(true));
    inorder.verify(writer).writeUInt64List(eq(38), same(msg.uint64PackedListField), eq(true));
    inorder.verify(writer).writeInt32List(eq(39), same(msg.int32PackedListField), eq(true));
    inorder.verify(writer).writeFixed64List(eq(40), same(msg.fixed64PackedListField), eq(true));
    inorder.verify(writer).writeFixed32List(eq(41), same(msg.fixed32PackedListField), eq(true));
    inorder.verify(writer).writeBoolList(eq(42), same(msg.getBoolPackedListField()), eq(true));
    inorder.verify(writer).writeUInt32List(eq(43), same(msg.uint32PackedListField), eq(true));
    inorder.verify(writer).writeEnumList(eq(44), same(msg.getEnumPackedListField()), eq(true));
    inorder.verify(writer).writeSFixed32List(eq(45), same(msg.sfixed32PackedListField), eq(true));
    inorder.verify(writer).writeSFixed64List(eq(46), same(msg.sfixed64PackedListField), eq(true));
    inorder.verify(writer).writeSInt32List(eq(47), same(msg.sint32PackedListField), eq(true));
    inorder.verify(writer).writeSInt64List(eq(48), same(msg.sint64PackedListField), eq(true));
    inorder.verifyNoMoreInteractions();
  }

  @Test
  public void mergeFromShouldReadAllFields() {
    Reader reader = new TestMessageReader(msg);

    TestMessage newMsg = new TestMessage();
    schema.mergeFrom(newMsg, reader);
    assertEquals(msg, newMsg);
  }
}
