package io.nproto.schema.handwritten;


import io.nproto.PojoMessage;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;

public final class HandwrittenSchemaFactory implements SchemaFactory {
  @Override
  @SuppressWarnings("unchecked")
  public <T> Schema<T> createSchema(Class<T> messageType) {
    if (PojoMessage.class == messageType) {
      return (Schema<T>) new HandwrittenSchema();
    }
    throw new IllegalArgumentException("Unsupported class: " + messageType.getName());
  }
}
