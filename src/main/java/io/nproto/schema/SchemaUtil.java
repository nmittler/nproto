package io.nproto.schema;

import io.nproto.FieldType;
import io.nproto.Internal;
import io.nproto.ProtoField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Internal
public final class SchemaUtil {
  private SchemaUtil() {
  }

  public static final class FieldInfo implements Comparable<FieldInfo> {
    public final Field field;
    public final FieldType fieldType;
    public final int fieldNumber;

    FieldInfo(Field field, ProtoField protoField) {
      this.field = field;
      this.fieldType = FieldType.forField(field, protoField);
      this.fieldNumber = protoField.number();
    }

    @Override
    public int compareTo(FieldInfo o) {
      return fieldNumber - o.fieldNumber;
    }
  }

  public static List<FieldInfo> getAllFieldInfo(Class<?> clazz) {
    List<FieldInfo> fields = new ArrayList<FieldInfo>();
    getAllFieldInfo(clazz, fields);
    // Now order them in ascending order by their field number.
    Collections.sort(fields);
    return fields;
  }

  private static void getAllFieldInfo(Class<?> clazz, List<FieldInfo> fields) {
    if (Object.class != clazz.getSuperclass()) {
      getAllFieldInfo(clazz.getSuperclass(), fields);
    }

    for (Field f : clazz.getDeclaredFields()) {
      int mod = f.getModifiers();
      ProtoField protoField = f.getAnnotation(ProtoField.class);
      if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && protoField != null) {
        fields.add(new FieldInfo(f, protoField));
      }
    }
  }
}
