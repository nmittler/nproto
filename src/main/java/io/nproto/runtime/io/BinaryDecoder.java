package io.nproto.runtime.io;

import io.nproto.ByteString;
import io.nproto.InternalUtil;

import java.util.List;

/**
 * Created by nathanmittler on 4/5/16.
 */
public final class BinaryDecoder extends Decoder {

  private final byte[] buffer;
  private final int limit;
  private int pos;
  private int lastTag;

  public BinaryDecoder(byte[] buffer) {
    this(buffer, 0, buffer.length);
  }

  public BinaryDecoder(byte[] buffer, int offset, int length) {
    this.buffer = buffer;
    this.pos = offset;
    this.limit = offset + length;
  }

  @Override
  public boolean hasRemaining() {
    return limit - pos > 0;
  }

  @Override
  public int readTag() {
    if (pos == limit) {
      lastTag = 0;
      return 0;
    }

    lastTag = readVarint32();
    if (BinaryFormat.getTagFieldNumber(lastTag) == 0) {
      // If we actually read zero (or any tag number corresponding to field
      // number zero), that's not a valid tag.
      throw new IllegalStateException("invalid tag");
    }
    return lastTag;
  }

  @Override
  public int readVarint32() {
    // See implementation notes for readRawVarint64
    fastpath: {
      int tempPos = pos;

      if (limit == tempPos) {
        break fastpath;
      }

      final byte[] buffer = this.buffer;
      int x;
      if ((x = buffer[tempPos++]) >= 0) {
        pos = tempPos;
        return x;
      } else if (limit - tempPos < 9) {
        break fastpath;
      } else if ((x ^= (buffer[tempPos++] << 7)) < 0) {
        x ^= (~0 << 7);
      } else if ((x ^= (buffer[tempPos++] << 14)) >= 0) {
        x ^= (~0 << 7) ^ (~0 << 14);
      } else if ((x ^= (buffer[tempPos++] << 21)) < 0) {
        x ^= (~0 << 7) ^ (~0 << 14) ^ (~0 << 21);
      } else {
        int y = buffer[tempPos++];
        x ^= y << 28;
        x ^= (~0 << 7) ^ (~0 << 14) ^ (~0 << 21) ^ (~0 << 28);
        if (y < 0 &&
                buffer[tempPos++] < 0 &&
                buffer[tempPos++] < 0 &&
                buffer[tempPos++] < 0 &&
                buffer[tempPos++] < 0 &&
                buffer[tempPos++] < 0) {
          break fastpath;  // Will throw malformedVarint()
        }
      }
      pos = tempPos;
      return x;
    }
    return (int) readRawVarint64SlowPath();
  }

  @Override
  public long readVarint64() {
    // Implementation notes:
    //
    // Optimized for one-byte values, expected to be common.
    // The particular code below was selected from various candidates
    // empirically, by winning VarintBenchmark.
    //
    // Sign extension of (signed) Java bytes is usually a nuisance, but
    // we exploit it here to more easily obtain the sign of bytes read.
    // Instead of cleaning up the sign extension bits by masking eagerly,
    // we delay until we find the final (positive) byte, when we clear all
    // accumulated bits with one xor.  We depend on javac to constant fold.
    fastpath: {
      int tempPos = pos;

      if (limit == tempPos) {
        break fastpath;
      }

      final byte[] buffer = this.buffer;
      long x;
      int y;
      if ((y = buffer[tempPos++]) >= 0) {
        pos = tempPos;
        return y;
      } else if (limit - tempPos < 9) {
        break fastpath;
      } else if ((y ^= (buffer[tempPos++] << 7)) < 0) {
        x = y ^ (~0 << 7);
      } else if ((y ^= (buffer[tempPos++] << 14)) >= 0) {
        x = y ^ ((~0 << 7) ^ (~0 << 14));
      } else if ((y ^= (buffer[tempPos++] << 21)) < 0) {
        x = y ^ ((~0 << 7) ^ (~0 << 14) ^ (~0 << 21));
      } else if ((x = ((long) y) ^ ((long) buffer[tempPos++] << 28)) >= 0L) {
        x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28);
      } else if ((x ^= ((long) buffer[tempPos++] << 35)) < 0L) {
        x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28) ^ (~0L << 35);
      } else if ((x ^= ((long) buffer[tempPos++] << 42)) >= 0L) {
        x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28) ^ (~0L << 35) ^ (~0L << 42);
      } else if ((x ^= ((long) buffer[tempPos++] << 49)) < 0L) {
        x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28) ^ (~0L << 35) ^ (~0L << 42)
                ^ (~0L << 49);
      } else {
        x ^= ((long) buffer[tempPos++] << 56);
        x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28) ^ (~0L << 35) ^ (~0L << 42)
                ^ (~0L << 49) ^ (~0L << 56);
        if (x < 0L) {
          if (buffer[tempPos++] < 0L) {
            break fastpath;  // Will throw malformedVarint()
          }
        }
      }
      pos = tempPos;
      return x;
    }
    return readRawVarint64SlowPath();
  }

  @Override
  public boolean readBool() {
    return read() == 1;
  }

  @Override
  public String readString() {
    int size = readVarint32();
    String result = new String(buffer, pos, size, InternalUtil.UTF_8);
    pos += size;
    return result;
  }

  @Override
  public ByteString readBytes() {
    int size = readVarint32();
    ByteString result = ByteString.copyFrom(buffer, pos, size);
    pos += size;
    return result;
  }

  public void readIntList(int fieldId, boolean packed, List<Integer> value) {
    if (packed) {
      final int fieldLimit = pos + readVarint32();
      while(pos < fieldLimit) {
        value.add(readVarint32());
      }
    } else {
      int nextFieldId = fieldId;
      int revertPos = pos;
      while (nextFieldId == fieldId) {
        value.add(readVarint32());
        revertPos = pos;
        nextFieldId = BinaryFormat.getTagFieldNumber(readTag());
      }
      // Restore the position to before the last tag was read.
      pos = revertPos;
    }
  }

  private long readRawVarint64SlowPath() {
    long result = 0;
    for (int shift = 0; shift < 64; shift += 7) {
      final byte b = read();
      result |= (long) (b & 0x7F) << shift;
      if ((b & 0x80) == 0) {
        return result;
      }
    }
    throw new IllegalStateException("Invalid varint");
  }

  private byte read() {
    return buffer[pos++];
  }
}
