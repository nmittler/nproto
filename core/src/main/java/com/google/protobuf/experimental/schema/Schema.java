package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.Reader;
import com.google.protobuf.experimental.Writer;

public interface Schema<T> extends Iterable<Field> {
  void writeTo(T message, Writer writer);
  void mergeFrom(T message, Reader reader);
}
