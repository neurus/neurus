package org.neurus.evolution;

import org.neurus.breeder.Breeder;
import org.neurus.fitness.FitnessEvaluator;
import org.neurus.rng.RandomNumberGenerator;

public class SteadyStateEvolution extends EvolutionBase {

  private Breeder breeder;

  public SteadyStateEvolution(PopulationFactory populationFactory,
      FitnessEvaluator fitnessEvaluator, TerminationCriteria terminationStrategy,
      RandomNumberGenerator rng, SelectionMethod selector, SelectionMethod deselector,
      Breeder breeder) {
    super(populationFactory, fitnessEvaluator, terminationStrategy, rng, selector, deselector);
    this.breeder = breeder;
  }

  @Override
  protected void evolveOneGeneration() {
    Population pop = evolutionState.getPopulation();
    int individualsGenerated = 0;
    while (individualsGenerated < pop.size()) {
      int parentsNeeded = breeder.getNumberOfParents();
      int[] parentsIndexes = new int[parentsNeeded];
      Individual[] parents = new Individual[parentsNeeded];
      for (int i = 0; i < parentsNeeded; i++) {
        parentsIndexes[i] = selector.select(pop);
        parents[i] = pop.get(parentsIndexes[i]);
      }
      Individual[] newIndividuals = breeder.breed(parents);
      for (int i = 0; i < newIndividuals.length; i++) {
        fitnessEvaluator.evaluateFitness(newIndividuals[i]);
        updateBestIndividuals(newIndividuals[i]);
        int toBeDiscardedIndex = deselector.select(pop);
        pop.replace(toBeDiscardedIndex, newIndividuals[i]);
        individualsGenerated++;
      }
    }
  }
}
