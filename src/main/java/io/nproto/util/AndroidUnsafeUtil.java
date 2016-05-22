package io.nproto.util;

import io.nproto.Internal;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

@Internal
public final class AndroidUnsafeUtil {
  private static final boolean IS_BIG_ENDIAN =
          ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
  private static final sun.misc.Unsafe UNSAFE = getUnsafe();

  private AndroidUnsafeUtil() {
  }

  public static long arrayBaseOffset(Class<?> arrayClass) {
    return UNSAFE.arrayBaseOffset(arrayClass);
  }

  public static Object getObject(Object target, long offset) {
    return UNSAFE.getObject(target, offset);
  }

  public static void putObject(Object target, long offset, Object value) {
    UNSAFE.putObject(target, offset, value);
  }

  public static long objectFieldOffset(Field field) {
      return UNSAFE.objectFieldOffset(field);
  }

  public static long objectFieldOffset(Class<?> klass, String field) {
    try {
      return objectFieldOffset(klass.getDeclaredField(field));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static boolean getBoolean(Object obj, long offset) {
    return getByte(obj, offset) != 0;
  }

  public static void putBoolean(Object obj, long offset, boolean value) {
    putByte(obj, offset, (byte) (value ? 1 : 0));
  }

  public static byte getByte(Object obj, long offset) {
    long alignedOffset = offset & ~3;
    long shiftAmount;
    if (IS_BIG_ENDIAN) {
      shiftAmount = (~offset & 3) * 8;
    } else {
      shiftAmount = (offset & 3) * 8;
    }
    return (byte) ((UNSAFE.getInt(obj, alignedOffset) >>> shiftAmount) & 0xFF);
  }

  public static void putByte(Object obj, long offset, byte value) {
    final long alignedOffset = offset & ~3;
    int intValue = getInt(obj, alignedOffset);
    int shiftAmount;
    if (IS_BIG_ENDIAN) {
      shiftAmount = (~((int) offset) & 3) * 8;
    } else {
      shiftAmount = (((int) offset) & 3) * 8;
    }
    intValue &= ((int) value) << shiftAmount;
    UNSAFE.putInt(obj, alignedOffset, intValue);
  }

  /*public static short getShort(Object obj, long offset) {
    // Since last bit should be zero, this could be 3, like in getByte
    long alignedOffset = offset & ~2;
    long shiftAmount;
    if (IS_BIG_ENDIAN) {
      shiftAmount = (~offset & 2) * 8;
    } else {
      shiftAmount = (offset & 2) * 8;
    }
    return (short) ((UNSAFE.getInt(obj, alignedOffset) >>> shiftAmount) & 0xFFFF);
  }*/

  public static int getInt(Object obj, long offset) {
    return UNSAFE.getInt(obj, offset);
  }

  public static void putInt(Object obj, long offset, int value) {
    UNSAFE.putInt(obj, offset, value);
  }

  public static long getLong(Object obj, long offset) {
    return UNSAFE.getLong(obj, offset);
  }

  public static void putLong(Object target, long offset, long value) {
    UNSAFE.putLong(target, offset, value);
  }

  public static float getFloat(Object obj, long offset) {
    return Float.intBitsToFloat(UNSAFE.getInt(obj, offset));
  }

  public static void putFloat(Object obj, long offset, float value) {
    UNSAFE.putInt(offset, Float.floatToIntBits(value));
  }

  public static double getDouble(Object obj, long offset) {
    return Double.longBitsToDouble(UNSAFE.getLong(obj, offset));
  }

  public static void putDouble(Object obj, long offset, double value) {
    UNSAFE.putLong(offset, Double.doubleToLongBits(value));
  }

  /**
   * Gets the {@code sun.misc.Unsafe} instance, or {@code null} if not available on this platform.
   */
  private static sun.misc.Unsafe getUnsafe() {
    sun.misc.Unsafe unsafe = null;
    try {
      unsafe = AccessController.doPrivileged(
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
}
