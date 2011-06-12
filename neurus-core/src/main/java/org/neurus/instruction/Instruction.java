package org.neurus.instruction;

public interface Instruction {

  int getInputRegisters();

  boolean hasOutputRegister();

  public double execute(double[] inputs);
}
