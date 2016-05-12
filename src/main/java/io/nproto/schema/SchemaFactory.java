package io.nproto.schema;

/**
 * Created by nathanmittler on 5/9/16.
 */
public interface SchemaFactory {
  <T> Schema<T> createSchema(Class<T> messageType);
}
