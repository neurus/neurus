package org.neurus.evolution;

import org.neurus.fitness.FitnessEvaluator;

public abstract class EvolutionBase implements Evolution {

  protected final PopulationFactory populationFactory;
  protected final TerminationCriteria terminationCriteria;
  protected final EvolutionListener evolutionListener;
  protected final FitnessEvaluator fitnessEvaluator;

  protected EvolutionState evolutionState;

  public EvolutionBase(PopulationFactory populationFactory, FitnessEvaluator fitnessEvaluator,
      TerminationCriteria terminationCriteria, EvolutionListener evolutionListener) {
    super();
    this.populationFactory = populationFactory;
    this.terminationCriteria = terminationCriteria;
    this.evolutionListener = evolutionListener;
    this.fitnessEvaluator = fitnessEvaluator;
  }

  public void evolve() {
    evolutionState = new EvolutionState();
    createInitialGeneration();
    evolutionListener.onNewGeneration(evolutionState);
    while (!terminationCriteria.shouldTerminate(evolutionState)) {
      evolveOneGeneration();
      evolutionState.incrementGeneration();
      evolutionListener.onNewGeneration(evolutionState);
    }
  }

  private void createInitialGeneration() {
    Population population = populationFactory.createPopulation();
    evolutionState.setPopulation(population);

    // evaluate fitness of each individual
    for (int x = 0; x < population.size(); x++) {
      fitnessEvaluator.evaluateFitness(population.get(x));
      updateBestIndividuals(population.get(x));
    }

    evolutionState.incrementGeneration();
  }

  protected void updateBestIndividuals(Individual individual) {
    Individual currentBest = evolutionState.getBestTrainingIndividual();
    if (currentBest == null || individual.getFitness().betterThan(currentBest.getFitness())) {
      evolutionState.setBestTrainingIndividual(individual);
      mayBeBestValidationIndividual(individual);
    }
  }

  private void mayBeBestValidationIndividual(Individual individual) {
    if (!fitnessEvaluator.validates()) {
      return;
    }
    fitnessEvaluator.evaluateValidationFitness(individual);
    Individual currentBest = evolutionState.getBestValidationIndividual();
    if (currentBest == null
        || individual.getValidationFitness().betterThan(currentBest.getValidationFitness())) {
      evolutionState.setBestValidationIndividual(individual);
    }
  }

  public EvolutionState getEvolutionState() {
    return evolutionState;
  }

  protected abstract void evolveOneGeneration();
}
