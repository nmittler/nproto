package com.google.protobuf.experimental.benchmark;

import com.google.protobuf.experimental.schema.SchemaNamingStrategy;

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
