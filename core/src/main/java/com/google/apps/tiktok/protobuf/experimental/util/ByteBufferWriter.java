package com.google.apps.tiktok.protobuf.experimental.util;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

/**
 * Utility class to provide efficient writing of {@link ByteBuffer}s to {@link OutputStream}s.
 */
@InternalApi
public final class ByteBufferWriter {
  private ByteBufferWriter() {}

  /**
   * Minimum size for a cached buffer. This prevents us from allocating buffers that are too
   * small to be easily reused.
   */
  // TODO(nathanmittler): tune this property or allow configuration?
  private static final int MIN_CACHED_BUFFER_SIZE = 1024;

  /**
   * Maximum size for a cached buffer. If a larger buffer is required, it will be allocated
   * but not cached.
   */
  // TODO(nathanmittler): tune this property or allow configuration?
  private static final int MAX_CACHED_BUFFER_SIZE = 16 * 1024;

  /**
   * The fraction of the requested buffer size under which the buffer will be reallocated.
   */
  // TODO(nathanmittler): tune this property or allow configuration?
  private static final float BUFFER_REALLOCATION_THRESHOLD = 0.5f;

  /**
   * Keeping a soft reference to a thread-local buffer. This buffer is used for writing a
   * {@link ByteBuffer} to an {@link OutputStream} when no zero-copy alternative was available.
   * Using a "soft" reference since VMs may keep this reference around longer than "weak"
   * (e.g. HotSpot will maintain soft references until memory pressure warrants collection).
   */
  private static final ThreadLocal<SoftReference<byte[]>> BUFFER =
      new ThreadLocal<SoftReference<byte[]>>();

  /**
   * For testing purposes only. Clears the cached buffer to force a new allocation on the next
   * invocation.
   */
  public static void clearCachedBuffer() {
    BUFFER.set(null);
  }

  /**
   * Writes the remaining content of the buffer to the given stream. The buffer {@code position}
   * will remain unchanged by this method.
   */
  public static void write(ByteBuffer buffer, OutputStream output) throws IOException {
    final int initialPos = buffer.position();
    try {
      if (buffer.hasArray()) {
        // Optimized write for array-backed buffers.
        // Note that we're taking the risk that a malicious OutputStream could modify the array.
        output.write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
      } else if (output instanceof FileOutputStream) {
        // Use a channel to write out the ByteBuffer. This will automatically empty the buffer.
        ((FileOutputStream) output).getChannel().write(buffer);
      } else {
        // Read all of the data from the buffer to an array.
        // TODO(nathanmittler): Consider performance improvements for other "known" stream types.
        final byte[] array = getOrCreateBuffer(buffer.remaining());
        while (buffer.hasRemaining()) {
          int length = min(buffer.remaining(), array.length);
          buffer.get(array, 0, length);
          output.write(array, 0, length);
        }
      }
    } finally {
      // Restore the initial position.
      buffer.position(initialPos);
    }
  }

  private static byte[] getOrCreateBuffer(int requestedSize) {
    requestedSize = max(requestedSize, MIN_CACHED_BUFFER_SIZE);

    byte[] buffer = getBuffer();
    // Only allocate if we need to.
    if (buffer == null || needToReallocate(requestedSize, buffer.length)) {
      buffer = new byte[requestedSize];

      // Only cache the buffer if it's not too big.
      if (requestedSize <= MAX_CACHED_BUFFER_SIZE) {
        setBuffer(buffer);
      }
    }
    return buffer;
  }

  private static boolean needToReallocate(int requestedSize, int bufferLength) {
    // First check against just the requested length to avoid the multiply.
    return bufferLength < requestedSize
        && bufferLength < requestedSize * BUFFER_REALLOCATION_THRESHOLD;
  }

  private static byte[] getBuffer() {
    SoftReference<byte[]> sr = BUFFER.get();
    return sr == null ? null : sr.get();
  }

  private static void setBuffer(byte[] value) {
    BUFFER.set(new SoftReference<byte[]>(value));
  }
}
