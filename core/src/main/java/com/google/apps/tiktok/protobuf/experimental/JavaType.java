package com.google.apps.tiktok.protobuf.experimental;

/**
 * Enum that identifies the Java types required to store protobuf fields.
 */
public enum JavaType {
  INT(int.class, 0),
  LONG(long.class, 0L),
  FLOAT(float.class, 0F),
  DOUBLE(double.class, 0D),
  BOOLEAN(boolean.class, false),
  STRING(String.class, ""),
  BYTE_STRING(ByteString.class, ByteString.EMPTY),
  ENUM(int.class, null),
  MESSAGE(Object.class, null);

  JavaType(Class<?> type, Object defaultDefault) {
    this.type = type;
    this.defaultDefault = defaultDefault;
  }

  /**
   * The default default value for fields of this type, if it's a primitive
   * type.
   */
  public Object getDefaultDefault() {
    return defaultDefault;
  }

  /**
   * Gets the required type for a field that would hold a value of this type.
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Indicates whether or not this {@link JavaType} can be applied to a field of the given type.
   */
  public boolean isValidType(Class<?> t) {
    return type.isAssignableFrom(t);
  }

  private final Object defaultDefault;
  private final Class<?> type;
}
