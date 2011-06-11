package org.neurus.instruction;

import com.google.common.base.Preconditions;

public class InstructionSet {

  private final Instruction[] instructions;
  private final int numberOfCalculationRegisters;
  private final int numberOfConstantRegisters;
  private final int bytesPerInstruction;

  protected InstructionSet(Instruction[] instructions, int numberOfCalculationRegisters,
      int numberOfConstantRegisters) {
    this.instructions = instructions;
    this.numberOfCalculationRegisters = numberOfCalculationRegisters;
    this.numberOfConstantRegisters = numberOfConstantRegisters;

    // calculate size in bytes of an instruction
    int maxInputs = 0;
    int maxOutputs = 0;
    for (Instruction i : instructions) {
      if (i.getInputRegisters() > maxInputs) {
        maxInputs = i.getInputRegisters();
      }
      if (i.hasOutputRegister()) {
        maxOutputs = 1;
      }
    }
    bytesPerInstruction = maxInputs + maxOutputs + 1;

    // validate that enough registers were provided
    int requiredCalcRegisters = Math.max(maxInputs, maxOutputs);
    Preconditions.checkArgument(requiredCalcRegisters <= numberOfCalculationRegisters,
            "The number of calculation registers should be greater than the number of "
                + "inputs required for a single instruction");
  }

  public int size() {
    return instructions.length;
  }

  public Instruction getInstruction(int index) {
    return instructions[index];
  }

  public int getNumberOfCalculationRegisters() {
    return numberOfCalculationRegisters;
  }

  public int getNumberOfConstantRegisters() {
    return numberOfConstantRegisters;
  }

  public int getBytesPerInstruction() {
    return bytesPerInstruction;
  }
}
