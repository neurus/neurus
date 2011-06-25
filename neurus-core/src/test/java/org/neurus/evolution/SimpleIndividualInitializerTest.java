package org.neurus.evolution;

import static junit.framework.Assert.assertTrue;

import org.junit.Test;
import org.neurus.evolution.ByteCodeTestUtils.ProgramHelper;
import org.neurus.instruction.Machine;
import org.neurus.instruction.TestMachines;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class SimpleIndividualInitializerTest {

  private Machine calculatorMachine = TestMachines.calculator();
  private RandomNumberGenerator rng = new DefaultRandomNumberGenerator(1L);

  @Test
  public void testRandomNewIndividual() {
    SimpleIndividualInitializer initializer = new SimpleIndividualInitializer(
        calculatorMachine, rng, 5, 8, 0.5);
    boolean atLeastOneSizeFive = false;
    boolean atLeastOneSizeEight = false;
    for (int x = 0; x < 50; x++) {
      Individual individual = initializer.newIndividual();
      ProgramHelper program = ByteCodeTestUtils.parseProgram(
          individual.getProgram(), calculatorMachine);
      assertTrue(program.getInstructions().length >= 5);
      assertTrue(program.getInstructions().length <= 8);
      if (program.getInstructions().length == 5) {
        atLeastOneSizeFive = true;
      }
      if (program.getInstructions().length == 8) {
        atLeastOneSizeEight = true;
      }
    }
    assertTrue(atLeastOneSizeFive);
    assertTrue(atLeastOneSizeEight);
  }
}
