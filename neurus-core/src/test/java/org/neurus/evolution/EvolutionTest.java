package org.neurus.evolution;

import org.junit.Test;
import org.neurus.instruction.FakeInstruction;
import org.neurus.instruction.Instruction;
import org.neurus.instruction.Machine;
import org.neurus.instruction.MachineBuilder;

public class EvolutionTest {

  private Instruction fakeInstruction1 = new FakeInstruction();
  private Instruction fakeInstruction2 = new FakeInstruction();

  @Test
  public void testEvolution() {
    Machine machine = new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(20)
        .withInstruction(fakeInstruction1)
        .withInstruction(fakeInstruction2)
        .build();
    Evolution evolution = new Evolution(machine);
    evolution.evolve();
  }
}
