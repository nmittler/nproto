package com.google.apps.tiktok.protobuf.experimental.schema.asm;

import com.google.apps.tiktok.protobuf.experimental.descriptor.AnnotationMessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.schema.AbstractSchemaFactoryTest;
import com.google.apps.tiktok.protobuf.experimental.schema.Schema;
import com.google.apps.tiktok.protobuf.experimental.testing.TestMessage;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AsmSchemaFactoryTest extends AbstractSchemaFactoryTest {
  public enum CodeSize {
    INLINE,
    MINCODE
  }

  public enum Safety {
    SAFE,
    UNSAFE
  }

  @Parameters(name = "{0}, {1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {CodeSize.INLINE, Safety.SAFE},
          {CodeSize.INLINE, Safety.UNSAFE},
          {CodeSize.MINCODE, Safety.SAFE},
          {CodeSize.MINCODE, Safety.UNSAFE}
        });
  }

  @Parameter public CodeSize codeSize;

  @Parameter(value = 1)
  public Safety safety;

  private Schema<TestMessage> schema;

  @Override
  @Before
  public void setup() {
    boolean minimizeGeneratedCode = codeSize == CodeSize.MINCODE;
    boolean preferUnsafe = safety == Safety.UNSAFE;
    AsmSchemaFactory factory =
        new AsmSchemaFactory(
            new InjectionClassLoadingStrategy(),
            AnnotationMessageDescriptorFactory.getValidatingInstance(),
            new RandomSchemaNamingStrategy(20),
            minimizeGeneratedCode,
            preferUnsafe);
    schema = factory.createSchema(TestMessage.class);

    super.setup();
  }

  @Override
  protected Schema<TestMessage> schema() {
    return schema;
  }
}
