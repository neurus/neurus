package org.neurus;

import org.neurus.evolution.Individual;
import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.ProgramRunner;

public class TestFitnessFunctions {

  public static FitnessFunction constantFitnessFunction(final double fitnessValue) {
    return new FitnessFunction() {

      @Override
      public Fitness evaluate(ProgramRunner programRunner, Individual individual) {
        return new Fitness(fitnessValue);
      }
    };
  }
}
