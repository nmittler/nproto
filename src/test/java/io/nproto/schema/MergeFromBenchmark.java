package io.nproto.schema;

import io.nproto.PojoMessage;
import io.nproto.Reader;
import io.nproto.schema.TestUtil.PojoReader;
import io.nproto.schema.gen.AsmSchemaFactory;
import io.nproto.schema.handwritten.HandwrittenSchemaFactory;
import io.nproto.schema.reflect.UnsafeReflectiveSchemaFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
@Fork(1)
public class MergeFromBenchmark {

  public enum SchemaType {
    HANDWRITTEN(new HandwrittenSchemaFactory()),
    REFLECTIVE(new UnsafeReflectiveSchemaFactory()),
    ASM(new AsmSchemaFactory());

    SchemaType(SchemaFactory factory) {
      schema = factory.createSchema(PojoMessage.class);
    }

    void mergeFrom(PojoMessage message, Reader reader) {
      schema.mergeFrom(message, reader);
    }

    private final Schema<PojoMessage> schema;
  };

  @Param
  private SchemaType schemaType;

  private PojoReader reader;

  @Setup
  public void setup() {
    reader = new PojoReader(TestUtil.newTestMessage());
  }

  @Benchmark
  public void mergeFrom() {
    schemaType.mergeFrom(new PojoMessage(), reader);
    reader.reset();
  }
}
