package org.neurus.machine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.neurus.evolution.ByteCodeTestUtils.isCalculationRegister;
import static org.neurus.evolution.ByteCodeTestUtils.isConstantRegister;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class InstructionRandomizerTest {

  private RandomNumberGenerator rng;
  private RandomNumberGenerator mockRng;
  private Machine calculatorMachine;

  @Before
  public void setUp() {
    rng = new DefaultRandomNumberGenerator(1L);
    mockRng = Mockito.mock(RandomNumberGenerator.class);
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
  public void testInstrWithNoInputsAndNoDestinations() {
    Machine noInputsNoDestinationsMachine = TestMachines.twoInstrnoInputsNoDestination();
    InstructionData instrData = noInputsNoDestinationsMachine.createInstructionData();
    InstructionRandomizer instrRandomizer = new InstructionRandomizer(calculatorMachine, rng, 0.5d);
    for (int x = 0; x < 10; x++) {
      instrRandomizer.fillRandomInstruction(instrData);
    }
  }

  @Test
  public void testRandomRegister() {
    InstructionRandomizer randomizer = new InstructionRandomizer(calculatorMachine, mockRng, 0.7d);
    // case for calculation register
    when(mockRng.nextDouble()).thenReturn(0.71d);
    when(mockRng.nextInt(5 /* calculation registers */)).thenReturn(2);
    assertEquals(2, randomizer.randomRegister());

    // case for constant
    when(mockRng.nextDouble()).thenReturn(0.69d);
    when(mockRng.nextInt(10 /* constant registers */)).thenReturn(4);
    assertEquals(4 + 5, randomizer.randomRegister());
  }

  @Test
  public void testRandomOperator() {
    InstructionRandomizer randomizer = new InstructionRandomizer(calculatorMachine, mockRng, 0.7d);
    when(mockRng.nextInt(4 /* number of operators */)).thenReturn(3);
    assertEquals(3, randomizer.randomOperator());
  }

  @Test
  public void testRandomCalculationRegister() {
    InstructionRandomizer randomizer = new InstructionRandomizer(calculatorMachine, mockRng, 0.7d);
    when(mockRng.nextInt(5 /* number of calc reg */)).thenReturn(3);
    assertEquals(3, randomizer.randomCalculationRegister());
  }
}
