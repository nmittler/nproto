package io.nproto.schema.reflect;

import io.nproto.Internal;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;
import io.nproto.descriptor.BeanDescriptorFactory;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;

@Internal
public final class UnsafeReflectiveSchemaFactory implements SchemaFactory {
  private final BeanDescriptorFactory beanDescriptorFactory;

  public UnsafeReflectiveSchemaFactory() {
    this(AnnotationBeanDescriptorFactory.getInstance());
  }

  public UnsafeReflectiveSchemaFactory(BeanDescriptorFactory beanDescriptorFactory) {
    if (beanDescriptorFactory == null) {
      throw new NullPointerException("beanDescriptorFactory");
    }
    this.beanDescriptorFactory = beanDescriptorFactory;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return new UnsafeReflectiveSchema<T>(beanDescriptorFactory.descriptorFor(messageType));
  }
}
