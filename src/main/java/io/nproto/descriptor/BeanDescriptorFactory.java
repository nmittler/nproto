package io.nproto.descriptor;

import io.nproto.Internal;

@Internal
public interface BeanDescriptorFactory {
  BeanDescriptor descriptorFor(Class<?> clazz);
}
