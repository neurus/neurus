package org.neurus.evolution;

import static org.neurus.utils.Primitives.ubtoi;
import junit.framework.Assert;

import org.neurus.instruction.Instruction;
import org.neurus.instruction.InstructionSet;

public class ByteCodeTestUtils {

  public static Program readBytecode(byte[] bytecode, InstructionSet iSet) {
    return new Program(bytecode, iSet);
  }

  static class Program {
    private byte[] bytecode;
    private InstructionSet iSet;
    private ProgramInstruction[] instructions;

    private Program(byte[] bytecode, InstructionSet iSet) {
      this.iSet = iSet;
      this.bytecode = bytecode;
      initializeInstructions();
    }

    private void initializeInstructions() {
      int size = bytecode.length / iSet.getBytesPerInstruction();
      instructions = new ProgramInstruction[size];
      int pos = 0;
      for (int x = 0; x < size; x++) {
        instructions[x] = new ProgramInstruction(iSet, bytecode, pos);
        pos += iSet.getBytesPerInstruction();
      }
    }

    public ProgramInstruction[] getInstructions() {
      return instructions;
    }
  }

  static class ProgramInstruction {
    private int instructionIndex;
    private Instruction instruction;
    private int[] inputRegisters;
    private int[] outputRegisters;
    private InstructionSet iSet;

    public ProgramInstruction(InstructionSet iSet, byte[] bytecode, int pos) {
      this.iSet = iSet;
      this.instructionIndex = ubtoi(bytecode[pos]);
      this.instruction = iSet.getInstruction(instructionIndex);
      this.inputRegisters = new int[this.instruction.getInputRegisters()];
      for (int i = 0; i < inputRegisters.length; i++) {
        inputRegisters[i] = ubtoi(bytecode[pos + i + 1]);
      }
      this.outputRegisters = new int[this.instruction.hasOutputRegister() ? 1 : 0];
      if (outputRegisters.length > 0) {
        outputRegisters[0] = ubtoi(bytecode[pos + iSet.getBytesPerInstruction() - 1]);
        Assert.assertTrue(isCalcuation(outputRegisters[0]));
      }
    }

    public boolean isConstantRegister(int inputIndex) {
      return inputRegisters[inputIndex] >= iSet.getNumberOfCalculationRegisters() &&
          inputRegisters[inputIndex] <
              (iSet.getNumberOfCalculationRegisters() + iSet.getNumberOfConstantRegisters());
    }

    public boolean isCalculationRegister(int inputIndex) {
      return isCalcuation(inputRegisters[inputIndex]);
    }

    private boolean isCalcuation(int registerNumber) {
      return registerNumber >= 0 && registerNumber < (iSet.getNumberOfCalculationRegisters());
    }

    public int getInstructionIndex() {
      return instructionIndex;
    }

    public Instruction getInstruction() {
      return instruction;
    }

    public int[] getInputRegisters() {
      return inputRegisters;
    }

    public int[] getOutputRegisters() {
      return outputRegisters;
    }
  }
}
