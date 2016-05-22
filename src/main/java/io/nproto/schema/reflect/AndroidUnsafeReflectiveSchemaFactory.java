package io.nproto.schema.reflect;

import io.nproto.Internal;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;
import io.nproto.descriptor.BeanDescriptorFactory;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;

@Internal
public final class AndroidUnsafeReflectiveSchemaFactory implements SchemaFactory {
  private final BeanDescriptorFactory beanDescriptorFactory;

  public AndroidUnsafeReflectiveSchemaFactory() {
    this(AnnotationBeanDescriptorFactory.getInstance());
  }

  public AndroidUnsafeReflectiveSchemaFactory(BeanDescriptorFactory beanDescriptorFactory) {
    if (beanDescriptorFactory == null) {
      throw new NullPointerException("beanDescriptorFactory");
    }
    this.beanDescriptorFactory = beanDescriptorFactory;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return new AndroidUnsafeReflectiveSchema<T>(beanDescriptorFactory.descriptorFor(messageType));
  }
}