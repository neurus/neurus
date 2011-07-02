package org.neurus.evolution;

import org.neurus.fitness.Fitness;
import org.neurus.instruction.Program;

public class TestPopulations {

  public static Population populationWithFitnesses(double... fitnesses) {
    Individual[] individuals = new Individual[fitnesses.length];
    for (int x = 0; x < individuals.length; x++) {
      Individual individual = new Individual(new Program(new byte[] {}));
      individual.setFitness(new Fitness(fitnesses[x]));
      individuals[x] = individual;
    }
    return new Population(individuals);
  }

  public static Population populationOfSize(int size) {
    Individual[] individuals = new Individual[size];
    for (int x = 0; x < individuals.length; x++) {
      Individual individual = new Individual(new Program(new byte[] {}));
      individuals[x] = individual;
    }
    return new Population(individuals);
  }
}
