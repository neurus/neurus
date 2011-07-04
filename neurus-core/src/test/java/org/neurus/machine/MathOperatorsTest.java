package org.neurus.machine;

import static junit.framework.Assert.assertEquals;
import static org.neurus.machine.MathOperators.addition;
import static org.neurus.machine.MathOperators.division;
import static org.neurus.machine.MathOperators.multiplication;
import static org.neurus.machine.MathOperators.substraction;

import org.junit.Test;

public class MathOperatorsTest {

  @Test
  public void testAddition() {
    double[] inputs = new double[] { 8, 9 };
    assertEquals(17d, addition().execute(inputs));
  }

  @Test
  public void testSubstraction() {
    double[] inputs = new double[] { 8, 9 };
    assertEquals(-1d, substraction().execute(inputs));
  }

  @Test
  public void testDivision() {
    double[] inputs = new double[] { 2, 10 };
    assertEquals(0.2d, division().execute(inputs));
  }

  @Test
  public void testProtectedDivision() {
    double[] inputs = new double[] { 4, 0 };
    assertEquals(0d, division().execute(inputs));
    double[] inputs2 = new double[] { 4, Double.POSITIVE_INFINITY };
    assertEquals(0d, division().execute(inputs2));
  }

  @Test
  public void testMultiplication() {
    double[] inputs = new double[] { 2, 0.3 };
    assertEquals(0.6d, multiplication().execute(inputs));
  }

  @Test
  public void testProtectedMultiplication() {
    double[] inputs = new double[] { 4, Double.NaN };
    assertEquals(0d, multiplication().execute(inputs));
    double[] inputs2 = new double[] { 4, Double.POSITIVE_INFINITY };
    assertEquals(0d, multiplication().execute(inputs2));
  }
}
