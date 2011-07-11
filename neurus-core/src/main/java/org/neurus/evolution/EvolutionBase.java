package org.neurus.evolution;

import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.Machine;
import org.neurus.machine.ProgramRunner;
import org.neurus.rng.RandomNumberGenerator;

public abstract class EvolutionBase implements Evolution {

  protected final Machine machine;
  protected final PopulationFactory populationFactory;
  protected final RandomNumberGenerator rng;
  protected final TerminationCriteria terminationCriteria;
  protected final FitnessFunction fitnessFunction;
  protected final EvolutionParameters params;
  protected final ProgramRunner programRunner;
  protected final EvolutionListener evolutionListener;
  protected final EffectivenessAnalyzer effectivenessAnalyzer;

  protected EvolutionState evolutionState;

  public EvolutionBase(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationCriteria terminationCriteria, EvolutionParameters params,
      EvolutionListener evolutionListener, EffectivenessAnalyzer effectivenessAnalyzer) {
    super();
    this.machine = machine;
    this.populationFactory = populationFactory;
    this.rng = rng;
    this.terminationCriteria = terminationCriteria;
    this.fitnessFunction = fitnessFunction;
    this.params = params;
    this.programRunner = machine.createRunner();
    this.evolutionListener = evolutionListener;
    this.effectivenessAnalyzer = effectivenessAnalyzer;
  }

  public void evolve() {
    createInitialGeneration();
    evolutionListener.onNewGeneration(evolutionState);
    while (!terminationCriteria.shouldTerminate(evolutionState)) {
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
      evaluateIndividual(population.get(x));
    }

    // update evolution with the new population
    newEvolutionState.nextGeneration(population);
    evolutionState = newEvolutionState;
  }

  protected void evaluateIndividual(Individual individual) {
    effectivenessAnalyzer.analyzeProgram(individual.getProgram());
    programRunner.load(individual.getProgram(), true);
    Fitness fitness = fitnessFunction.evaluate(programRunner, individual);
    individual.setFitness(fitness);
  }

  public EvolutionState getEvolutionState() {
    return evolutionState;
  }

  protected abstract Population evolveOneGeneration();
}
