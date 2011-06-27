package org.neurus.evolution;

import org.neurus.breeder.Breeder;
import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.Machine;
import org.neurus.rng.RandomNumberGenerator;

public class SteadyStateEvolution extends EvolutionBase {

  private SelectionMethod selector;
  private SelectionMethod deselector;
  private Breeder breeder;

  public SteadyStateEvolution(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationStrategy terminationStrategy, EvolutionParameters params,
      SelectionMethod selector, SelectionMethod deselector, Breeder breeder) {
    super(machine, populationFactory, rng, fitnessFunction, terminationStrategy, params);
    this.selector = selector;
    this.deselector = deselector;
    this.breeder = breeder;
  }

  @Override
  protected void evolveOneGeneration() {
    Population newPop = new Population(evolutionState.getPopulation());
    int individualsGenerated = 0;
    while (individualsGenerated < newPop.size()) {
      int parentsNeeded = breeder.getNumberOfParents();
      int[] parentsIndexes = new int[parentsNeeded];
      Individual[] parents = new Individual[parentsNeeded];
      for (int i = 0; i < parentsNeeded; i++) {
        parentsIndexes[i] = selector.select(newPop);
        parents[i] = newPop.get(parentsIndexes[i]);
      }
      Individual[] newIndividuals = breeder.breed(parents);

      int individualsToBeDiscarded = newIndividuals.length;
      for (int i = 0; i < individualsToBeDiscarded; i++) {
        int toBeDiscardedIndex = deselector.select(newPop);
        newPop.replace(toBeDiscardedIndex, newIndividuals[i]);
      }
    }
    evolutionState.nextGeneration(newPop);
  }
}
