package org.neurus.machine;

public interface ProgramRunner {

  void load(Program program, boolean runEffectiveInstructionsOnly);

  double[] run(double[] inputs);
}
