package com.google.apps.tiktok.protobuf.experimental.testing;

import com.google.apps.tiktok.protobuf.experimental.schema.Schema;
import com.google.apps.tiktok.protobuf.experimental.schema.SchemaFactory;

/**
 * A factory for {@link HandwrittenSchema}.
 */
public final class HandwrittenSchemaFactory implements SchemaFactory {
  @Override
  @SuppressWarnings("unchecked")
  public <T> Schema<T> createSchema(Class<T> messageType) {
    if (TestMessage.class == messageType) {
      return (Schema<T>) new HandwrittenSchema();
    }
    throw new IllegalArgumentException("Unsupported class: " + messageType.getName());
  }
}
