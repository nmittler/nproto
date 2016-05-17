package io.nproto.schema.reflect;

import io.nproto.PojoMessage;
import io.nproto.schema.AbstractSchemaFactoryTest;
import io.nproto.schema.Schema;

public class UnsafeReflectiveSchemaFactoryTest extends AbstractSchemaFactoryTest {
  private static final Schema<PojoMessage> SCHEMA =
          new UnsafeReflectiveSchemaFactory().createSchema(PojoMessage.class);

  @Override
  protected Schema<PojoMessage> schema() {
    return SCHEMA;
  }
}
