package io.nproto.runtime.io;

import io.nproto.ByteString;
import io.nproto.InternalUtil;
import io.nproto.Utf8;
import io.nproto.runtime.collect.IntArrayList;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by nathanmittler on 4/4/16.
 */
public final class BinaryEncoder extends Encoder {
  private static final int LITTLE_ENDIAN_32_SIZE = 4;
  private static final int LITTLE_ENDIAN_64_SIZE = 8;

  private final byte[] buffer;
  private final int offset;
  private final int limit;
  private int position;

  public BinaryEncoder(byte[] buffer) {
    this(buffer, 0, buffer.length);
  }

  public BinaryEncoder(byte[] buffer, int offset, int length) {
    this.buffer = buffer;
    this.position = offset;
    this.offset = offset;
    this.limit = offset + length;
  }

  @Override
  public void visitInt(int fieldId, int value) {
    writeTag(fieldId, BinaryFormat.WIRETYPE_VARINT);
    writeVarint32(value);
  }

  @Override
  public void visitLong(int fieldId, long value) {
    writeTag(fieldId, BinaryFormat.WIRETYPE_VARINT);
    writeVarint64(value);
  }

  @Override
  public void visitBool(int fieldId, boolean value) {
    writeTag(fieldId, BinaryFormat.WIRETYPE_VARINT);
    write((byte)(value ? 1 : 0));
  }

  @Override
  public void visitString(int fieldId, String value) {
    writeTag(fieldId, BinaryFormat.WIRETYPE_LENGTH_DELIMITED);
    writeString(value);
  }

  @Override
  public void visitBytes(int fieldId, ByteString value) {
    writeTag(fieldId, BinaryFormat.WIRETYPE_LENGTH_DELIMITED);
    writeBytes(value);
  }

  @Override
  public void visitIntList(int fieldId, boolean packed, List<Integer> value) {
    if (packed) {
      writeTag(fieldId, BinaryFormat.WIRETYPE_LENGTH_DELIMITED);
      // Compute the total data size so the length can be written.
      int dataSize = 0;
      final IntArrayList arrList;
      if (value instanceof IntArrayList) {
        arrList = (IntArrayList) value;
        for (int index = 0; index < value.size(); ++index) {
          dataSize += computeRawVarint32Size(arrList.getInt(index));
        }
      } else {
        arrList = null;
        for (Integer element : value) {
          dataSize += computeRawVarint32Size(element);
        }
      }
      writeVarint32(dataSize);
      if (arrList != null) {
        for (int index = 0; index < value.size(); ++index) {
          writeVarint32(arrList.getInt(index));
        }
      } else {
        for (Integer element : value) {
          writeVarint32(element);
        }
      }
    } else {
      if (value instanceof IntArrayList) {
        IntArrayList arrList = (IntArrayList) value;
        for (int index = 0; index < value.size(); ++index) {
          visitInt(fieldId, arrList.getInt(index));
        }
      } else {
        for (Integer element : value) {
          visitInt(fieldId, element);
        }
      }
    }
  }

  @Override
  public int getTotalBytesEncoded() {
    return position - offset;
  }

  @Override
  public void flush() {
    // Do nothing.
  }

  private void writeTag(int fieldId, int type) {
    writeVarint32(BinaryFormat.makeTag(fieldId, type));
  }

  private void writeVarint32(int value) {
    while (true) {
      if ((value & ~0x7F) == 0) {
        buffer[position++] = (byte) value;
        return;
      } else {
        buffer[position++] = (byte) ((value & 0x7F) | 0x80);
        value >>>= 7;
      }
    }
  }

  private void writeVarint64(long value) {
    while (true) {
      if ((value & ~0x7FL) == 0) {
        buffer[position++] = (byte) value;
        return;
      } else {
        buffer[position++] = (byte) (((int) value & 0x7F) | 0x80);
        value >>>= 7;
      }
    }
  }

  private void writeBytes(ByteString value) {
    int length = value.size();
    writeVarint32(length);
    value.copyTo(buffer, position);
    position += length;
  }

  private void writeString(String value) {
    final int oldPosition = position;
    // UTF-8 byte length of the string is at least its UTF-16 code unit length (value.length()),
    // and at most 3 times of it. We take advantage of this in both branches below.
    final int maxLength = value.length() * Utf8.MAX_BYTES_PER_CHAR;
    final int maxLengthVarIntSize = computeUInt32SizeNoTag(maxLength);
    final int minLengthVarIntSize = computeUInt32SizeNoTag(value.length());
    if (minLengthVarIntSize == maxLengthVarIntSize) {
      position = oldPosition + minLengthVarIntSize;
      int newPosition = Utf8.encode(value, buffer, position, limit - position);
      // Since this class is stateful and tracks the position, we rewind and store the state,
      // prepend the length, then reset it back to the end of the string.
      position = oldPosition;
      int length = newPosition - oldPosition - minLengthVarIntSize;
      writeVarint32(length);
      position = newPosition;
    } else {
      int length = Utf8.encodedLength(value);
      writeVarint32(length);
      position = Utf8.encode(value, buffer, position, limit - position);
    }
  }

  private void write(byte value)  {
    buffer[position++] = value;
  }

  /** Compute the number of bytes that would be needed to encode a tag. */
  public static int computeTagSize(int fieldNumber) {
    return computeRawVarint32Size(BinaryFormat.makeTag(fieldNumber, 0));
  }

  public static int computeIntListSize(int fieldNumber, boolean packed, final List<Integer> value) {
    if (packed) {
      int dataSize = 0;
      if (value instanceof IntArrayList) {
        IntArrayList intList = (IntArrayList) value;
        for (int index = 0; index < intList.size(); ++index) {
          dataSize += computeRawVarint32Size(intList.getInt(index));
        }
      } else {
        for (Integer aValue : value) {
          dataSize += computeRawVarint32Size(aValue);
        }
      }
      return dataSize + computeTagSize(fieldNumber) + computeRawVarint32Size(dataSize);
    } else {
      int size = 0;
      if (value instanceof IntArrayList) {
        IntArrayList intList = (IntArrayList) value;
        for (int index = 0; index < value.size(); ++index) {
          size += computeUInt32Size(fieldNumber, intList.getInt(index));
        }
      } else {
        for (Integer aValue : value) {
          size += computeUInt32Size(fieldNumber, aValue);
        }
      }
      return size;
    }
  }

  /**
   * Compute the number of bytes that would be needed to encode a varint.
   * {@code value} is treated as unsigned, so it won't be sign-extended if
   * negative.
   */
  public static int computeRawVarint32Size(final int value) {
    if ((value & (~0 <<  7)) == 0) return 1;
    if ((value & (~0 << 14)) == 0) return 2;
    if ((value & (~0 << 21)) == 0) return 3;
    if ((value & (~0 << 28)) == 0) return 4;
    return 5;
  }

  /** Compute the number of bytes that would be needed to encode a varint. */
  public static int computeRawVarint64Size(long value) {
    // handle two popular special cases up front ...
    if ((value & (~0L << 7)) == 0L) return 1;
    if (value < 0L) return 10;
    // ... leaving us with 8 remaining, which we can divide and conquer
    int n = 2;
    if ((value & (~0L << 35)) != 0L) { n += 4; value >>>= 28; }
    if ((value & (~0L << 21)) != 0L) { n += 2; value >>>= 14; }
    if ((value & (~0L << 14)) != 0L) { n += 1; }
    return n;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code double} field, including tag.
   */
  public static int computeDoubleSize(final int fieldNumber,
                                      final double value) {
    return computeTagSize(fieldNumber) + computeDoubleSizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code float} field, including tag.
   */
  public static int computeFloatSize(final int fieldNumber, final float value) {
    return computeTagSize(fieldNumber) + computeFloatSizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code uint64} field, including tag.
   */
  public static int computeUInt64Size(final int fieldNumber, final long value) {
    return computeTagSize(fieldNumber) + computeUInt64SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code int64} field, including tag.
   */
  public static int computeInt64Size(final int fieldNumber, final long value) {
    return computeTagSize(fieldNumber) + computeInt64SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code int32} field, including tag.
   */
  public static int computeInt32Size(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeInt32SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code fixed64} field, including tag.
   */
  public static int computeFixed64Size(final int fieldNumber,
                                       final long value) {
    return computeTagSize(fieldNumber) + computeFixed64SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code fixed32} field, including tag.
   */
  public static int computeFixed32Size(final int fieldNumber,
                                       final int value) {
    return computeTagSize(fieldNumber) + computeFixed32SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bool} field, including tag.
   */
  public static int computeBoolSize(final int fieldNumber,
                                    final boolean value) {
    return computeTagSize(fieldNumber) + computeBoolSizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code string} field, including tag.
   */
  public static int computeStringSize(final int fieldNumber,
                                      final String value) {
    return computeTagSize(fieldNumber) + computeStringSizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bytes} field, including tag.
   */
  public static int computeBytesSize(final int fieldNumber,
                                     final ByteString value) {
    return computeTagSize(fieldNumber) + computeBytesSizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bytes} field, including tag.
   */
  public static int computeByteArraySize(final int fieldNumber,
                                         final byte[] value) {
    return computeTagSize(fieldNumber) + computeByteArraySizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bytes} field, including tag.
   */
  public static int computeByteBufferSize(final int fieldNumber,
                                          final ByteBuffer value) {
    return computeTagSize(fieldNumber) + computeByteBufferSizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code uint32} field, including tag.
   */
  public static int computeUInt32Size(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeUInt32SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * enum field, including tag.  Caller is responsible for converting the
   * enum value to its numeric value.
   */
  public static int computeEnumSize(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeEnumSizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sfixed32} field, including tag.
   */
  public static int computeSFixed32Size(final int fieldNumber,
                                        final int value) {
    return computeTagSize(fieldNumber) + computeSFixed32SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sfixed64} field, including tag.
   */
  public static int computeSFixed64Size(final int fieldNumber,
                                        final long value) {
    return computeTagSize(fieldNumber) + computeSFixed64SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sint32} field, including tag.
   */
  public static int computeSInt32Size(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeSInt32SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sint64} field, including tag.
   */
  public static int computeSInt64Size(final int fieldNumber, final long value) {
    return computeTagSize(fieldNumber) + computeSInt64SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * unparsed MessageSet extension field to the stream.  For
   * historical reasons, the wire format differs from normal fields.
   */
  public static int computeRawMessageSetExtensionSize(
          final int fieldNumber, final ByteString value) {
    return computeTagSize(BinaryFormat.MESSAGE_SET_ITEM) * 2 +
            computeUInt32Size(BinaryFormat.MESSAGE_SET_TYPE_ID, fieldNumber) +
            computeBytesSize(BinaryFormat.MESSAGE_SET_MESSAGE, value);
  }

  // -----------------------------------------------------------------

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code double} field, including tag.
   */
  public static int computeDoubleSizeNoTag(final double value) {
    return LITTLE_ENDIAN_64_SIZE;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code float} field, including tag.
   */
  public static int computeFloatSizeNoTag(final float value) {
    return LITTLE_ENDIAN_32_SIZE;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code uint64} field, including tag.
   */
  public static int computeUInt64SizeNoTag(final long value) {
    return computeRawVarint64Size(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code int64} field, including tag.
   */
  public static int computeInt64SizeNoTag(final long value) {
    return computeRawVarint64Size(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code int32} field, including tag.
   */
  public static int computeInt32SizeNoTag(final int value) {
    if (value >= 0) {
      return computeRawVarint32Size(value);
    } else {
      // Must sign-extend.
      return 10;
    }
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code fixed64} field.
   */
  public static int computeFixed64SizeNoTag(final long value) {
    return LITTLE_ENDIAN_64_SIZE;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code fixed32} field.
   */
  public static int computeFixed32SizeNoTag(final int value) {
    return LITTLE_ENDIAN_32_SIZE;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bool} field.
   */
  public static int computeBoolSizeNoTag(final boolean value) {
    return 1;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code string} field.
   */
  public static int computeStringSizeNoTag(final String value) {
    int length;
    try {
      length = Utf8.encodedLength(value);
    } catch (Utf8.UnpairedSurrogateException e) {
      // TODO(dweis): Consider using nio Charset methods instead.
      final byte[] bytes = value.getBytes(InternalUtil.UTF_8);
      length = bytes.length;
    }

    return computeRawVarint32Size(length) + length;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bytes} field.
   */
  public static int computeBytesSizeNoTag(final ByteString value) {
    return computeRawVarint32Size(value.size()) +
            value.size();
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bytes} field.
   */
  public static int computeByteArraySizeNoTag(final byte[] value) {
    return computeRawVarint32Size(value.length) + value.length;
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code bytes} field.
   */
  public static int computeByteBufferSizeNoTag(final ByteBuffer value) {
    return computeRawVarint32Size(value.capacity()) + value.capacity();
  }

  /**
   * Compute the number of bytes that would be needed to encode a
   * {@code uint32} field.
   */
  public static int computeUInt32SizeNoTag(final int value) {
    return computeRawVarint32Size(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an enum field.
   * Caller is responsible for converting the enum value to its numeric value.
   */
  public static int computeEnumSizeNoTag(final int value) {
    return computeInt32SizeNoTag(value);
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sfixed32} field.
   */
  public static int computeSFixed32SizeNoTag(final int value) {
    return LITTLE_ENDIAN_32_SIZE;
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sfixed64} field.
   */
  public static int computeSFixed64SizeNoTag(final long value) {
    return LITTLE_ENDIAN_64_SIZE;
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sint32} field.
   */
  public static int computeSInt32SizeNoTag(final int value) {
    return computeRawVarint32Size(encodeZigZag32(value));
  }

  /**
   * Compute the number of bytes that would be needed to encode an
   * {@code sint64} field.
   */
  public static int computeSInt64SizeNoTag(final long value) {
    return computeRawVarint64Size(encodeZigZag64(value));
  }

  /**
   * Encode a ZigZag-encoded 32-bit value.  ZigZag encodes signed integers
   * into values that can be efficiently encoded with varint.  (Otherwise,
   * negative values must be sign-extended to 64 bits to be varint encoded,
   * thus always taking 10 bytes on the wire.)
   *
   * @param n A signed 32-bit integer.
   * @return An unsigned 32-bit integer, stored in a signed int because
   *         Java has no explicit unsigned support.
   */
  public static int encodeZigZag32(final int n) {
    // Note:  the right-shift must be arithmetic
    return (n << 1) ^ (n >> 31);
  }

  /**
   * Encode a ZigZag-encoded 64-bit value.  ZigZag encodes signed integers
   * into values that can be efficiently encoded with varint.  (Otherwise,
   * negative values must be sign-extended to 64 bits to be varint encoded,
   * thus always taking 10 bytes on the wire.)
   *
   * @param n A signed 64-bit integer.
   * @return An unsigned 64-bit integer, stored in a signed int because
   *         Java has no explicit unsigned support.
   */
  public static long encodeZigZag64(final long n) {
    // Note:  the right-shift must be arithmetic
    return (n << 1) ^ (n >> 63);
  }
}
