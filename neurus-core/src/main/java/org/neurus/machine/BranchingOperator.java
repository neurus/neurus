package org.neurus.machine;

public abstract class BranchingOperator extends BaseOperator {

  public BranchingOperator(int inputRegisters) {
    super(inputRegisters, false);
  }

  @Override
  public boolean isBranching() {
    return true;
  }
}
