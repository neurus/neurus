package org.neurus.evolution;

public class PopulationFactory {

  private final IndividualInitializer initializer;

  public PopulationFactory(IndividualInitializer initializer) {
    this.initializer = initializer;
  }

  public Population initialize(int populationSize) {
    Individual[] individuals = new Individual[populationSize];
    for (int x = 0; x < individuals.length; x++) {
      individuals[x] = initializer.newIndividual();
    }
    return new Population(individuals);
  }
}
