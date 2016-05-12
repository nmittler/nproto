package io.nproto.schema.reflect;

import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NativeSchemaFactory implements SchemaFactory {
  private static final Logger logger = Logger.getLogger(NativeSchemaFactory.class.getName());
  private static final Throwable UNAVAILABILITY_CAUSE;

  static {
    Throwable cause = null;
    try {
      NativeLibraryLoader.load("native-schema", NativeSchemaFactory.class.getClassLoader());
    } catch (Throwable t) {
      cause = t;
      logger.log(Level.FINER,
              "Failed to load native-schema; " + NativeSchemaFactory.class.getName() +
                      " will be unavailable.", t);
    }
    UNAVAILABILITY_CAUSE = cause;
  }

  public boolean isAvailable() {
    return UNAVAILABILITY_CAUSE == null;
  }

  public Throwable getUnavailabilityCause() {
    return UNAVAILABILITY_CAUSE;
  }

  @Override
  public <T> Schema<T> createSchema(Class<T> messageType) {
    return NativeSchema.newInstance(messageType);
  }
}
