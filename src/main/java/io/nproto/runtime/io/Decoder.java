package io.nproto.runtime.io;

import io.nproto.ByteString;

/**
 * Created by nathanmittler on 4/5/16.
 */
public abstract class Decoder {

  public abstract int readTag();
  public abstract int readVarint32();
  public abstract long readVarint64();
  public abstract boolean readBool();
  public abstract String readString();
  public abstract ByteString readBytes();
  public abstract boolean hasRemaining();
}
