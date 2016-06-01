package com.google.protobuf.experimental.schema.asm;

import com.google.protobuf.experimental.Internal;

/**
 * Class loading strategy that uses a single child {@link ClassLoader} of the one used to load this
 * class. Schemas loaded by this strategy will not have package-private access to the message
 * even though they were created in the same package. To enable package-private access,
 */
@Internal
public class ForwardingClassLoadingStrategy implements ClassLoadingStrategy {
  private final GeneratedClassLoader classLoader =
          new GeneratedClassLoader(ForwardingClassLoadingStrategy.class.getClassLoader());

  @Override
  public Class<?> loadSchemaClass(Class<?> messageClass, String name, byte[] binaryRepresentation) {
    return classLoader.defineClass(binaryRepresentation);
  }

  @Override
  public boolean isPackagePrivateAccessSupported() {
    return false;
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
