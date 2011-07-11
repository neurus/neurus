package org.neurus.evolution;

import org.neurus.breeder.Breeder;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.Machine;
import org.neurus.rng.RandomNumberGenerator;

public class SteadyStateEvolution extends EvolutionBase {

  private SelectionMethod selector;
  private SelectionMethod deselector;
  private Breeder breeder;

  public SteadyStateEvolution(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationCriteria terminationStrategy, EvolutionParameters params,
      SelectionMethod selector, SelectionMethod deselector, Breeder breeder,
      EvolutionListener evolutionListener, EffectivenessAnalyzer effectivenessAnalyzer) {
    super(machine, populationFactory, rng, fitnessFunction, terminationStrategy, params,
        evolutionListener, effectivenessAnalyzer);
    this.selector = selector;
    this.deselector = deselector;
    this.breeder = breeder;
  }

  @Override
  protected Population evolveOneGeneration() {
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
      for (int i = 0; i < newIndividuals.length; i++) {
        evaluateIndividual(newIndividuals[i]);
      }
      int individualsToBeDiscarded = newIndividuals.length;
      for (int i = 0; i < individualsToBeDiscarded; i++) {
        int toBeDiscardedIndex = deselector.select(newPop);
        newPop.replace(toBeDiscardedIndex, newIndividuals[i]);
        individualsGenerated++;
      }
    }
    return newPop;
  }
}
