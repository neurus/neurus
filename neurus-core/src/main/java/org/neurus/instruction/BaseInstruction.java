package org.neurus.instruction;

public abstract class BaseInstruction implements Instruction {

  private int inputRegisters;
  private boolean hasOutputRegister;

  public BaseInstruction(int inputRegisters, boolean hasOutputRegister) {
    super();
    this.inputRegisters = inputRegisters;
    this.hasOutputRegister = hasOutputRegister;
  }

  public int getInputRegisters() {
    return inputRegisters;
  }

  public boolean hasOutputRegister() {
    return hasOutputRegister;
  }

  @Override
  public boolean isBranching() {
    return false;
  }
}
