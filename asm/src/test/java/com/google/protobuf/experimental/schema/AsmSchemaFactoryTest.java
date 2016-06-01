package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.example.PojoMessage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AsmSchemaFactoryTest extends AbstractSchemaFactoryTest {
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {false, false}, {false, true}, {true, false}, {true, true}
    });
  }

  @Parameter
  public boolean minimizeGeneratedCode;

  @Parameter(value = 1)
  public boolean preferUnsafe;

  private Schema<PojoMessage> schema;

  @Override
  @Before
  public void setup() {
    AsmSchemaFactory factory = new AsmSchemaFactory(new InjectionClassLoadingStrategy(),
            AnnotationBeanDescriptorFactory.getInstance(),
            new RandomSchemaNamingStrategy(20),
            minimizeGeneratedCode,
            preferUnsafe);
    schema = factory.createSchema(PojoMessage.class);

    super.setup();
  }

  @Override
  protected Schema<PojoMessage> schema() {
    return schema;
  }

  @Override
  @Test
  @Ignore("Ignore until iterator is implemented")
  public void iteratedFieldsShouldMatchExpected() {
    super.iteratedFieldsShouldMatchExpected();
  }
}
