package io.nproto.benchmark;

import io.nproto.PojoMessage;
import io.nproto.schema.SchemaFactory;
import io.nproto.schema.gen.AsmSchemaFactory;
import io.nproto.schema.handwritten.HandwrittenSchemaFactory;
import io.nproto.schema.reflect.AndroidUnsafeReflectiveSchemaFactory;
import io.nproto.schema.reflect.UnsafeReflectiveSchemaFactory;
import io.nproto.util.TestUtil.PojoDescriptorFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
@Fork(1)
public class CreateSchemaBenchmark {
  public enum SchemaType {
    HANDWRITTEN(new HandwrittenSchemaFactory()),
    REFLECTIVE(new UnsafeReflectiveSchemaFactory()),
    REFLECTIVE_NO_ANNOTATIONS(new UnsafeReflectiveSchemaFactory(PojoDescriptorFactory.getInstance())),
    ANDROID(new AndroidUnsafeReflectiveSchemaFactory()),
    ANDROID_NO_ANNOTATIONS(new AndroidUnsafeReflectiveSchemaFactory(PojoDescriptorFactory.getInstance())),
    ASM(new AsmSchemaFactory()) {
      @Override
      byte[] createSchema() {
        return ((AsmSchemaFactory) factory).createSchemaClass(PojoMessage.class);
      }
    },
    ASM_NO_ANNOTATIONS(new AsmSchemaFactory(PojoDescriptorFactory.getInstance())) {
      @Override
      byte[] createSchema() {
        return ((AsmSchemaFactory) factory).createSchemaClass(PojoMessage.class);
      }
    };

    SchemaType(SchemaFactory factory) {
      this.factory = factory;
    }

    Object createSchema() {
      return factory.createSchema(PojoMessage.class);
    }

    final SchemaFactory factory;
  }

  @Param
  public SchemaType schemaType;

  @Benchmark
  public Object create() {
    return schemaType.createSchema();
  }
}
