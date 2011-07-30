package org.neurus.evolution;

public class PopulationFactory {

  private final IndividualInitializer initializer;
  private final int populationSize;

  public PopulationFactory(IndividualInitializer initializer, int populationSize) {
    this.initializer = initializer;
    this.populationSize = populationSize;
  }

  public Population createPopulation() {
    Individual[] individuals = new Individual[populationSize];
    for (int x = 0; x < individuals.length; x++) {
      individuals[x] = initializer.newIndividual();
    }
    return new Population(individuals);
  }
}
