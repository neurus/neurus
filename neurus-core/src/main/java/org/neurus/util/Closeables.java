package org.neurus.util;

import java.io.Closeable;
import java.io.IOException;

import com.google.common.base.Throwables;

public class Closeables {

  private Closeables() {
  };

  public static void close(Closeable closeable) {
    if (closeable == null) {
      return;
    }
    try {
      closeable.close();
    } catch (IOException ex) {
      throw Throwables.propagate(ex);
    }
  }
}
