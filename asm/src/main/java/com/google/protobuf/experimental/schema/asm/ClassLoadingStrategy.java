package com.google.protobuf.experimental.schema.asm;


import com.google.protobuf.experimental.Internal;

@Internal
public interface ClassLoadingStrategy {
  Class<?> loadSchemaClass(Class<?> messageClass, String name, byte[] binaryRepresentation);
  boolean isPackagePrivateAccessSupported();
}
