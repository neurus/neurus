package org.neurus.instruction;

public abstract class BranchingInstruction extends BaseInstruction {

  public BranchingInstruction(int inputRegisters) {
    super(inputRegisters, false);
  }

  @Override
  public boolean isBranching() {
    return true;
  }
}
