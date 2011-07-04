package org.neurus.machine;

import org.neurus.machine.BaseOperator;

public class FakeOperator extends BaseOperator {

  public FakeOperator() {
    super(0, false);
  }

  public FakeOperator(int inputRegisters, boolean hasDestinationRegister) {
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
