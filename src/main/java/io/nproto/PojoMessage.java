package io.nproto;

import io.nproto.WireFormat.FieldType;

import java.util.List;

public class PojoMessage {
  @ProtoField(number = 1, type = FieldType.ENUM)
  public int enumField;

  @ProtoField(number = 2, type = FieldType.BOOL)
  public boolean boolField;

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

  @ProtoField(number = 16, type = FieldType.ENUM, repeated = true, packed = true)
  public List<Integer> enumListField;

  @ProtoField(number = 17, type = FieldType.BOOL, repeated = true, packed = true)
  public List<Boolean> boolListField;

  @ProtoField(number = 18, type = FieldType.UINT32, repeated = true, packed = true)
  public List<Integer> uint32ListField;

  @ProtoField(number = 19, type = FieldType.INT32, repeated = true, packed = true)
  public List<Integer> int32ListField;

  @ProtoField(number = 20, type = FieldType.SINT32, repeated = true, packed = true)
  public List<Integer> sInt32ListField;

  @ProtoField(number = 21, type = FieldType.FIXED32, repeated = true, packed = true)
  public List<Integer> fixedInt32ListField;

  @ProtoField(number = 22, type = FieldType.SFIXED32, repeated = true, packed = true)
  public List<Integer> sFixedInt32ListField;

  @ProtoField(number = 23, type = FieldType.UINT64, repeated = true, packed = true)
  public List<Long> uint64ListField;

  @ProtoField(number = 24, type = FieldType.INT64, repeated = true, packed = true)
  public List<Long> int64ListField;

  @ProtoField(number = 25, type = FieldType.SINT64, repeated = true, packed = true)
  public List<Long> sInt64ListField;

  @ProtoField(number = 26, type = FieldType.FIXED64, repeated = true, packed = true)
  public List<Long> fixedInt64ListField;

  @ProtoField(number = 27, type = FieldType.SFIXED64, repeated = true, packed = true)
  public List<Long> sFixedInt64ListField;

  @ProtoField(number = 28, type = FieldType.STRING, repeated = true, packed = false)
  public List<String> stringListField;

  @ProtoField(number = 29, type = FieldType.BYTES, repeated = true, packed = false)
  public List<ByteString> bytesListField;

  @ProtoField(number = 30, type = FieldType.MESSAGE, repeated = true, packed = false)
  public List<Object> messageListField;
}
