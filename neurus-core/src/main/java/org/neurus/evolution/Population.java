package org.neurus.evolution;

public class Population {
  
  private Individual[] individuals;

  public Population(Individual[] individuals) {
    this.individuals = individuals;
  }

  public Population(Population population) {
    this.individuals = population.individuals.clone();
  }

  public int size() {
    return individuals.length;
  }

  public Individual get(int x) {
    return individuals[x];
  }

  public void replace(int toBeDiscardedIndex, Individual individual) {
    individuals[toBeDiscardedIndex] = individual;
  }
}
