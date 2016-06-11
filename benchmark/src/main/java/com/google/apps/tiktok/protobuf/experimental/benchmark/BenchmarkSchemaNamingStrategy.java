package com.google.apps.tiktok.protobuf.experimental.benchmark;

import com.google.apps.tiktok.protobuf.experimental.schema.asm.SchemaNamingStrategy;

/**
 * A simple {@link SchemaNamingStrategy} that always returns the same hard-coded name for any
 * message type.
 */
class BenchmarkSchemaNamingStrategy implements SchemaNamingStrategy {
  private final String name;

  BenchmarkSchemaNamingStrategy(String name) {
    this.name = name;
  }

  @Override
  public String schemaNameFor(Class<?> messageClass) {
    return name;
  }
}
