package org.neurus.instruction;

import static junit.framework.Assert.assertTrue;
import static org.neurus.evolution.ByteCodeTestUtils.isCalculationRegister;
import static org.neurus.evolution.ByteCodeTestUtils.isConstantRegister;

import org.junit.Before;
import org.junit.Test;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class InstructionRandomizerTest {

  private RandomNumberGenerator rng;
  private Machine calculatorMachine;

  @Before
  public void setUp() {
    rng = new DefaultRandomNumberGenerator(1L);
    calculatorMachine = TestMachines.calculator();
  }

  @Test
  public void testFillInstructionsWithAllConstants() {
    InstructionRandomizer instrRandomizer = new InstructionRandomizer(calculatorMachine, rng, 1d);
    InstructionData instrData = calculatorMachine.createInstructionData();
    for (int x = 0; x < 10; x++) {
      instrRandomizer.fillRandomInstruction(instrData);
      isCalculationRegister(calculatorMachine, instrData.inputRegisters[0]);
      isConstantRegister(calculatorMachine, instrData.inputRegisters[1]);
    }
  }

  @Test
  public void testFillInstructionsWithNoConstants() {
    InstructionRandomizer instrRandomizer = new InstructionRandomizer(calculatorMachine, rng, 0d);
    InstructionData instrData = calculatorMachine.createInstructionData();
    for (int x = 0; x < 10; x++) {
      instrRandomizer.fillRandomInstruction(instrData);
      isCalculationRegister(calculatorMachine, instrData.inputRegisters[0]);
      isCalculationRegister(calculatorMachine, instrData.inputRegisters[1]);
    }
  }

  @Test
  public void testRandomFillInstructions() {
    InstructionRandomizer instrRandomizer = new InstructionRandomizer(calculatorMachine, rng, 0.5d);
    InstructionData instrData = calculatorMachine.createInstructionData();
    boolean atLeastOneConstant = false;
    boolean atLeastOneCalculation = false;
    for (int x = 0; x < 10; x++) {
      instrRandomizer.fillRandomInstruction(instrData);
      if (isCalculationRegister(calculatorMachine, instrData.inputRegisters[1])) {
        atLeastOneCalculation = true;
      }
      if (isConstantRegister(calculatorMachine, instrData.inputRegisters[1])) {
        atLeastOneConstant = true;
      }
    }
    assertTrue(atLeastOneConstant);
    assertTrue(atLeastOneCalculation);
  }

  @Test
  public void testInstrWithNoInputsAndOutputs() {
    Machine noInputsNoOutputsMachine = TestMachines.twoInstrnoInputsNoOutputs();
    InstructionData instrData = noInputsNoOutputsMachine.createInstructionData();
    InstructionRandomizer instrRandomizer = new InstructionRandomizer(calculatorMachine, rng, 0.5d);
    for (int x = 0; x < 10; x++) {
      instrRandomizer.fillRandomInstruction(instrData);
    }
  }

}
