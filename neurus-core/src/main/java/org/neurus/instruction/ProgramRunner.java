package org.neurus.instruction;

public interface ProgramRunner {

  double[] run(byte[] bytecode, double[] inputs);
}
