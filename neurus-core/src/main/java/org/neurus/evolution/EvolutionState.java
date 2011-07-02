package org.neurus.evolution;

public class EvolutionState {

  // TODO Make this immutable or return copies in Evolution?

  private int generationNumber = -1;
  private Population population;

  public void nextGeneration(Population newPopulation) {
    generationNumber++;
    population = newPopulation;
  }

  public Population getPopulation() {
    return population;
  }

  public int getGeneration() {
    return generationNumber;
  }
}
