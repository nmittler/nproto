package com.google.apps.tiktok.protobuf.experimental.descriptor;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;
import com.google.apps.tiktok.protobuf.experimental.ProtoField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory for descriptors that relies on {@link ProtoField} annotations on the fields of the
 * message.
 */
@InternalApi
public final class AnnotationMessageDescriptorFactory implements MessageDescriptorFactory {
  private static final AnnotationMessageDescriptorFactory INSTANCE =
      new AnnotationMessageDescriptorFactory();

  private AnnotationMessageDescriptorFactory() {
  }

  public static AnnotationMessageDescriptorFactory getInstance() {
    return INSTANCE;
  }

  @Override
  public MessageDescriptor descriptorFor(Class<?> clazz) {
    return new MessageDescriptor(getPropertyDescriptors(clazz));
  }

  private static List<FieldDescriptor> getPropertyDescriptors(Class<?> clazz) {
    List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();
    getPropertyDescriptors(clazz, fields);
    return fields;
  }

  private static void getPropertyDescriptors(Class<?> clazz, List<FieldDescriptor> fields) {
    if (Object.class != clazz.getSuperclass()) {
      getPropertyDescriptors(clazz.getSuperclass(), fields);
    }

    for (Field f : clazz.getDeclaredFields()) {
      int mod = f.getModifiers();
      ProtoField protoField = f.getAnnotation(ProtoField.class);
      if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && protoField != null) {
        fields.add(new FieldDescriptor(f, protoField.fieldNumber(), protoField.type()));
      }
    }
  }
}
