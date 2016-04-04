package io.nproto.runtime.io;

import io.nproto.runtime.MessageVisitor;

/**
 * Created by nathanmittler on 4/4/16.
 */
public abstract class Encoder extends MessageVisitor {

  public abstract int getTotalBytesEncoded();
  public abstract void flush();
}
