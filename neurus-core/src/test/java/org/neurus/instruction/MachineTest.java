package org.neurus.instruction;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;

public class MachineTest {

  private static final int CALCULATION_REGISTERS = 5;
  private static final ConstantRegisters CONSTANT_REGISTERS = new ConstantRegisters(0, 4, 1);
  private Instruction fakeInstruction1 = new FakeInstruction();
  private Instruction fakeInstruction2 = new FakeInstruction();

  @Test
  public void testGetInstructionAndSizeReturnCorrectValues() {
    Instruction[] instrs = new Instruction[] { fakeInstruction1, fakeInstruction2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(2, machine.size());
    Assert.assertEquals(fakeInstruction1, machine.getInstruction(0));
    Assert.assertEquals(fakeInstruction2, machine.getInstruction(1));
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
    assertEquals(5, machine.getNumberOfConstantRegisters());
  }

  @Test
  public void testGetMaxInputsForASingleInstruction() {
    Instruction instrWithDestination = new FakeInstruction(2, true);
    Instruction instrWithNoDestination = new FakeInstruction(3, false);
    Instruction[] instrs = new Instruction[] { instrWithDestination, instrWithNoDestination };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(3, machine.getMaxInputsForASingleInstruction());
  }

  @Test
  public void testBytesPerInstructionWithDestinationRegister() {
    Instruction instrWithDestination = new FakeInstruction(2, true);
    Instruction instrWithNoDestination = new FakeInstruction(3, false);
    Instruction[] instrs = new Instruction[] { instrWithDestination, instrWithNoDestination };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(5 /* 1 instr, 3 inputs, 1 destination */, machine.getBytesPerInstruction());
  }

  @Test
  public void testHasDestinationRegisterReturnsTrue() {
    Instruction instrWithNoDestination = new FakeInstruction(3, false);
    Instruction instrWithNoDestination2 = new FakeInstruction(3, false);
    Instruction[] instrs = new Instruction[] { instrWithNoDestination, instrWithNoDestination2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertFalse(machine.hasDestinationRegister());
  }

  @Test
  public void testHasDestinationRegisterReturnsFalse() {
    Instruction instrWithNoDestination = new FakeInstruction(3, false);
    Instruction instrWithDestination = new FakeInstruction(3, true);
    Instruction[] instrs = new Instruction[] { instrWithNoDestination, instrWithDestination };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertTrue(machine.hasDestinationRegister());
  }

  @Test
  public void testBytesPerInstructionWithoutDestination() {
    Instruction instrWithNoDestination1 = new FakeInstruction(1, false);
    Instruction instrWithNoDestination2 = new FakeInstruction(2, false);
    Instruction[] instrs = new Instruction[] { instrWithNoDestination1, instrWithNoDestination2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(3 /* 1 instr, 2 inputs */, machine.getBytesPerInstruction());
  }

  @Test
  public void testBytesPerInstructionWithNoInputsNorDestinations() {
    Instruction instrWithNoDestination1 = new FakeInstruction(0, false);
    Instruction instrWithNoDestination2 = new FakeInstruction(0, false);
    Instruction[] instrs = new Instruction[] { instrWithNoDestination1, instrWithNoDestination2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    assertEquals(1, machine.getBytesPerInstruction());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsErrorsWhenNotEnoughRegistersForInstructions() {
    Instruction instrWithNoDestination1 = new FakeInstruction(1, false);
    Instruction instrWithNoDestination2 = new FakeInstruction(2, false);
    Instruction[] instrs = new Instruction[] { instrWithNoDestination1, instrWithNoDestination2 };
    new Machine(instrs, 1, CONSTANT_REGISTERS);
  }

  @Test
  public void testCreateInstructionData() {
    Instruction instrWithDestination = new FakeInstruction(2, true);
    Instruction instrWithNoDestination = new FakeInstruction(3, false);
    Instruction[] instrs = new Instruction[] { instrWithDestination, instrWithNoDestination };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS);
    InstructionData instrData = machine.createInstructionData();
    assertEquals(3, instrData.inputRegisters.length);
    assertNotSame(instrData, machine.createInstructionData());
  }
}
