package io.nproto.schema;

import io.nproto.PojoMessage;
import io.nproto.schema.gen.AsmSchemaFactory;
import io.nproto.schema.reflect.UnsafeReflectiveSchemaFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
@Fork(1)
public class SchemaCreationBenchmark {
  public enum SchemaType {
    REFLECTIVE {
      private final UnsafeReflectiveSchemaFactory factory = new UnsafeReflectiveSchemaFactory();

      @Override
      Schema<?> createSchema() {
        return factory.createSchema(PojoMessage.class);
      }
    },
    ASM {
      @Override
      byte[] createSchema() {
        return AsmSchemaFactory.createSchemaClass(PojoMessage.class);
      }
    };

    abstract Object createSchema();
  };

  @Param
  private SchemaType schemaType;

  @Benchmark
  public Object create() {
    return schemaType.createSchema();
  }
}
