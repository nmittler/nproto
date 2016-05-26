package com.google.protobuf.experimental.benchmark;

import com.google.protobuf.experimental.PojoMessage;
import com.google.protobuf.experimental.schema.AndroidGenericSchemaFactory;
import com.google.protobuf.experimental.schema.AsmSchemaFactory;
import com.google.protobuf.experimental.schema.GenericSchemaFactory;
import com.google.protobuf.experimental.schema.HandwrittenSchemaFactory;
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
    ANDROID(new AndroidGenericSchemaFactory()),
    ANDROID_NO_ANNOTATIONS(new AndroidGenericSchemaFactory(TestUtil.PojoDescriptorFactory.getInstance())),
    ASM(new AsmSchemaFactory()) {
      @Override
      byte[] createSchema() {
        return ((AsmSchemaFactory) factory).createSchemaClass(PojoMessage.class);
      }
    },
    ASM_NO_ANNOTATIONS(new AsmSchemaFactory(TestUtil.PojoDescriptorFactory.getInstance())) {
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
