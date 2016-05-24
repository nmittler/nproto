package io.nproto.schema;


import io.nproto.PojoMessage;

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
