package com.google.apps.tiktok.protobuf.experimental.schema.asm;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;

/**
 * A strategy for naming generated schema classes.
 */
@InternalApi
public interface SchemaNamingStrategy {
  /**
   * Gets the name to be used for the schema for the given message class. This method must always
   * return the same string for a given message class.
   */
  String schemaNameFor(Class<?> messageClass);
}
