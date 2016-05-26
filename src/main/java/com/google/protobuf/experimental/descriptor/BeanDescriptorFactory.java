package com.google.protobuf.experimental.descriptor;

import com.google.protobuf.experimental.Internal;

@Internal
public interface BeanDescriptorFactory {
  BeanDescriptor descriptorFor(Class<?> clazz);
}
