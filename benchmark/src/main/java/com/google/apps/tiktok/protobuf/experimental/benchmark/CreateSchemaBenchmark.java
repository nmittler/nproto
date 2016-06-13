package com.google.apps.tiktok.protobuf.experimental.benchmark;

import com.google.apps.tiktok.protobuf.experimental.descriptor.AnnotationMessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.GenericSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.SchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.asm.AsmSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.asm.InjectionClassLoadingStrategy;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessage;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessageDescriptorFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
@Fork(1)
public class CreateSchemaBenchmark {
  public enum SchemaType {
    GENERIC_VALIDATION(new GenericSchemaFactory(AnnotationMessageDescriptorFactory.getValidatingInstance())),
    GENERIC_NO_VALIDATION(new GenericSchemaFactory(AnnotationMessageDescriptorFactory.getNonValidatingInstance())),
    GENERIC_MANUAL(new GenericSchemaFactory(TestMessageDescriptorFactory.getInstance())),
    ASM_INLINE(
            new AsmSchemaFactory(
                    new InjectionClassLoadingStrategy(),
                    AnnotationMessageDescriptorFactory.getNonValidatingInstance(),
                    new BenchmarkSchemaNamingStrategy(TestMessage.class.getName() + "InlineSchema"),
                    false,
                    false)) {
      @Override
      byte[] createSchema() {
        return ((AsmSchemaFactory) factory).createSchemaClass(TestMessage.class);
      }
    },
    ASM_MINCODE(
            new AsmSchemaFactory(
                    new InjectionClassLoadingStrategy(),
                    AnnotationMessageDescriptorFactory.getNonValidatingInstance(),
                    new BenchmarkSchemaNamingStrategy(TestMessage.class.getName() + "MinCodeSchema"),
                    true,
                    false)) {
      @Override
      byte[] createSchema() {
        return ((AsmSchemaFactory) factory).createSchemaClass(TestMessage.class);
      }
    };

    SchemaType(SchemaFactory factory) {
      this.factory = factory;
    }

    Object createSchema() {
      return factory.createSchema(TestMessage.class);
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
