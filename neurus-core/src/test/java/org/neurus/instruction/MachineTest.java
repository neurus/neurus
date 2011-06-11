package org.neurus.instruction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MachineTest {

  private static final int CALCULATION_REGISTERS = 5;
  private static final int CONSTANT_REGISTERS = 5;
  private Instruction fakeInstruction1 = new FakeInstruction();
  private Instruction fakeInstruction2 = new FakeInstruction();

  @Test
  public void testGetInstructionAndSizeReturnCorrectValues() {
    Instruction[] instrs = new Instruction[] { fakeInstruction1, fakeInstruction2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(2, machine.size());
    assertEquals(fakeInstruction1, machine.getInstruction(0));
    assertEquals(fakeInstruction2, machine.getInstruction(1));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetInstructionThrowsIndexOutOfBoundsForInvalidIndex() {
    Instruction[] instrs = new Instruction[] { fakeInstruction1, fakeInstruction2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    machine.getInstruction(2);
  }

  @Test
  public void testRegisterValues() {
    Instruction[] instrs = new Instruction[] { fakeInstruction1, fakeInstruction2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(CALCULATION_REGISTERS, machine.getNumberOfCalculationRegisters());
    assertEquals(CONSTANT_REGISTERS, machine.getNumberOfConstantRegisters());
  }

  @Test
  public void testBytesPerInstructionWithOutput() {
    Instruction instrWithOutput = new FakeInstruction(2, true);
    Instruction instrWithNoOutput = new FakeInstruction(3, false);
    Instruction[] instrs = new Instruction[] { instrWithOutput, instrWithNoOutput };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(5 /* 1 instr, 3 inputs, 1 output */, machine.getBytesPerInstruction());
  }

  @Test
  public void testBytesPerInstructionWithoutOutput() {
    Instruction instrWithNoOutput1 = new FakeInstruction(1, false);
    Instruction instrWithNoOutput2 = new FakeInstruction(2, false);
    Instruction[] instrs = new Instruction[] { instrWithNoOutput1, instrWithNoOutput2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(3 /* 1 instr, 2 inputs */, machine.getBytesPerInstruction());
  }

  @Test
  public void testBytesPerInstructionWithNoInputsNorOutputs() {
    Instruction instrWithNoOutput1 = new FakeInstruction(0, false);
    Instruction instrWithNoOutput2 = new FakeInstruction(0, false);
    Instruction[] instrs = new Instruction[] { instrWithNoOutput1, instrWithNoOutput2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(1, machine.getBytesPerInstruction());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsErrorsWhenNotEnoughRegistersForInstructions() {
    Instruction instrWithNoOutput1 = new FakeInstruction(1, false);
    Instruction instrWithNoOutput2 = new FakeInstruction(2, false);
    Instruction[] instrs = new Instruction[] { instrWithNoOutput1, instrWithNoOutput2 };
    new Machine(instrs, 1, 5);
  }
}
