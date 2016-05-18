package io.nproto.descriptor;

import io.nproto.Internal;
import io.nproto.ProtoField;

import java.lang.reflect.Field;

@Internal
public final class PropertyDescriptor implements Comparable<PropertyDescriptor> {
  public final Field field;
  public final PropertyType type;
  public final int fieldNumber;

  PropertyDescriptor(Field field, ProtoField protoField) {
    this.field = field;
    this.type = PropertyType.forField(field, protoField);
    this.fieldNumber = protoField.number();
  }

  @Override
  public int compareTo(PropertyDescriptor o) {
    return fieldNumber - o.fieldNumber;
  }
}
