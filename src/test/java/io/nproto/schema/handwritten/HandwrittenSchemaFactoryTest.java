package io.nproto.schema.handwritten;

import io.nproto.PojoMessage;
import io.nproto.schema.AbstractSchemaFactoryTest;
import io.nproto.schema.Schema;

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
