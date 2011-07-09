package org.neurus.util;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.times;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

public class CloseablesTest {

  @Test
  public void testCloseNullDoesNotBlow() {
    Closeables.close(null);
  }

  @Test
  public void testCloseSuccess() throws IOException {
    Closeable closeable = Mockito.mock(Closeable.class);
    Closeables.close(closeable);
    Mockito.verify(closeable, times(1)).close();
  }

  @Test
  public void testClosePropagatesIoExceptionAsRuntimeException() throws IOException {
    Closeable closeable = Mockito.mock(Closeable.class);
    IOException toBeThrown = new IOException();
    Mockito.doThrow(toBeThrown).when(closeable).close();
    try {
      Closeables.close(closeable);
      fail();
    } catch (RuntimeException ex) {
      assertSame(toBeThrown, ex.getCause());
    }
  }
}
