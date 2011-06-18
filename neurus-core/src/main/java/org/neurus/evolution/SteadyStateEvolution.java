package org.neurus.evolution;

import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.Machine;
import org.neurus.rng.RandomNumberGenerator;

public class SteadyStateEvolution extends BaseEvolution {

  public SteadyStateEvolution(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationStrategy terminationStrategy, EvolutionParameters params) {
    super(machine, populationFactory, rng, fitnessFunction, terminationStrategy, params);
  }

  public void evolve() {
    createInitialGeneration();
    while (!terminationStrategy.terminate(evolutionState)) {
      evolveOneGeneration();
    }
  }

  private void evolveOneGeneration() {
    Population pop = evolutionState.getPopulation();
    for (int x = 0; x < pop.size() / 2; x++) {
    }
  }

  private void createInitialGeneration() {
    // create evolution state and initial population
    EvolutionState newEvolutionState = new EvolutionState();
    int populationSize = params.getPopulationSize();
    Population population = populationFactory.initialize(populationSize);

    // evaluate fitness of each individual
    for (int x = 0; x < population.size(); x++) {
      Individual individual = population.get(x);
      Fitness fitness = fitnessFunction.evaluate(individual);
      individual.setFitness(fitness);
    }

    // update evolution with the new population
    newEvolutionState.nextGeneration(population);
    evolutionState = newEvolutionState;
  }
}
