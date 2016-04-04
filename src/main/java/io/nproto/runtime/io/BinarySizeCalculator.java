package io.nproto.runtime.io;

import io.nproto.ByteString;
import io.nproto.runtime.MessageVisitor;

import java.util.List;

/**
 * Created by nathanmittler on 4/5/16.
 */
public final class BinarySizeCalculator extends MessageVisitor {
  private int size;

  @Override
  public void visitInt(int fieldId, int value) {
    size += BinaryEncoder.computeInt32Size(fieldId, value);
  }

  @Override
  public void visitLong(int fieldId, long value) {
    size += BinaryEncoder.computeInt64Size(fieldId, value);
  }

  @Override
  public void visitBool(int fieldId, boolean value) {
    size += BinaryEncoder.computeBoolSize(fieldId, value);
  }

  @Override
  public void visitString(int fieldId, String value) {
    size += BinaryEncoder.computeStringSize(fieldId, value);
  }

  @Override
  public void visitBytes(int fieldId, ByteString value) {
    size += BinaryEncoder.computeBytesSize(fieldId, value);
  }

  @Override
  public void visitIntList(int fieldId, boolean packed, List<Integer> value) {
    size += BinaryEncoder.computeIntListSize(fieldId, packed, value);
  }

  public int getSize() {
    return size;
  }
}
