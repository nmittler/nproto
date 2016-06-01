package com.google.protobuf.experimental.schema;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * For testing purposes.
 */
public class RandomSchemaNamingStrategy implements SchemaNamingStrategy {
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final Random RANDOM = new Random(100);

  private final int shortNameLength;
  private final ConcurrentHashMap<Class<?>, String> names = new ConcurrentHashMap<Class<?>, String>();

  RandomSchemaNamingStrategy(int shortNameLength) {
    this.shortNameLength = shortNameLength;
  }

  @Override
  public String schemaNameFor(Class<?> messageClass) {
    String newName = generateNewName(messageClass);
    String previousName = names.putIfAbsent(messageClass, newName);
    return previousName != null ? previousName : newName;
  }

  private String generateNewName(Class<?> messageClass) {
    StringBuilder builder = new StringBuilder();
    builder.append(messageClass.getPackage().getName());
    builder.append('.');
    for (int i = 0; i < shortNameLength; ++i) {
      builder.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
    }
    return builder.toString();
  }
}
