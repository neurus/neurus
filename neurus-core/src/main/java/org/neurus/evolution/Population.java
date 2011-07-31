package org.neurus.evolution;

import org.neurus.Copyable;

public class Population implements Copyable<Population> {
  
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

  public void replace(int toBeDiscardedIndex, Individual individual) {
    individuals[toBeDiscardedIndex] = individual;
  }

  @Override
  public Population copy() {
    return new Population(individuals.clone());
  }
}
