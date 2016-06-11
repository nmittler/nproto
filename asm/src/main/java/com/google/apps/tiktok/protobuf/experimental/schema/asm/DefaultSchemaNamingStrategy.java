package com.google.apps.tiktok.protobuf.experimental.schema.asm;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;

/**
 * Default naming strategy for schemas. Just appends "Schema" to the message class name.
 */
@InternalApi
public final class DefaultSchemaNamingStrategy implements SchemaNamingStrategy {
  private static final DefaultSchemaNamingStrategy INSTANCE = new DefaultSchemaNamingStrategy();

  public static DefaultSchemaNamingStrategy getInstance() {
    return INSTANCE;
  }

  @Override
  public String schemaNameFor(Class<?> messageClass) {
    return messageClass.getName() + "Schema";
  }
}
