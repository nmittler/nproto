package io.nproto.schema;

import io.nproto.Internal;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;
import io.nproto.descriptor.BeanDescriptorFactory;

@Internal
public final class GenericSchemaFactory implements SchemaFactory {
  private final BeanDescriptorFactory beanDescriptorFactory;

  public GenericSchemaFactory() {
    this(AnnotationBeanDescriptorFactory.getInstance());
  }

  public GenericSchemaFactory(BeanDescriptorFactory beanDescriptorFactory) {
    if (beanDescriptorFactory == null) {
      throw new NullPointerException("beanDescriptorFactory");
    }
    this.beanDescriptorFactory = beanDescriptorFactory;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return new GenericSchema<T>(beanDescriptorFactory.descriptorFor(messageType));
  }
}
