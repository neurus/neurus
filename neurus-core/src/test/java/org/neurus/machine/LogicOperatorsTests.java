package org.neurus.machine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.neurus.machine.LogicOperators.ifEquals;
import static org.neurus.machine.LogicOperators.ifGreaterThan;
import static org.neurus.machine.LogicOperators.ifGreaterThanOrEquals;
import static org.neurus.machine.LogicOperators.ifLessThan;
import static org.neurus.machine.LogicOperators.ifLessThanOrEquals;

import org.junit.Test;

public class LogicOperatorsTests {

  @Test
  public void testIfEquals() {
    assertTrue(ifEquals().isBranching());
    double[] inputs = new double[] { 8, 9 };
    assertEquals(Operator.FALSE, ifEquals().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Operator.TRUE, ifEquals().execute(inputs2));
  }

  @Test
  public void testIfGreaterThan() {
    assertTrue(ifGreaterThan().isBranching());
    double[] inputs = new double[] { 8, 9 };
    assertEquals(Operator.FALSE, ifGreaterThan().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Operator.FALSE, ifGreaterThan().execute(inputs2));
    double[] inputs3 = new double[] { 9, 8 };
    assertEquals(Operator.TRUE, ifGreaterThan().execute(inputs3));
  }

  @Test
  public void testIfGreaterThanOrEquals() {
    assertTrue(ifGreaterThanOrEquals().isBranching());
    double[] inputs = new double[] { 8, 9 };
    assertEquals(Operator.FALSE, ifGreaterThanOrEquals().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Operator.TRUE, ifGreaterThanOrEquals().execute(inputs2));
    double[] inputs3 = new double[] { 9, 8 };
    assertEquals(Operator.TRUE, ifGreaterThanOrEquals().execute(inputs3));
  }

  @Test
  public void testIfLessThan() {
    assertTrue(ifLessThan().isBranching());
    double[] inputs = new double[] { 9, 8 };
    assertEquals(Operator.FALSE, ifLessThan().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Operator.FALSE, ifLessThan().execute(inputs2));
    double[] inputs3 = new double[] { 8, 9 };
    assertEquals(Operator.TRUE, ifLessThan().execute(inputs3));
  }

  @Test
  public void testIfLessThanOrEquals() {
    assertTrue(ifLessThanOrEquals().isBranching());
    double[] inputs = new double[] { 9, 8 };
    assertEquals(Operator.FALSE, ifLessThanOrEquals().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Operator.TRUE, ifLessThanOrEquals().execute(inputs2));
    double[] inputs3 = new double[] { 8, 9 };
    assertEquals(Operator.TRUE, ifLessThanOrEquals().execute(inputs3));
  }
}
