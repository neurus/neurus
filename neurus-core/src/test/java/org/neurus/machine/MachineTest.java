package org.neurus.machine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;
import org.neurus.machine.ConstantRegisters;
import org.neurus.machine.InstructionData;
import org.neurus.machine.Machine;
import org.neurus.machine.Operator;

public class MachineTest {

  private static final int CALCULATION_REGISTERS = 5;
  private static final int OUTPUT_REGISTERS = 2;
  private static final ConstantRegisters CONSTANT_REGISTERS = new ConstantRegisters(0, 4, 1);
  private Operator fakeOperator1 = new FakeOperator();
  private Operator fakeOperator2 = new FakeOperator();

  @Test
  public void testGetOperatorAndSizeReturnCorrectValues() {
    Operator[] operators = new Operator[] { fakeOperator1, fakeOperator2 };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertEquals(2, machine.size());
    Assert.assertEquals(fakeOperator1, machine.getOperator(0));
    Assert.assertEquals(fakeOperator2, machine.getOperator(1));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetOperatorThrowsIndexOutOfBoundsForInvalidIndex() {
    Operator[] instrs = new Operator[] { fakeOperator1, fakeOperator2 };
    Machine machine = new Machine(instrs, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    machine.getOperator(2);
  }

  @Test
  public void testRegisterValues() {
    Operator[] operators = new Operator[] { fakeOperator1, fakeOperator2 };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertEquals(CALCULATION_REGISTERS, machine.getNumberOfCalculationRegisters());
    assertEquals(5, machine.getNumberOfConstantRegisters());
  }

  @Test
  public void testGetMaxInputsForASingleInstruction() {
    Operator operatorWithDestination = new FakeOperator(2, true);
    Operator operatorWithNoDestination = new FakeOperator(3, false);
    Operator[] operators = new Operator[] { operatorWithDestination, operatorWithNoDestination };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertEquals(3, machine.getMaxInputsForASingleInstruction());
  }

  @Test
  public void testBytesPerInstructionWithDestinationRegister() {
    Operator operatorWithDestination = new FakeOperator(2, true);
    Operator operatorWithNoDestination = new FakeOperator(3, false);
    Operator[] operators = new Operator[] { operatorWithDestination, operatorWithNoDestination };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertEquals(5 /* 1 instr, 3 inputs, 1 destination */, machine.getBytesPerInstruction());
  }

  @Test
  public void testHasDestinationRegisterReturnsTrue() {
    Operator operatorWithNoDestination = new FakeOperator(3, false);
    Operator operatorWithNoDestination2 = new FakeOperator(3, false);
    Operator[] operators = new Operator[] { operatorWithNoDestination, operatorWithNoDestination2 };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertFalse(machine.hasDestinationRegister());
  }

  @Test
  public void testHasDestinationRegisterReturnsFalse() {
    Operator operatorWithNoDestination = new FakeOperator(3, false);
    Operator operatorWithDestination = new FakeOperator(3, true);
    Operator[] operators = new Operator[] { operatorWithNoDestination, operatorWithDestination };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertTrue(machine.hasDestinationRegister());
  }

  @Test
  public void testBytesPerInstructionWithoutDestination() {
    Operator operatorWithNoDestination1 = new FakeOperator(1, false);
    Operator operatorWithNoDestination2 = new FakeOperator(2, false);
    Operator[] operators = new Operator[] { operatorWithNoDestination1, operatorWithNoDestination2 };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertEquals(3 /* 1 instr, 2 inputs */, machine.getBytesPerInstruction());
  }

  @Test
  public void testBytesPerInstructionWithNoInputsNorDestinations() {
    Operator operatorWithNoDestination1 = new FakeOperator(0, false);
    Operator operatorWithNoDestination2 = new FakeOperator(0, false);
    Operator[] operators = new Operator[] { operatorWithNoDestination1, operatorWithNoDestination2 };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertEquals(1, machine.getBytesPerInstruction());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsErrorsWhenNotEnoughRegistersForInstructions() {
    Operator operatorWithNoDestination1 = new FakeOperator(1, false);
    Operator operatorWithNoDestination2 = new FakeOperator(2, false);
    Operator[] operators = new Operator[] { operatorWithNoDestination1, operatorWithNoDestination2 };
    new Machine(operators, 1, CONSTANT_REGISTERS, OUTPUT_REGISTERS);
  }

  @Test
  public void testCreateInstructionData() {
    Operator operatorWithDestination = new FakeOperator(2, true);
    Operator operatorWithNoDestination = new FakeOperator(3, false);
    Operator[] operators = new Operator[] { operatorWithDestination, operatorWithNoDestination };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    InstructionData instrData = machine.createInstructionData();
    assertEquals(3, instrData.inputRegisters.length);
    assertNotSame(instrData, machine.createInstructionData());
  }

  @Test
  public void testGetOutputRegisters() {
    Operator operatorWithDestination = new FakeOperator(2, true);
    Operator[] operators = new Operator[] { operatorWithDestination };
    Machine machine = new Machine(operators, CALCULATION_REGISTERS, CONSTANT_REGISTERS,
        OUTPUT_REGISTERS);
    assertEquals(OUTPUT_REGISTERS, machine.getNumberOfOutputRegisters());
  }
}
