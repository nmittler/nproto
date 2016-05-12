package io.nproto;

import java.util.List;

/**
 * Created by nathanmittler on 5/10/16.
 */
public interface Writer {
  void writeSFixed32(int fieldNumber, int value);

  void writeInt64(int fieldNumber, long value);

  void writeSFixed64(int fieldNumber, long value);

  void writeFloat(int fieldNumber, float value);

  void writeDouble(int fieldNumber, double value);

  void writeEnum(int fieldNumber, int value);

  void writeUInt64(int fieldNumber, long value);

  void writeInt32(int fieldNumber, int value);

  void writeFixed64(int fieldNumber, long value);

  void writeFixed32(int fieldNumber, int value);

  void writeBool(int fieldNumber, boolean value);

  void writeString(int fieldNumber, String value);

  void writeBytes(int fieldNumber, ByteString value);

  void writeUInt32(int fieldNumber, int value);

  void writeSInt32(int fieldNumber, int value);

  void writeSInt64(int fieldNumber, long value);

  void writeMessage(int fieldNumber, Object value);

  void writeInt32List(int fieldNumber, boolean packed, List<Integer> list);

  void writeFixed32List(int fieldNumber, boolean packed, List<Integer> list);

  void writeInt64List(int fieldNumber, boolean packed, List<Long> list);

  void writeUInt64List(int fieldNumber, boolean packed, List<Long> list);

  void writeFixed64List(int fieldNumber, boolean packed, List<Long> list);

  void writeFloatList(int fieldNumber, boolean packed, List<Float> list);

  void writeDoubleList(int fieldNumber, boolean packed, List<Double> list);

  void writeEnumList(int fieldNumber, boolean packed, List<Integer> list);

  void writeBoolList(int fieldNumber, boolean packed, List<Boolean> list);

  void writeStringList(int fieldNumber, List<String> list);

  void writeBytesList(int fieldNumber, List<ByteString> list);

  void writeUInt32List(int fieldNumber, boolean packed, List<Integer> list);

  void writeSFixed32List(int fieldNumber, boolean packed, List<Integer> list);

  void writeSFixed64List(int fieldNumber, boolean packed, List<Long> list);

  void writeSInt32List(int fieldNumber, boolean packed, List<Integer> list);

  void writeSInt64List(int fieldNumber, boolean packed, List<Long> list);

  void writeMessageList(int fieldNumber, List<?> list);
}
