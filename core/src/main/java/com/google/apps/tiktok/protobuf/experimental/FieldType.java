package com.google.apps.tiktok.protobuf.experimental;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Enumeration identifying all relevant type information for a protobuf field.
 */
public enum FieldType {
  DOUBLE(0, double.class, JavaType.DOUBLE, false),
  FLOAT(1, float.class, JavaType.FLOAT, false),
  INT64(2, long.class, JavaType.LONG, false),
  UINT64(3, long.class, JavaType.LONG, false),
  INT32(4, int.class, JavaType.INT, false),
  FIXED64(5, long.class, JavaType.LONG, false),
  FIXED32(6, int.class, JavaType.INT, false),
  BOOL(7, boolean.class, JavaType.BOOLEAN, false),
  STRING(8, String.class, JavaType.STRING, false),
  MESSAGE(9, Object.class, JavaType.MESSAGE, false),
  BYTES(10, ByteString.class, JavaType.BYTE_STRING, false),
  UINT32(11, int.class, JavaType.INT, false),
  ENUM(12, int.class, JavaType.ENUM, false),
  SFIXED32(13, int.class, JavaType.INT, false),
  SFIXED64(14, long.class, JavaType.LONG, false),
  SINT32(15, int.class, JavaType.INT, false),
  SINT64(16, long.class, JavaType.LONG, false),
  DOUBLE_LIST(17, List.class, JavaType.DOUBLE, false),
  FLOAT_LIST(18, List.class, JavaType.FLOAT, false),
  INT64_LIST(19, List.class, JavaType.LONG, false),
  UINT64_LIST(20, List.class, JavaType.LONG, false),
  INT32_LIST(21, List.class, JavaType.INT, false),
  FIXED64_LIST(22, List.class, JavaType.LONG, false),
  FIXED32_LIST(23, List.class, JavaType.INT, false),
  BOOL_LIST(24, List.class, JavaType.BOOLEAN, false),
  STRING_LIST(25, List.class, JavaType.STRING, false),
  MESSAGE_LIST(26, List.class, JavaType.MESSAGE, false),
  BYTES_LIST(27, List.class, JavaType.BYTE_STRING, false),
  UINT32_LIST(28, List.class, JavaType.INT, false),
  ENUM_LIST(29, List.class, JavaType.ENUM, false),
  SFIXED32_LIST(30, List.class, JavaType.INT, false),
  SFIXED64_LIST(31, List.class, JavaType.LONG, false),
  SINT32_LIST(32, List.class, JavaType.INT, false),
  SINT64_LIST(33, List.class, JavaType.LONG, false),
  DOUBLE_LIST_PACKED(34, List.class, JavaType.DOUBLE, true),
  FLOAT_LIST_PACKED(35, List.class, JavaType.FLOAT, true),
  INT64_LIST_PACKED(36, List.class, JavaType.LONG, true),
  UINT64_LIST_PACKED(37, List.class, JavaType.LONG, true),
  INT32_LIST_PACKED(38, List.class, JavaType.INT, true),
  FIXED64_LIST_PACKED(39, List.class, JavaType.LONG, true),
  FIXED32_LIST_PACKED(40, List.class, JavaType.INT, true),
  BOOL_LIST_PACKED(41, List.class, JavaType.BOOLEAN, true),
  UINT32_LIST_PACKED(42, List.class, JavaType.INT, true),
  ENUM_LIST_PACKED(43, List.class, JavaType.ENUM, true),
  SFIXED32_LIST_PACKED(44, List.class, JavaType.INT, true),
  SFIXED64_LIST_PACKED(45, List.class, JavaType.LONG, true),
  SINT32_LIST_PACKED(46, List.class, JavaType.INT, true),
  SINT64_LIST_PACKED(47, List.class, JavaType.LONG, true);

  FieldType(int id, Class<?> type, JavaType javaType, boolean packed) {
    this.id = id;
    this.type = type;
    this.javaType = javaType;
    this.packed = packed;

    Class<?> elementType = null;
    if (type == List.class) {
      // Set the allowed type within the list.
      switch (javaType) {
        case BOOLEAN:
          elementType = Boolean.class;
          break;
        case DOUBLE:
          elementType = Double.class;
          break;
        case ENUM:
          elementType = Integer.class;
          break;
        case FLOAT:
          elementType = Float.class;
          break;
        case INT:
          elementType = Integer.class;
          break;
        case LONG:
          elementType = Long.class;
          break;
        default:
          elementType = javaType.getType();
          break;
      }
    }
    listElementType = elementType;
  }

  private Class<?> type;
  private final JavaType javaType;
  private final int id;
  private final boolean packed;
  private final Class<?> listElementType;

  /**
   * A reliable unique identifier for this type.
   */
  public int id() {
    return id;
  }

  /**
   * Gets the expected type for a message field. The field type must be assignable from this type.
   *
   * @see Class#isAssignableFrom(Class)
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Gets the {@link JavaType} for this field. For lists, this identifies the type of the elements
   * contained within the list.
   */
  public JavaType getJavaType() {
    return javaType;
  }

  /**
   * Indicates whether a list field should be represented on the wire in packed form.
   */
  public boolean isPacked() {
    return packed;
  }

  /**
   * Indicates whether this field represents a list of values.
   */
  public boolean isList() {
    return type == List.class;
  }

  /**
   * Indicates whether or not this {@link FieldType} can be applied to the given {@link Field}.
   */
  public boolean isValidForField(Field field) {
    if (isList()) {
      Class<?> clazz = field.getType();
      if (!type.isAssignableFrom(clazz)) {
        // The field isn't a List type.
        return false;
      }
      Type[] types = EMPTY_TYPES;
      Type genericType = field.getGenericType();
      if (genericType instanceof ParameterizedType) {
        types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
      }
      Type listParameter = getListParameter(clazz, types);
      if (!(listParameter instanceof Class)) {
        // It's a wildcard, we should allow anything in the list.
        return true;
      }
      return listElementType.isAssignableFrom((Class<?>) listParameter);
    } else {
      return type.isAssignableFrom(field.getType());
    }
  }

  /**
   * Looks up the appropriate {@link FieldType} by it's identifier.
   *
   * @return the {@link FieldType} or {@code null} if not found.
   */
  @Nullable
  public static FieldType forId(byte id) {
    if (id < 0 || id >= VALUES.length) {
      return null;
    }
    return VALUES[id];
  }

  private static final FieldType[] VALUES;
  private static final Type[] EMPTY_TYPES = new Type[0];

  static {
    FieldType[] values = values();
    VALUES = new FieldType[values.length];
    for (FieldType type : values) {
      VALUES[type.id] = type;
    }
  }

  /**
   * Given a class, finds a generic super class or interface that extends {@link List}.
   *
   * @return the generic super class/interface, or {@code null} if not found.
   */
  @Nullable
  private static Type getGenericSuperList(Class<?> clazz) {
    // First look at interfaces.
    Type[] genericInterfaces = clazz.getGenericInterfaces();
    for (Type genericInterface : genericInterfaces) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        if (List.class.isAssignableFrom(rawType)) {
          return genericInterface;
        }
      }
    }

    // Try the subclass
    Type type = clazz.getGenericSuperclass();
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Class<?> rawType = (Class<?>) parameterizedType.getRawType();
      if (List.class.isAssignableFrom(rawType)) {
        return type;
      }
    }

    // No super class/interface extends List.
    return null;
  }

  /**
   * Inspects the inheritance hierarchy for the given class and finds the generic type parameter
   * for {@link List}.
   *
   * @param clazz the class to begin the search.
   * @param realTypes the array of actual type parameters for {@code clazz}. These will be used to
   * substitute generic parameters up the inheritance hierarchy. If {@code clazz} does not have any
   * generic parameters, this list should be empty.
   * @return the {@link List} parameter.
   */
  private static Type getListParameter(Class<?> clazz, Type[] realTypes) {
    top:
    while (clazz != List.class) {
      // First look at generic subclass and interfaces.
      Type genericType = getGenericSuperList(clazz);
      if (genericType instanceof ParameterizedType) {
        // Replace any generic parameters with the real values.
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type[] superArgs = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < superArgs.length; ++i) {
          Type superArg = superArgs[i];
          if (superArg instanceof TypeVariable) {
            // Get the type variables for this class so that we can match them to the variables
            // used on the super class.
            TypeVariable<?>[] clazzParams = clazz.getTypeParameters();
            if (realTypes.length != clazzParams.length) {
              throw new RuntimeException("Type array mismatch");
            }

            // Replace the variable parameter with the real type.
            boolean foundReplacement = false;
            for (int j = 0; j < clazzParams.length; ++j) {
              if (superArg == clazzParams[j]) {
                Type realType = realTypes[j];
                superArgs[i] = realType;
                foundReplacement = true;
                break;
              }
            }
            if (!foundReplacement) {
              throw new RuntimeException("Unable to find replacement for " + superArg);
            }
          }
        }

        Class<?> parent = (Class<?>) parameterizedType.getRawType();

        realTypes = superArgs;
        clazz = parent;
        continue;
      }

      // None of the parameterized types inherit List. Just continue up the inheritance hierarchy
      // toward the List interface until we can identify the parameters.
      realTypes = EMPTY_TYPES;
      for (Class<?> iface : clazz.getInterfaces()) {
        if (List.class.isAssignableFrom(iface)) {
          clazz = iface;
          continue top;
        }
      }
      clazz = clazz.getSuperclass();
    }

    if (realTypes.length != 1) {
      throw new RuntimeException("Unable to identify parameter type for List<T>");
    }
    return realTypes[0];
  }
}
