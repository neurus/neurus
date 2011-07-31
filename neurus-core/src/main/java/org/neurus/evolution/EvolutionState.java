package org.neurus.evolution;

public class EvolutionState {

  private int generationNumber = -1;
  private Population population;
  private Individual bestTrainingIndividual;
  private Individual bestValidationIndividual;

  public void incrementGeneration() {
    generationNumber++;
  }

  public void setPopulation(Population population) {
    this.population = population;
  }

  public Population getPopulation() {
    return population;
  }

  public int getGeneration() {
    return generationNumber;
  }

  public Individual getBestTrainingIndividual() {
    return bestTrainingIndividual;
  }

  public void setBestTrainingIndividual(Individual bestTrainingIndividual) {
    this.bestTrainingIndividual = bestTrainingIndividual;
  }

  public Individual getBestValidationIndividual() {
    return bestValidationIndividual;
  }

  public void setBestValidationIndividual(Individual bestValidationIndividual) {
    this.bestValidationIndividual = bestValidationIndividual;
  }

  public EvolutionSnapshot takeSnapshot() {
    Population popCopy = population == null ? null : population.copy();
    return new EvolutionSnapshot(generationNumber, popCopy, bestTrainingIndividual,
        bestValidationIndividual);
  }
}
