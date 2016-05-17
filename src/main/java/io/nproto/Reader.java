package io.nproto;

public interface Reader {
  int READ_DONE = Integer.MAX_VALUE;

  //int readTag();
  int fieldNumber();

  boolean skipField();

  // TODO(nathanmittler): Consider reading Lists. Could improve performance for repeated fields.
  double readDouble();

  float readFloat();

  long readUInt64();

  long readInt64();

  int readInt32();

  long readFixed64();

  int readFixed32();

  boolean readBool();

  String readString();

  Object readMessage();

  ByteString readBytes();

  int readUInt32();

  Enum<?> readEnum();

  int readSFixed32();

  long readSFixed64();

  int readSInt32();

  long readSInt64();
}
