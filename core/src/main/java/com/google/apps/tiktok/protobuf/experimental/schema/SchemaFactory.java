package com.google.apps.tiktok.protobuf.experimental.schema;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;

/**
 * A factory that manufactures {@link Schema} instances for protobuf messages.
 */
@InternalApi
public interface SchemaFactory {
  /**
   * Creates a schema instance for the given protobuf message type.
   */
  <T> Schema<T> createSchema(Class<T> messageType);
}
