package com.google.apps.tiktok.protobuf.experimental.benchmark;

import com.google.apps.tiktok.protobuf.experimental.ByteString;
import com.google.apps.tiktok.protobuf.experimental.descriptor.AnnotationMessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.GenericSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.Schema;
import com.google.apps.tiktok.protobuf.experimental.schema.SchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.Writer;
import com.google.apps.tiktok.protobuf.experimental.schema.asm.AsmSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.asm.InjectionClassLoadingStrategy;
import com.google.apps.tiktok.protobuf.experimental.testing.HandwrittenSchemaFactory;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessage;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessageFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;

@State(Scope.Benchmark)
@Fork(1)
public class WriteToBenchmark {
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

    final void writeTo(TestMessage message, Writer writer) {
      schema.writeTo(message, writer);
    }

    final SchemaFactory factory;
    final Schema<TestMessage> schema;
  }

  @Param public SchemaType schemaType;

  private TestMessage msg = TestMessageFactory.newTestMessage();
  private TestWriter writer = new TestWriter();

  @Benchmark
  public void writeTo(Blackhole bh) {
    writer.bh = bh;
    schemaType.writeTo(msg, writer);
  }

  private static final class TestWriter implements Writer {
    Blackhole bh;

    @Override
    public void writeSFixed32(int fieldNumber, int value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeInt64(int fieldNumber, long value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFloat(int fieldNumber, float value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeDouble(int fieldNumber, double value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeEnum(int fieldNumber, int value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeInt32(int fieldNumber, int value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeBool(int fieldNumber, boolean value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeString(int fieldNumber, String value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeUInt32(int fieldNumber, int value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeMessage(int fieldNumber, Object value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeInt32List(int fieldNumber, List<Integer> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFixed32List(int fieldNumber, List<Integer> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeInt64List(int fieldNumber, List<Long> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeUInt64List(int fieldNumber, List<Long> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFixed64List(int fieldNumber, List<Long> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFloatList(int fieldNumber, List<Float> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeDoubleList(int fieldNumber, List<Double> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeEnumList(int fieldNumber, List<Integer> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeBoolList(int fieldNumber, List<Boolean> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeStringList(int fieldNumber, List<String> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeBytesList(int fieldNumber, List<ByteString> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeUInt32List(int fieldNumber, List<Integer> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSFixed32List(int fieldNumber, List<Integer> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSFixed64List(int fieldNumber, List<Long> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSInt32List(int fieldNumber, List<Integer> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSInt64List(int fieldNumber, List<Long> value, boolean packed) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeMessageList(int fieldNumber, List<?> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }
  }
}
