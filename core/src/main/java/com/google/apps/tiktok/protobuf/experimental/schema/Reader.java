package com.google.apps.tiktok.protobuf.experimental.schema;

import com.google.apps.tiktok.protobuf.experimental.ByteString;
import com.google.apps.tiktok.protobuf.experimental.FieldType;

import java.util.List;

/**
 * A reader of fields from a serialized protobuf message.
 */
// TODO(nathanmittler): Refactor to allow the reader to allocate properly sized lists.
public interface Reader {
  /**
   * Value used to indicate that the end of input has been reached.
   */
  int READ_DONE = Integer.MAX_VALUE;

  /**
   * Gets the field number for the current field being read.
   *
   * @return the current field number or {@link #READ_DONE} if the end of input has been reached.
   */
  int getFieldNumber();

  /**
   * Skips the current field and advances the reader to the next field.
   *
   * @return {@code true} if there are more fields or {@link #READ_DONE} if the end of input has
   * been reached.
   */
  boolean skipField();

  /**
   * Reads and returns the next field of type {@link FieldType#DOUBLE} and advances the reader to
   * the next field.
   */
  double readDouble();

  /**
   * Reads and returns the next field of type {@link FieldType#FLOAT} and advances the reader to
   * the next field.
   */
  float readFloat();

  /**
   * Reads and returns the next field of type {@link FieldType#UINT64} and advances the reader to
   * the next field.
   */
  long readUInt64();

  /**
   * Reads and returns the next field of type {@link FieldType#INT64} and advances the reader to
   * the next field.
   */
  long readInt64();

  /**
   * Reads and returns the next field of type {@link FieldType#INT32} and advances the reader to
   * the next field.
   */
  int readInt32();

  /**
   * Reads and returns the next field of type {@link FieldType#FIXED64} and advances the reader to
   * the next field.
   */
  long readFixed64();

  /**
   * Reads and returns the next field of type {@link FieldType#FIXED32} and advances the reader to
   * the next field.
   */
  int readFixed32();

  /**
   * Reads and returns the next field of type {@link FieldType#BOOL} and advances the reader to the
   * next field.
   */
  boolean readBool();

  /**
   * Reads and returns the next field of type {@link FieldType#STRING} and advances the reader to
   * the next field.
   */
  String readString();

  /**
   * Reads and returns the next field of type {@link FieldType#MESSAGE} and advances the reader to
   * the next field.
   */
  Object readMessage();

  /**
   * Reads and returns the next field of type {@link FieldType#BYTES} and advances the reader to
   * the next field.
   */
  ByteString readBytes();

  /**
   * Reads and returns the next field of type {@link FieldType#UINT32} and advances the reader to
   * the next field.
   */
  int readUInt32();

  /**
   * Reads and returns the next field of type {@link FieldType#ENUM} and advances the reader to the
   * next field.
   */
  int readEnum();

  /**
   * Reads and returns the next field of type {@link FieldType#SFIXED32} and advances the reader to
   * the next field.
   */
  int readSFixed32();

  /**
   * Reads and returns the next field of type {@link FieldType#SFIXED64} and advances the reader to
   * the next field.
   */
  long readSFixed64();

  /**
   * Reads and returns the next field of type {@link FieldType#SINT32} and advances the reader to
   * the next field.
   */
  int readSInt32();

  /**
   * Reads and returns the next field of type {@link FieldType#SINT64} and advances the reader to
   * the next field.
   */
  long readSInt64();

  /**
   * Reads the next field of type {@link FieldType#DOUBLE_LIST} or
   * {@link FieldType#DOUBLE_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readDoubleList(List<Double> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#FLOAT_LIST} or
   * {@link FieldType#FLOAT_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readFloatList(List<Float> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#UINT64_LIST} or
   * {@link FieldType#UINT64_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readUInt64List(List<Long> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#INT64_LIST} or
   * {@link FieldType#INT64_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readInt64List(List<Long> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#INT32_LIST} or
   * {@link FieldType#INT32_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readInt32List(List<Integer> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#FIXED64_LIST} or
   * {@link FieldType#FIXED64_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readFixed64List(List<Long> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#FIXED32_LIST} or
   * {@link FieldType#FIXED32_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readFixed32List(List<Integer> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#BOOL_LIST} or {@link FieldType#BOOL_LIST_PACKED}
   * and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readBoolList(List<Boolean> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#STRING_LIST} and advances the reader to the next
   * field.
   *
   * @param target the list that will receive the read values.
   */
  void readStringList(List<String> target);

  /**
   * Reads the next field of type {@link FieldType#MESSAGE_LIST} and advances the reader to the
   * next field.
   *
   * @param target the list that will receive the read values.
   * @param targetType the type of the elements stored in the {@code target} list.
   */
  <T> void readMessageList(List<T> target, Class<T> targetType);

  /**
   * Reads the next field of type {@link FieldType#BYTES_LIST} and advances the reader to the next
   * field.
   *
   * @param target the list that will receive the read values.
   */
  void readBytesList(List<ByteString> target);

  /**
   * Reads the next field of type {@link FieldType#UINT32_LIST} or
   * {@link FieldType#UINT32_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readUInt32List(List<Integer> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#ENUM_LIST} or {@link FieldType#ENUM_LIST_PACKED}
   * and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readEnumList(List<Integer> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#SFIXED32_LIST} or
   * {@link FieldType#SFIXED32_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readSFixed32List(List<Integer> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#SFIXED64_LIST} or
   * {@link FieldType#SFIXED64_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readSFixed64List(List<Long> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#SINT32_LIST} or
   * {@link FieldType#SINT32_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readSInt32List(List<Integer> target, boolean packed);

  /**
   * Reads the next field of type {@link FieldType#SINT64_LIST} or
   * {@link FieldType#SINT64_LIST_PACKED} and advances the reader to the next field.
   *
   * @param target the list that will receive the read values.
   * @param packed {@code true} the field type is packed.
   */
  void readSInt64List(List<Long> target, boolean packed);
}
