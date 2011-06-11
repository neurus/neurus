package org.neurus.instruction;

import junit.framework.Assert;

import org.junit.Test;

public class InstructionSetBuilderTest {

  private Instruction fakeInstruction1 = new FakeInstruction();
  private Instruction fakeInstruction2 = new FakeInstruction();

  @Test
  public void testbuildCreatesAnInstructionSet() {
    InstructionSet instrSet = new InstructionSetBuilder()
        .withConstantRegisters(10)
        .withCalculationRegisters(8)
        .withInstruction(fakeInstruction1)
        .withInstruction(fakeInstruction2)
        .build();
    Assert.assertEquals(2, instrSet.size());
    Assert.assertEquals(fakeInstruction1, instrSet.getInstruction(0));
    Assert.assertEquals(fakeInstruction2, instrSet.getInstruction(1));
    Assert.assertEquals(8, instrSet.getNumberOfCalculationRegisters());
    Assert.assertEquals(10, instrSet.getNumberOfConstantRegisters());
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildFailsWhenThereAreNoInstructions() {
    new InstructionSetBuilder().withConstantRegisters(10)
        .withCalculationRegisters(10).build();
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildFailsWhenRegistersNotSpecified() {
    new InstructionSetBuilder().withInstruction(fakeInstruction1).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithConstantRegistersFailsForNegativeValues() {
    new InstructionSetBuilder().withConstantRegisters(-1);
  }

  @Test
  public void testWithConstantRegistersAllowsZero() {
    new InstructionSetBuilder().withConstantRegisters(0);
  }

  @Test(expected = NullPointerException.class)
  public void testWithInstructionFailsWhenGivenNullInstruction() {
    new InstructionSetBuilder().withInstruction(null);
  }
}
