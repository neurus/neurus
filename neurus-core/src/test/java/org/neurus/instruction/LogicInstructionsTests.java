package org.neurus.instruction;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.neurus.instruction.LogicInstructions.ifEquals;
import static org.neurus.instruction.LogicInstructions.ifGreaterThan;
import static org.neurus.instruction.LogicInstructions.ifLessThan;

import org.junit.Test;

public class LogicInstructionsTests {

  @Test
  public void testIfEquals() {
    assertTrue(ifEquals().isBranching());
    double[] inputs = new double[] { 8, 9 };
    assertEquals(Instruction.FALSE, ifEquals().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Instruction.TRUE, ifEquals().execute(inputs2));
  }

  @Test
  public void testIfGreaterThan() {
    assertTrue(ifGreaterThan().isBranching());
    double[] inputs = new double[] { 8, 9 };
    assertEquals(Instruction.FALSE, ifGreaterThan().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Instruction.FALSE, ifGreaterThan().execute(inputs2));
    double[] inputs3 = new double[] { 9, 8 };
    assertEquals(Instruction.TRUE, ifGreaterThan().execute(inputs3));
  }

  @Test
  public void testIfLessThan() {
    assertTrue(ifLessThan().isBranching());
    double[] inputs = new double[] { 9, 8 };
    assertEquals(Instruction.FALSE, ifLessThan().execute(inputs));
    double[] inputs2 = new double[] { 8, 8 };
    assertEquals(Instruction.FALSE, ifLessThan().execute(inputs2));
    double[] inputs3 = new double[] { 8, 9 };
    assertEquals(Instruction.TRUE, ifLessThan().execute(inputs3));
  }
}
