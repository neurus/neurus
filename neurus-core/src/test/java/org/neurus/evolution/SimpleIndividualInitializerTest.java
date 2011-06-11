package org.neurus.evolution;

import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.neurus.evolution.ByteCodeTestUtils.ProgramInstruction;
import org.neurus.instruction.FakeInstruction;
import org.neurus.instruction.InstructionSet;
import org.neurus.instruction.InstructionSetBuilder;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class SimpleIndividualInitializerTest {

  private InstructionSet iSet2Inputs1Output;
  private RandomNumberGenerator rng;

  @Before
  public void setUp() {
    rng = new DefaultRandomNumberGenerator(1L);
    iSet2Inputs1Output = new InstructionSetBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(10)
        .withInstruction(new FakeInstruction(2, true))
        .withInstruction(new FakeInstruction(2, true))
        .build();
  }

  @Test
  public void testNewIndividualWithAllConstants() {
    IndividualInitializer initializer = new SimpleIndividualInitializer(iSet2Inputs1Output, rng,
        10 /* min */, 10 /* max */, 1 /* pconst */);
    Individual individual = initializer.newIndividual();
    ByteCodeTestUtils.Program program = ByteCodeTestUtils.readBytecode(individual.getByteCode(),
          iSet2Inputs1Output);
    for (ProgramInstruction pi : program.getInstructions()) {
      assertTrue(pi.isCalculationRegister(0));
      assertTrue(pi.isConstantRegister(1));
    }
  }

  @Test
  public void testNewIndividualWithNoConstants() {
    IndividualInitializer initializer = new SimpleIndividualInitializer(iSet2Inputs1Output, rng,
        10 /* min */, 10 /* max */, 0 /* pconst */);
    Individual individual = initializer.newIndividual();
    ByteCodeTestUtils.Program program = ByteCodeTestUtils.readBytecode(individual.getByteCode(),
          iSet2Inputs1Output);
    for (ProgramInstruction pi : program.getInstructions()) {
      assertTrue(pi.isCalculationRegister(0));
      assertTrue(pi.isCalculationRegister(1));
    }
  }

  @Test
  public void testNewIndividualRandom() {
    IndividualInitializer initializer = new SimpleIndividualInitializer(iSet2Inputs1Output, rng,
        5 /* min */, 8 /* max */, 0.5 /* pconst */);
    boolean atLeastOneSizeFive = false;
    boolean atLeastOneSizeEight = false;
    boolean atLeastOneConstant = false;
    boolean atLeastOneCalculation = false;
    for (int x = 0; x < 50; x++) {
      Individual individual = initializer.newIndividual();
      ByteCodeTestUtils.Program program = ByteCodeTestUtils.readBytecode(individual.getByteCode(),
          iSet2Inputs1Output);
      assertTrue(program.getInstructions().length >= 5);
      assertTrue(program.getInstructions().length <= 8);
      if (program.getInstructions().length == 5) {
        atLeastOneSizeFive = true;
      }
      if (program.getInstructions().length == 8) {
        atLeastOneSizeEight = true;
      }
      for (int i = 0; i < program.getInstructions().length; i++) {
        ProgramInstruction pi = program.getInstructions()[i];
        assertTrue(pi.isCalculationRegister(0));
        if (pi.isCalculationRegister(1)) {
          atLeastOneCalculation = true;
        }
        if (pi.isConstantRegister(1)) {
          atLeastOneConstant = true;
        }
      }
    }
    assertTrue(atLeastOneSizeFive);
    assertTrue(atLeastOneSizeEight);
    assertTrue(atLeastOneConstant);
    assertTrue(atLeastOneCalculation);
  }

  @Test
  public void testNewIndividualWithNoInputsAndOutputs() {
    InstructionSet noInputsNoOutputsInstrSet = new InstructionSetBuilder()
        .withCalculationRegisters(0)
        .withConstantRegisters(0)
        .withInstruction(new FakeInstruction(0, false))
        .withInstruction(new FakeInstruction(0, false))
        .build();
    IndividualInitializer initializer = new SimpleIndividualInitializer(noInputsNoOutputsInstrSet,
        rng, 10 /* min */, 10 /* max */, 0.5 /* pconst */);
    Individual individual = initializer.newIndividual();
    ByteCodeTestUtils.Program program = ByteCodeTestUtils.readBytecode(individual.getByteCode(),
        noInputsNoOutputsInstrSet);
    assertTrue(program.getInstructions().length == 10);
  }
}
