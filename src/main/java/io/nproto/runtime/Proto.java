package io.nproto.runtime;

import io.nproto.ByteString;
import io.nproto.Message;
import io.nproto.UnsafeByteOperations;
import io.nproto.runtime.io.BinaryEncoder;

/**
 * This is the main entry-point for interacting with the protobuf runtime.
 */
public final class Proto {
  final static int RUNTIME_VERSION = 1;

  private Proto() {
  }

  public <T extends Message> T getDefaultInstanceForType(T message) {
    return getRuntime(message).getDefaultInstanceForType();
  }

  public int getSerializedMessageSize(Message message) {
    return getRuntime(message).getSerializedMessageSize();
  }

  public ByteString toByteString(Message message) {
    byte[] bytes = toByteArray(message);
    return UnsafeByteOperations.unsafeWrap(bytes);
  }

  public <T extends Message> byte[] toByteArray(T message) {
    MessageRuntime<T, ? extends MessageRuntime> runtime = getRuntime(message);
    byte[] bytes = new byte[runtime.getSerializedMessageSize()];
    BinaryEncoder encoder = new BinaryEncoder(bytes);
    runtime.visit(encoder);
    encoder.flush();
    return bytes;
  }

  public <T extends Message> void visit(T message, MessageVisitor visitor) {
    getRuntime(message).visit(visitor);
  }

  public <T extends Message> void mergeFrom(Message.Builder<T> builder, byte[] data) {
    getRuntimeBuilder(builder).mergeFrom(data);
  }

  private static <T extends Message> MessageRuntime<T, ? extends MessageRuntime> getRuntime(T message) {
    return checkSupported(((MessageRuntimeContainer<T>) message).getRuntime());
  }

  private static <T extends Message> MessageRuntimeBuilder<T> getRuntimeBuilder(Message.Builder<T> builder) {
    MessageRuntimeBuilder<T> runtimeBuilder = (MessageRuntimeBuilder<T>) builder;
    checkSupported(runtimeBuilder.getRuntime());
    return runtimeBuilder;
  }

  private static <T extends Message, R extends MessageRuntime> boolean isRuntimeSupported(MessageRuntime<T, R> runtime) {
    return runtime.getRuntimeVersion() == RUNTIME_VERSION;
  }

  private static <T extends Message, R extends MessageRuntime> MessageRuntime<T, R> checkSupported(MessageRuntime<T, R> runtime) {
    if (!isRuntimeSupported(runtime)) {
      throw new IllegalArgumentException(
              String.format("Unsupported message version=%d (Runtime version=%d)",
                      runtime.getRuntimeVersion(), RUNTIME_VERSION));
    }
    return runtime;
  }
}
