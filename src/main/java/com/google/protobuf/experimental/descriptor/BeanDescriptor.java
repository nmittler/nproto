package com.google.protobuf.experimental.descriptor;

import com.google.protobuf.experimental.Internal;

import java.util.Collections;
import java.util.List;

@Internal
public class BeanDescriptor {
  private final List<PropertyDescriptor> propertyDescriptors;

  public BeanDescriptor(List<PropertyDescriptor> propertyDescriptors) {
    this.propertyDescriptors = Collections.unmodifiableList(propertyDescriptors);
  }

  public List<PropertyDescriptor> getPropertyDescriptors() {
    return propertyDescriptors;
  }
}
