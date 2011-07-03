package org.neurus.instruction;

public interface ProgramRunner {

  void load(Program program);

  double[] run(double[] inputs);
}
