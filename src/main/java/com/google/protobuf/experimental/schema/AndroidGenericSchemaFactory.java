package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.Internal;
import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.BeanDescriptorFactory;

@Internal
public final class AndroidGenericSchemaFactory implements SchemaFactory {
  private final BeanDescriptorFactory beanDescriptorFactory;

  public AndroidGenericSchemaFactory() {
    this(AnnotationBeanDescriptorFactory.getInstance());
  }

  public AndroidGenericSchemaFactory(BeanDescriptorFactory beanDescriptorFactory) {
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