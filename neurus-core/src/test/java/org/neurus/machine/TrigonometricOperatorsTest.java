package org.neurus.machine;

import static junit.framework.Assert.assertEquals;
import static org.neurus.machine.TrigonometricOperators.cos;
import static org.neurus.machine.TrigonometricOperators.sin;

import org.junit.Test;

public class TrigonometricOperatorsTest {

  @Test
  public void testSin() {
    double[] inputs = new double[] { 2.4 };
    assertEquals(0.675463181, sin().execute(inputs), 0.000001);
  }

  @Test
  public void testCos() {
    double[] inputs = new double[] { 2.4 };
    assertEquals((-0.737393716), cos().execute(inputs), 0.000001);
  }
}
