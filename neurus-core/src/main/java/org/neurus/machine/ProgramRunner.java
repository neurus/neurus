package org.neurus.machine;

public interface ProgramRunner {

  void load(Program program);

  double[] run(double[] inputs);
}
