package com.google.apps.tiktok.protobuf.experimental.benchmark;

import com.google.apps.tiktok.protobuf.experimental.descriptor.AnnotationMessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.GenericSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.Reader;
import com.google.apps.tiktok.protobuf.experimental.schema.Schema;
import com.google.apps.tiktok.protobuf.experimental.schema.SchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.asm.AsmSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.asm.InjectionClassLoadingStrategy;
import com.google.apps.tiktok.protobuf.experimental.testing.HandwrittenSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessage;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessageFactory;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessageReader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
@Fork(1)
public class MergeFromBenchmark {
  public enum SchemaType {
    HANDWRITTEN(new HandwrittenSchemaFactory()),
    GENERIC(new GenericSchemaFactory()),
    ASM_INLINE_SAFE(
        new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            AnnotationMessageDescriptorFactory.getValidatingInstance(),
            new BenchmarkSchemaNamingStrategy(TestMessage.class.getName() + "InlineSafeSchema"),
            false,
            false)),
    ASM_INLINE_UNSAFE(
        new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            AnnotationMessageDescriptorFactory.getValidatingInstance(),
            new BenchmarkSchemaNamingStrategy(TestMessage.class.getName() + "InlineUnsafeSchema"),
            false,
            true)),
    ASM_MINCODE_SAFE(
        new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            AnnotationMessageDescriptorFactory.getValidatingInstance(),
            new BenchmarkSchemaNamingStrategy(TestMessage.class.getName() + "MinCodeSafeSchema"),
            true,
            false)),
    ASM_MINCODE_UNSAFE(
        new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            AnnotationMessageDescriptorFactory.getValidatingInstance(),
            new BenchmarkSchemaNamingStrategy(TestMessage.class.getName() + "MinCodeUnsafeSchema"),
            true,
            true));

    SchemaType(SchemaFactory factory) {
      this.factory = factory;
      schema = factory.createSchema(TestMessage.class);
    }

    final void mergeFrom(TestMessage message, Reader reader) {
      schema.mergeFrom(message, reader);
    }

    final SchemaFactory factory;
    final Schema<TestMessage> schema;
  }

  @Param public SchemaType schemaType;

  private TestMessage msg = TestMessageFactory.newTestMessage();
  private TestMessageReader reader = new TestMessageReader(msg);

  @Benchmark
  public void mergeFrom() {
    schemaType.mergeFrom(new TestMessage(), reader);
    reader.reset();
  }
}
