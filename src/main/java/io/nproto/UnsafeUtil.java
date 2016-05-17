package io.nproto;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * Utility class for working with unsafe operations.
 */
@Internal
// TODO(nathanmittler): Add support for Android Memory/MemoryBlock
public final class UnsafeUtil {
  private static final sun.misc.Unsafe UNSAFE = getUnsafe();
  private static final boolean HAS_UNSAFE_BYTEBUFFER_OPERATIONS =
          supportsUnsafeByteBufferOperations();
  private static final boolean HAS_UNSAFE_ARRAY_OPERATIONS = supportsUnsafeArrayOperations();
  private static final long BYTE_ARRAY_BASE_OFFSET = arrayBaseOffset(byte[].class);
  private static final long BUFFER_ADDRESS_OFFSET =
          UNSAFE == null ? -1 : fieldOffset(field(Buffer.class, "address"));

  private UnsafeUtil() {
  }

  public static boolean hasUnsafeArrayOperations() {
    return HAS_UNSAFE_ARRAY_OPERATIONS;
  }

  public static boolean hasUnsafeByteBufferOperations() {
    return HAS_UNSAFE_BYTEBUFFER_OPERATIONS;
  }

  public static long getByteArrayBaseOffset() {
    return BYTE_ARRAY_BASE_OFFSET;
  }

  public static long arrayBaseOffset(Class<?> arrayClass) {
    return HAS_UNSAFE_ARRAY_OPERATIONS ? UNSAFE.arrayBaseOffset(arrayClass) : -1;
  }

  /**
   * Returns the offset of the provided field, or {@code -1} if {@code sun.misc.Unsafe} is not
   * available.
   */
  public static long fieldOffset(Field field) {
    return UNSAFE.objectFieldOffset(field);
  }

  public static byte getByte(Object target, long offset) {
    return UNSAFE.getByte(target, offset);
  }

  public static void putByte(Object target, long offset, byte value) {
    UNSAFE.putByte(target, offset, value);
  }

  public static void putInt(Object target, long offset, int value) {
    UNSAFE.putInt(target, offset, value);
  }

  public static void putLong(Object target, long offset, long value) {
    UNSAFE.putLong(target, offset, value);
  }

  public static void putBoolean(Object target, long offset, boolean value) {
    UNSAFE.putBoolean(target, offset, value);
  }

  public static void putDouble(Object target, long offset, double value) {
    UNSAFE.putDouble(target, offset, value);
  }

  public static void putFloat(Object target, long offset, float value) {
    UNSAFE.putFloat(target, offset, value);
  }

  public static void putObject(Object target, long offset, Object value) {
    UNSAFE.putObject(target, offset, value);
  }

  public static long getLong(Object target, long offset) {
    return UNSAFE.getLong(target, offset);
  }

  public static float getFloat(Object target, long offset) {
    return UNSAFE.getFloat(target, offset);
  }

  public static double getDouble(Object target, long offset) {
    return UNSAFE.getDouble(target, offset);
  }

  public static int getInt(Object target, long offset) {
    return UNSAFE.getInt(target, offset);
  }

  public static boolean getBoolean(Object target, long offset) {
    return UNSAFE.getBoolean(target, offset);
  }

  public static Object getObject(Object target, long offset) {
    return UNSAFE.getObject(target, offset);
  }

  public static void copyMemory(
          Object src, long srcOffset, Object target, long targetOffset, long length) {
    UNSAFE.copyMemory(src, srcOffset, target, targetOffset, length);
  }

  public static byte getByte(long address) {
    return UNSAFE.getByte(address);
  }

  public static void putByte(long address, byte value) {
    UNSAFE.putByte(address, value);
  }

  public static long getLong(long address) {
    return UNSAFE.getLong(address);
  }

  public static void copyMemory(long srcAddress, long targetAddress, long length) {
    UNSAFE.copyMemory(srcAddress, targetAddress, length);
  }

  /**
   * Gets the offset of the {@code address} field of the given direct {@link ByteBuffer}.
   */
  public static long addressOffset(ByteBuffer buffer) {
    return UNSAFE.getLong(buffer, BUFFER_ADDRESS_OFFSET);
  }

  /**
   * Gets the {@code sun.misc.Unsafe} instance, or {@code null} if not available on this platform.
   */
  private static sun.misc.Unsafe getUnsafe() {
    sun.misc.Unsafe unsafe = null;
    try {
      unsafe =
              AccessController.doPrivileged(
                      new PrivilegedExceptionAction<Unsafe>() {
                        @Override
                        public sun.misc.Unsafe run() throws Exception {
                          Class<sun.misc.Unsafe> k = sun.misc.Unsafe.class;

                          for (Field f : k.getDeclaredFields()) {
                            f.setAccessible(true);
                            Object x = f.get(null);
                            if (k.isInstance(x)) {
                              return k.cast(x);
                            }
                          }
                          // The sun.misc.Unsafe field does not exist.
                          return null;
                        }
                      });
    } catch (Throwable e) {
      // Catching Throwable here due to the fact that Google AppEngine raises NoClassDefFoundError
      // for Unsafe.
    }
    return unsafe;
  }

  /**
   * Indicates whether or not unsafe array operations are supported on this platform.
   */
  private static boolean supportsUnsafeArrayOperations() {
    boolean supported = false;
    if (UNSAFE != null) {
      try {
        Class<?> clazz = UNSAFE.getClass();
        clazz.getMethod("arrayBaseOffset", Class.class);
        clazz.getMethod("getByte", Object.class, long.class);
        clazz.getMethod("putByte", Object.class, long.class, byte.class);
        clazz.getMethod("getLong", Object.class, long.class);
        clazz.getMethod(
                "copyMemory", Object.class, long.class, Object.class, long.class, long.class);
        supported = true;
      } catch (Throwable e) {
        // Do nothing.
      }
    }
    return supported;
  }

  private static boolean supportsUnsafeByteBufferOperations() {
    boolean supported = false;
    if (UNSAFE != null) {
      try {
        Class<?> clazz = UNSAFE.getClass();
        clazz.getMethod("objectFieldOffset", Field.class);
        clazz.getMethod("getByte", long.class);
        clazz.getMethod("getLong", Object.class, long.class);
        clazz.getMethod("putByte", long.class, byte.class);
        clazz.getMethod("getLong", long.class);
        clazz.getMethod("copyMemory", long.class, long.class, long.class);
        supported = true;
      } catch (Throwable e) {
        // Do nothing.
      }
    }
    return supported;
  }

  /**
   * Gets the field with the given name within the class, or {@code null} if not found. If found,
   * the field is made accessible.
   */
  private static Field field(Class<?> clazz, String fieldName) {
    Field field;
    try {
      field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
    } catch (Throwable t) {
      // Failed to access the fields.
      field = null;
    }
    return field;
  }
}