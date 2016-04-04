package io.nproto.runtime;

import io.nproto.Internal;
import io.nproto.Message;
import io.nproto.runtime.io.BinaryEncoder;

/**
 * Contains and manages the internal state for a message.
 */
@Internal
public interface MessageRuntime <M extends Message, R extends MessageRuntime> {
  int getRuntimeVersion();

  M getDefaultInstanceForType();

  int getSerializedMessageSize();

  void visit(MessageVisitor visitor);

  void mergeFrom(byte[] data);

  R makeImmutable();

  R mutableCopy();
}
