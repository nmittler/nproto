package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.descriptor.PropertyType;
import com.google.protobuf.experimental.ByteString;

import java.util.List;

public interface Field {
  int number();
  PropertyType type();
  int intValue(Object message);
  <E extends Enum<E>> Enum<E> enumValue(Object message, Class<E> clazz);
  long longValue(Object message);
  double doubleValue(Object message);
  float floatValue(Object message);
  Object messageValue(Object message);
  String stringValue(Object message);
  ByteString bytesValue(Object message);
  <L> List<L> values(Object message, Class<? extends List<L>> clazz);
}
