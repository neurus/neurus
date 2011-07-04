package org.neurus.machine;

public abstract class BaseOperator implements Operator {

  private int inputRegisters;
  private boolean hasDestinationRegister;

  public BaseOperator(int inputRegisters, boolean hasDestinationRegister) {
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
