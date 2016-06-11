package com.google.apps.tiktok.protobuf.experimental;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to identify a field in a protobuf message class that is to be included in
 * serialization/deserialization.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ProtoField {
  /**
   * Gets the number of the field as indicated in the proto file.
   */
  int fieldNumber();

  /**
   * Gets the property type for this field.
   */
  FieldType type();
}
