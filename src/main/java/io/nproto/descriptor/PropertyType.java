package io.nproto.descriptor;

import static io.nproto.WireFormat.WIRETYPE_FIXED32;
import static io.nproto.WireFormat.WIRETYPE_FIXED64;
import static io.nproto.WireFormat.WIRETYPE_LENGTH_DELIMITED;
import static io.nproto.WireFormat.WIRETYPE_VARINT;

import io.nproto.ByteString;
import io.nproto.JavaType;
import io.nproto.ProtoField;
import io.nproto.WireFormat;

import java.lang.reflect.Field;
import java.util.List;

public enum PropertyType {
  DOUBLE(1, JavaType.DOUBLE, WIRETYPE_FIXED64),
  FLOAT(2, JavaType.FLOAT, WIRETYPE_FIXED32),
  INT64(3, JavaType.LONG, WIRETYPE_VARINT),
  UINT64(4, JavaType.LONG, WIRETYPE_VARINT),
  INT32(5, JavaType.INT, WIRETYPE_VARINT),
  FIXED64(6, JavaType.LONG, WIRETYPE_FIXED64),
  FIXED32(7, JavaType.INT, WIRETYPE_FIXED32),
  BOOL(8, JavaType.BOOLEAN, WIRETYPE_VARINT),
  STRING(9, JavaType.STRING, WIRETYPE_LENGTH_DELIMITED),
  MESSAGE(10, JavaType.MESSAGE, WIRETYPE_LENGTH_DELIMITED),
  BYTES(11, JavaType.BYTE_STRING, WIRETYPE_LENGTH_DELIMITED),
  UINT32(12, JavaType.INT, WIRETYPE_VARINT),
  ENUM(13, JavaType.ENUM, WIRETYPE_VARINT),
  SFIXED32(14, JavaType.INT, WIRETYPE_FIXED32),
  SFIXED64(15, JavaType.LONG, WIRETYPE_FIXED64),
  SINT32(16, JavaType.INT, WIRETYPE_VARINT),
  SINT64(17, JavaType.LONG, WIRETYPE_VARINT),
  DOUBLE_LIST(18, JavaType.LIST, WIRETYPE_FIXED64),
  FLOAT_LIST(19, JavaType.LIST, WIRETYPE_FIXED32),
  INT64_LIST(20, JavaType.LIST, WIRETYPE_VARINT),
  UINT64_LIST(21, JavaType.LIST, WIRETYPE_VARINT),
  INT32_LIST(22, JavaType.LIST, WIRETYPE_VARINT),
  FIXED64_LIST(23, JavaType.LIST, WIRETYPE_FIXED64),
  FIXED32_LIST(24, JavaType.LIST, WIRETYPE_FIXED32),
  BOOL_LIST(25, JavaType.LIST, WIRETYPE_VARINT),
  STRING_LIST(26, JavaType.LIST, WIRETYPE_LENGTH_DELIMITED),
  MESSAGE_LIST(27, JavaType.LIST, WIRETYPE_LENGTH_DELIMITED),
  BYTES_LIST(28, JavaType.LIST, WIRETYPE_LENGTH_DELIMITED),
  UINT32_LIST(29, JavaType.LIST, WIRETYPE_VARINT),
  ENUM_LIST(30, JavaType.LIST, WIRETYPE_VARINT),
  SFIXED32_LIST(31, JavaType.LIST, WIRETYPE_FIXED32),
  SFIXED64_LIST(32, JavaType.LIST, WIRETYPE_FIXED64),
  SINT32_LIST(33, JavaType.LIST, WIRETYPE_VARINT),
  SINT64_LIST(34, JavaType.LIST, WIRETYPE_VARINT);


  PropertyType(int id, JavaType javaType, int wireType) {
    this.id = id;
    this.javaType = javaType;
    this.wireType = wireType;
  }

  private final JavaType javaType;
  private final int wireType;
  private final int id;

  public int id() {
    return id;
  }

  public JavaType getJavaType() {
    return javaType;
  }

  public int getWireType() {
    return wireType;
  }

  private static final PropertyType[] VALUES;
  static {
    int maxId = -1;
    PropertyType[] propertyTypes = values();
    for (int i = 0; i < propertyTypes.length; ++i) {
      maxId = Math.max(maxId, propertyTypes[i].id);
    }
    VALUES = new PropertyType[maxId + 1];
    for (int i = 0; i < propertyTypes.length; ++i) {
      PropertyType propertyType = propertyTypes[i];
      VALUES[propertyType.id] = propertyType;
    }
  }

  public static PropertyType forOrdinal(int ordinal) {
    return VALUES[ordinal];
  }

  public static PropertyType forId(byte id) {
    return VALUES[id];
  }

  public static PropertyType forField(Field f) {
    return forField(f, f.getAnnotation(ProtoField.class).type());
  }

  public static PropertyType forField(Field f, WireFormat.FieldType fieldType) {
    if (fieldType == null) {
      throw new NullPointerException("fieldType");
    }

    final Class<?> clazz = f.getType();
    final boolean repeated = List.class.isAssignableFrom(clazz);
    switch (fieldType) {
      case DOUBLE:
        if (repeated) {
          return DOUBLE_LIST;
        }
        checkRequiredType(double.class, fieldType, clazz);
        return DOUBLE;
      case FLOAT:
        if (repeated) {
          return FLOAT_LIST;
        }
        checkRequiredType(float.class, fieldType, clazz);
        return FLOAT;
      case INT64:
        if (repeated) {
          return INT64_LIST;
        }
        checkRequiredType(long.class, fieldType, clazz);
        return INT64;
      case UINT64:
        if (repeated) {
          return UINT64_LIST;
        }
        checkRequiredType(long.class, fieldType, clazz);
        return UINT64;
      case INT32:
        if (repeated) {
          return INT32_LIST;
        }
        return INT32;
      case FIXED64:
        if (repeated) {
          return FIXED64_LIST;
        }
        checkRequiredType(long.class, fieldType, clazz);
        return FIXED64;
      case FIXED32:
        if (repeated) {
          return FIXED32_LIST;
        }
        checkRequiredType(int.class, fieldType, clazz);
        return FIXED32;
      case BOOL:
        if (repeated) {
          return BOOL_LIST;
        }
        checkRequiredType(boolean.class, fieldType, clazz);
        return BOOL;
      case STRING:
        if (repeated) {
          return STRING_LIST;
        }
        checkRequiredType(String.class, fieldType, clazz);
        return STRING;
      case MESSAGE:
        if (repeated) {
          return MESSAGE_LIST;
        }
        return MESSAGE;
      case BYTES:
        if (repeated) {
          return BYTES_LIST;
        }
        checkRequiredType(ByteString.class, fieldType, clazz);
        return BYTES;
      case UINT32:
        if (repeated) {
          return UINT32_LIST;
        }
        checkRequiredType(int.class, fieldType, clazz);
        return UINT32;
      case ENUM:
        if (repeated) {
          return ENUM_LIST;
        }
        checkRequiredType(Enum.class, fieldType, clazz);
        return ENUM;
      case SFIXED32:
        if (repeated) {
          return SFIXED32_LIST;
        }
        checkRequiredType(int.class, fieldType, clazz);
        return SFIXED32;
      case SFIXED64:
        if (repeated) {
          return SFIXED64_LIST;
        }
        checkRequiredType(long.class, fieldType, clazz);
        return SFIXED64;
      case SINT32:
        if (repeated) {
          return SINT32_LIST;
        }
        checkRequiredType(int.class, fieldType, clazz);
        return SINT32;
      case SINT64:
        if (repeated) {
          return SINT64_LIST;
        }
        checkRequiredType(long.class, fieldType, clazz);
        return SINT64;
      default:
        throw new IllegalArgumentException("Unsupported field type: " + fieldType);
    }
  }

  private static void checkRequiredType(Class<?> requiredType, WireFormat.FieldType fieldType, Class<?> clazz) {
    if (!requiredType.isAssignableFrom(clazz)) {
      throw new IllegalArgumentException(String.format(
              "Field type %s cannot be applied to %s ",
              fieldType.name(),
              clazz.getName()));
    }
  }
}
