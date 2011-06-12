package org.neurus.evolution;

import static org.neurus.util.Primitives.ubtoi;
import junit.framework.Assert;

import org.neurus.instruction.Instruction;
import org.neurus.instruction.Machine;
import org.neurus.instruction.Program;

public class ByteCodeTestUtils {

  public static ProgramHelper parseProgram(Program program, Machine machine) {
    return new ProgramHelper(program.getBytecode(), machine);
  }

  static class ProgramHelper {
    private byte[] bytecode;
    private Machine machine;
    private InstructionHelper[] instructions;

    private ProgramHelper(byte[] bytecode, Machine machine) {
      this.machine = machine;
      this.bytecode = bytecode;
      initializeInstructions();
    }

    private void initializeInstructions() {
      int size = bytecode.length / machine.getBytesPerInstruction();
      instructions = new InstructionHelper[size];
      int pos = 0;
      for (int x = 0; x < size; x++) {
        instructions[x] = new InstructionHelper(machine, bytecode, pos);
        pos += machine.getBytesPerInstruction();
      }
    }

    public InstructionHelper[] getInstructions() {
      return instructions;
    }
  }

  static class InstructionHelper {
    private int instructionIndex;
    private Instruction instruction;
    private int[] inputRegisters;
    private int[] outputRegisters;
    private Machine machine;

    public InstructionHelper(Machine machine, byte[] bytecode, int pos) {
      this.machine = machine;
      this.instructionIndex = ubtoi(bytecode[pos]);
      this.instruction = machine.getInstruction(instructionIndex);
      this.inputRegisters = new int[this.instruction.getInputRegisters()];
      for (int i = 0; i < inputRegisters.length; i++) {
        inputRegisters[i] = ubtoi(bytecode[pos + i + 1]);
      }
      this.outputRegisters = new int[this.instruction.hasOutputRegister() ? 1 : 0];
      if (outputRegisters.length > 0) {
        outputRegisters[0] = ubtoi(bytecode[pos + machine.getBytesPerInstruction() - 1]);
        Assert.assertTrue(isCalcuation(outputRegisters[0]));
      }
    }

    public boolean isConstantRegister(int inputIndex) {
      return inputRegisters[inputIndex] >= machine.getNumberOfCalculationRegisters() &&
          inputRegisters[inputIndex] <
              (machine.getNumberOfCalculationRegisters() + machine.getNumberOfConstantRegisters());
    }

    public boolean isCalculationRegister(int inputIndex) {
      return isCalcuation(inputRegisters[inputIndex]);
    }

    private boolean isCalcuation(int registerNumber) {
      return registerNumber >= 0 && registerNumber < (machine.getNumberOfCalculationRegisters());
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
