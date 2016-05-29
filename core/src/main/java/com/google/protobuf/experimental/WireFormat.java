package com.google.protobuf.experimental;

public final class WireFormat {
  private WireFormat() {
  }

  public static final int WIRETYPE_VARINT           = 0;
  public static final int WIRETYPE_FIXED64          = 1;
  public static final int WIRETYPE_LENGTH_DELIMITED = 2;
  public static final int WIRETYPE_START_GROUP      = 3;
  public static final int WIRETYPE_END_GROUP        = 4;
  public static final int WIRETYPE_FIXED32          = 5;

  public enum FieldType {
    DOUBLE  (JavaType.DOUBLE     , WIRETYPE_FIXED64         ),
    FLOAT   (JavaType.FLOAT      , WIRETYPE_FIXED32         ),
    INT64   (JavaType.LONG       , WIRETYPE_VARINT          ),
    UINT64  (JavaType.LONG       , WIRETYPE_VARINT          ),
    INT32   (JavaType.INT        , WIRETYPE_VARINT          ),
    FIXED64 (JavaType.LONG       , WIRETYPE_FIXED64         ),
    FIXED32 (JavaType.INT        , WIRETYPE_FIXED32         ),
    BOOL    (JavaType.BOOLEAN    , WIRETYPE_VARINT          ),
    STRING  (JavaType.STRING     , WIRETYPE_LENGTH_DELIMITED) {
      @Override
      public boolean isPackable() {
        return false; }
    },
    GROUP   (JavaType.MESSAGE    , WIRETYPE_START_GROUP     ) {
      @Override
      public boolean isPackable() {
        return false; }
    },
    MESSAGE (JavaType.MESSAGE    , WIRETYPE_LENGTH_DELIMITED) {
      @Override
      public boolean isPackable() {
        return false; }
    },
    BYTES   (JavaType.BYTE_STRING, WIRETYPE_LENGTH_DELIMITED) {
      @Override
      public boolean isPackable() {
        return false; }
    },
    UINT32  (JavaType.INT        , WIRETYPE_VARINT          ),
    ENUM    (JavaType.ENUM       , WIRETYPE_VARINT          ),
    SFIXED32(JavaType.INT        , WIRETYPE_FIXED32         ),
    SFIXED64(JavaType.LONG       , WIRETYPE_FIXED64         ),
    SINT32  (JavaType.INT        , WIRETYPE_VARINT          ),
    SINT64  (JavaType.LONG       , WIRETYPE_VARINT          );

    FieldType(final JavaType javaType, final int wireType) {
      this.javaType = javaType;
      this.wireType = wireType;
    }

    private final JavaType javaType;
    private final int wireType;

    public JavaType getJavaType() { return javaType; }
    public int getWireType() { return wireType; }

    public boolean isPackable() { return true; }
  }
}
