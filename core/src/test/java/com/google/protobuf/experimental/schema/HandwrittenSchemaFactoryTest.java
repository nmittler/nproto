package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.example.HandwrittenSchemaFactory;
import com.google.protobuf.experimental.example.PojoMessage;

import org.junit.Ignore;
import org.junit.Test;

public class HandwrittenSchemaFactoryTest extends AbstractSchemaFactoryTest {
  private static final Schema<PojoMessage> SCHEMA =
          new HandwrittenSchemaFactory().createSchema(PojoMessage.class);

  @Override
  protected Schema<PojoMessage> schema() {
    return SCHEMA;
  }

  @Override
  @Test
  @Ignore("Ignore until iterator is implemented")
  public void iteratedFieldsShouldMatchExpected() {
    super.iteratedFieldsShouldMatchExpected();
  }
}
