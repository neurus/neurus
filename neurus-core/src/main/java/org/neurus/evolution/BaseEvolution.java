package org.neurus.evolution;

import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.Machine;
import org.neurus.rng.RandomNumberGenerator;

public abstract class BaseEvolution implements Evolution {

  protected final Machine machine;
  protected final PopulationFactory populationFactory;
  protected final RandomNumberGenerator rng;
  protected final TerminationStrategy terminationStrategy;
  protected final FitnessFunction fitnessFunction;
  protected final EvolutionParameters params;

  protected EvolutionState evolutionState;

  public BaseEvolution(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationStrategy terminationStrategy, EvolutionParameters params) {
    super();
    this.machine = machine;
    this.populationFactory = populationFactory;
    this.rng = rng;
    this.terminationStrategy = terminationStrategy;
    this.fitnessFunction = fitnessFunction;
    this.params = params;
  }
}
