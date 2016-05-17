package io.nproto.schema;

import io.nproto.ByteString;
import io.nproto.PojoMessage;
import io.nproto.Reader;
import io.nproto.Writer;
import io.nproto.schema.gen.AsmSchemaFactory;
import io.nproto.schema.handwritten.HandwrittenSchemaFactory;
import io.nproto.schema.reflect.UnsafeReflectiveSchemaFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;

@State(Scope.Benchmark)
@Fork(1)
public class SchemaBenchmark {
  public enum SchemaType {
    HANDWRITTEN(new HandwrittenSchemaFactory()),
    REFLECTIVE(new UnsafeReflectiveSchemaFactory()),
    ASM(new AsmSchemaFactory()) {
      @Override
      byte[] createSchema() {
        return AsmSchemaFactory.createSchemaClass(PojoMessage.class);
      }
    };

    SchemaType(SchemaFactory factory) {
      this.factory = factory;
      schema = factory.createSchema(PojoMessage.class);
    }

    final void mergeFrom(PojoMessage message, Reader reader) {
      schema.mergeFrom(message, reader);
    }

    final void writeTo(PojoMessage message, Writer writer) {
      schema.writeTo(message, writer);
    }

    Object createSchema() {
      return factory.createSchema(PojoMessage.class);
    }

    private final SchemaFactory factory;
    private final Schema<PojoMessage> schema;
  }

  @Param
  private SchemaType schemaType;

  private PojoMessage msg = TestUtil.newTestMessage();
  private TestWriter writer = new TestWriter();
  private TestUtil.PojoReader reader = new TestUtil.PojoReader(msg);

  @Benchmark
  public Object create() {
    return schemaType.createSchema();
  }

  @Benchmark
  public void mergeFrom() {
    schemaType.mergeFrom(new PojoMessage(), reader);
    reader.reset();
  }

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
    public <E extends Enum<E>> void writeEnum(int fieldNumber, E value) {
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
    public void writeInt32List(int fieldNumber, boolean packed, List<Integer> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFixed32List(int fieldNumber, boolean packed, List<Integer> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeInt64List(int fieldNumber, boolean packed, List<Long> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeUInt64List(int fieldNumber, boolean packed, List<Long> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFixed64List(int fieldNumber, boolean packed, List<Long> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeFloatList(int fieldNumber, boolean packed, List<Float> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeDoubleList(int fieldNumber, boolean packed, List<Double> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public <E extends Enum<E>> void writeEnumList(int fieldNumber, boolean packed, List<E> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeBoolList(int fieldNumber, boolean packed, List<Boolean> value) {
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
    public void writeUInt32List(int fieldNumber, boolean packed, List<Integer> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSFixed32List(int fieldNumber, boolean packed, List<Integer> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSFixed64List(int fieldNumber, boolean packed, List<Long> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSInt32List(int fieldNumber, boolean packed, List<Integer> value) {
      bh.consume(fieldNumber);
      bh.consume(value);
    }

    @Override
    public void writeSInt64List(int fieldNumber, boolean packed, List<Long> value) {
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