package org.neurus.machine;

import com.google.common.base.Preconditions;

public class Machine {

  // TODO Verify limits on number of operators and registers (127, 255, etc)

  private final Operator[] operators;
  private final int numberOfCalculationRegisters;
  private final int numberOfOutputRegisters;
  private final int maxInputsForASingleInstruction;
  private final int bytesPerInstruction;
  private final boolean hasDestinationRegister;
  private final ConstantRegisters constantRegisters;

  protected Machine(Operator[] operators, int numberOfCalculationRegisters,
      ConstantRegisters constantRegisters, int numberOfOutputRegisters) {
    this.operators = operators;
    this.numberOfCalculationRegisters = numberOfCalculationRegisters;
    this.constantRegisters = constantRegisters;
    this.numberOfOutputRegisters = numberOfOutputRegisters;

    // calculate size in bytes of an instruction
    int maxInputs = 0;
    int maxDestinationRegisters = 0;
    for (Operator i : operators) {
      if (i.getInputRegisters() > maxInputs) {
        maxInputs = i.getInputRegisters();
      }
      if (i.hasDestinationRegister()) {
        maxDestinationRegisters = 1;
      }
    }
    bytesPerInstruction = maxInputs + maxDestinationRegisters + 1;
    maxInputsForASingleInstruction = maxInputs;
    hasDestinationRegister = maxDestinationRegisters > 0;

    // validate that enough registers were provided
    int requiredCalcRegisters = Math.max(maxInputs, maxDestinationRegisters);
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
    return operators.length;
  }

  public Operator getOperator(int index) {
    return operators[index];
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

  public boolean hasDestinationRegister() {
    return hasDestinationRegister;
  }

  public int getNumberOfOutputRegisters() {
    return numberOfOutputRegisters;
  }
}
