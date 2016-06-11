package com.google.apps.tiktok.protobuf.experimental.descriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A descriptor for a protobuf message class. This describes all of the fields contained within a
 * message.
 */
public final class MessageDescriptor {
  private final List<FieldDescriptor> fieldDescriptors;

  /**
   * Constructs the descriptor.
   *
   * @param fieldDescriptors the set of fields for the message.
   */
  public MessageDescriptor(Collection<FieldDescriptor> fieldDescriptors) {
    // Make a defensive copy and sort them in ascending order by field number.
    List<FieldDescriptor> list = new ArrayList<FieldDescriptor>(fieldDescriptors);
    Collections.sort(list);
    this.fieldDescriptors = Collections.unmodifiableList(list);
  }

  /**
   * Gets the list of descriptors for all of the fields within this message, sorted in ascending
   * order by their field number.
   */
  public List<FieldDescriptor> getFieldDescriptors() {
    return fieldDescriptors;
  }
}
