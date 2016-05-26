package com.google.protobuf.experimental;

/**
 * Created by nathanmittler on 4/4/16.
 */
public interface Message {

  interface Builder<T extends Message> {
    T build();
  }
}
