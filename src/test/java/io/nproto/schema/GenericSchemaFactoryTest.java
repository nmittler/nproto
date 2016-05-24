package io.nproto.schema;

import io.nproto.PojoMessage;
import io.nproto.schema.AbstractSchemaFactoryTest;
import io.nproto.schema.Schema;
import io.nproto.schema.GenericSchemaFactory;

public class GenericSchemaFactoryTest extends AbstractSchemaFactoryTest {
  private static final Schema<PojoMessage> SCHEMA =
          new GenericSchemaFactory().createSchema(PojoMessage.class);

  @Override
  protected Schema<PojoMessage> schema() {
    return SCHEMA;
  }
}
