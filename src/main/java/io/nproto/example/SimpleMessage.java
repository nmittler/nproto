package io.nproto.example;

import io.nproto.ByteString;
import io.nproto.Message;

import java.util.Collection;
import java.util.List;

/**
 * Created by nathanmittler on 4/4/16.
 */
public abstract class SimpleMessage implements Message {
  public abstract int getInt();

  public abstract long getLong();

  public abstract boolean getBool();

  public abstract String getString();

  public abstract ByteString getBytes();

  public abstract List<Integer> getRepeated();

  public abstract Builder newBuilder();

  public abstract Builder toBuilder();

  public static abstract class Builder implements Message.Builder<SimpleMessage> {
    public abstract void setInt(int value);

    public abstract void setLong(long value);

    public abstract void setBool(boolean value);

    public abstract void setString(String value);

    public abstract void setBytes(ByteString value);

    public abstract void addRepeated(Integer value);

    public abstract void addAllRepeated(Collection<Integer> values);
  }
}
