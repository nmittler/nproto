package io.nproto.schema;

import io.nproto.Writer;

public interface Schema<T> extends Iterable<Field> {
  void writeTo(T message, Writer writer);
}
