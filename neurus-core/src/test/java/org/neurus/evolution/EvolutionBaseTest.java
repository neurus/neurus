package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.neurus.TestFitnessFunctions;
import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.Machine;
import org.neurus.instruction.Program;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class EvolutionBaseTest {

  private static final int POP_SIZE = 5;
  private TestEvolutionBaseImplementor evolution;
  private TerminationStrategy terminationStrategy;
  private PopulationFactory populationFactory;
  private Population population;

  @Before
  public void setUp() {
    Machine machine = mock(Machine.class);
    EvolutionParameters params = new EvolutionParameters();
    params.setPopulationSize(POP_SIZE);
    createPopulation();
    terminationStrategy = mock(TerminationStrategy.class);
    FitnessFunction fitnessFunction = TestFitnessFunctions.constantFitnessFunction(10d);
    RandomNumberGenerator rng = new DefaultRandomNumberGenerator(1L);
    populationFactory = mock(PopulationFactory.class);
    when(populationFactory.initialize(POP_SIZE)).thenReturn(population);
    evolution = new TestEvolutionBaseImplementor(machine, populationFactory, rng,
        fitnessFunction, terminationStrategy, params);
  }

  @Test
  public void testEvolutionBaseInitializesCorrectlyTheFirstGeneration() {
    // the evolution should terminate after the first generation is created
    when(terminationStrategy.terminate(any(EvolutionState.class))).thenReturn(true);
    // run the evolution algorithm, its only going to create a population
    evolution.evolve();
    // verify that the evolution state is correct
    EvolutionState evolutionState = evolution.getEvolutionState();
    assertEquals(0, evolutionState.getGeneration());
    assertEquals(5, evolutionState.getPopulation().size());
    assertAllIndividualsHaveFitness(evolutionState.getPopulation(), 10d);
  }

  private void assertAllIndividualsHaveFitness(Population pop, double fitnessValue) {
    for (int x = 0; x < pop.size(); x++) {
      assertEquals(pop.get(x).getFitness().getValue(), fitnessValue);
    }
  }

  private void createPopulation() {
    Individual[] individuals = new Individual[POP_SIZE];
    for (int x = 0; x < POP_SIZE; x++) {
      // TODO make a test utility to create individuals?
      individuals[x] = new Individual(new Program(new byte[] {}));
    }
    population = new Population(individuals);
  }
}

class TestEvolutionBaseImplementor extends EvolutionBase {

  public TestEvolutionBaseImplementor(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationStrategy terminationStrategy, EvolutionParameters params) {
    super(machine, populationFactory, rng, fitnessFunction, terminationStrategy, params);
  }

  @Override
  protected void evolveOneGeneration() {
  }
}
