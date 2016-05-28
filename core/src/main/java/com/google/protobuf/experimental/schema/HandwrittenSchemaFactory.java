package com.google.protobuf.experimental.schema;


import com.google.protobuf.experimental.PojoMessage;

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
