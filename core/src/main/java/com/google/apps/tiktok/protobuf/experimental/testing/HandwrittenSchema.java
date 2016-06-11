package com.google.apps.tiktok.protobuf.experimental.testing;

import com.google.apps.tiktok.protobuf.experimental.ByteString;
import com.google.apps.tiktok.protobuf.experimental.schema.Reader;
import com.google.apps.tiktok.protobuf.experimental.schema.Schema;
import com.google.apps.tiktok.protobuf.experimental.schema.Writer;

import java.util.ArrayList;

/**
 * A hand-written schema for a {@link TestMessage}. This is used to compare performance against
 * dynamically-generated schemas.
 */
final class HandwrittenSchema implements Schema<TestMessage> {
  @Override
  public void writeTo(TestMessage message, Writer writer) {
    if (message.doubleField != 0.0) {
      writer.writeDouble(1, message.doubleField);
    }
    if (message.floatField != 0.0f) {
      writer.writeFloat(2, message.floatField);
    }
    if (message.int64Field != 0L) {
      writer.writeInt64(3, message.int64Field);
    }
    if (message.uint64Field != 0L) {
      writer.writeUInt64(4, message.uint64Field);
    }
    if (message.int32Field != 0) {
      writer.writeInt32(5, message.int32Field);
    }
    if (message.fixed64Field != 0L) {
      writer.writeFixed64(6, message.fixed64Field);
    }
    if (message.fixed32Field != 0) {
      writer.writeFixed32(7, message.fixed32Field);
    }
    if (message.isBoolField()) {
      writer.writeBool(8, true);
    }
    if (message.stringField != null) {
      writer.writeString(9, message.stringField);
    }
    if (message.messageField != null) {
      writer.writeMessage(10, message.messageField);
    }
    if (message.bytesField != null) {
      writer.writeBytes(11, message.bytesField);
    }
    if (message.uint32Field != 0) {
      writer.writeUInt32(12, message.uint32Field);
    }
    if (message.enumField != 0) {
      writer.writeEnum(13, message.enumField);
    }
    if (message.sfixed32Field != 0) {
      writer.writeSFixed32(14, message.sfixed32Field);
    }
    if (message.sfixed64Field != 0L) {
      writer.writeSFixed64(15, message.sfixed64Field);
    }
    if (message.sint32Field != 0) {
      writer.writeSInt32(16, message.sint32Field);
    }
    if (message.sint64Field != 0L) {
      writer.writeSInt64(17, message.sint64Field);
    }
    if (message.doubleListField != null) {
      writer.writeDoubleList(18, message.doubleListField, false);
    }
    if (message.floatListField != null) {
      writer.writeFloatList(19, message.floatListField, false);
    }
    if (message.int64ListField != null) {
      writer.writeInt64List(20, message.int64ListField, false);
    }
    if (message.uint64ListField != null) {
      writer.writeUInt64List(21, message.uint64ListField, false);
    }
    if (message.int32ListField != null) {
      writer.writeInt32List(22, message.int32ListField, false);
    }
    if (message.fixed64ListField != null) {
      writer.writeFixed64List(23, message.fixed64ListField, false);
    }
    if (message.fixed32ListField != null) {
      writer.writeFixed32List(24, message.fixed32ListField, false);
    }
    if (message.getBoolListField() != null) {
      writer.writeBoolList(25, message.getBoolListField(), false);
    }
    if (message.stringListField != null) {
      writer.writeStringList(26, message.stringListField);
    }
    if (message.messageListField != null) {
      writer.writeMessageList(27, message.messageListField);
    }
    if (message.bytesListField != null) {
      writer.writeBytesList(28, message.bytesListField);
    }
    if (message.uint32ListField != null) {
      writer.writeUInt32List(29, message.uint32ListField, false);
    }
    if (message.enumListField != null) {
      writer.writeEnumList(30, message.enumListField, false);
    }
    if (message.sfixed32ListField != null) {
      writer.writeSFixed32List(31, message.sfixed32ListField, false);
    }
    if (message.sfixed64ListField != null) {
      writer.writeSFixed64List(32, message.sfixed64ListField, false);
    }
    if (message.sint32ListField != null) {
      writer.writeSInt32List(33, message.sint32ListField, false);
    }
    if (message.sint64ListField != null) {
      writer.writeSInt64List(34, message.sint64ListField, false);
    }
    if (message.doublePackedListField != null) {
      writer.writeDoubleList(35, message.doublePackedListField, true);
    }
    if (message.floatPackedListField != null) {
      writer.writeFloatList(36, message.floatPackedListField, true);
    }
    if (message.int64PackedListField != null) {
      writer.writeInt64List(37, message.int64PackedListField, true);
    }
    if (message.uint64PackedListField != null) {
      writer.writeUInt64List(38, message.uint64PackedListField, true);
    }
    if (message.int32PackedListField != null) {
      writer.writeInt32List(39, message.int32PackedListField, true);
    }
    if (message.fixed64PackedListField != null) {
      writer.writeFixed64List(40, message.fixed64PackedListField, true);
    }
    if (message.fixed32PackedListField != null) {
      writer.writeFixed32List(41, message.fixed32PackedListField, true);
    }
    if (message.getBoolPackedListField() != null) {
      writer.writeBoolList(42, message.getBoolPackedListField(), true);
    }
    if (message.uint32PackedListField != null) {
      writer.writeUInt32List(43, message.uint32PackedListField, true);
    }
    if (message.enumPackedListField != null) {
      writer.writeEnumList(44, message.enumPackedListField, true);
    }
    if (message.sfixed32PackedListField != null) {
      writer.writeSFixed32List(45, message.sfixed32PackedListField, true);
    }
    if (message.sfixed64PackedListField != null) {
      writer.writeSFixed64List(46, message.sfixed64PackedListField, true);
    }
    if (message.sint32PackedListField != null) {
      writer.writeSInt32List(47, message.sint32PackedListField, true);
    }
    if (message.sint64PackedListField != null) {
      writer.writeSInt64List(48, message.sint64PackedListField, true);
    }
  }

  @Override
  public void mergeFrom(TestMessage message, Reader reader) {
    while (true) {
      int fieldNumber = reader.getFieldNumber();
      if (fieldNumber == Reader.READ_DONE) {
        return;
      }

      switch (fieldNumber) {
        case 1:
          message.doubleField = reader.readDouble();
          break;
        case 2:
          message.floatField = reader.readFloat();
          break;
        case 3:
          message.int64Field = reader.readInt64();
          break;
        case 4:
          message.uint64Field = reader.readUInt64();
          break;
        case 5:
          message.int32Field = reader.readInt32();
          break;
        case 6:
          message.fixed64Field = reader.readFixed64();
          break;
        case 7:
          message.fixed32Field = reader.readFixed32();
          break;
        case 8:
          message.setBoolField(reader.readBool());
          break;
        case 9:
          message.stringField = reader.readString();
          break;
        case 10:
          message.messageField = (TestMessage.InnerMessage) reader.readMessage();
          break;
        case 11:
          message.bytesField = reader.readBytes();
          break;
        case 12:
          message.uint32Field = reader.readUInt32();
          break;
        case 13:
          message.enumField = reader.readEnum();
          break;
        case 14:
          message.sfixed32Field = reader.readSFixed32();
          break;
        case 15:
          message.sfixed64Field = reader.readSFixed64();
          break;
        case 16:
          message.sint32Field = reader.readSInt32();
          break;
        case 17:
          message.sint64Field = reader.readSInt64();
          break;
        case 18:
          if (message.doubleListField == null) {
            message.doubleListField = new ArrayList<Double>();
          }
          reader.readDoubleList(message.doubleListField, false);
          break;
        case 19:
          if (message.floatListField == null) {
            message.floatListField = new ArrayList<Float>();
          }
          reader.readFloatList(message.floatListField, false);
          break;
        case 20:
          if (message.int64ListField == null) {
            message.int64ListField = new ArrayList<Long>();
          }
          reader.readInt64List(message.int64ListField, false);
          break;
        case 21:
          if (message.uint64ListField == null) {
            message.uint64ListField = new ArrayList<Long>();
          }
          reader.readUInt64List(message.uint64ListField, false);
          break;
        case 22:
          if (message.int32ListField == null) {
            message.int32ListField = new ArrayList<Integer>();
          }
          reader.readInt32List(message.int32ListField, false);
          break;
        case 23:
          if (message.fixed64ListField == null) {
            message.fixed64ListField = new ArrayList<Long>();
          }
          reader.readFixed64List(message.fixed64ListField, false);
          break;
        case 24:
          if (message.fixed32ListField == null) {
            message.fixed32ListField = new ArrayList<Integer>();
          }
          reader.readFixed32List(message.fixed32ListField, false);
          break;
        case 25:
          if (message.getBoolListField() == null) {
            message.setBoolListField(new ArrayList<Boolean>());
          }
          reader.readBoolList(message.getBoolListField(), false);
          break;
        case 26:
          if (message.stringListField == null) {
            message.stringListField = new ArrayList<String>();
          }
          reader.readStringList(message.stringListField);
          break;
        case 27:
          if (message.messageListField == null) {
            message.messageListField = new ArrayList<TestMessage.InnerMessage>();
          }
          reader.readMessageList(message.messageListField, TestMessage.InnerMessage.class);
          break;
        case 28:
          if (message.bytesListField == null) {
            message.bytesListField = new ArrayList<ByteString>();
          }
          reader.readBytesList(message.bytesListField);
          break;
        case 29:
          if (message.uint32ListField == null) {
            message.uint32ListField = new ArrayList<Integer>();
          }
          reader.readUInt32List(message.uint32ListField, false);
          break;
        case 30:
          if (message.enumListField == null) {
            message.enumListField = new ArrayList<Integer>();
          }
          reader.readEnumList(message.enumListField, false);
          break;
        case 31:
          if (message.sfixed32ListField == null) {
            message.sfixed32ListField = new ArrayList<Integer>();
          }
          reader.readSFixed32List(message.sfixed32ListField, false);
          break;
        case 32:
          if (message.sfixed64ListField == null) {
            message.sfixed64ListField = new ArrayList<Long>();
          }
          reader.readSFixed64List(message.sfixed64ListField, false);
          break;
        case 33:
          if (message.sint32ListField == null) {
            message.sint32ListField = new ArrayList<Integer>();
          }
          reader.readSInt32List(message.sint32ListField, false);
          break;
        case 34:
          if (message.sint64ListField == null) {
            message.sint64ListField = new ArrayList<Long>();
          }
          reader.readSInt64List(message.sint64ListField, false);
          break;
        case 35:
          if (message.doublePackedListField == null) {
            message.doublePackedListField = new ArrayList<Double>();
          }
          reader.readDoubleList(message.doublePackedListField, true);
          break;
        case 36:
          if (message.floatPackedListField == null) {
            message.floatPackedListField = new ArrayList<Float>();
          }
          reader.readFloatList(message.floatPackedListField, true);
          break;
        case 37:
          if (message.int64PackedListField == null) {
            message.int64PackedListField = new ArrayList<Long>();
          }
          reader.readInt64List(message.int64PackedListField, true);
          break;
        case 38:
          if (message.uint64PackedListField == null) {
            message.uint64PackedListField = new ArrayList<Long>();
          }
          reader.readUInt64List(message.uint64PackedListField, true);
          break;
        case 39:
          if (message.int32PackedListField == null) {
            message.int32PackedListField = new ArrayList<Integer>();
          }
          reader.readInt32List(message.int32PackedListField, true);
          break;
        case 40:
          if (message.fixed64PackedListField == null) {
            message.fixed64PackedListField = new ArrayList<Long>();
          }
          reader.readFixed64List(message.fixed64PackedListField, true);
          break;
        case 41:
          if (message.fixed32PackedListField == null) {
            message.fixed32PackedListField = new ArrayList<Integer>();
          }
          reader.readFixed32List(message.fixed32PackedListField, true);
          break;
        case 42:
          if (message.getBoolPackedListField() == null) {
            message.setBoolPackedListField(new ArrayList<Boolean>());
          }
          reader.readBoolList(message.getBoolPackedListField(), true);
          break;
        case 43:
          if (message.uint32PackedListField == null) {
            message.uint32PackedListField = new ArrayList<Integer>();
          }
          reader.readUInt32List(message.uint32PackedListField, true);
          break;
        case 44:
          if (message.enumPackedListField == null) {
            message.enumPackedListField = new ArrayList<Integer>();
          }
          reader.readEnumList(message.enumPackedListField, true);
          break;
        case 45:
          if (message.sfixed32PackedListField == null) {
            message.sfixed32PackedListField = new ArrayList<Integer>();
          }
          reader.readSFixed32List(message.sfixed32PackedListField, true);
          break;
        case 46:
          if (message.sfixed64PackedListField == null) {
            message.sfixed64PackedListField = new ArrayList<Long>();
          }
          reader.readSFixed64List(message.sfixed64PackedListField, true);
          break;
        case 47:
          if (message.sint32PackedListField == null) {
            message.sint32PackedListField = new ArrayList<Integer>();
          }
          reader.readSInt32List(message.sint32PackedListField, true);
          break;
        case 48:
          if (message.sint64PackedListField == null) {
            message.sint64PackedListField = new ArrayList<Long>();
          }
          reader.readSInt64List(message.sint64PackedListField, true);
          break;
        default:
          if (!reader.skipField()) {
            return;
          }
      }
    }
  }
}
