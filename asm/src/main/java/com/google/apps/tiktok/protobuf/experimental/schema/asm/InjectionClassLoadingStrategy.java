package com.google.apps.tiktok.protobuf.experimental.schema.asm;

import com.google.apps.tiktok.protobuf.experimental.InternalApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * Class loading strategy that injects the schema class directly into the classloader that created
 * the message class. When using a child classloader (as is done by the
 * {@link ForwardingClassLoadingStrategy}, the generated class will not have package-private access
 * to the message, even though the schema is created within the same package as the message class.
 * Injecting the schema directly into the classloader of the message, however, will allow the
 * schema to access package-private fields.
 */
@InternalApi
public final class InjectionClassLoadingStrategy implements ClassLoadingStrategy {
  private final Method defineClassMethod;

  public InjectionClassLoadingStrategy() {
    try {
      defineClassMethod =
          ClassLoader.class
              .getDeclaredMethod(
                  "defineClass",
                  String.class,
                  byte[].class,
                  int.class,
                  int.class,
                  ProtectionDomain.class);
      defineClassMethod.setAccessible(true);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Class<?> loadSchemaClass(Class<?> messageClass, String name, byte[] binaryRepresentation) {
    try {
      return (Class<?>)
          defineClassMethod.invoke(
              messageClass.getClassLoader(),
              name,
              binaryRepresentation,
              0,
              binaryRepresentation.length,
              null);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean isPackagePrivateAccessSupported() {
    return true;
  }
}
