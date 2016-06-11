package com.google.apps.tiktok.protobuf.experimental.util;

import java.util.Random;

/**
 * A provider of randomized {@link String} values.
 */
public final class RandomString {

  /**
   * The default length of a randomized {@link String}.
   */
  public static final int DEFAULT_LENGTH = 8;

  /**
   * The symbols which are used to create a random {@link String}.
   */
  private static final char[] symbol;

  /*
   * Creates the symbol array.
   */
  static {
    symbol = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
  }

  /**
   * A provider of random values.
   */
  private final Random random;

  /**
   * The length of the random strings that are created by this instance.
   */
  private final int length;

  /**
   * Creates a random {@link String} provider where each {@link String} is of
   * {@link RandomString#DEFAULT_LENGTH} length.
   */
  public RandomString() {
    this(DEFAULT_LENGTH);
  }

  /**
   * Creates a random {@link String} provider where each value is of the given length.
   *
   * @param length The length of the random {@link String}.
   */
  public RandomString(int length) {
    if (length <= 0) {
      throw new IllegalArgumentException("A random string's length cannot be zero or negative");
    }
    this.length = length;
    random = new Random();
  }

  /**
   * Creates a random {@link String} of {@link RandomString#DEFAULT_LENGTH} length.
   *
   * @return A random {@link String}.
   */
  public static String make() {
    return make(DEFAULT_LENGTH);
  }

  /**
   * Creates a random {@link String} of the given {@code length}.
   *
   * @param length The length of the random {@link String}.
   * @return A random {@link String}.
   */
  public static String make(int length) {
    return new RandomString(length).nextString();
  }

  /**
   * Creates a new random {@link String}.
   *
   * @return A random {@link String} of the given length for this instance.
   */
  public String nextString() {
    char[] buffer = new char[length];
    for (int index = 0; index < length; index++) {
      buffer[index] = symbol[random.nextInt(symbol.length)];
    }
    return new String(buffer);
  }

  @Override
  public String toString() {
    return "RandomString{" + "random=" + random + ", length=" + length + '}';
  }
}
