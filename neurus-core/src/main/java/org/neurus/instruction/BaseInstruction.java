package org.neurus.instruction;

public abstract class BaseInstruction implements Instruction {

  private int inputRegisters;
  private boolean hasDestinationRegister;

  public BaseInstruction(int inputRegisters, boolean hasDestinationRegister) {
    super();
    this.inputRegisters = inputRegisters;
    this.hasDestinationRegister = hasDestinationRegister;
  }

  public int getInputRegisters() {
    return inputRegisters;
  }

  public boolean hasDestinationRegister() {
    return hasDestinationRegister;
  }

  @Override
  public boolean isBranching() {
    return false;
  }
}
