package org.neurus.machine;

public interface Operator {

  static final double FALSE = 0d;
  static final double TRUE = 1d;

  int getInputRegisters();

  boolean hasDestinationRegister();

  public double execute(double[] inputs);

  public boolean isBranching();
}
