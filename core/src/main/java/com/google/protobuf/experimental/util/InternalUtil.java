package com.google.protobuf.experimental.util;

import com.google.protobuf.experimental.Internal;

import java.nio.charset.Charset;

@Internal
public final class InternalUtil {
  private InternalUtil() {}

  public static final Charset UTF_8 = Charset.forName("UTF-8");

  /**
   * An empty byte array constant used in generated code.
   */
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  public static int INVALID_SIZE = -1;

  /**
   * Helper method for continuously hashing bytes.
   */
  public static int partialHash(int h, byte[] bytes, int offset, int length) {
    for (int i = offset; i < offset + length; i++) {
      h = h * 31 + bytes[i];
    }
    return h;
  }
}
