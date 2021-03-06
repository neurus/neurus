package org.neurus.util;

import static org.neurus.util.Primitives.ubtoi;

import org.junit.Assert;
import org.junit.Test;

public class PrimitivesTest {

  @Test
  public void testbtoi() {
    Assert.assertEquals(127, ubtoi((byte) 127));
    Assert.assertEquals(128, ubtoi((byte) -128));
    Assert.assertEquals(255, ubtoi((byte) -1));
  }
}
