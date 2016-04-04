package io.nproto.runtime;

import io.nproto.Internal;
import io.nproto.Message;

/**
 * An object that contains a {@link MessageRuntime} (i.e. a message or a builder).
 */
@Internal
public interface MessageRuntimeContainer<M extends Message> {
  MessageRuntime<M, ? extends MessageRuntime> getRuntime();
}
