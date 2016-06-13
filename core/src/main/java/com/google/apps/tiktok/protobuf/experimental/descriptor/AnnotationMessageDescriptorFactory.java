package com.google.apps.tiktok.protobuf.experimental.descriptor;

import com.google.apps.tiktok.protobuf.experimental.FieldType;
import com.google.apps.tiktok.protobuf.experimental.InternalApi;
import com.google.apps.tiktok.protobuf.experimental.ProtoField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * A factory for descriptors that relies on {@link ProtoField} annotations on the fields of the
 * message.
 */
@InternalApi
public final class AnnotationMessageDescriptorFactory implements MessageDescriptorFactory {
  private static final AnnotationMessageDescriptorFactory VALIDATING_INSTANCE =
          new AnnotationMessageDescriptorFactory(true);

  private static final AnnotationMessageDescriptorFactory NON_VALIDATING_INSTANCE =
          new AnnotationMessageDescriptorFactory(false);

  private final boolean validateFields;

  private AnnotationMessageDescriptorFactory(boolean validateFields) {
    this.validateFields = validateFields;
  }

  /**
   * Gets the singleton instance that performs validation on field types. Field type validation for
   * lists, in particular, is somewhat slow since generic type parameters must be inspected.
   */
  public static AnnotationMessageDescriptorFactory getValidatingInstance() {
    return VALIDATING_INSTANCE;
  }

  /**
   * Gets the singleton instance that skips field validation checks. This will perform significantly
   * faster than {@link #getValidatingInstance()} at the cost of potential data corruption if the
   * fields are not annotated with the proper {@link FieldType}.
   */
  public static AnnotationMessageDescriptorFactory getNonValidatingInstance() {
    return NON_VALIDATING_INSTANCE;
  }

  @Override
  public MessageDescriptor descriptorFor(Class<?> clazz) {
    return getFields(clazz, null).build();
  }

  private MessageDescriptor.Builder getFields(Class<?> clazz, MessageDescriptor.Builder builder) {
    if (Object.class != clazz.getSuperclass()) {
      builder = getFields(clazz.getSuperclass(), builder);
    }

    Field[] fields = clazz.getDeclaredFields();
    if (builder == null) {
      builder = MessageDescriptor.newBuilder(fields.length);
    }
    for (Field f : fields) {
      int mod = f.getModifiers();
      ProtoField protoField = f.getAnnotation(ProtoField.class);
      if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && protoField != null) {
        FieldType type = protoField.type();
        if (validateFields && !type.isValidForField(f)) {
          throw new IllegalArgumentException(
                  String.format(
                          "Field type %s cannot be applied to %s ", type.name(), f.getType().getName()));
        }
        builder.add(new FieldDescriptor(f, protoField.fieldNumber(), type));
      }
    }
    return builder;
  }
}
