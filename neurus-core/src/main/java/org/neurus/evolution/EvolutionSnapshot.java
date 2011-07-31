package org.neurus.evolution;

public class EvolutionSnapshot {

  private int generationNumber;
  private Population population;
  private Individual bestTrainingIndividual;
  private Individual bestValidationIndividual;

  public EvolutionSnapshot() {
    this.generationNumber = -1;
  }

  public EvolutionSnapshot(int generationNumber, Population population,
      Individual bestTrainingIndividual, Individual bestValidationIndividual) {
    super();
    this.generationNumber = generationNumber;
    this.population = population;
    this.bestTrainingIndividual = bestTrainingIndividual;
    this.bestValidationIndividual = bestValidationIndividual;
  }

  public int getGenerationNumber() {
    return generationNumber;
  }

  public Population getPopulation() {
    return population;
  }

  public Individual getBestTrainingIndividual() {
    return bestTrainingIndividual;
  }

  public Individual getBestValidationIndividual() {
    return bestValidationIndividual;
  }
}
