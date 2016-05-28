package com.google.protobuf.experimental.schema;

import com.google.protobuf.experimental.Internal;
import com.google.protobuf.experimental.util.RandomString;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.DexFile;

import dalvik.system.DexClassLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;

@Internal
public class AndroidClassLoadingStrategy implements ClassLoadingStrategy {
  private static final int DEX_COMPATIBLE_API_VERSION = 13;
  private static final String JAR_FILE_EXTENSION = ".jar";
  private static final String CLASS_FILE_EXTENSION = ".class";

  /**
   * The name of the dex file that the {@link dalvik.system.DexClassLoader} expects to find inside of a jar file
   * that is handed to it as its argument.
   */
  private static final String DEX_CLASS_FILE = "classes.dex";

  private final File privateDirectory;

  private final DexOptions dexFileOptions = new DexOptions();
  private final CfOptions dexCompilerOptions = new CfOptions();

  /**
   * A generator for random string values.
   */
  private final RandomString randomString;

  private final ClassLoader parentClassLoader = AndroidClassLoadingStrategy.class.getClassLoader();

  /**
   * @param privateDirectory A directory that is <b>not shared with other applications</b> to be used for storing
   *                         generated classes and their processed forms.
   */
  AndroidClassLoadingStrategy(File privateDirectory) {
    dexFileOptions.targetApiLevel=DEX_COMPATIBLE_API_VERSION;
    this.privateDirectory = privateDirectory;
    randomString = new RandomString();
  }

  @Override
  public Class<?> loadClass(String name, byte[] binaryRepresentation) throws ClassNotFoundException {
    DexFile dexFile = new DexFile(dexFileOptions);
    dexFile.add(CfTranslator.translate(name.replace('.', '/') + CLASS_FILE_EXTENSION,
            binaryRepresentation,
            dexCompilerOptions,
            dexFileOptions));

    File zipFile = new File(privateDirectory, randomString.nextString() + JAR_FILE_EXTENSION);
    try {
      if (!zipFile.createNewFile()) {
        throw new IllegalStateException("Cannot create " + zipFile);
      }
      JarOutputStream zipOutputStream = new JarOutputStream(new FileOutputStream(zipFile));
      try {
        zipOutputStream.putNextEntry(new JarEntry(DEX_CLASS_FILE));
        dexFile.writeTo(zipOutputStream, null, false);
        zipOutputStream.closeEntry();
      } finally {
        zipOutputStream.close();
      }
      ClassLoader dexClassLoader = new DexClassLoader(zipFile.getAbsolutePath(),
              privateDirectory.getAbsolutePath(), null, parentClassLoader);
      return dexClassLoader.loadClass(name);
    } catch (IOException exception) {
      throw new IllegalStateException("Cannot write to zip file " + zipFile, exception);
    } finally {
      if (!zipFile.delete()) {
        Logger.getAnonymousLogger().warning("Could not delete " + zipFile);
      }
    }
  }
}
