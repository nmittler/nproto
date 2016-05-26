package com.google.protobuf.experimental;

import java.nio.ByteBuffer;

/**
 * Provides a number of unsafe byte operations to be used by advanced applications with high
 * performance requirements. These methods are referred to as "unsafe" due to the fact that they
 * potentially expose the backing buffer of a {@link ByteString} to the application.
 *
 * <p><strong>DISCLAIMER:</strong> The methods in this class should only be called if it is
 * guaranteed that the buffer backing the {@link ByteString} will never change! Mutation of a
 * {@link ByteString} can lead to unexpected and undesirable consequences in your application,
 * and will likely be difficult to debug. Proceed with caution!
 */
@ExperimentalApi
public final class UnsafeByteOperations {
  private UnsafeByteOperations() {}

  public static ByteString unsafeWrap(byte[] buffer) {
    return ByteString.wrap(buffer);
  }

  public static ByteString unsafeWrap(byte[] buffer, int offset, int length) {
    return ByteString.wrap(buffer, offset, length);
  }

  /**
   * An unsafe operation that returns a {@link ByteString} that is backed by the provided buffer.
   *
   * @param buffer the Java NIO buffer to be wrapped.
   * @return a {@link ByteString} backed by the provided buffer.
   */
  public static ByteString unsafeWrap(ByteBuffer buffer) {
    if (buffer.hasArray()) {
      final int offset = buffer.arrayOffset();
      return ByteString.wrap(buffer.array(), offset + buffer.position(), buffer.remaining());
    } else {
      return new NioByteString(buffer);
    }
  }
}
