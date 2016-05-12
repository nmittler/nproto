package io.nproto.schema;

import io.nproto.ByteString;
import io.nproto.PojoMessage;
import io.nproto.Writer;
import io.nproto.schema.asm.AsmSchemaFactory;
import io.nproto.schema.reflect.ReflectiveSchemaFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Arrays;
import java.util.List;

@State(Scope.Benchmark)
@Fork(1)
public class WriteToBenchmark {
  public enum SchemaType {
    REFLECTIVE(new ReflectiveSchemaFactory()),
    ASM(new AsmSchemaFactory());

    SchemaType(SchemaFactory factory) {
      schema = factory.createSchema(PojoMessage.class);
    }

    void writeTo(PojoMessage message, Writer writer) {
      schema.writeTo(message, writer);
    }

    private final Schema<PojoMessage> schema;
  };

  @Param
  private SchemaType schemaType;

  private PojoMessage msg;
  private Writer writer;

  @Setup
  public void setup() {
    writer = new TestWriter();
    msg = new PojoMessage();
    msg.uint32Field = 1;
    msg.int32Field = 2;
    msg.fixedInt32Field = 3;
    msg.sInt32Field = 4;
    msg.sFixedInt32Field = 5;

    msg.uint64Field = 5;
    msg.int64Field = 6;
    msg.fixedInt64Field = 7;
    msg.sInt64Field = 8;
    msg.sFixedInt64Field = 9;

    msg.stringField = "hello world";
    msg.bytesField = ByteString.copyFromUtf8("here are some bytes");
    msg.messageField = new Object();

    msg.uint32ListField = Arrays.asList(1, 2);
    msg.int32ListField = Arrays.asList(3, 4);
    msg.fixedInt32ListField = Arrays.asList(5, 6);
    msg.sInt32ListField = Arrays.asList(7, 8);
    msg.sFixedInt32ListField = Arrays.asList(9, 10);

    msg.uint64ListField = Arrays.asList(1L, 2L);
    msg.int64ListField = Arrays.asList(3L, 4L);
    msg.fixedInt64ListField = Arrays.asList(5L, 6L);
    msg.sInt64ListField = Arrays.asList(7L, 8L);
    msg.sFixedInt64ListField = Arrays.asList(9L, 10L);

    msg.stringListField = Arrays.asList("ab", "cd");
    msg.bytesListField = Arrays.asList(ByteString.copyFromUtf8("ab"), ByteString.copyFromUtf8("cd"));
    msg.messageListField = Arrays.asList(new Object(), new Object());
  }

  @Benchmark
  public void writeTo() {
    schemaType.writeTo(msg, writer);
  }

  private static final class TestWriter implements Writer {

    @Override
    public void writeSFixed32(int fieldNumber, int value) {

    }

    @Override
    public void writeInt64(int fieldNumber, long value) {

    }

    @Override
    public void writeSFixed64(int fieldNumber, long value) {

    }

    @Override
    public void writeFloat(int fieldNumber, float value) {

    }

    @Override
    public void writeDouble(int fieldNumber, double value) {

    }

    @Override
    public void writeEnum(int fieldNumber, int value) {

    }

    @Override
    public void writeUInt64(int fieldNumber, long value) {

    }

    @Override
    public void writeInt32(int fieldNumber, int value) {

    }

    @Override
    public void writeFixed64(int fieldNumber, long value) {

    }

    @Override
    public void writeFixed32(int fieldNumber, int value) {

    }

    @Override
    public void writeBool(int fieldNumber, boolean value) {

    }

    @Override
    public void writeString(int fieldNumber, String value) {

    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value) {

    }

    @Override
    public void writeUInt32(int fieldNumber, int value) {

    }

    @Override
    public void writeSInt32(int fieldNumber, int value) {

    }

    @Override
    public void writeSInt64(int fieldNumber, long value) {

    }

    @Override
    public void writeMessage(int fieldNumber, Object value) {

    }

    @Override
    public void writeInt32List(int fieldNumber, boolean packed, List<Integer> list) {

    }

    @Override
    public void writeFixed32List(int fieldNumber, boolean packed, List<Integer> list) {

    }

    @Override
    public void writeInt64List(int fieldNumber, boolean packed, List<Long> list) {

    }

    @Override
    public void writeUInt64List(int fieldNumber, boolean packed, List<Long> list) {

    }

    @Override
    public void writeFixed64List(int fieldNumber, boolean packed, List<Long> list) {

    }

    @Override
    public void writeFloatList(int fieldNumber, boolean packed, List<Float> list) {

    }

    @Override
    public void writeDoubleList(int fieldNumber, boolean packed, List<Double> list) {

    }

    @Override
    public void writeEnumList(int fieldNumber, boolean packed, List<Integer> list) {

    }

    @Override
    public void writeBoolList(int fieldNumber, boolean packed, List<Boolean> list) {

    }

    @Override
    public void writeStringList(int fieldNumber, List<String> list) {

    }

    @Override
    public void writeBytesList(int fieldNumber, List<ByteString> list) {

    }

    @Override
    public void writeUInt32List(int fieldNumber, boolean packed, List<Integer> list) {

    }

    @Override
    public void writeSFixed32List(int fieldNumber, boolean packed, List<Integer> list) {

    }

    @Override
    public void writeSFixed64List(int fieldNumber, boolean packed, List<Long> list) {

    }

    @Override
    public void writeSInt32List(int fieldNumber, boolean packed, List<Integer> list) {

    }

    @Override
    public void writeSInt64List(int fieldNumber, boolean packed, List<Long> list) {

    }

    @Override
    public void writeMessageList(int fieldNumber, List<?> list) {

    }
  }
}
