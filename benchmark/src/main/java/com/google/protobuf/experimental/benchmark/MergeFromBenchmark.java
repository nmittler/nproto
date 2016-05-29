package com.google.protobuf.experimental.benchmark;

import com.google.protobuf.experimental.example.PojoMessage;
import com.google.protobuf.experimental.Reader;
import com.google.protobuf.experimental.schema.AsmSchemaFactory;
import com.google.protobuf.experimental.schema.GenericSchemaFactory;
import com.google.protobuf.experimental.example.HandwrittenSchemaFactory;
import com.google.protobuf.experimental.schema.Schema;
import com.google.protobuf.experimental.schema.SchemaFactory;
import com.google.protobuf.experimental.util.TestUtil;

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
    ASM(new AsmSchemaFactory());

    SchemaType(SchemaFactory factory) {
      this.factory = factory;
      schema = factory.createSchema(PojoMessage.class);
    }

    final void mergeFrom(PojoMessage message, Reader reader) {
      schema.mergeFrom(message, reader);
    }

    final SchemaFactory factory;
    final Schema<PojoMessage> schema;
  }

  @Param
  public SchemaType schemaType;

  private PojoMessage msg = TestUtil.newTestMessage();
  private TestUtil.PojoReader reader = new TestUtil.PojoReader(msg);

  @Benchmark
  public void mergeFrom() {
    schemaType.mergeFrom(new PojoMessage(), reader);
    reader.reset();
  }
}
