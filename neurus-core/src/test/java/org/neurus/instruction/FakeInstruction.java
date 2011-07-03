package org.neurus.instruction;

public class FakeInstruction extends BaseInstruction {

  public FakeInstruction() {
    super(0, false);
  }

  public FakeInstruction(int inputRegisters, boolean hasDestinationRegister) {
    super(inputRegisters, hasDestinationRegister);
  }

  @Override
  public double execute(double[] inputs) {
    return 0;
  }

  @Override
  public boolean isBranching() {
    return false;
  }
}
