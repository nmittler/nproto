package io.nproto.descriptor;

import io.nproto.Internal;

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
