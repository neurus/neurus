package org.neurus.instruction;

import com.google.common.base.Preconditions;

public class Machine {

  private final Instruction[] instructions;
  private final int numberOfCalculationRegisters;
  private final int numberOfConstantRegisters;
  private final int maxInputsForASingleInstruction;
  private final int bytesPerInstruction;

  protected Machine(Instruction[] instructions, int numberOfCalculationRegisters,
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
    maxInputsForASingleInstruction = maxInputs;

    // validate that enough registers were provided
    int requiredCalcRegisters = Math.max(maxInputs, maxOutputs);
    Preconditions.checkArgument(requiredCalcRegisters <= numberOfCalculationRegisters,
            "The number of calculation registers should be greater than the number of "
                + "inputs required for a single instruction");
    
  }

  public ProgramRunner createRunner() {
    return new InterpreterRunner(this);
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

  public int getMaxInputsForASingleInstruction() {
    return maxInputsForASingleInstruction;
  }
}
