package org.neurus.instruction;

import com.google.common.base.Preconditions;

public class Machine {

  // TODO Verify limits on number of instructions and registers (127, 255, etc)

  private final Instruction[] instructions;
  private final int numberOfCalculationRegisters;
  private final int maxInputsForASingleInstruction;
  private final int bytesPerInstruction;
  private final boolean hasOutputRegister;
  private final ConstantRegisters constantRegisters;

  protected Machine(Instruction[] instructions, int numberOfCalculationRegisters,
      ConstantRegisters constantRegisters) {
    this.instructions = instructions;
    this.numberOfCalculationRegisters = numberOfCalculationRegisters;
    this.constantRegisters = constantRegisters;

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
    hasOutputRegister = maxOutputs > 0;

    // validate that enough registers were provided
    int requiredCalcRegisters = Math.max(maxInputs, maxOutputs);
    Preconditions.checkArgument(requiredCalcRegisters <= numberOfCalculationRegisters,
            "The number of calculation registers should be greater than the number of "
                + "inputs required for a single instruction");

  }

  public ProgramRunner createRunner() {
    return new InterpreterRunner(this);
  }

  public InstructionData createInstructionData() {
    InstructionData instrData = new InstructionData();
    instrData.inputRegisters = new int[getMaxInputsForASingleInstruction()];
    return instrData;
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

  public ConstantRegisters getConstantRegisters() {
    return constantRegisters;
  }

  public int getBytesPerInstruction() {
    return bytesPerInstruction;
  }

  public int getMaxInputsForASingleInstruction() {
    return maxInputsForASingleInstruction;
  }

  public int getNumberOfConstantRegisters() {
    return constantRegisters.size();
  }

  public boolean hasOutputRegister() {
    return hasOutputRegister;
  }
}
