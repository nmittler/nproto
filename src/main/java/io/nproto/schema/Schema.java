package io.nproto.schema;

import io.nproto.Writer;

public interface Schema<T> {
  void writeTo(T message, Writer writer);
}
