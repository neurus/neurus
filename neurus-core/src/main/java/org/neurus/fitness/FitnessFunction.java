package org.neurus.fitness;

import org.neurus.evolution.Individual;
import org.neurus.machine.ProgramRunner;

public interface FitnessFunction {

  // TODO We need to always call load on the programRunner, does it make sense? Should it be
  // preloaded?
  public Fitness evaluate(ProgramRunner programRunner, Individual individual);
}
