package io.nproto.schema;

import io.nproto.Internal;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;
import io.nproto.descriptor.BeanDescriptorFactory;

@Internal
public final class AndroidGenericFactory implements SchemaFactory {
  private final BeanDescriptorFactory beanDescriptorFactory;

  public AndroidGenericFactory() {
    this(AnnotationBeanDescriptorFactory.getInstance());
  }

  public AndroidGenericFactory(BeanDescriptorFactory beanDescriptorFactory) {
    if (beanDescriptorFactory == null) {
      throw new NullPointerException("beanDescriptorFactory");
    }
    this.beanDescriptorFactory = beanDescriptorFactory;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return new AndroidGenericSchema<T>(beanDescriptorFactory.descriptorFor(messageType));
  }
}