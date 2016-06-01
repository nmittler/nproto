package com.google.protobuf.experimental.benchmark;

import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.example.HandwrittenSchemaFactory;
import com.google.protobuf.experimental.example.PojoMessage;
import com.google.protobuf.experimental.schema.AsmSchemaFactory;
import com.google.protobuf.experimental.schema.GenericSchemaFactory;
import com.google.protobuf.experimental.schema.InjectionClassLoadingStrategy;
import com.google.protobuf.experimental.schema.SchemaFactory;
import com.google.protobuf.experimental.util.TestUtil;

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
    GENERIC(new GenericSchemaFactory()),
    GENERIC_NO_ANNOTATIONS(new GenericSchemaFactory(TestUtil.PojoDescriptorFactory.getInstance())),
    ASM_INLINE(new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            AnnotationBeanDescriptorFactory.getInstance(),
            new BenchmarkSchemaNamingStrategy(PojoMessage.class.getName() + "InlineSchema"),
            false,
            false)) {
      @Override
      byte[] createSchema() {
        return ((AsmSchemaFactory) factory).createSchemaClass(PojoMessage.class);
      }
    },
    ASM_MINCODE(new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            AnnotationBeanDescriptorFactory.getInstance(),
            new BenchmarkSchemaNamingStrategy(PojoMessage.class.getName() + "MinCodeSchema"),
            true,
            false)) {
      @Override
      byte[] createSchema() {
        return ((AsmSchemaFactory) factory).createSchemaClass(PojoMessage.class);
      }
    },
    ASM_NOANNOTATIONS(new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            TestUtil.PojoDescriptorFactory.getInstance(),
            new BenchmarkSchemaNamingStrategy(PojoMessage.class.getName() + "NoAnnotationsSchema"),
            false,
            false)) {
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
