package io.nproto;

public interface Reader {

  int readTag();

  void skipField();

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

  void readMessage(Object message);

  ByteString readBytes();

  int readUInt32();

  int readEnum();

  int readSFixed32();

  long readSFixed64();

  int readSInt32();

  long readSInt64();
}
