package io.nproto.schema;

import io.nproto.ByteString;
import io.nproto.FieldType;

import java.util.List;

public interface Field {
  int number();
  FieldType type();
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
