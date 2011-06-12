package org.neurus.fitness;

import org.neurus.evolution.Individual;

public interface FitnessFunction {

  public Fitness evaluate(Individual individual);
}
