package org.neurus.evolution;

import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.Machine;
import org.neurus.rng.RandomNumberGenerator;

public class SteadyStateEvolution extends EvolutionBase {

  public SteadyStateEvolution(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationStrategy terminationStrategy, EvolutionParameters params) {
    super(machine, populationFactory, rng, fitnessFunction, terminationStrategy, params);
  }

  @Override
  protected void evolveOneGeneration() {
    Population pop = evolutionState.getPopulation();
    for (int x = 0; x < pop.size() / 2; x++) {
    }
  }
}
