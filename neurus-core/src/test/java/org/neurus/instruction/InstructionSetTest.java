package org.neurus.instruction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InstructionSetTest {

  private static final int CALCULATION_REGISTERS = 5;
  private static final int CONSTANT_REGISTERS = 5;
  private Instruction fakeInstruction1 = new FakeInstruction();
  private Instruction fakeInstruction2 = new FakeInstruction();

  @Test
  public void testGetInstructionAndSizeReturnCorrectValues() {
    Instruction[] instrs = new Instruction[] { fakeInstruction1, fakeInstruction2 };
    InstructionSet instrSet = new InstructionSet(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(2, instrSet.size());
    assertEquals(fakeInstruction1, instrSet.getInstruction(0));
    assertEquals(fakeInstruction2, instrSet.getInstruction(1));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetInstructionThrowsIndexOutOfBoundsForInvalidIndex() {
    Instruction[] instrs = new Instruction[] { fakeInstruction1, fakeInstruction2 };
    InstructionSet instrSet = new InstructionSet(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    instrSet.getInstruction(2);
  }

  @Test
  public void testRegisterValues() {
    Instruction[] instrs = new Instruction[] { fakeInstruction1, fakeInstruction2 };
    InstructionSet instrSet = new InstructionSet(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(CALCULATION_REGISTERS, instrSet.getNumberOfCalculationRegisters());
    assertEquals(CONSTANT_REGISTERS, instrSet.getNumberOfConstantRegisters());
  }

  @Test
  public void testBytesPerInstructionWithOutput() {
    Instruction instrWithOutput = new FakeInstruction(2, true);
    Instruction instrWithNoOutput = new FakeInstruction(3, false);
    Instruction[] instrs = new Instruction[] { instrWithOutput, instrWithNoOutput };
    InstructionSet instrSet = new InstructionSet(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(5 /* 1 instr, 3 inputs, 1 output */, instrSet.getBytesPerInstruction());
  }

  @Test
  public void testBytesPerInstructionWithoutOutput() {
    Instruction instrWithNoOutput1 = new FakeInstruction(1, false);
    Instruction instrWithNoOutput2 = new FakeInstruction(2, false);
    Instruction[] instrs = new Instruction[] { instrWithNoOutput1, instrWithNoOutput2 };
    InstructionSet instrSet = new InstructionSet(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(3 /* 1 instr, 2 inputs */, instrSet.getBytesPerInstruction());
  }

  @Test
  public void testBytesPerInstructionWithNoInputsNorOutputs() {
    Instruction instrWithNoOutput1 = new FakeInstruction(0, false);
    Instruction instrWithNoOutput2 = new FakeInstruction(0, false);
    Instruction[] instrs = new Instruction[] { instrWithNoOutput1, instrWithNoOutput2 };
    InstructionSet instrSet = new InstructionSet(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(1, instrSet.getBytesPerInstruction());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsErrorsWhenNotEnoughRegistersForInstructions() {
    Instruction instrWithNoOutput1 = new FakeInstruction(1, false);
    Instruction instrWithNoOutput2 = new FakeInstruction(2, false);
    Instruction[] instrs = new Instruction[] { instrWithNoOutput1, instrWithNoOutput2 };
    new InstructionSet(instrs, 1, 5);
  }
}
