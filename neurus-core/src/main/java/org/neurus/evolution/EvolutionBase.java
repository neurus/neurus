package org.neurus.evolution;

import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.Machine;
import org.neurus.instruction.ProgramRunner;
import org.neurus.rng.RandomNumberGenerator;

public abstract class EvolutionBase implements Evolution {

  protected final Machine machine;
  protected final PopulationFactory populationFactory;
  protected final RandomNumberGenerator rng;
  protected final TerminationStrategy terminationStrategy;
  protected final FitnessFunction fitnessFunction;
  protected final EvolutionParameters params;
  protected final ProgramRunner programRunner;
  protected final EvolutionListener evolutionListener;

  protected EvolutionState evolutionState;

  public EvolutionBase(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationStrategy terminationStrategy, EvolutionParameters params,
      EvolutionListener evolutionListener) {
    super();
    this.machine = machine;
    this.populationFactory = populationFactory;
    this.rng = rng;
    this.terminationStrategy = terminationStrategy;
    this.fitnessFunction = fitnessFunction;
    this.params = params;
    this.programRunner = machine.createRunner();
    this.evolutionListener = evolutionListener;
  }

  public void evolve() {
    createInitialGeneration();
    evolutionListener.onNewGeneration(evolutionState);
    while (!terminationStrategy.terminate(evolutionState)) {
      Population newPopulation = evolveOneGeneration();
      evolutionState.nextGeneration(newPopulation);
      evolutionListener.onNewGeneration(evolutionState);
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
      Fitness fitness = fitnessFunction.evaluate(programRunner, individual);
      individual.setFitness(fitness);
    }

    // update evolution with the new population
    newEvolutionState.nextGeneration(population);
    evolutionState = newEvolutionState;
  }

  public EvolutionState getEvolutionState() {
    return evolutionState;
  }

  protected abstract Population evolveOneGeneration();
}
