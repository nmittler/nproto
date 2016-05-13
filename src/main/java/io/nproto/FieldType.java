package io.nproto;

import static io.nproto.WireFormat.WIRETYPE_FIXED32;
import static io.nproto.WireFormat.WIRETYPE_FIXED64;
import static io.nproto.WireFormat.WIRETYPE_LENGTH_DELIMITED;
import static io.nproto.WireFormat.WIRETYPE_VARINT;

import java.lang.reflect.Field;
import java.util.List;

public enum FieldType {
  DOUBLE(1, JavaType.DOUBLE, WIRETYPE_FIXED64, false, false),
  FLOAT(2, JavaType.FLOAT, WIRETYPE_FIXED32, false, false),
  INT64(3, JavaType.LONG, WIRETYPE_VARINT, false, false),
  UINT64(4, JavaType.LONG, WIRETYPE_VARINT, false, false),
  INT32(5, JavaType.INT, WIRETYPE_VARINT, false, false),
  FIXED64(6, JavaType.LONG, WIRETYPE_FIXED64, false, false),
  FIXED32(7, JavaType.INT, WIRETYPE_FIXED32, false, false),
  BOOL(8, JavaType.BOOLEAN, WIRETYPE_VARINT, false, false),
  STRING(9, JavaType.STRING, WIRETYPE_LENGTH_DELIMITED, false, false),
  MESSAGE(10, JavaType.MESSAGE, WIRETYPE_LENGTH_DELIMITED, false, false),
  BYTES(11, JavaType.BYTE_STRING, WIRETYPE_LENGTH_DELIMITED, false, false),
  UINT32(12, JavaType.INT, WIRETYPE_VARINT, false, false),
  ENUM(13, JavaType.ENUM, WIRETYPE_VARINT, false, false),
  SFIXED32(14, JavaType.INT, WIRETYPE_FIXED32, false, false),
  SFIXED64(15, JavaType.LONG, WIRETYPE_FIXED64, false, false),
  SINT32(16, JavaType.INT, WIRETYPE_VARINT, false, false),
  SINT64(17, JavaType.LONG, WIRETYPE_VARINT, false, false),
  DOUBLE_LIST(18, JavaType.LIST, WIRETYPE_FIXED64, true, false),
  FLOAT_LIST(19, JavaType.LIST, WIRETYPE_FIXED32, true, false),
  INT64_LIST(20, JavaType.LIST, WIRETYPE_VARINT, true, false),
  UINT64_LIST(21, JavaType.LIST, WIRETYPE_VARINT, true, false),
  INT32_LIST(22, JavaType.LIST, WIRETYPE_VARINT, true, false),
  FIXED64_LIST(23, JavaType.LIST, WIRETYPE_FIXED64, true, false),
  FIXED32_LIST(24, JavaType.LIST, WIRETYPE_FIXED32, true, false),
  BOOL_LIST(25, JavaType.LIST, WIRETYPE_VARINT, true, false),
  STRING_LIST(26, JavaType.LIST, WIRETYPE_LENGTH_DELIMITED, true, false),
  MESSAGE_LIST(27, JavaType.LIST, WIRETYPE_LENGTH_DELIMITED, true, false),
  BYTES_LIST(28, JavaType.LIST, WIRETYPE_LENGTH_DELIMITED, true, false),
  UINT32_LIST(29, JavaType.LIST, WIRETYPE_VARINT, true, false),
  ENUM_LIST(30, JavaType.LIST, WIRETYPE_VARINT, true, false),
  SFIXED32_LIST(31, JavaType.LIST, WIRETYPE_FIXED32, true, false),
  SFIXED64_LIST(32, JavaType.LIST, WIRETYPE_FIXED64, true, false),
  SINT32_LIST(33, JavaType.LIST, WIRETYPE_VARINT, true, false),
  SINT64_LIST(34, JavaType.LIST, WIRETYPE_VARINT, true, false),
  PACKED_DOUBLE_LIST(35, JavaType.LIST, WIRETYPE_FIXED64, true, true),
  PACKED_FLOAT_LIST(36, JavaType.LIST, WIRETYPE_FIXED32, true, true),
  PACKED_INT64_LIST(37, JavaType.LIST, WIRETYPE_VARINT, true, true),
  PACKED_UINT64_LIST(38, JavaType.LIST, WIRETYPE_VARINT, true, true),
  PACKED_INT32_LIST(39, JavaType.LIST, WIRETYPE_VARINT, true, true),
  PACKED_FIXED64_LIST(40, JavaType.LIST, WIRETYPE_FIXED64, true, true),
  PACKED_FIXED32_LIST(41, JavaType.LIST, WIRETYPE_FIXED32, true, true),
  PACKED_BOOL_LIST(42, JavaType.LIST, WIRETYPE_VARINT, true, true),
  PACKED_UINT32_LIST(43, JavaType.LIST, WIRETYPE_VARINT, true, true),
  PACKED_ENUM_LIST(44, JavaType.LIST, WIRETYPE_VARINT, true, true),
  PACKED_SFIXED32_LIST(45, JavaType.LIST, WIRETYPE_FIXED32, true, true),
  PACKED_SFIXED64_LIST(46, JavaType.LIST, WIRETYPE_FIXED64, true, true),
  PACKED_SINT32_LIST(47, JavaType.LIST, WIRETYPE_VARINT, true, true),
  PACKED_SINT64_LIST(48, JavaType.LIST, WIRETYPE_VARINT, true, true);

  /*public static final byte DOUBLE_ORDINAL = (byte) DOUBLE.ordinal();
  public static final byte FLOAT_ORDINAL = (byte) FLOAT.ordinal();
  public static final byte UINT32_ORDINAL = (byte) UINT32.ordinal();
  public static final byte INT32_ORDINAL = (byte) INT32.ordinal();
  public static final byte SINT32_ORDINAL = (byte) SINT32.ordinal();
  public static final byte FIXED32_ORDINAL = (byte) FIXED32.ordinal();
  public static final byte SFIXED32_ORDINAL = (byte) SFIXED32.ordinal();
  public static final byte UINT64_ORDINAL = (byte) UINT64.ordinal();
  public static final byte INT64_ORDINAL = (byte) INT64.ordinal();
  public static final byte SINT64_ORDINAL = (byte) SINT64.ordinal();
  public static final byte FIXED64_ORDINAL = (byte) FIXED64.ordinal();
  public static final byte SFIXED64_ORDINAL = (byte) SFIXED64.ordinal();
  public static final byte BOOL_ORDINAL = (byte) BOOL.ordinal();
  public static final byte ENUM_ORDINAL = (byte) ENUM.ordinal();
  public static final byte MESSAGE_ORDINAL = (byte) MESSAGE.ordinal();
  public static final byte BYTES_ORDINAL = (byte) BYTES.ordinal();
  public static final byte STRING_ORDINAL = (byte) STRING.ordinal();*/


  FieldType(int id, JavaType javaType, int wireType, boolean repeated, boolean packed) {
    this.id = id;
    this.javaType = javaType;
    this.wireType = wireType;
    this.repeated = repeated;
    this.packed = packed;
  }

  private final JavaType javaType;
  private final int wireType;
  private final boolean repeated;
  private final boolean packed;
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

  public boolean isRepeated() {
    return repeated;
  }

  public boolean isPacked() {
    return packed;
  }

  private static final FieldType[] VALUES;
  static {
    int maxId = -1;
    FieldType[] fieldTypes = values();
    for (int i = 0; i < fieldTypes.length; ++i) {
      maxId = Math.max(maxId, fieldTypes[i].id);
    }
    VALUES = new FieldType[maxId + 1];
    for (int i = 0; i < fieldTypes.length; ++i) {
      FieldType fieldType = fieldTypes[i];
      VALUES[fieldType.id] = fieldType;
    }
  }

  public static FieldType forOrdinal(int ordinal) {
    return VALUES[ordinal];
  }

  public static FieldType forId(byte id) {
    return VALUES[id];
  }

  public static FieldType forField(Field f) {
    return forField(f, f.getAnnotation(ProtoField.class));
  }

  public static FieldType forField(Field f, ProtoField protoField) {
    if (protoField == null) {
      throw new IllegalArgumentException("Field must have the ProtoField annotation");
    }

    final Class<?> fieldType = f.getType();
    final boolean list = List.class.isAssignableFrom(fieldType);
    final boolean packed = protoField.packed();
    final boolean repeated = protoField.repeated();
    if (repeated && !list) {
      throw new IllegalArgumentException("Field type is not appropriate for the repeated parameter");
    }

    switch (protoField.type()) {
      case DOUBLE:
        if (repeated) {
          return packed ? PACKED_DOUBLE_LIST : DOUBLE_LIST;
        }
        checkRequiredType(double.class, protoField, fieldType);
        return DOUBLE;
      case FLOAT:
        if (repeated) {
          return packed ? PACKED_FLOAT_LIST : FLOAT_LIST;
        }
        checkRequiredType(float.class, protoField, fieldType);
        return FLOAT;
      case INT64:
        if (repeated) {
          return packed ? PACKED_INT64_LIST : INT64_LIST;
        }
        checkRequiredType(long.class, protoField, fieldType);
        return INT64;
      case UINT64:
        if (repeated) {
          return packed ? PACKED_UINT64_LIST : UINT64_LIST;
        }
        checkRequiredType(long.class, protoField, fieldType);
        return UINT64;
      case INT32:
        if (repeated) {
          return packed ? PACKED_INT32_LIST : INT32_LIST;
        }
        return INT32;
      case FIXED64:
        if (repeated) {
          return packed ? PACKED_FIXED64_LIST : FIXED64_LIST;
        }
        checkRequiredType(long.class, protoField, fieldType);
        return FIXED64;
      case FIXED32:
        if (repeated) {
          return packed ? PACKED_FIXED32_LIST : FIXED32_LIST;
        }
        checkRequiredType(int.class, protoField, fieldType);
        return FIXED32;
      case BOOL:
        if (repeated) {
          return packed ? PACKED_BOOL_LIST : BOOL_LIST;
        }
        checkRequiredType(boolean.class, protoField, fieldType);
        return BOOL;
      case STRING:
        if (repeated) {
          if (packed) {
            throw new IllegalArgumentException("String values must not be packed");
          }
          return STRING_LIST;
        }
        checkRequiredType(String.class, protoField, fieldType);
        return STRING;
      case MESSAGE:
        if (repeated) {
          if (packed) {
            throw new IllegalArgumentException("Message values must not be packed");
          }
          return MESSAGE_LIST;
        }
        return MESSAGE;
      case BYTES:
        if (repeated) {
          if (packed) {
            throw new IllegalArgumentException("Bytes values must not be packed");
          }
          return BYTES_LIST;
        }
        checkRequiredType(ByteString.class, protoField, fieldType);
        return BYTES;
      case UINT32:
        if (repeated) {
          return packed ? PACKED_UINT32_LIST : UINT32_LIST;
        }
        checkRequiredType(int.class, protoField, fieldType);
        return UINT32;
      case ENUM:
        if (repeated) {
          return packed ? PACKED_ENUM_LIST : ENUM_LIST;
        }
        checkRequiredType(int.class, protoField, fieldType);
        return ENUM;
      case SFIXED32:
        if (repeated) {
          return packed ? PACKED_SFIXED32_LIST : SFIXED32_LIST;
        }
        checkRequiredType(int.class, protoField, fieldType);
        return SFIXED32;
      case SFIXED64:
        if (repeated) {
          return packed ? PACKED_SFIXED64_LIST : SFIXED64_LIST;
        }
        checkRequiredType(long.class, protoField, fieldType);
        return SFIXED64;
      case SINT32:
        if (repeated) {
          return packed ? PACKED_SINT32_LIST : SINT32_LIST;
        }
        checkRequiredType(int.class, protoField, fieldType);
        return SINT32;
      case SINT64:
        if (repeated) {
          return packed ? PACKED_SINT64_LIST : SINT64_LIST;
        }
        checkRequiredType(long.class, protoField, fieldType);
        return SINT64;
      default:
        throw new IllegalArgumentException("Unsupported field type: " + protoField.type());
    }
  }

  private static void checkRequiredType(Class<?> requiredType, ProtoField protoField, Class<?> fieldType) {
    if (requiredType != fieldType) {
      throw new IllegalArgumentException(String.format(
              "Field type %s cannot be applied to %s ",
              protoField.type().name(),
              fieldType.getName()));
    }
  }
}
