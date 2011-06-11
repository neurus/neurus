package org.neurus.instruction;

public class FakeInstruction implements Instruction {

  private int inputRegisters = 2;
  private boolean hasOutputRegister = true;

  public FakeInstruction() {
    super();
  }

  public FakeInstruction(int inputRegisters, boolean hasOutputRegister) {
    super();
    this.inputRegisters = inputRegisters;
    this.hasOutputRegister = hasOutputRegister;
  }

  @Override
  public int getInputRegisters() {
    return inputRegisters;
  }

  @Override
  public boolean hasOutputRegister() {
    return hasOutputRegister;
  }
}
