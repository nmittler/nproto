package com.google.apps.tiktok.protobuf.experimental.descriptor;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;

/**
 * A factory that creates {@link MessageDescriptor} instances for message types.
 */
@InternalApi
public interface MessageDescriptorFactory {
  /**
   * Returns a descriptor of the message class.
   */
  MessageDescriptor descriptorFor(Class<?> clazz);
}
