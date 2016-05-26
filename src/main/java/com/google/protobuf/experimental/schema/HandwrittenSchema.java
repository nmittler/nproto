package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.ByteString;
import com.google.protobuf.experimental.PojoMessage;
import com.google.protobuf.experimental.Reader;
import com.google.protobuf.experimental.Writer;

import java.util.ArrayList;
import java.util.Iterator;

final class HandwrittenSchema implements Schema<PojoMessage> {
  @Override
  public void writeTo(PojoMessage message, Writer writer) {
    writer.writeEnum(1, message.enumField);
    writer.writeBool(2, message.boolField);
    writer.writeUInt32(3, message.uint32Field);
    writer.writeInt32(4, message.int32Field);
    writer.writeSInt32(5, message.sInt32Field);
    writer.writeFixed32(6, message.fixedInt32Field);
    writer.writeSFixed32(7, message.sFixedInt32Field);
    writer.writeUInt64(8, message.uint64Field);
    writer.writeInt64(9, message.int64Field);
    writer.writeSInt64(10, message.sInt64Field);
    writer.writeFixed64(11, message.fixedInt64Field);
    writer.writeSFixed64(12, message.sFixedInt64Field);
    writer.writeString(13, message.stringField);
    writer.writeBytes(14, message.bytesField);
    writer.writeMessage(15, message.messageField);
    if (message.enumListField != null && !message.enumListField.isEmpty()) {
      writer.writeEnumList(16, message.enumListField);
    }
    if (message.boolListField != null && !message.boolListField.isEmpty()) {
      writer.writeBoolList(17, message.boolListField);
    }
    if (message.uint32ListField != null && !message.uint32ListField.isEmpty()) {
      writer.writeUInt32List(18, message.uint32ListField);
    }
    if (message.int32ListField != null && !message.int32ListField.isEmpty()) {
      writer.writeInt32List(19, message.int32ListField);
    }
    if (message.sInt32ListField != null && !message.sInt32ListField.isEmpty()) {
      writer.writeSInt32List(20, message.sInt32ListField);
    }
    if (message.fixedInt32ListField != null && !message.fixedInt32ListField.isEmpty()) {
      writer.writeFixed32List(21, message.fixedInt32ListField);
    }
    if (message.sFixedInt32ListField != null && !message.sFixedInt32ListField.isEmpty()) {
      writer.writeSFixed32List(22, message.sFixedInt32ListField);
    }
    if (message.uint64ListField != null && !message.uint64ListField.isEmpty()) {
      writer.writeUInt64List(23, message.uint64ListField);
    }
    if (message.int64ListField != null && !message.int64ListField.isEmpty()) {
      writer.writeInt64List(24, message.int64ListField);
    }
    if (message.sInt64ListField != null && !message.sInt64ListField.isEmpty()) {
      writer.writeSInt64List(25, message.sInt64ListField);
    }
    if (message.fixedInt64ListField != null && !message.fixedInt64ListField.isEmpty()) {
      writer.writeFixed64List(26, message.fixedInt64ListField);
    }
    if (message.sFixedInt64ListField != null && !message.sFixedInt64ListField.isEmpty()) {
      writer.writeSFixed64List(27, message.sFixedInt64ListField);
    }
    if (message.stringListField != null && !message.stringListField.isEmpty()) {
      writer.writeStringList(28, message.stringListField);
    }
    if (message.bytesListField != null && !message.bytesListField.isEmpty()) {
      writer.writeBytesList(29, message.bytesListField);
    }
    if (message.messageListField != null && !message.messageListField.isEmpty()) {
      writer.writeMessageList(30, message.messageListField);
    }
  }

  @Override
  public void mergeFrom(PojoMessage message, Reader reader) {
    while (true) {
      int fieldNumber = reader.fieldNumber();
      if (fieldNumber == Reader.READ_DONE) {
        return;
      }

      switch(fieldNumber) {
        case 1:
          message.enumField = PojoMessage.MyEnum.class.cast(reader.readEnum());
          break;
        case 2:
          message.boolField = reader.readBool();
          break;
        case 3:
          message.uint32Field = reader.readUInt32();
          break;
        case 4:
          message.int32Field = reader.readInt32();
          break;
        case 5:
          message.sInt32Field = reader.readSInt32();
          break;
        case 6:
          message.fixedInt32Field = reader.readFixed32();
          break;
        case 7:
          message.sFixedInt32Field = reader.readSFixed32();
          break;
        case 8:
          message.uint64Field = reader.readUInt64();
          break;
        case 9:
          message.int64Field = reader.readInt64();
          break;
        case 10:
          message.sInt64Field = reader.readSInt64();
          break;
        case 11:
          message.fixedInt64Field = reader.readFixed64();
          break;
        case 12:
          message.sFixedInt64Field = reader.readSFixed64();
          break;
        case 13:
          message.stringField = reader.readString();
          break;
        case 14:
          message.bytesField = reader.readBytes();
          break;
        case 15:
          message.messageField = reader.readMessage();
          break;
        case 16:
          if (message.enumListField == null) {
            message.enumListField = new ArrayList<PojoMessage.MyEnum>();
          }
          message.enumListField.add(PojoMessage.MyEnum.class.cast(reader.readEnum()));
          break;
        case 17:
          if (message.boolListField == null) {
            message.boolListField = new ArrayList<Boolean>();
          }
          message.boolListField.add(reader.readBool());
          break;
        case 18:
          if (message.uint32ListField == null) {
            message.uint32ListField = new ArrayList<Integer>();
          }
          message.uint32ListField.add(reader.readUInt32());
          break;
        case 19:
          if (message.int32ListField == null) {
            message.int32ListField = new ArrayList<Integer>();
          }
          message.int32ListField.add(reader.readInt32());
          break;
        case 20:
          if (message.sInt32ListField == null) {
            message.sInt32ListField = new ArrayList<Integer>();
          }
          message.sInt32ListField.add(reader.readSInt32());
          break;
        case 21:
          if (message.fixedInt32ListField == null) {
            message.fixedInt32ListField = new ArrayList<Integer>();
          }
          message.fixedInt32ListField.add(reader.readFixed32());
          break;
        case 22:
          if (message.sFixedInt32ListField == null) {
            message.sFixedInt32ListField = new ArrayList<Integer>();
          }
          message.sFixedInt32ListField.add(reader.readSFixed32());
          break;
        case 23:
          if (message.uint64ListField == null) {
            message.uint64ListField = new ArrayList<Long>();
          }
          message.uint64ListField.add(reader.readUInt64());
          break;
        case 24:
          if (message.int64ListField == null) {
            message.int64ListField = new ArrayList<Long>();
          }
          message.int64ListField.add(reader.readInt64());
          break;
        case 25:
          if (message.sInt64ListField == null) {
            message.sInt64ListField = new ArrayList<Long>();
          }
          message.sInt64ListField.add(reader.readSInt64());
          break;
        case 26:
          if (message.fixedInt64ListField == null) {
            message.fixedInt64ListField = new ArrayList<Long>();
          }
          message.fixedInt64ListField.add(reader.readFixed64());
          break;
        case 27:
          if (message.sFixedInt64ListField == null) {
            message.sFixedInt64ListField = new ArrayList<Long>();
          }
          message.sFixedInt64ListField.add(reader.readSFixed64());
          break;
        case 28:
          if (message.stringListField == null) {
            message.stringListField = new ArrayList<String>();
          }
          message.stringListField.add(reader.readString());
          break;
        case 29:
          if (message.bytesListField == null) {
            message.bytesListField = new ArrayList<ByteString>();
          }
          message.bytesListField.add(reader.readBytes());
          break;
        case 30:
          if (message.messageListField == null) {
            message.messageListField = new ArrayList<Object>();
          }
          message.messageListField.add(reader.readMessage());
          break;
        default:
          if (!reader.skipField()) {
            return;
          }
      }
    }
  }

  @Override
  public Iterator<Field> iterator() {
    // TODO:
    throw new UnsupportedOperationException();
  }
}
