package com.google.protobuf.experimental.descriptor;

import com.google.protobuf.experimental.Internal;
import com.google.protobuf.experimental.ProtoField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Internal
public class AnnotationBeanDescriptorFactory implements BeanDescriptorFactory {
  private static final AnnotationBeanDescriptorFactory INSTANCE =
          new AnnotationBeanDescriptorFactory();

  public static AnnotationBeanDescriptorFactory getInstance() {
    return INSTANCE;
  }

  @Override
  public BeanDescriptor descriptorFor(Class<?> clazz) {
    return new BeanDescriptor(getPropertyDescriptors(clazz));
  }

  private static List<PropertyDescriptor> getPropertyDescriptors(Class<?> clazz) {
    List<PropertyDescriptor> fields = new ArrayList<PropertyDescriptor>();
    getPropertyDescriptors(clazz, fields);
    // Now order them in ascending order by their field number.
    Collections.sort(fields);
    return fields;
  }

  private static void getPropertyDescriptors(Class<?> clazz, List<PropertyDescriptor> fields) {
    if (Object.class != clazz.getSuperclass()) {
      getPropertyDescriptors(clazz.getSuperclass(), fields);
    }

    for (Field f : clazz.getDeclaredFields()) {
      int mod = f.getModifiers();
      ProtoField protoField = f.getAnnotation(ProtoField.class);
      if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && protoField != null) {
        fields.add(new PropertyDescriptor(f, protoField));
      }
    }
  }
}
