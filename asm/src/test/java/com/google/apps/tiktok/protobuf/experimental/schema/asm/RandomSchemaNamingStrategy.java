package com.google.apps.tiktok.protobuf.experimental.schema.asm;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;
import com.google.apps.tiktok.protobuf.experimental.schema.asm.SchemaNamingStrategy;
import com.google.apps.tiktok.protobuf.experimental.util.RandomString;

import java.util.concurrent.ConcurrentHashMap;

/**
 * For testing purposes only. Generates random schema names.
 */
@InternalApi
public final class RandomSchemaNamingStrategy implements SchemaNamingStrategy {

  private final RandomString randomString;
  private final ConcurrentHashMap<Class<?>, String> names =
      new ConcurrentHashMap<Class<?>, String>();

  public RandomSchemaNamingStrategy(int shortNameLength) {
    randomString = new RandomString(shortNameLength);
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
    builder.append(randomString.nextString());
    return builder.toString();
  }
}
