package org.neurus.instruction;

import static org.neurus.instruction.MathInstructions.addition;
import static org.neurus.instruction.MathInstructions.division;
import static org.neurus.instruction.MathInstructions.multiplication;
import static org.neurus.instruction.MathInstructions.substraction;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class InterpreterRunnerTest {

  private Machine machine;

  @Before
  public void setUp() {
    machine = new MachineBuilder()
        .withCalculationRegisters(6)
        .withConstantRegisters(10)
        .withInstruction(addition())
        .withInstruction(substraction())
        .withInstruction(multiplication())
        .withInstruction(division())
        .build();
  }

  @Test
  public void testExecutionOfTwoTimesThreePlusEightIsFourteen() {
    double[] inputs = new double [] {2, 3, 8};
    byte[] program = new byte[] {
        2, 0, 1, 3, //r3 = r0 * r1
        0, 3, 2, 0, //r0 = r3 + r2
    };
    InterpreterRunner runner = new InterpreterRunner(machine);
    double[] result = runner.run(program, inputs);
    Assert.assertEquals(14d, result[0]);
    //if I execute the same program again, it should still give the same result
    result = runner.run(program, inputs);
    Assert.assertEquals(14d, result[0]);
  }
}
