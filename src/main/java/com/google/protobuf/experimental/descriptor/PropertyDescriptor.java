package com.google.protobuf.experimental.descriptor;

import com.google.protobuf.experimental.Internal;
import com.google.protobuf.experimental.ProtoField;
import com.google.protobuf.experimental.WireFormat;

import java.lang.reflect.Field;

@Internal
public final class PropertyDescriptor implements Comparable<PropertyDescriptor> {
  public final Field field;
  public final PropertyType type;
  public final int fieldNumber;

  public PropertyDescriptor(Field field, ProtoField protoField) {
    this( field, protoField.number(), protoField.type());
  }

  public PropertyDescriptor(Field field, int fieldNumber, WireFormat.FieldType fieldType) {
    this( field, fieldNumber, PropertyType.forField(field, fieldType));
  }

  public PropertyDescriptor(Field field, int fieldNumber, PropertyType type) {
    if (field == null) {
      throw new NullPointerException("field");
    }
    if (type == null) {
      throw new NullPointerException("type");
    }
    this.field = field;
    this.type = type;
    this.fieldNumber = fieldNumber;
  }

  @Override
  public int compareTo(PropertyDescriptor o) {
    return fieldNumber - o.fieldNumber;
  }
}
