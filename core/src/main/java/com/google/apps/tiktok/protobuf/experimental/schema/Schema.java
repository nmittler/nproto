package com.google.apps.tiktok.protobuf.experimental.schema;

/**
 * A runtime schema for a single protobuf message. A schema provides operations on message
 * instances such as serialization/deserialization.
 */
public interface Schema<T> {
  /**
   * Writes the given message to the target {@link Writer}.
   */
  void writeTo(T message, Writer writer);

  /**
   * Reads fields from the given {@link Reader} and merges them into the message.
   */
  void mergeFrom(T message, Reader reader);
}
