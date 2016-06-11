package com.google.apps.tiktok.protobuf.experimental.testing;

import com.google.apps.tiktok.protobuf.experimental.ByteString;
import com.google.apps.tiktok.protobuf.experimental.FieldType;
import com.google.apps.tiktok.protobuf.experimental.ProtoField;

import java.util.List;

/**
 * An test message for showing all of the possible field types.
 */
public final class TestMessage {

  /**
   * An inner message contained within {@link TestMessage}.
   */
  public static final class InnerMessage {
    public InnerMessage() {}

    public InnerMessage(boolean boolField) {
      this.boolField = boolField;
    }

    @ProtoField(fieldNumber = 1, type = FieldType.BOOL)
    public boolean boolField;

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (boolField ? 1231 : 1237);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      InnerMessage other = (InnerMessage) obj;
      if (boolField != other.boolField) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "InnerMessage [boolField=" + boolField + "]";
    }
  }

  @ProtoField(fieldNumber = 1, type = FieldType.DOUBLE)
  public double doubleField;

  @ProtoField(fieldNumber = 2, type = FieldType.FLOAT)
  public float floatField;

  @ProtoField(fieldNumber = 3, type = FieldType.INT64)
  public long int64Field;

  @ProtoField(fieldNumber = 4, type = FieldType.UINT64)
  public long uint64Field;

  @ProtoField(fieldNumber = 5, type = FieldType.INT32)
  public int int32Field;

  @ProtoField(fieldNumber = 6, type = FieldType.FIXED64)
  public long fixed64Field;

  @ProtoField(fieldNumber = 7, type = FieldType.FIXED32)
  public int fixed32Field;

  /**
   * Test a private field.
   */
  @ProtoField(fieldNumber = 8, type = FieldType.BOOL)
  private boolean boolField;

  @ProtoField(fieldNumber = 9, type = FieldType.STRING)
  public String stringField;

  @ProtoField(fieldNumber = 10, type = FieldType.MESSAGE)
  public InnerMessage messageField;

  @ProtoField(fieldNumber = 11, type = FieldType.BYTES)
  public ByteString bytesField;

  @ProtoField(fieldNumber = 12, type = FieldType.UINT32)
  public int uint32Field;

  /**
   * Test a package-private field.
   */
  @ProtoField(fieldNumber = 13, type = FieldType.ENUM)
  int enumField;

  @ProtoField(fieldNumber = 14, type = FieldType.SFIXED32)
  public int sfixed32Field;

  @ProtoField(fieldNumber = 15, type = FieldType.SFIXED64)
  public long sfixed64Field;

  @ProtoField(fieldNumber = 16, type = FieldType.SINT32)
  public int sint32Field;

  @ProtoField(fieldNumber = 17, type = FieldType.SINT64)
  public long sint64Field;

  @ProtoField(fieldNumber = 18, type = FieldType.DOUBLE_LIST)
  public List<Double> doubleListField;

  @ProtoField(fieldNumber = 19, type = FieldType.FLOAT_LIST)
  public List<Float> floatListField;

  @ProtoField(fieldNumber = 20, type = FieldType.INT64_LIST)
  public List<Long> int64ListField;

  @ProtoField(fieldNumber = 21, type = FieldType.UINT64_LIST)
  public List<Long> uint64ListField;

  @ProtoField(fieldNumber = 22, type = FieldType.INT32_LIST)
  public List<Integer> int32ListField;

  @ProtoField(fieldNumber = 23, type = FieldType.FIXED64_LIST)
  public List<Long> fixed64ListField;

  @ProtoField(fieldNumber = 24, type = FieldType.FIXED32_LIST)
  public List<Integer> fixed32ListField;

  /**
   * Test a private field.
   */
  @ProtoField(fieldNumber = 25, type = FieldType.BOOL_LIST)
  private List<Boolean> boolListField;

  @ProtoField(fieldNumber = 26, type = FieldType.STRING_LIST)
  public List<String> stringListField;

  @ProtoField(fieldNumber = 27, type = FieldType.MESSAGE_LIST)
  public List<InnerMessage> messageListField;

  @ProtoField(fieldNumber = 28, type = FieldType.BYTES_LIST)
  public List<ByteString> bytesListField;

  @ProtoField(fieldNumber = 29, type = FieldType.UINT32_LIST)
  public List<Integer> uint32ListField;

  /**
   * Test a package-private field.
   */
  @ProtoField(fieldNumber = 30, type = FieldType.ENUM_LIST)
  List<Integer> enumListField;

  @ProtoField(fieldNumber = 31, type = FieldType.SFIXED32_LIST)
  public List<Integer> sfixed32ListField;

  @ProtoField(fieldNumber = 32, type = FieldType.SFIXED64_LIST)
  public List<Long> sfixed64ListField;

  @ProtoField(fieldNumber = 33, type = FieldType.SINT32_LIST)
  public List<Integer> sint32ListField;

  @ProtoField(fieldNumber = 34, type = FieldType.SINT64_LIST)
  public List<Long> sint64ListField;

  @ProtoField(fieldNumber = 35, type = FieldType.DOUBLE_LIST_PACKED)
  public List<Double> doublePackedListField;

  @ProtoField(fieldNumber = 36, type = FieldType.FLOAT_LIST_PACKED)
  public List<Float> floatPackedListField;

  @ProtoField(fieldNumber = 37, type = FieldType.INT64_LIST_PACKED)
  public List<Long> int64PackedListField;

  @ProtoField(fieldNumber = 38, type = FieldType.UINT64_LIST_PACKED)
  public List<Long> uint64PackedListField;

  @ProtoField(fieldNumber = 39, type = FieldType.INT32_LIST_PACKED)
  public List<Integer> int32PackedListField;

  @ProtoField(fieldNumber = 40, type = FieldType.FIXED64_LIST_PACKED)
  public List<Long> fixed64PackedListField;

  @ProtoField(fieldNumber = 41, type = FieldType.FIXED32_LIST_PACKED)
  public List<Integer> fixed32PackedListField;

  /**
   * Test a private field.
   */
  @ProtoField(fieldNumber = 42, type = FieldType.BOOL_LIST_PACKED)
  private List<Boolean> boolPackedListField;

  @ProtoField(fieldNumber = 43, type = FieldType.UINT32_LIST_PACKED)
  public List<Integer> uint32PackedListField;

  /**
   * Test a package-private field.
   */
  @ProtoField(fieldNumber = 44, type = FieldType.ENUM_LIST_PACKED)
  List<Integer> enumPackedListField;

  @ProtoField(fieldNumber = 45, type = FieldType.SFIXED32_LIST_PACKED)
  public List<Integer> sfixed32PackedListField;

  @ProtoField(fieldNumber = 46, type = FieldType.SFIXED64_LIST_PACKED)
  public List<Long> sfixed64PackedListField;

  @ProtoField(fieldNumber = 47, type = FieldType.SINT32_LIST_PACKED)
  public List<Integer> sint32PackedListField;

  @ProtoField(fieldNumber = 48, type = FieldType.SINT64_LIST_PACKED)
  public List<Long> sint64PackedListField;

  public double getDoubleField() {
    return doubleField;
  }

  public void setDoubleField(double doubleField) {
    this.doubleField = doubleField;
  }

  public float getFloatField() {
    return floatField;
  }

  public void setFloatField(float floatField) {
    this.floatField = floatField;
  }

  public long getInt64Field() {
    return int64Field;
  }

  public void setInt64Field(long int64Field) {
    this.int64Field = int64Field;
  }

  public long getUint64Field() {
    return uint64Field;
  }

  public void setUint64Field(long uint64Field) {
    this.uint64Field = uint64Field;
  }

  public int getInt32Field() {
    return int32Field;
  }

  public void setInt32Field(int int32Field) {
    this.int32Field = int32Field;
  }

  public long getFixed64Field() {
    return fixed64Field;
  }

  public void setFixed64Field(long fixed64Field) {
    this.fixed64Field = fixed64Field;
  }

  public int getFixed32Field() {
    return fixed32Field;
  }

  public void setFixed32Field(int fixed32Field) {
    this.fixed32Field = fixed32Field;
  }

  public boolean isBoolField() {
    return boolField;
  }

  public void setBoolField(boolean boolField) {
    this.boolField = boolField;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public Object getMessageField() {
    return messageField;
  }

  public void setMessageField(InnerMessage messageField) {
    this.messageField = messageField;
  }

  public ByteString getBytesField() {
    return bytesField;
  }

  public void setBytesField(ByteString bytesField) {
    this.bytesField = bytesField;
  }

  public int getUint32Field() {
    return uint32Field;
  }

  public void setUint32Field(int uint32Field) {
    this.uint32Field = uint32Field;
  }

  public int getEnumField() {
    return enumField;
  }

  public void setEnumField(int enumField) {
    this.enumField = enumField;
  }

  public int getSfixed32Field() {
    return sfixed32Field;
  }

  public void setSfixed32Field(int sfixed32Field) {
    this.sfixed32Field = sfixed32Field;
  }

  public long getSfixed64Field() {
    return sfixed64Field;
  }

  public void setSfixed64Field(long sfixed64Field) {
    this.sfixed64Field = sfixed64Field;
  }

  public int getSint32Field() {
    return sint32Field;
  }

  public void setSint32Field(int sint32Field) {
    this.sint32Field = sint32Field;
  }

  public long getSint64Field() {
    return sint64Field;
  }

  public void setSint64Field(long sint64Field) {
    this.sint64Field = sint64Field;
  }

  public List<Double> getDoubleListField() {
    return doubleListField;
  }

  public void setDoubleListField(List<Double> doubleListField) {
    this.doubleListField = doubleListField;
  }

  public List<Float> getFloatListField() {
    return floatListField;
  }

  public void setFloatListField(List<Float> floatListField) {
    this.floatListField = floatListField;
  }

  public List<Long> getInt64ListField() {
    return int64ListField;
  }

  public void setInt64ListField(List<Long> int64ListField) {
    this.int64ListField = int64ListField;
  }

  public List<Long> getUint64ListField() {
    return uint64ListField;
  }

  public void setUint64ListField(List<Long> uint64ListField) {
    this.uint64ListField = uint64ListField;
  }

  public List<Integer> getInt32ListField() {
    return int32ListField;
  }

  public void setInt32ListField(List<Integer> int32ListField) {
    this.int32ListField = int32ListField;
  }

  public List<Long> getFixed64ListField() {
    return fixed64ListField;
  }

  public void setFixed64ListField(List<Long> fixed64ListField) {
    this.fixed64ListField = fixed64ListField;
  }

  public List<Integer> getFixed32ListField() {
    return fixed32ListField;
  }

  public void setFixed32ListField(List<Integer> fixed32ListField) {
    this.fixed32ListField = fixed32ListField;
  }

  public List<Boolean> getBoolListField() {
    return boolListField;
  }

  public void setBoolListField(List<Boolean> boolListField) {
    this.boolListField = boolListField;
  }

  public List<String> getStringListField() {
    return stringListField;
  }

  public void setStringListField(List<String> stringListField) {
    this.stringListField = stringListField;
  }

  public List<InnerMessage> getMessageListField() {
    return messageListField;
  }

  public void setMessageListField(List<InnerMessage> messageListField) {
    this.messageListField = messageListField;
  }

  public List<ByteString> getBytesListField() {
    return bytesListField;
  }

  public void setBytesListField(List<ByteString> bytesListField) {
    this.bytesListField = bytesListField;
  }

  public List<Integer> getUint32ListField() {
    return uint32ListField;
  }

  public void setUint32ListField(List<Integer> uint32ListField) {
    this.uint32ListField = uint32ListField;
  }

  public List<Integer> getEnumListField() {
    return enumListField;
  }

  public void setEnumListField(List<Integer> enumListField) {
    this.enumListField = enumListField;
  }

  public List<Integer> getSfixed32ListField() {
    return sfixed32ListField;
  }

  public void setSfixed32ListField(List<Integer> sfixed32ListField) {
    this.sfixed32ListField = sfixed32ListField;
  }

  public List<Long> getSfixed64ListField() {
    return sfixed64ListField;
  }

  public void setSfixed64ListField(List<Long> sfixed64ListField) {
    this.sfixed64ListField = sfixed64ListField;
  }

  public List<Integer> getSint32ListField() {
    return sint32ListField;
  }

  public void setSint32ListField(List<Integer> sint32ListField) {
    this.sint32ListField = sint32ListField;
  }

  public List<Long> getSint64ListField() {
    return sint64ListField;
  }

  public void setSint64ListField(List<Long> sint64ListField) {
    this.sint64ListField = sint64ListField;
  }

  public List<Double> getDoublePackedListField() {
    return doublePackedListField;
  }

  public void setDoublePackedListField(List<Double> doublePackedListField) {
    this.doublePackedListField = doublePackedListField;
  }

  public List<Float> getFloatPackedListField() {
    return floatPackedListField;
  }

  public void setFloatPackedListField(List<Float> floatPackedListField) {
    this.floatPackedListField = floatPackedListField;
  }

  public List<Long> getInt64PackedListField() {
    return int64PackedListField;
  }

  public void setInt64PackedListField(List<Long> int64PackedListField) {
    this.int64PackedListField = int64PackedListField;
  }

  public List<Long> getUint64PackedListField() {
    return uint64PackedListField;
  }

  public void setUint64PackedListField(List<Long> uint64PackedListField) {
    this.uint64PackedListField = uint64PackedListField;
  }

  public List<Integer> getInt32PackedListField() {
    return int32PackedListField;
  }

  public void setInt32PackedListField(List<Integer> int32PackedListField) {
    this.int32PackedListField = int32PackedListField;
  }

  public List<Long> getFixed64PackedListField() {
    return fixed64PackedListField;
  }

  public void setFixed64PackedListField(List<Long> fixed64PackedListField) {
    this.fixed64PackedListField = fixed64PackedListField;
  }

  public List<Integer> getFixed32PackedListField() {
    return fixed32PackedListField;
  }

  public void setFixed32PackedListField(List<Integer> fixed32PackedListField) {
    this.fixed32PackedListField = fixed32PackedListField;
  }

  public List<Boolean> getBoolPackedListField() {
    return boolPackedListField;
  }

  public void setBoolPackedListField(List<Boolean> boolPackedListField) {
    this.boolPackedListField = boolPackedListField;
  }

  public List<Integer> getUint32PackedListField() {
    return uint32PackedListField;
  }

  public void setUint32PackedListField(List<Integer> uint32PackedListField) {
    this.uint32PackedListField = uint32PackedListField;
  }

  public List<Integer> getEnumPackedListField() {
    return enumPackedListField;
  }

  public void setEnumPackedListField(List<Integer> enumPackedListField) {
    this.enumPackedListField = enumPackedListField;
  }

  public List<Integer> getSfixed32PackedListField() {
    return sfixed32PackedListField;
  }

  public void setSfixed32PackedListField(List<Integer> sfixed32PackedListField) {
    this.sfixed32PackedListField = sfixed32PackedListField;
  }

  public List<Long> getSfixed64PackedListField() {
    return sfixed64PackedListField;
  }

  public void setSfixed64PackedListField(List<Long> sfixed64PackedListField) {
    this.sfixed64PackedListField = sfixed64PackedListField;
  }

  public List<Integer> getSint32PackedListField() {
    return sint32PackedListField;
  }

  public void setSint32PackedListField(List<Integer> sint32PackedListField) {
    this.sint32PackedListField = sint32PackedListField;
  }

  public List<Long> getSint64PackedListField() {
    return sint64PackedListField;
  }

  public void setSint64PackedListField(List<Long> sint64PackedListField) {
    this.sint64PackedListField = sint64PackedListField;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (boolField ? 1231 : 1237);
    result = prime * result + ((boolListField == null) ? 0 : boolListField.hashCode());
    result = prime * result + ((boolPackedListField == null) ? 0 : boolPackedListField.hashCode());
    result = prime * result + ((bytesField == null) ? 0 : bytesField.hashCode());
    result = prime * result + ((bytesListField == null) ? 0 : bytesListField.hashCode());
    long temp;
    temp = Double.doubleToLongBits(doubleField);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((doubleListField == null) ? 0 : doubleListField.hashCode());
    result =
        prime * result + ((doublePackedListField == null) ? 0 : doublePackedListField.hashCode());
    result = prime * result + enumField;
    result = prime * result + ((enumListField == null) ? 0 : enumListField.hashCode());
    result = prime * result + ((enumPackedListField == null) ? 0 : enumPackedListField.hashCode());
    result = prime * result + fixed32Field;
    result = prime * result + ((fixed32ListField == null) ? 0 : fixed32ListField.hashCode());
    result =
        prime * result + ((fixed32PackedListField == null) ? 0 : fixed32PackedListField.hashCode());
    result = prime * result + (int) (fixed64Field ^ (fixed64Field >>> 32));
    result = prime * result + ((fixed64ListField == null) ? 0 : fixed64ListField.hashCode());
    result =
        prime * result + ((fixed64PackedListField == null) ? 0 : fixed64PackedListField.hashCode());
    result = prime * result + Float.floatToIntBits(floatField);
    result = prime * result + ((floatListField == null) ? 0 : floatListField.hashCode());
    result =
        prime * result + ((floatPackedListField == null) ? 0 : floatPackedListField.hashCode());
    result = prime * result + int32Field;
    result = prime * result + ((int32ListField == null) ? 0 : int32ListField.hashCode());
    result =
        prime * result + ((int32PackedListField == null) ? 0 : int32PackedListField.hashCode());
    result = prime * result + (int) (int64Field ^ (int64Field >>> 32));
    result = prime * result + ((int64ListField == null) ? 0 : int64ListField.hashCode());
    result =
        prime * result + ((int64PackedListField == null) ? 0 : int64PackedListField.hashCode());
    result = prime * result + ((messageField == null) ? 0 : messageField.hashCode());
    result = prime * result + ((messageListField == null) ? 0 : messageListField.hashCode());
    result = prime * result + sfixed32Field;
    result = prime * result + ((sfixed32ListField == null) ? 0 : sfixed32ListField.hashCode());
    result =
        prime * result
            + ((sfixed32PackedListField == null) ? 0 : sfixed32PackedListField.hashCode());
    result = prime * result + (int) (sfixed64Field ^ (sfixed64Field >>> 32));
    result = prime * result + ((sfixed64ListField == null) ? 0 : sfixed64ListField.hashCode());
    result =
        prime * result
            + ((sfixed64PackedListField == null) ? 0 : sfixed64PackedListField.hashCode());
    result = prime * result + sint32Field;
    result = prime * result + ((sint32ListField == null) ? 0 : sint32ListField.hashCode());
    result =
        prime * result + ((sint32PackedListField == null) ? 0 : sint32PackedListField.hashCode());
    result = prime * result + (int) (sint64Field ^ (sint64Field >>> 32));
    result = prime * result + ((sint64ListField == null) ? 0 : sint64ListField.hashCode());
    result =
        prime * result + ((sint64PackedListField == null) ? 0 : sint64PackedListField.hashCode());
    result = prime * result + ((stringField == null) ? 0 : stringField.hashCode());
    result = prime * result + ((stringListField == null) ? 0 : stringListField.hashCode());
    result = prime * result + uint32Field;
    result = prime * result + ((uint32ListField == null) ? 0 : uint32ListField.hashCode());
    result =
        prime * result + ((uint32PackedListField == null) ? 0 : uint32PackedListField.hashCode());
    result = prime * result + (int) (uint64Field ^ (uint64Field >>> 32));
    result = prime * result + ((uint64ListField == null) ? 0 : uint64ListField.hashCode());
    result =
        prime * result + ((uint64PackedListField == null) ? 0 : uint64PackedListField.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TestMessage other = (TestMessage) obj;
    if (boolField != other.boolField) {
      return false;
    }
    if (boolListField == null) {
      if (other.boolListField != null) {
        return false;
      }
    } else if (!boolListField.equals(other.boolListField)) {
      return false;
    }
    if (boolPackedListField == null) {
      if (other.boolPackedListField != null) {
        return false;
      }
    } else if (!boolPackedListField.equals(other.boolPackedListField)) {
      return false;
    }
    if (bytesField == null) {
      if (other.bytesField != null) {
        return false;
      }
    } else if (!bytesField.equals(other.bytesField)) {
      return false;
    }
    if (bytesListField == null) {
      if (other.bytesListField != null) {
        return false;
      }
    } else if (!bytesListField.equals(other.bytesListField)) {
      return false;
    }
    if (Double.doubleToLongBits(doubleField) != Double.doubleToLongBits(other.doubleField)) {
      return false;
    }
    if (doubleListField == null) {
      if (other.doubleListField != null) {
        return false;
      }
    } else if (!doubleListField.equals(other.doubleListField)) {
      return false;
    }
    if (doublePackedListField == null) {
      if (other.doublePackedListField != null) {
        return false;
      }
    } else if (!doublePackedListField.equals(other.doublePackedListField)) {
      return false;
    }
    if (enumField != other.enumField) {
      return false;
    }
    if (enumListField == null) {
      if (other.enumListField != null) {
        return false;
      }
    } else if (!enumListField.equals(other.enumListField)) {
      return false;
    }
    if (enumPackedListField == null) {
      if (other.enumPackedListField != null) {
        return false;
      }
    } else if (!enumPackedListField.equals(other.enumPackedListField)) {
      return false;
    }
    if (fixed32Field != other.fixed32Field) {
      return false;
    }
    if (fixed32ListField == null) {
      if (other.fixed32ListField != null) {
        return false;
      }
    } else if (!fixed32ListField.equals(other.fixed32ListField)) {
      return false;
    }
    if (fixed32PackedListField == null) {
      if (other.fixed32PackedListField != null) {
        return false;
      }
    } else if (!fixed32PackedListField.equals(other.fixed32PackedListField)) {
      return false;
    }
    if (fixed64Field != other.fixed64Field) {
      return false;
    }
    if (fixed64ListField == null) {
      if (other.fixed64ListField != null) {
        return false;
      }
    } else if (!fixed64ListField.equals(other.fixed64ListField)) {
      return false;
    }
    if (fixed64PackedListField == null) {
      if (other.fixed64PackedListField != null) {
        return false;
      }
    } else if (!fixed64PackedListField.equals(other.fixed64PackedListField)) {
      return false;
    }
    if (Float.floatToIntBits(floatField) != Float.floatToIntBits(other.floatField)) {
      return false;
    }
    if (floatListField == null) {
      if (other.floatListField != null) {
        return false;
      }
    } else if (!floatListField.equals(other.floatListField)) {
      return false;
    }
    if (floatPackedListField == null) {
      if (other.floatPackedListField != null) {
        return false;
      }
    } else if (!floatPackedListField.equals(other.floatPackedListField)) {
      return false;
    }
    if (int32Field != other.int32Field) {
      return false;
    }
    if (int32ListField == null) {
      if (other.int32ListField != null) {
        return false;
      }
    } else if (!int32ListField.equals(other.int32ListField)) {
      return false;
    }
    if (int32PackedListField == null) {
      if (other.int32PackedListField != null) {
        return false;
      }
    } else if (!int32PackedListField.equals(other.int32PackedListField)) {
      return false;
    }
    if (int64Field != other.int64Field) {
      return false;
    }
    if (int64ListField == null) {
      if (other.int64ListField != null) {
        return false;
      }
    } else if (!int64ListField.equals(other.int64ListField)) {
      return false;
    }
    if (int64PackedListField == null) {
      if (other.int64PackedListField != null) {
        return false;
      }
    } else if (!int64PackedListField.equals(other.int64PackedListField)) {
      return false;
    }
    if (messageField == null) {
      if (other.messageField != null) {
        return false;
      }
    } else if (!messageField.equals(other.messageField)) {
      return false;
    }
    if (messageListField == null) {
      if (other.messageListField != null) {
        return false;
      }
    } else if (!messageListField.equals(other.messageListField)) {
      return false;
    }
    if (sfixed32Field != other.sfixed32Field) {
      return false;
    }
    if (sfixed32ListField == null) {
      if (other.sfixed32ListField != null) {
        return false;
      }
    } else if (!sfixed32ListField.equals(other.sfixed32ListField)) {
      return false;
    }
    if (sfixed32PackedListField == null) {
      if (other.sfixed32PackedListField != null) {
        return false;
      }
    } else if (!sfixed32PackedListField.equals(other.sfixed32PackedListField)) {
      return false;
    }
    if (sfixed64Field != other.sfixed64Field) {
      return false;
    }
    if (sfixed64ListField == null) {
      if (other.sfixed64ListField != null) {
        return false;
      }
    } else if (!sfixed64ListField.equals(other.sfixed64ListField)) {
      return false;
    }
    if (sfixed64PackedListField == null) {
      if (other.sfixed64PackedListField != null) {
        return false;
      }
    } else if (!sfixed64PackedListField.equals(other.sfixed64PackedListField)) {
      return false;
    }
    if (sint32Field != other.sint32Field) {
      return false;
    }
    if (sint32ListField == null) {
      if (other.sint32ListField != null) {
        return false;
      }
    } else if (!sint32ListField.equals(other.sint32ListField)) {
      return false;
    }
    if (sint32PackedListField == null) {
      if (other.sint32PackedListField != null) {
        return false;
      }
    } else if (!sint32PackedListField.equals(other.sint32PackedListField)) {
      return false;
    }
    if (sint64Field != other.sint64Field) {
      return false;
    }
    if (sint64ListField == null) {
      if (other.sint64ListField != null) {
        return false;
      }
    } else if (!sint64ListField.equals(other.sint64ListField)) {
      return false;
    }
    if (sint64PackedListField == null) {
      if (other.sint64PackedListField != null) {
        return false;
      }
    } else if (!sint64PackedListField.equals(other.sint64PackedListField)) {
      return false;
    }
    if (stringField == null) {
      if (other.stringField != null) {
        return false;
      }
    } else if (!stringField.equals(other.stringField)) {
      return false;
    }
    if (stringListField == null) {
      if (other.stringListField != null) {
        return false;
      }
    } else if (!stringListField.equals(other.stringListField)) {
      return false;
    }
    if (uint32Field != other.uint32Field) {
      return false;
    }
    if (uint32ListField == null) {
      if (other.uint32ListField != null) {
        return false;
      }
    } else if (!uint32ListField.equals(other.uint32ListField)) {
      return false;
    }
    if (uint32PackedListField == null) {
      if (other.uint32PackedListField != null) {
        return false;
      }
    } else if (!uint32PackedListField.equals(other.uint32PackedListField)) {
      return false;
    }
    if (uint64Field != other.uint64Field) {
      return false;
    }
    if (uint64ListField == null) {
      if (other.uint64ListField != null) {
        return false;
      }
    } else if (!uint64ListField.equals(other.uint64ListField)) {
      return false;
    }
    if (uint64PackedListField == null) {
      if (other.uint64PackedListField != null) {
        return false;
      }
    } else if (!uint64PackedListField.equals(other.uint64PackedListField)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "TestMessage [doubleField="
        + doubleField
        + ", floatField="
        + floatField
        + ", int64Field="
        + int64Field
        + ", uint64Field="
        + uint64Field
        + ", int32Field="
        + int32Field
        + ", fixed64Field="
        + fixed64Field
        + ", fixed32Field="
        + fixed32Field
        + ", boolField="
        + boolField
        + ", stringField="
        + stringField
        + ", messageField="
        + messageField
        + ", bytesField="
        + bytesField
        + ", uint32Field="
        + uint32Field
        + ", enumField="
        + enumField
        + ", sfixed32Field="
        + sfixed32Field
        + ", sfixed64Field="
        + sfixed64Field
        + ", sint32Field="
        + sint32Field
        + ", sint64Field="
        + sint64Field
        + ", doubleListField="
        + doubleListField
        + ", floatListField="
        + floatListField
        + ", int64ListField="
        + int64ListField
        + ", uint64ListField="
        + uint64ListField
        + ", int32ListField="
        + int32ListField
        + ", fixed64ListField="
        + fixed64ListField
        + ", fixed32ListField="
        + fixed32ListField
        + ", boolListField="
        + boolListField
        + ", stringListField="
        + stringListField
        + ", messageListField="
        + messageListField
        + ", bytesListField="
        + bytesListField
        + ", uint32ListField="
        + uint32ListField
        + ", enumListField="
        + enumListField
        + ", sfixed32ListField="
        + sfixed32ListField
        + ", sfixed64ListField="
        + sfixed64ListField
        + ", sint32ListField="
        + sint32ListField
        + ", sint64ListField="
        + sint64ListField
        + ", doublePackedListField="
        + doublePackedListField
        + ", floatPackedListField="
        + floatPackedListField
        + ", int64PackedListField="
        + int64PackedListField
        + ", uint64PackedListField="
        + uint64PackedListField
        + ", int32PackedListField="
        + int32PackedListField
        + ", fixed64PackedListField="
        + fixed64PackedListField
        + ", fixed32PackedListField="
        + fixed32PackedListField
        + ", boolPackedListField="
        + boolPackedListField
        + ", uint32PackedListField="
        + uint32PackedListField
        + ", enumPackedListField="
        + enumPackedListField
        + ", sfixed32PackedListField="
        + sfixed32PackedListField
        + ", sfixed64PackedListField="
        + sfixed64PackedListField
        + ", sint32PackedListField="
        + sint32PackedListField
        + ", sint64PackedListField="
        + sint64PackedListField
        + "]";
  }
}
