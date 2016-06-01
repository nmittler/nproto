package com.google.protobuf.experimental.schema;

public interface SchemaNamingStrategy {
  /**
   * Gets the name to be used for the schema for the given message class. This method must always
   * return the same string for a given message class.
   */
  String schemaNameFor(Class<?> messageClass);
}
