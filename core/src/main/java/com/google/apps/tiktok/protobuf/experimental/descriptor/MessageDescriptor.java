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
  private MessageDescriptor(ArrayList<FieldDescriptor> fieldDescriptors) {
    this.fieldDescriptors = Collections.unmodifiableList(fieldDescriptors);
  }

  /**
   * Gets the list of descriptors for all of the fields within this message, sorted in ascending
   * order by their field number.
   */
  public List<FieldDescriptor> getFieldDescriptors() {
    return fieldDescriptors;
  }

  /**
   * Helper method for creating a new builder for {@link MessageDescriptor}.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Helper method for creating a new builder for {@link MessageDescriptor}.
   */
  public static Builder newBuilder(int numFields) {
    return new Builder(numFields);
  }

  /**
   * A builder of {@link MessageDescriptor} instances.
   */
  public static final class Builder {
    private final ArrayList<FieldDescriptor> fieldDescriptors;
    private boolean wasBuilt;

    public Builder() {
      fieldDescriptors = new ArrayList<FieldDescriptor>();
    }

    public Builder(int numFields) {
      fieldDescriptors = new ArrayList<FieldDescriptor>(numFields);
    }

    public void add(FieldDescriptor fieldDescriptor) {
      if (wasBuilt) {
        throw new IllegalStateException("Builder can only build once");
      }
      fieldDescriptors.add(fieldDescriptor);
    }

    public MessageDescriptor build() {
      if (wasBuilt) {
        throw new IllegalStateException("Builder can only build once");
      }
      wasBuilt = true;
      Collections.sort(fieldDescriptors);
      return new MessageDescriptor(fieldDescriptors);
    }
  }
}
