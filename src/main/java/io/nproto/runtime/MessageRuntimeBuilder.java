package io.nproto.runtime;

import io.nproto.Internal;
import io.nproto.Message;

/**
 * A builder for {@link MessageRuntime} instances.
 */
@Internal
public interface MessageRuntimeBuilder<M extends Message> extends MessageRuntimeContainer<M> {
  void mergeFrom(byte[] data);
}
