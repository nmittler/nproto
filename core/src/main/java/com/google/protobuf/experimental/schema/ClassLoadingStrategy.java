package com.google.protobuf.experimental.schema;


import com.google.protobuf.experimental.Internal;

@Internal
public interface ClassLoadingStrategy {
  Class<?> loadClass(String name, byte[] binaryRepresentation) throws ClassNotFoundException;
}
