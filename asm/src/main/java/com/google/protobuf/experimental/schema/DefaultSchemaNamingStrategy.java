package com.google.protobuf.experimental.schema;

public class DefaultSchemaNamingStrategy implements SchemaNamingStrategy {
  private final static DefaultSchemaNamingStrategy INSTANCE = new DefaultSchemaNamingStrategy();

  public static DefaultSchemaNamingStrategy getInstance() {
    return INSTANCE;
  }

  @Override
  public String schemaNameFor(Class<?> messageClass) {
    return messageClass.getName() + "Schema";
  }
}
