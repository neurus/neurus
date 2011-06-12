package org.neurus.evolution;

import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.Machine;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class Evolution {

  // TODO all these constants should be configurable
  private static final int POPULATION_SIZE = 50;
  private static final int MIN_INITIALIZATION_PROGRAM_SIZE = 2;
  private static final int MAX_INITIALIZATION_PROGRAM_SIZE = 10;
  private static final int RNG_SEED = 10;
  private static final double PCONST = 0.5d;

  private RandomNumberGenerator rng;
  private Machine machine;
  private PopulationFactory populationInitializer;
  private IndividualInitializer individualInitializer;
  private EvolutionStrategy evolutionStrategy;
  private TerminationStrategy terminationStrategy;
  private FitnessFunction fitnessFunction;

  public Evolution(Machine machine) {
    this.machine = machine;
    this.rng = new DefaultRandomNumberGenerator(RNG_SEED);
    this.individualInitializer = new SimpleIndividualInitializer(this.machine, rng,
        MIN_INITIALIZATION_PROGRAM_SIZE, MAX_INITIALIZATION_PROGRAM_SIZE, PCONST);
    this.populationInitializer = new PopulationFactory(individualInitializer);
  }

  public void evolve() {
    EvolutionState evolutionState = new EvolutionState();
    Population population = populationInitializer.initialize(POPULATION_SIZE);
    evolutionState.nextGeneration(population);
    for(int x = 0; x < population.size(); x++){
      Individual individual = population.get(x);
      Fitness fitness = fitnessFunction.evaluate(individual);
      individual.setFitness(fitness);
    }
    do {
      population = evolutionStrategy.evolveOneGeneration(population);
      evolutionState.nextGeneration(population);
    } while (!terminationStrategy.terminate(evolutionState));
  }
}
