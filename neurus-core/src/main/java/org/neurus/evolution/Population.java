package org.neurus.evolution;

public class Population {
  
  private Individual[] individuals;

  public Population(Individual[] individuals) {
    this.individuals = individuals;
  }

  public int size() {
    return individuals.length;
  }

  public Individual get(int x) {
    return individuals[x];
  }
}
