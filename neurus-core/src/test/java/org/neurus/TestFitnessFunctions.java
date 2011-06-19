package org.neurus;

import org.neurus.evolution.Individual;
import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;

public class TestFitnessFunctions {

  public static FitnessFunction constantFitnessFunction(final double fitnessValue) {
    return new FitnessFunction() {

      @Override
      public Fitness evaluate(Individual individual) {
        return new Fitness(fitnessValue);
      }
    };
  }
}
