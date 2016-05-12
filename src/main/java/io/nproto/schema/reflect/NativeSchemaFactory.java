package io.nproto.schema.reflect;

import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;

public class NativeSchemaFactory implements SchemaFactory {

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return NativeSchema.newInstance(messageType);
  }
}
