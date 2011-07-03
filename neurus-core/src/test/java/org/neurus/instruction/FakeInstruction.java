package org.neurus.instruction;

public class FakeInstruction extends BaseInstruction {

  public FakeInstruction() {
    super(0, false);
  }

  public FakeInstruction(int inputRegisters, boolean hasOutputRegister) {
    super(inputRegisters, hasOutputRegister);
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
