package io.nproto.schema.reflect;

import io.nproto.Internal;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;

@Internal
public final class UnsafeReflectiveSchema2Factory implements SchemaFactory {

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return UnsafeReflectiveSchema2.newInstance(messageType);
  }
}
