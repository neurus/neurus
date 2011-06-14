package org.neurus.instruction;

import junit.framework.Assert;

import org.junit.Test;

public class MachineBuilderTest {

  private Instruction fakeInstruction1 = new FakeInstruction();
  private Instruction fakeInstruction2 = new FakeInstruction();

  @Test
  public void testbuildCreatesAnInstructionSet() {
    Machine machine = new MachineBuilder()
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withCalculationRegisters(8)
        .withInstruction(fakeInstruction1)
        .withInstruction(fakeInstruction2)
        .build();
    Assert.assertEquals(2, machine.size());
    Assert.assertEquals(fakeInstruction1, machine.getInstruction(0));
    Assert.assertEquals(fakeInstruction2, machine.getInstruction(1));
    Assert.assertEquals(8, machine.getNumberOfCalculationRegisters());
    Assert.assertEquals(10, machine.getNumberOfConstantRegisters());
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildFailsWhenThereAreNoInstructions() {
    new MachineBuilder().withCalculationRegisters(10).build();
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildFailsWhenRegistersNotSpecified() {
    new MachineBuilder().withInstruction(fakeInstruction1).build();
  }

  @Test(expected = NullPointerException.class)
  public void testWithInstructionFailsWhenGivenNullInstruction() {
    new MachineBuilder().withInstruction(null);
  }
}
