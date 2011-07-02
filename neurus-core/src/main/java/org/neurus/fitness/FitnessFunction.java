package org.neurus.fitness;

import org.neurus.evolution.Individual;
import org.neurus.instruction.ProgramRunner;

public interface FitnessFunction {

  public Fitness evaluate(ProgramRunner programRunner, Individual individual);
}
