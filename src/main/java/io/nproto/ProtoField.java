package io.nproto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ProtoField {
  int number();
  WireFormat.FieldType type();
  boolean repeated() default false;
  boolean packed() default true;
}
