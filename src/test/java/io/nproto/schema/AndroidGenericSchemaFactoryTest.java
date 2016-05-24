package io.nproto.schema;

import io.nproto.PojoMessage;
import io.nproto.schema.AbstractSchemaFactoryTest;
import io.nproto.schema.AndroidGenericFactory;
import io.nproto.schema.Schema;

public class AndroidGenericSchemaFactoryTest extends AbstractSchemaFactoryTest {
  private static final Schema<PojoMessage> SCHEMA =
          new AndroidGenericFactory().createSchema(PojoMessage.class);

  @Override
  protected Schema<PojoMessage> schema() {
    return SCHEMA;
  }
}
