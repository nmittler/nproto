package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.PojoMessage;

public class AndroidGenericSchemaFactoryTest extends AbstractSchemaFactoryTest {
  private static final Schema<PojoMessage> SCHEMA =
          new AndroidGenericSchemaFactory().createSchema(PojoMessage.class);

  @Override
  protected Schema<PojoMessage> schema() {
    return SCHEMA;
  }
}
