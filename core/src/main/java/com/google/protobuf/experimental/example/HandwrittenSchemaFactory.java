package com.google.protobuf.experimental.example;

import com.google.protobuf.experimental.schema.Schema;
import com.google.protobuf.experimental.schema.SchemaFactory;

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
