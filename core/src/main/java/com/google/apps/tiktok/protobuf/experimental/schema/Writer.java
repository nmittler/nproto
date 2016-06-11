package com.google.apps.tiktok.protobuf.experimental.schema;

import com.google.apps.tiktok.protobuf.experimental.ByteString;

import java.util.List;

/**
 * A writer that performs serialization of protobuf message fields.
 */
public interface Writer {
  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SFIXED32}.
   */
  void writeSFixed32(int fieldNumber, int value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#INT64}.
   */
  void writeInt64(int fieldNumber, long value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SFIXED64}.
   */
  void writeSFixed64(int fieldNumber, long value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#FLOAT}.
   */
  void writeFloat(int fieldNumber, float value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#DOUBLE}.
   */
  void writeDouble(int fieldNumber, double value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#ENUM}.
   */
  void writeEnum(int fieldNumber, int value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#UINT64}.
   */
  void writeUInt64(int fieldNumber, long value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#INT32}.
   */
  void writeInt32(int fieldNumber, int value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#FIXED64}.
   */
  void writeFixed64(int fieldNumber, long value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#FIXED32}.
   */
  void writeFixed32(int fieldNumber, int value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#BOOL}.
   */
  void writeBool(int fieldNumber, boolean value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#STRING}.
   */
  void writeString(int fieldNumber, String value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#BYTES}.
   */
  void writeBytes(int fieldNumber, ByteString value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#UINT32}.
   */
  void writeUInt32(int fieldNumber, int value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SINT32}.
   */
  void writeSInt32(int fieldNumber, int value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SINT64}.
   */
  void writeSInt64(int fieldNumber, long value);

  /**
   * Writes a field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#MESSAGE}.
   */
  void writeMessage(int fieldNumber, Object value);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#INT32}.
   */
  void writeInt32List(int fieldNumber, List<Integer> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#FIXED32}.
   */
  void writeFixed32List(int fieldNumber, List<Integer> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#INT64}.
   */
  void writeInt64List(int fieldNumber, List<Long> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#UINT64}.
   */
  void writeUInt64List(int fieldNumber, List<Long> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#FIXED64}.
   */
  void writeFixed64List(int fieldNumber, List<Long> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#FLOAT}.
   */
  void writeFloatList(int fieldNumber, List<Float> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#DOUBLE}.
   */
  void writeDoubleList(int fieldNumber, List<Double> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#ENUM}.
   */
  void writeEnumList(int fieldNumber, List<Integer> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#BOOL}.
   */
  void writeBoolList(int fieldNumber, List<Boolean> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#STRING}.
   */
  void writeStringList(int fieldNumber, List<String> value);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#BYTES}.
   */
  void writeBytesList(int fieldNumber, List<ByteString> value);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#UINT32}.
   */
  void writeUInt32List(int fieldNumber, List<Integer> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SFIXED32}.
   */
  void writeSFixed32List(int fieldNumber, List<Integer> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SFIXED64}.
   */
  void writeSFixed64List(int fieldNumber, List<Long> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SINT32}.
   */
  void writeSInt32List(int fieldNumber, List<Integer> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#SINT64}.
   */
  void writeSInt64List(int fieldNumber, List<Long> value, boolean packed);

  /**
   * Writes a list field of type
   * {@link com.google.apps.tiktok.protobuf.experimental.FieldType#MESSAGE}.
   */
  void writeMessageList(int fieldNumber, List<?> value);
}
