package com.google.protobuf.experimental.example;

import com.google.protobuf.experimental.ByteString;
import com.google.protobuf.experimental.ProtoField;
import com.google.protobuf.experimental.WireFormat.FieldType;

import java.util.List;

public final class PojoMessage {
  public enum MyEnum {
    VALUE1,
    VALUE2
  }

  /**
   * Test a package-private field.
   */
  @ProtoField(number = 1, type = FieldType.ENUM)
  MyEnum enumField;

  /**
   * Test a private field.
   */
  @ProtoField(number = 2, type = FieldType.BOOL)
  private boolean boolField;

  @ProtoField(number = 3, type = FieldType.UINT32)
  public int uint32Field;

  @ProtoField(number = 4, type = FieldType.INT32)
  public int int32Field;

  @ProtoField(number = 5, type = FieldType.SINT32)
  public int sInt32Field;

  @ProtoField(number = 6, type = FieldType.FIXED32)
  public int fixedInt32Field;

  @ProtoField(number = 7, type = FieldType.SFIXED32)
  public int sFixedInt32Field;

  @ProtoField(number = 8, type = FieldType.UINT64)
  public long uint64Field;

  @ProtoField(number = 9, type = FieldType.INT64)
  public long int64Field;

  @ProtoField(number = 10, type = FieldType.SINT64)
  public long sInt64Field;

  @ProtoField(number = 11, type = FieldType.FIXED64)
  public long fixedInt64Field;

  @ProtoField(number = 12, type = FieldType.SFIXED64)
  public long sFixedInt64Field;

  @ProtoField(number = 13, type = FieldType.STRING)
  public String stringField;

  @ProtoField(number = 14, type = FieldType.BYTES)
  public ByteString bytesField;

  @ProtoField(number = 15, type = FieldType.MESSAGE)
  public Object messageField;

  /**
   * Test a package-private list.
   */
  @ProtoField(number = 16, type = FieldType.ENUM)
  List<MyEnum> enumListField;

  /**
   * Test a private list.
   */
  @ProtoField(number = 17, type = FieldType.BOOL)
  private List<Boolean> boolListField;

  @ProtoField(number = 18, type = FieldType.UINT32)
  public List<Integer> uint32ListField;

  @ProtoField(number = 19, type = FieldType.INT32)
  public List<Integer> int32ListField;

  @ProtoField(number = 20, type = FieldType.SINT32)
  public List<Integer> sInt32ListField;

  @ProtoField(number = 21, type = FieldType.FIXED32)
  public List<Integer> fixedInt32ListField;

  @ProtoField(number = 22, type = FieldType.SFIXED32)
  public List<Integer> sFixedInt32ListField;

  @ProtoField(number = 23, type = FieldType.UINT64)
  public List<Long> uint64ListField;

  @ProtoField(number = 24, type = FieldType.INT64)
  public List<Long> int64ListField;

  @ProtoField(number = 25, type = FieldType.SINT64)
  public List<Long> sInt64ListField;

  @ProtoField(number = 26, type = FieldType.FIXED64)
  public List<Long> fixedInt64ListField;

  @ProtoField(number = 27, type = FieldType.SFIXED64)
  public List<Long> sFixedInt64ListField;

  @ProtoField(number = 28, type = FieldType.STRING)
  public List<String> stringListField;

  @ProtoField(number = 29, type = FieldType.BYTES)
  public List<ByteString> bytesListField;

  @ProtoField(number = 30, type = FieldType.MESSAGE)
  public List<Object> messageListField;

  public MyEnum getEnumField() {
    return enumField;
  }

  public void setEnumField(MyEnum enumField) {
    this.enumField = enumField;
  }

  public boolean isBoolField() {
    return boolField;
  }

  public void setBoolField(boolean boolField) {
    this.boolField = boolField;
  }

  public int getUint32Field() {
    return uint32Field;
  }

  public void setUint32Field(int uint32Field) {
    this.uint32Field = uint32Field;
  }

  public int getInt32Field() {
    return int32Field;
  }

  public void setInt32Field(int int32Field) {
    this.int32Field = int32Field;
  }

  public int getsInt32Field() {
    return sInt32Field;
  }

  public void setsInt32Field(int sInt32Field) {
    this.sInt32Field = sInt32Field;
  }

  public int getFixedInt32Field() {
    return fixedInt32Field;
  }

  public void setFixedInt32Field(int fixedInt32Field) {
    this.fixedInt32Field = fixedInt32Field;
  }

  public int getsFixedInt32Field() {
    return sFixedInt32Field;
  }

  public void setsFixedInt32Field(int sFixedInt32Field) {
    this.sFixedInt32Field = sFixedInt32Field;
  }

  public long getUint64Field() {
    return uint64Field;
  }

  public void setUint64Field(long uint64Field) {
    this.uint64Field = uint64Field;
  }

  public long getInt64Field() {
    return int64Field;
  }

  public void setInt64Field(long int64Field) {
    this.int64Field = int64Field;
  }

  public long getsInt64Field() {
    return sInt64Field;
  }

  public void setsInt64Field(long sInt64Field) {
    this.sInt64Field = sInt64Field;
  }

  public long getFixedInt64Field() {
    return fixedInt64Field;
  }

  public void setFixedInt64Field(long fixedInt64Field) {
    this.fixedInt64Field = fixedInt64Field;
  }

  public long getsFixedInt64Field() {
    return sFixedInt64Field;
  }

  public void setsFixedInt64Field(long sFixedInt64Field) {
    this.sFixedInt64Field = sFixedInt64Field;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public ByteString getBytesField() {
    return bytesField;
  }

  public void setBytesField(ByteString bytesField) {
    this.bytesField = bytesField;
  }

  public Object getMessageField() {
    return messageField;
  }

  public void setMessageField(Object messageField) {
    this.messageField = messageField;
  }

  public List<MyEnum> getEnumListField() {
    return enumListField;
  }

  public void setEnumListField(List<MyEnum> enumListField) {
    this.enumListField = enumListField;
  }

  public List<Boolean> getBoolListField() {
    return boolListField;
  }

  public void setBoolListField(List<Boolean> boolListField) {
    this.boolListField = boolListField;
  }

  public List<Integer> getUint32ListField() {
    return uint32ListField;
  }

  public void setUint32ListField(List<Integer> uint32ListField) {
    this.uint32ListField = uint32ListField;
  }

  public List<Integer> getInt32ListField() {
    return int32ListField;
  }

  public void setInt32ListField(List<Integer> int32ListField) {
    this.int32ListField = int32ListField;
  }

  public List<Integer> getsInt32ListField() {
    return sInt32ListField;
  }

  public void setsInt32ListField(List<Integer> sInt32ListField) {
    this.sInt32ListField = sInt32ListField;
  }

  public List<Integer> getFixedInt32ListField() {
    return fixedInt32ListField;
  }

  public void setFixedInt32ListField(List<Integer> fixedInt32ListField) {
    this.fixedInt32ListField = fixedInt32ListField;
  }

  public List<Integer> getsFixedInt32ListField() {
    return sFixedInt32ListField;
  }

  public void setsFixedInt32ListField(List<Integer> sFixedInt32ListField) {
    this.sFixedInt32ListField = sFixedInt32ListField;
  }

  public List<Long> getUint64ListField() {
    return uint64ListField;
  }

  public void setUint64ListField(List<Long> uint64ListField) {
    this.uint64ListField = uint64ListField;
  }

  public List<Long> getInt64ListField() {
    return int64ListField;
  }

  public void setInt64ListField(List<Long> int64ListField) {
    this.int64ListField = int64ListField;
  }

  public List<Long> getsInt64ListField() {
    return sInt64ListField;
  }

  public void setsInt64ListField(List<Long> sInt64ListField) {
    this.sInt64ListField = sInt64ListField;
  }

  public List<Long> getFixedInt64ListField() {
    return fixedInt64ListField;
  }

  public void setFixedInt64ListField(List<Long> fixedInt64ListField) {
    this.fixedInt64ListField = fixedInt64ListField;
  }

  public List<Long> getsFixedInt64ListField() {
    return sFixedInt64ListField;
  }

  public void setsFixedInt64ListField(List<Long> sFixedInt64ListField) {
    this.sFixedInt64ListField = sFixedInt64ListField;
  }

  public List<String> getStringListField() {
    return stringListField;
  }

  public void setStringListField(List<String> stringListField) {
    this.stringListField = stringListField;
  }

  public List<ByteString> getBytesListField() {
    return bytesListField;
  }

  public void setBytesListField(List<ByteString> bytesListField) {
    this.bytesListField = bytesListField;
  }

  public List<Object> getMessageListField() {
    return messageListField;
  }

  public void setMessageListField(List<Object> messageListField) {
    this.messageListField = messageListField;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PojoMessage that = (PojoMessage) o;

    if (boolField != that.boolField) return false;
    if (uint32Field != that.uint32Field) return false;
    if (int32Field != that.int32Field) return false;
    if (sInt32Field != that.sInt32Field) return false;
    if (fixedInt32Field != that.fixedInt32Field) return false;
    if (sFixedInt32Field != that.sFixedInt32Field) return false;
    if (uint64Field != that.uint64Field) return false;
    if (int64Field != that.int64Field) return false;
    if (sInt64Field != that.sInt64Field) return false;
    if (fixedInt64Field != that.fixedInt64Field) return false;
    if (sFixedInt64Field != that.sFixedInt64Field) return false;
    if (enumField != that.enumField) return false;
    if (stringField != null ? !stringField.equals(that.stringField) : that.stringField != null)
      return false;
    if (bytesField != null ? !bytesField.equals(that.bytesField) : that.bytesField != null)
      return false;
    if (messageField != null ? !messageField.equals(that.messageField) : that.messageField != null)
      return false;
    if (enumListField != null ? !enumListField.equals(that.enumListField) : that.enumListField != null)
      return false;
    if (boolListField != null ? !boolListField.equals(that.boolListField) : that.boolListField != null)
      return false;
    if (uint32ListField != null ? !uint32ListField.equals(that.uint32ListField) : that.uint32ListField != null)
      return false;
    if (int32ListField != null ? !int32ListField.equals(that.int32ListField) : that.int32ListField != null)
      return false;
    if (sInt32ListField != null ? !sInt32ListField.equals(that.sInt32ListField) : that.sInt32ListField != null)
      return false;
    if (fixedInt32ListField != null ? !fixedInt32ListField.equals(that.fixedInt32ListField) : that.fixedInt32ListField != null)
      return false;
    if (sFixedInt32ListField != null ? !sFixedInt32ListField.equals(that.sFixedInt32ListField) : that.sFixedInt32ListField != null)
      return false;
    if (uint64ListField != null ? !uint64ListField.equals(that.uint64ListField) : that.uint64ListField != null)
      return false;
    if (int64ListField != null ? !int64ListField.equals(that.int64ListField) : that.int64ListField != null)
      return false;
    if (sInt64ListField != null ? !sInt64ListField.equals(that.sInt64ListField) : that.sInt64ListField != null)
      return false;
    if (fixedInt64ListField != null ? !fixedInt64ListField.equals(that.fixedInt64ListField) : that.fixedInt64ListField != null)
      return false;
    if (sFixedInt64ListField != null ? !sFixedInt64ListField.equals(that.sFixedInt64ListField) : that.sFixedInt64ListField != null)
      return false;
    if (stringListField != null ? !stringListField.equals(that.stringListField) : that.stringListField != null)
      return false;
    if (bytesListField != null ? !bytesListField.equals(that.bytesListField) : that.bytesListField != null)
      return false;
    return messageListField != null ? messageListField.equals(that.messageListField) : that.messageListField == null;

  }

  @Override
  public int hashCode() {
    int result = enumField != null ? enumField.hashCode() : 0;
    result = 31 * result + (boolField ? 1 : 0);
    result = 31 * result + uint32Field;
    result = 31 * result + int32Field;
    result = 31 * result + sInt32Field;
    result = 31 * result + fixedInt32Field;
    result = 31 * result + sFixedInt32Field;
    result = 31 * result + (int) (uint64Field ^ (uint64Field >>> 32));
    result = 31 * result + (int) (int64Field ^ (int64Field >>> 32));
    result = 31 * result + (int) (sInt64Field ^ (sInt64Field >>> 32));
    result = 31 * result + (int) (fixedInt64Field ^ (fixedInt64Field >>> 32));
    result = 31 * result + (int) (sFixedInt64Field ^ (sFixedInt64Field >>> 32));
    result = 31 * result + (stringField != null ? stringField.hashCode() : 0);
    result = 31 * result + (bytesField != null ? bytesField.hashCode() : 0);
    result = 31 * result + (messageField != null ? messageField.hashCode() : 0);
    result = 31 * result + (enumListField != null ? enumListField.hashCode() : 0);
    result = 31 * result + (boolListField != null ? boolListField.hashCode() : 0);
    result = 31 * result + (uint32ListField != null ? uint32ListField.hashCode() : 0);
    result = 31 * result + (int32ListField != null ? int32ListField.hashCode() : 0);
    result = 31 * result + (sInt32ListField != null ? sInt32ListField.hashCode() : 0);
    result = 31 * result + (fixedInt32ListField != null ? fixedInt32ListField.hashCode() : 0);
    result = 31 * result + (sFixedInt32ListField != null ? sFixedInt32ListField.hashCode() : 0);
    result = 31 * result + (uint64ListField != null ? uint64ListField.hashCode() : 0);
    result = 31 * result + (int64ListField != null ? int64ListField.hashCode() : 0);
    result = 31 * result + (sInt64ListField != null ? sInt64ListField.hashCode() : 0);
    result = 31 * result + (fixedInt64ListField != null ? fixedInt64ListField.hashCode() : 0);
    result = 31 * result + (sFixedInt64ListField != null ? sFixedInt64ListField.hashCode() : 0);
    result = 31 * result + (stringListField != null ? stringListField.hashCode() : 0);
    result = 31 * result + (bytesListField != null ? bytesListField.hashCode() : 0);
    result = 31 * result + (messageListField != null ? messageListField.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PojoMessage{" +
            "enumField=" + enumField +
            ", boolField=" + boolField +
            ", uint32Field=" + uint32Field +
            ", int32Field=" + int32Field +
            ", sInt32Field=" + sInt32Field +
            ", fixedInt32Field=" + fixedInt32Field +
            ", sFixedInt32Field=" + sFixedInt32Field +
            ", uint64Field=" + uint64Field +
            ", int64Field=" + int64Field +
            ", sInt64Field=" + sInt64Field +
            ", fixedInt64Field=" + fixedInt64Field +
            ", sFixedInt64Field=" + sFixedInt64Field +
            ", stringField='" + stringField + '\'' +
            ", bytesField=" + bytesField +
            ", messageField=" + messageField +
            ", enumListField=" + enumListField +
            ", boolListField=" + boolListField +
            ", uint32ListField=" + uint32ListField +
            ", int32ListField=" + int32ListField +
            ", sInt32ListField=" + sInt32ListField +
            ", fixedInt32ListField=" + fixedInt32ListField +
            ", sFixedInt32ListField=" + sFixedInt32ListField +
            ", uint64ListField=" + uint64ListField +
            ", int64ListField=" + int64ListField +
            ", sInt64ListField=" + sInt64ListField +
            ", fixedInt64ListField=" + fixedInt64ListField +
            ", sFixedInt64ListField=" + sFixedInt64ListField +
            ", stringListField=" + stringListField +
            ", bytesListField=" + bytesListField +
            ", messageListField=" + messageListField +
            '}';
  }
}
