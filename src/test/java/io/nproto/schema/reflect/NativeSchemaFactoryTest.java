package io.nproto.schema.reflect;

import org.junit.Test;

public class NativeSchemaFactoryTest {

  @Test
  public void test() {
    NativeLibraryLoader.load("nproto", getClass().getClassLoader());
    System.err.println("loaded the lib");
  }
}
