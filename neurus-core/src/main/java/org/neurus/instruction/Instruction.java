package org.neurus.instruction;

public interface Instruction {

  static final double FALSE = 0d;
  static final double TRUE = 1d;

  int getInputRegisters();

  boolean hasOutputRegister();

  public double execute(double[] inputs);

  public boolean isBranching();
}
