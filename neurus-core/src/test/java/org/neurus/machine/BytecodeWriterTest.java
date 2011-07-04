package org.neurus.machine;

import static junit.framework.Assert.assertTrue;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.neurus.machine.BytecodeWriter;
import org.neurus.machine.InstructionData;
import org.neurus.machine.Machine;

public class BytecodeWriterTest {

  private BytecodeWriter writer;
  private Machine calculatorMachine;

  @Before
  public void setUp() {
    calculatorMachine = TestMachines.calculator();
    writer = new BytecodeWriter(calculatorMachine);
  }

  @Test
  public void testByteCodeWriterGeneratesProgramOfRightSize() {
    writer.initProgram(10);
    byte[] bytecode = writer.getBytecode();
    Assert.assertEquals(calculatorMachine.getBytesPerInstruction() * 10, bytecode.length);
  }

  @Test
  public void testByteCodeWriterWritesCorrectInstructions() {
    writer.initProgram(2);
    writeInstruction(1, new int[] { 2, 3 }, 4);
    writeInstruction(2, new int[] { 3, 4 }, 5);
    byte[] bytecode = writer.getBytecode();
    byte[] expected = new byte[] {
        1, 2, 3, 4,
        2, 3, 4, 5
    };
    assertTrue(Arrays.equals(expected, bytecode));
  }

  @Test
  public void testByteCodeWriterCopiesInstructions() {
    writer.initProgram(6);
    byte[] original = {
        1, 2, 3, 4,
        2, 3, 4, 5,
        3, 4, 5, 6
    };
    writeInstruction(4, new int[] { 3, 2 }, 1);
    writer.copyInstructions(original, 0, 2);
    writeInstruction(4, new int[] { 3, 2 }, 1);
    writer.copyInstructions(original, 1, 2);
    byte[] expected = new byte[] {
        4, 3, 2, 1,
        1, 2, 3, 4,
        2, 3, 4, 5,
        4, 3, 2, 1,
        2, 3, 4, 5,
        3, 4, 5, 6
    };
    Assert.assertTrue(Arrays.equals(expected, writer.getBytecode()));
  }

  private void writeInstruction(int instr, int[] inputs, int destination) {
    InstructionData instrData = calculatorMachine.createInstructionData();
    instrData.operatorIndex = instr;
    for (int x = 0; x < inputs.length; x++) {
      instrData.inputRegisters[x] = inputs[x];
    }
    instrData.destinationRegister = destination;
    writer.writeInstruction(instrData);
  }
}
