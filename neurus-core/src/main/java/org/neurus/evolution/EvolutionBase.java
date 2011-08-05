package org.neurus.evolution;

import java.util.List;

import org.neurus.fitness.FitnessEvaluator;
import org.neurus.rng.RandomNumberGenerator;

public abstract class EvolutionBase implements Evolution {

  protected final PopulationFactory populationFactory;
  protected final TerminationCriteria terminationCriteria;
  protected final FitnessEvaluator fitnessEvaluator;
  protected final SelectionMethod selector;
  protected final SelectionMethod deselector;
  protected EvolutionListener evolutionListener;
  private RandomNumberGenerator rng;
  private EvolutionSnapshot lastSnapshot = new EvolutionSnapshot();
  protected EvolutionState evolutionState;
  private Exchanger exchanger;

  public EvolutionBase(PopulationFactory populationFactory, FitnessEvaluator fitnessEvaluator,
      TerminationCriteria terminationCriteria, RandomNumberGenerator rng, SelectionMethod selector,
      SelectionMethod deselector) {
    super();
    this.populationFactory = populationFactory;
    this.terminationCriteria = terminationCriteria;
    this.fitnessEvaluator = fitnessEvaluator;
    this.selector = selector;
    this.deselector = deselector;
    this.rng = rng;
  }

  public void evolve() {
    evolutionState = new EvolutionState();
    createInitialGeneration();
    doAfterGeneration();
    while (!terminationCriteria.shouldTerminate(evolutionState)) {
      receiveImmigrants();
      evolveOneGeneration();
      doAfterGeneration();
      migrateIndividuals();
    }
  }

  private void migrateIndividuals() {
    if(exchanger == null) {
      return;
    }
    exchanger.migrateIndividuals(evolutionState.getPopulation(), evolutionState.getGeneration());
  }

  private void receiveImmigrants() {
    if (exchanger == null) {
      return;
    }
    List<Individual> individuals = exchanger.receiveIndividuals();
    for (Individual individual : individuals) {
      // TODO: this three lines are the same as those inside steady state
      updateBestIndividuals(individual);
      int toBeDiscardedIndex = deselector.select(evolutionState.getPopulation());
      evolutionState.getPopulation().replace(toBeDiscardedIndex, individual);
    }
  }

  public void setExchanger(Exchanger exchanger) {
    this.exchanger = exchanger;
  }

  @Override
  public void setEvolutionListener(EvolutionListener listener) {
    this.evolutionListener = listener;
  }

  private void createInitialGeneration() {
    Population population = populationFactory.createPopulation();
    evolutionState.setPopulation(population);

    // evaluate fitness of each individual
    for (int x = 0; x < population.size(); x++) {
      fitnessEvaluator.evaluateFitness(population.get(x));
      updateBestIndividuals(population.get(x));
    }
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
    if(individual.getValidationFitness() == null) {
      fitnessEvaluator.evaluateValidationFitness(individual);
    }
    Individual currentBest = evolutionState.getBestValidationIndividual();
    if (currentBest == null
        || individual.getValidationFitness().betterThan(currentBest.getValidationFitness())) {
      evolutionState.setBestValidationIndividual(individual);
    }
  }

  private void doAfterGeneration() {
    evolutionState.incrementGeneration();
    lastSnapshot = evolutionState.takeSnapshot();
    if (evolutionListener != null) {
      evolutionListener.onNewGeneration(lastSnapshot);
    }
  }

  public EvolutionSnapshot getEvolutionSnapshot() {
    return lastSnapshot;
  }

  @Override
  public RandomNumberGenerator getRandomNumberGenerator() {
    return rng;
  }

  protected abstract void evolveOneGeneration();
}
