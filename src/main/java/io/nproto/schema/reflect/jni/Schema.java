package io.nproto.schema.reflect.jni;

import io.nproto.Writer;

public class Schema {
  private native void writeTo(Object message, Writer writer, long[] schemaData);
}
