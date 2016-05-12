package io.nproto.schema.reflect;

import static io.nproto.UnsafeUtil.fieldOffset;

import io.nproto.Writer;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaUtil;
import io.nproto.schema.SchemaUtil.FieldInfo;

import java.util.List;

class NativeSchema<T> implements Schema<T> {
  private static final int ENTRIES_PER_FIELD = 2;

  private final long[] data;

  static <T> NativeSchema<T> newInstance(Class<T> messageType) {
    return new NativeSchema<T>(messageType);
  }

  private NativeSchema(Class<T> messageType) {
    data = getDataForType(messageType);
  }

  @Override
  public void writeTo(T message, Writer writer) {
    // TODO:
  }

  private static <T> long[] getDataForType(Class<T> messageType) {
    List<FieldInfo> fields = SchemaUtil.getAllFieldInfo(messageType);
    final int numFields = fields.size();
    long[] data = new long[numFields * ENTRIES_PER_FIELD];
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0, dataPos = 0; i < numFields; ++i) {
      FieldInfo f = fields.get(i);
      if (f.fieldNumber == lastFieldNumber) {
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      data[dataPos++] = (((long) f.fieldType.ordinal()) << 32) | f.fieldNumber;
      data[dataPos++] = fieldOffset(f.field);
    }
    return data;
  }
}
