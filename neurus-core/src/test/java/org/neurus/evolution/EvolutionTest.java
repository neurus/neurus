package org.neurus.evolution;

import org.junit.Test;
import org.neurus.instruction.FakeInstruction;
import org.neurus.instruction.Instruction;
import org.neurus.instruction.InstructionSet;
import org.neurus.instruction.InstructionSetBuilder;

public class EvolutionTest {

  private Instruction fakeInstruction1 = new FakeInstruction();
  private Instruction fakeInstruction2 = new FakeInstruction();

  @Test
  public void testEvolution() {
    InstructionSet instructionSet = new InstructionSetBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(20)
        .withInstruction(fakeInstruction1)
        .withInstruction(fakeInstruction2)
        .build();
    Evolution evolution = new Evolution(instructionSet);
    evolution.evolve();
  }
}
