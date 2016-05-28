package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.Internal;

/**
 * Class loading strategy that uses a single child {@link ClassLoader} of the one used to load this
 * class.
 */
@Internal
public class ForwardingClassLoadingStrategy implements ClassLoadingStrategy {
  private static final GeneratedClassLoader CLASS_LOADER =
          new GeneratedClassLoader(ForwardingClassLoadingStrategy.class.getClassLoader());

  @Override
  public Class<?> loadClass(String name, byte[] binaryRepresentation) {
    return CLASS_LOADER.defineClass(binaryRepresentation);
  }

  private static final class GeneratedClassLoader extends ClassLoader {
    GeneratedClassLoader(ClassLoader classLoader) {
      super(classLoader);
    }

    Class<?> defineClass(byte[] bytes) {
      return defineClass(null, bytes, 0, bytes.length);
    }
  }
}
