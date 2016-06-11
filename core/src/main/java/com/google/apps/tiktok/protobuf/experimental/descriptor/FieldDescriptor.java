package com.google.apps.tiktok.protobuf.experimental.descriptor;

import com.google.apps.tiktok.protobuf.experimental.FieldType;

import java.lang.reflect.Field;

/**
 * The descriptor for a single field in a protobuf message class.
 */
public final class FieldDescriptor implements Comparable<FieldDescriptor> {
  private final Field field;
  private final FieldType type;
  private final int fieldNumber;

  /**
   * Constructs a descriptor
   *
   * @param field the field from the protobuf message.
   * @param fieldNumber the field number for the field.
   * @param type the field type information.
   * @throws IllegalArgumentException if the {@code type} cannot be applied to the {@code field}.
   */
  public FieldDescriptor(Field field, int fieldNumber, FieldType type) {
    if (field == null) {
      throw new NullPointerException("field");
    }
    if (type == null) {
      throw new NullPointerException("type");
    }
    if (!type.isValidForField(field)) {
      throw new IllegalArgumentException(
          String.format(
              "Field type %s cannot be applied to %s ", type.name(), field.getType().getName()));
    }

    this.field = field;
    this.type = type;
    this.fieldNumber = fieldNumber;
  }

  /**
   * Gets the subject {@link Field} of this descriptor.
   */
  public Field getField() {
    return field;
  }

  /**
   * Gets the type information for the field.
   */
  public FieldType getType() {
    return type;
  }

  /**
   * Gets the field number for the field.
   */
  public int getFieldNumber() {
    return fieldNumber;
  }

  @Override
  public int compareTo(FieldDescriptor o) {
    return fieldNumber - o.fieldNumber;
  }
}
