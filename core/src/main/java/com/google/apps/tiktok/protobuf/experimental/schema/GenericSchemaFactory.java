package com.google.apps.tiktok.protobuf.experimental.schema;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;
import com.google.apps.tiktok.protobuf.experimental.descriptor.AnnotationMessageDescriptorFactory;
import com.google.apps.tiktok.protobuf.experimental.descriptor.MessageDescriptorFactory;

/**
 * Manufactures instances of {@link GenericSchema}.
 */
@InternalApi
public final class GenericSchemaFactory implements SchemaFactory {
  private final MessageDescriptorFactory messageDescriptorFactory;

  public GenericSchemaFactory() {
    this(AnnotationMessageDescriptorFactory.getValidatingInstance());
  }

  public GenericSchemaFactory(MessageDescriptorFactory messageDescriptorFactory) {
    if (messageDescriptorFactory == null) {
      throw new NullPointerException("messageDescriptorFactory");
    }
    this.messageDescriptorFactory = messageDescriptorFactory;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return new GenericSchema<T>(messageDescriptorFactory.descriptorFor(messageType));
  }
}
