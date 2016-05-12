package io.nproto;

import io.nproto.ByteString;

import java.util.List;

/**
 * Created by nathanmittler on 4/4/16.
 */
public abstract class MessageVisitor {
  public abstract void visitInt(int fieldId, int value);
  public abstract void visitLong(int fieldId, long value);
  public abstract void visitBool(int fieldId, boolean value);
  public abstract void visitString(int fieldId, String value);
  public abstract void visitBytes(int fieldId, ByteString value);

  public abstract void visitIntList(int fieldId, boolean packed, List<Integer> value);
  // TODO: add other repeated types.
}
