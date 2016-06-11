package com.google.apps.tiktok.protobuf.experimental.util;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * Utility class for working with unsafe operations.
 */
@InternalApi
public final class UnsafeUtil {
  private static final Unsafe UNSAFE = getUnsafe();
  private static final Strategy STRATEGY =
      JdkUnsafeStrategy.IS_SUPPORTED ? new JdkUnsafeStrategy() : new AndroidUnsafeStrategy();
  private static final long BYTE_ARRAY_BASE_OFFSET =
      STRATEGY.isSupported() ? STRATEGY.arrayBaseOffset(byte[].class) : -1;

  private UnsafeUtil() {}

  public static boolean isSupported() {
    return STRATEGY.isSupported();
  }

  public static boolean hasDirectAddressingOperations() {
    return STRATEGY.hasDirectAddressingOperations();
  }

  public static long getByteArrayBaseOffset() {
    return BYTE_ARRAY_BASE_OFFSET;
  }

  public static long arrayBaseOffset(Class<?> arrayClass) {
    return STRATEGY.arrayBaseOffset(arrayClass);
  }

  /**
   * Returns the offset of the provided field, or {@code -1} if {@code sun.misc.Unsafe} is not
   * available.
   */
  public static long objectFieldOffset(Field field) {
    return STRATEGY.objectFieldOffset(field);
  }

  public static byte getByte(Object target, long offset) {
    return STRATEGY.getByte(target, offset);
  }

  public static void putByte(Object target, long offset, byte value) {
    STRATEGY.putByte(target, offset, value);
  }

  public static void putInt(Object target, long offset, int value) {
    STRATEGY.putInt(target, offset, value);
  }

  public static void putLong(Object target, long offset, long value) {
    STRATEGY.putLong(target, offset, value);
  }

  public static void putBoolean(Object target, long offset, boolean value) {
    STRATEGY.putBoolean(target, offset, value);
  }

  public static void putDouble(Object target, long offset, double value) {
    STRATEGY.putDouble(target, offset, value);
  }

  public static void putFloat(Object target, long offset, float value) {
    STRATEGY.putFloat(target, offset, value);
  }

  public static void putObject(Object target, long offset, Object value) {
    STRATEGY.putObject(target, offset, value);
  }

  public static long getLong(Object target, long offset) {
    return STRATEGY.getLong(target, offset);
  }

  public static float getFloat(Object target, long offset) {
    return STRATEGY.getFloat(target, offset);
  }

  public static double getDouble(Object target, long offset) {
    return STRATEGY.getDouble(target, offset);
  }

  public static int getInt(Object target, long offset) {
    return STRATEGY.getInt(target, offset);
  }

  public static boolean getBoolean(Object target, long offset) {
    return STRATEGY.getBoolean(target, offset);
  }

  public static Object getObject(Object target, long offset) {
    return STRATEGY.getObject(target, offset);
  }

  public static void copyMemory(
      Object src, long srcOffset, Object target, long targetOffset, long length) {
    STRATEGY.copyMemory(src, srcOffset, target, targetOffset, length);
  }

  public static byte getByte(long address) {
    return STRATEGY.getByte(address);
  }

  public static void putByte(long address, byte value) {
    STRATEGY.putByte(address, value);
  }

  public static long getLong(long address) {
    return STRATEGY.getLong(address);
  }

  public static void putLong(long address, long value) {
    STRATEGY.putLong(address, value);
  }

  public static void copyMemory(long srcAddress, long targetAddress, long length) {
    STRATEGY.copyMemory(srcAddress, targetAddress, length);
  }

  public static void setMemory(long address, long numBytes, byte value) {
    STRATEGY.setMemory(address, numBytes, value);
  }

  /**
   * Gets the offset of the {@code address} field of the given direct {@link ByteBuffer}.
   */
  public static long addressOffset(ByteBuffer buffer) {
    return STRATEGY.addressOffset(buffer);
  }

  /**
   * Gets the {@code sun.misc.Unsafe} instance, or {@code null} if not available on this platform.
   */
  private static Unsafe getUnsafe() {
    Unsafe unsafe = null;
    try {
      unsafe =
          AccessController.doPrivileged(
              new PrivilegedExceptionAction<Unsafe>() {
                @Override
                public Unsafe run() throws Exception {
                  Class<Unsafe> k = Unsafe.class;

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

  private abstract static class Strategy {
    abstract boolean isSupported();

    abstract boolean hasDirectAddressingOperations();

    abstract long arrayBaseOffset(Class<?> arrayClass);

    abstract long objectFieldOffset(Field field);

    abstract byte getByte(Object target, long offset);

    abstract int getInt(Object target, long offset);

    abstract long getLong(Object target, long offset);

    abstract boolean getBoolean(Object target, long offset);

    abstract float getFloat(Object target, long offset);

    abstract double getDouble(Object target, long offset);

    abstract Object getObject(Object target, long offset);

    abstract void putByte(Object target, long offset, byte value);

    abstract void putInt(Object target, long offset, int value);

    abstract void putLong(Object target, long offset, long value);

    abstract void putBoolean(Object target, long offset, boolean value);

    abstract void putFloat(Object target, long offset, float value);

    abstract void putDouble(Object target, long offset, double value);

    abstract void putObject(Object target, long offset, Object value);

    abstract void copyMemory(
        Object src, long srcOffset, Object target, long targetOffset, long length);

    // ByteBuffer operations (i.e. direct memory access).
    abstract long addressOffset(ByteBuffer buffer);

    abstract byte getByte(long address);

    abstract void putByte(long address, byte value);

    abstract long getLong(long address);

    abstract void putLong(long address, long value);

    abstract void copyMemory(long srcAddress, long targetAddress, long length);

    abstract void setMemory(long address, long numBytes, byte value);
  }

  private static final class JdkUnsafeStrategy extends Strategy {
    private static final boolean IS_SUPPORTED;
    private static final long BUFFER_ADDRESS_OFFSET;

    static {
      boolean supported = false;
      if (UNSAFE != null) {
        try {
          Class<?> clazz = UNSAFE.getClass();

          clazz.getMethod("objectFieldOffset", Field.class);

          // Heap operations.
          clazz.getMethod("arrayBaseOffset", Class.class);
          clazz.getMethod("getByte", Object.class, long.class);
          clazz.getMethod("getInt", Object.class, long.class);
          clazz.getMethod("getLong", Object.class, long.class);
          clazz.getMethod("getBoolean", Object.class, long.class);
          clazz.getMethod("getFloat", Object.class, long.class);
          clazz.getMethod("getDouble", Object.class, long.class);
          clazz.getMethod("getObject", Object.class, long.class);
          clazz.getMethod("putByte", Object.class, long.class, byte.class);
          clazz.getMethod("putInt", Object.class, long.class, int.class);
          clazz.getMethod("putLong", Object.class, long.class, long.class);
          clazz.getMethod("putBoolean", Object.class, long.class, boolean.class);
          clazz.getMethod("putFloat", Object.class, long.class, float.class);
          clazz.getMethod("putDouble", Object.class, long.class, double.class);
          clazz.getMethod("putObject", Object.class, long.class, Object.class);
          clazz.getMethod(
              "copyMemory", Object.class, long.class, Object.class, long.class, long.class);

          // Direct memory operations.
          clazz.getMethod("getByte", long.class);
          clazz.getMethod("putByte", long.class, byte.class);
          clazz.getMethod("getLong", long.class);
          clazz.getMethod("putLong", long.class, long.class);
          clazz.getMethod("setMemory", long.class, long.class, byte.class);
          clazz.getMethod("copyMemory", long.class, long.class, long.class);

          supported = true;
        } catch (Throwable e) {
          // Do nothing.
        }
      }
      IS_SUPPORTED = supported;
      BUFFER_ADDRESS_OFFSET =
          supported ? UNSAFE.objectFieldOffset(field(Buffer.class, "address")) : -1;
    }

    @Override
    boolean isSupported() {
      return IS_SUPPORTED;
    }

    @Override
    boolean hasDirectAddressingOperations() {
      return true;
    }

    @Override
    long arrayBaseOffset(Class<?> arrayClass) {
      return UNSAFE.arrayBaseOffset(arrayClass);
    }

    @Override
    long objectFieldOffset(Field field) {
      return UNSAFE.objectFieldOffset(field);
    }

    @Override
    byte getByte(Object target, long offset) {
      return UNSAFE.getByte(target, offset);
    }

    @Override
    int getInt(Object target, long offset) {
      return UNSAFE.getByte(target, offset);
    }

    @Override
    long getLong(Object target, long offset) {
      return UNSAFE.getLong(target, offset);
    }

    @Override
    boolean getBoolean(Object target, long offset) {
      return UNSAFE.getBoolean(target, offset);
    }

    @Override
    float getFloat(Object target, long offset) {
      return UNSAFE.getFloat(target, offset);
    }

    @Override
    double getDouble(Object target, long offset) {
      return UNSAFE.getDouble(target, offset);
    }

    @Override
    Object getObject(Object target, long offset) {
      return UNSAFE.getObject(target, offset);
    }

    @Override
    void putByte(Object target, long offset, byte value) {
      UNSAFE.putByte(target, offset, value);
    }

    @Override
    void putInt(Object target, long offset, int value) {
      UNSAFE.putInt(target, offset, value);
    }

    @Override
    void putLong(Object target, long offset, long value) {
      UNSAFE.putLong(target, offset, value);
    }

    @Override
    void putBoolean(Object target, long offset, boolean value) {
      UNSAFE.putBoolean(target, offset, value);
    }

    @Override
    void putFloat(Object target, long offset, float value) {
      UNSAFE.putFloat(target, offset, value);
    }

    @Override
    void putDouble(Object target, long offset, double value) {
      UNSAFE.putDouble(target, offset, value);
    }

    @Override
    void putObject(Object target, long offset, Object value) {
      UNSAFE.putObject(target, offset, value);
    }

    @Override
    void copyMemory(Object src, long srcOffset, Object target, long targetOffset, long length) {
      UNSAFE.copyMemory(src, srcOffset, target, targetOffset, length);
    }

    @Override
    long addressOffset(ByteBuffer buffer) {
      return UNSAFE.getLong(buffer, BUFFER_ADDRESS_OFFSET);
    }

    @Override
    byte getByte(long address) {
      return UNSAFE.getByte(address);
    }

    @Override
    void putByte(long address, byte value) {
      UNSAFE.putByte(address, value);
    }

    @Override
    long getLong(long address) {
      return UNSAFE.getLong(address);
    }

    @Override
    void putLong(long address, long value) {
      UNSAFE.putLong(address, value);
    }

    @Override
    void copyMemory(long srcAddress, long targetAddress, long length) {
      UNSAFE.copyMemory(srcAddress, targetAddress, length);
    }

    @Override
    void setMemory(long address, long numBytes, byte value) {
      UNSAFE.setMemory(address, numBytes, value);
    }
  }

  /**
   * Does not currently support direct memory operations.
   */
  // TODO(nathanmittler): Add support for Memory/MemoryBlock.
  private static final class AndroidUnsafeStrategy extends Strategy {
    private static final boolean IS_BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    private static final boolean IS_SUPPORTED;

    static {
      boolean supported = false;
      if (UNSAFE != null) {
        try {
          Class<?> clazz = UNSAFE.getClass();

          clazz.getMethod("objectFieldOffset", Field.class);

          // Heap operations.
          clazz.getMethod("arrayBaseOffset", Class.class);
          clazz.getMethod("getInt", Object.class, long.class);
          clazz.getMethod("getLong", Object.class, long.class);
          clazz.getMethod("getObject", Object.class, long.class);
          clazz.getMethod("putInt", Object.class, long.class, int.class);
          clazz.getMethod("putLong", Object.class, long.class, long.class);
          clazz.getMethod("putObject", Object.class, long.class, Object.class);

          supported = true;
        } catch (Throwable e) {
          // Do nothing.
        }
      }
      IS_SUPPORTED = supported;
    }

    @Override
    boolean isSupported() {
      return IS_SUPPORTED;
    }

    @Override
    boolean hasDirectAddressingOperations() {
      return false;
    }

    @Override
    long arrayBaseOffset(Class<?> arrayClass) {
      return UNSAFE.arrayBaseOffset(arrayClass);
    }

    @Override
    long objectFieldOffset(Field field) {
      return UNSAFE.objectFieldOffset(field);
    }

    @Override
    byte getByte(Object target, long offset) {
      if (IS_BIG_ENDIAN) {
        return (byte) ((UNSAFE.getInt(target, offset & ~3) >>> ((~offset & 3) << 3)) & 0xFF);
      } else {
        return (byte) ((UNSAFE.getInt(target, offset & ~3) >>> ((offset & 3) << 3)) & 0xFF);
      }
    }

    @Override
    int getInt(Object target, long offset) {
      return UNSAFE.getInt(target, offset);
    }

    @Override
    long getLong(Object target, long offset) {
      return UNSAFE.getLong(target, offset);
    }

    @Override
    boolean getBoolean(Object target, long offset) {
      return getByte(target, offset) != 0;
    }

    @Override
    float getFloat(Object target, long offset) {
      return Float.intBitsToFloat(UNSAFE.getInt(target, offset));
    }

    @Override
    double getDouble(Object target, long offset) {
      return Double.longBitsToDouble(UNSAFE.getLong(target, offset));
    }

    @Override
    Object getObject(Object target, long offset) {
      return UNSAFE.getObject(target, offset);
    }

    @Override
    void putByte(Object target, long offset, byte value) {
      if (IS_BIG_ENDIAN) {
        int intValue = UNSAFE.getInt(target, offset & ~3);
        intValue &= ((int) value) << ((~((int) offset) & 3) << 3);
        UNSAFE.putInt(target, offset & ~3, intValue);
      } else {
        int intValue = UNSAFE.getInt(target, offset & ~3);
        intValue &= ((int) value) << ((((int) offset) & 3) << 3);
        UNSAFE.putInt(target, offset & ~3, intValue);
      }
    }

    @Override
    void putInt(Object target, long offset, int value) {
      UNSAFE.putInt(target, offset, value);
    }

    @Override
    void putLong(Object target, long offset, long value) {
      UNSAFE.putLong(target, offset, value);
    }

    @Override
    void putBoolean(Object target, long offset, boolean value) {
      putByte(target, offset, (byte) (value ? 1 : 0));
    }

    @Override
    void putFloat(Object target, long offset, float value) {
      UNSAFE.putInt(target, offset, Float.floatToIntBits(value));
    }

    @Override
    void putDouble(Object target, long offset, double value) {
      UNSAFE.putLong(target, offset, Double.doubleToLongBits(value));
    }

    @Override
    void putObject(Object target, long offset, Object value) {
      UNSAFE.putObject(target, offset, value);
    }

    @Override
    void copyMemory(Object src, long srcOffset, Object target, long targetOffset, long length) {
      throw new UnsupportedOperationException();
    }

    @Override
    long addressOffset(ByteBuffer buffer) {
      throw new UnsupportedOperationException();
    }

    @Override
    byte getByte(long address) {
      throw new UnsupportedOperationException();
    }

    @Override
    void putByte(long address, byte value) {
      throw new UnsupportedOperationException();
    }

    @Override
    long getLong(long address) {
      throw new UnsupportedOperationException();
    }

    @Override
    void putLong(long address, long value) {
      throw new UnsupportedOperationException();
    }

    @Override
    void copyMemory(long srcAddress, long targetAddress, long length) {
      throw new UnsupportedOperationException();
    }

    @Override
    void setMemory(long address, long numBytes, byte value) {
      throw new UnsupportedOperationException();
    }
  }
}
