package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.neurus.TestFitnessFunctions;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.Machine;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class EvolutionBaseTest {

  private static final int POP_SIZE = 5;
  private TestEvolutionBaseImplementor evolution;
  private TerminationCriteria terminationCriteria;
  private PopulationFactory populationFactory;
  private Population population;
  private TestEvolutionListener evolutionListener;

  @Before
  public void setUp() {
    Machine machine = mock(Machine.class);
    EvolutionParameters params = new EvolutionParameters();
    params.setPopulationSize(POP_SIZE);
    population = TestPopulations.populationOfSize(POP_SIZE);
    terminationCriteria = mock(TerminationCriteria.class);
    FitnessFunction fitnessFunction = TestFitnessFunctions.constantFitnessFunction(10d);
    RandomNumberGenerator rng = new DefaultRandomNumberGenerator(1L);
    populationFactory = mock(PopulationFactory.class);
    when(populationFactory.initialize(POP_SIZE)).thenReturn(population);
    evolutionListener = new TestEvolutionListener();
    evolution = new TestEvolutionBaseImplementor(machine, populationFactory, rng,
        fitnessFunction, terminationCriteria, params, evolutionListener);
  }

  @Test
  public void testEvolutionBaseInitializesCorrectlyTheFirstGeneration() {
    stopRightAfterGeneration(0);
    evolution.evolve();
    // verify that the evolution state is correct
    EvolutionState evolutionState = evolution.getEvolutionState();
    assertEquals(0, evolutionState.getGeneration());
    assertEquals(5, evolutionState.getPopulation().size());
    assertEquals(0, evolution.evolveGenerationCounter);
    assertAllIndividualsHaveFitness(evolutionState.getPopulation(), 10d);
  }

  @Test
  public void testEvolutionAdvancesGenerationsCorrectly() {
    stopRightAfterGeneration(2);
    evolution.evolve();
    // verify that the evolution state is correct
    EvolutionState evolutionState = evolution.getEvolutionState();
    assertEquals(2, evolutionState.getGeneration());
    assertEquals(2, evolution.evolveGenerationCounter);
    assertSame(evolution.lastPopulationReturned, evolutionState.getPopulation());
  }

  @Test
  public void testEvolutionCallsListenerAfterEachPopulation() {
    stopRightAfterGeneration(2);
    evolution.evolve();
    // verify that the listener got called three times, initial pop + 2 evolutions
    Assert.assertEquals(2, evolutionListener.lastCalledForGeneration);
  }

  private void stopRightAfterGeneration(int genNumber) {
    OngoingStubbing<Boolean> stub = when(terminationCriteria.shouldTerminate(any(EvolutionState.class)));
    for (int x = 0; x < genNumber; x++) {
      stub = stub.thenReturn(false);
    }
    stub.thenReturn(true);
  }

  private void assertAllIndividualsHaveFitness(Population pop, double fitnessValue) {
    for (int x = 0; x < pop.size(); x++) {
      assertEquals(pop.get(x).getFitness().getValue(), fitnessValue);
    }
  }
}

class TestEvolutionBaseImplementor extends EvolutionBase {

  int evolveGenerationCounter;
  Population lastPopulationReturned;

  public TestEvolutionBaseImplementor(Machine machine, PopulationFactory populationFactory,
      RandomNumberGenerator rng, FitnessFunction fitnessFunction,
      TerminationCriteria terminationStrategy, EvolutionParameters params,
      EvolutionListener evolutionListener) {
    super(machine, populationFactory, rng, fitnessFunction, terminationStrategy, params,
        evolutionListener);
  }

  @Override
  protected Population evolveOneGeneration() {
    evolveGenerationCounter++;
    lastPopulationReturned = new Population(evolutionState.getPopulation());
    return lastPopulationReturned;
  }
}

class TestEvolutionListener implements EvolutionListener {

  int lastCalledForGeneration = -1;

  @Override
  public void onNewGeneration(EvolutionState evolutionState) {
    assertEquals("Was expecting a different generation", lastCalledForGeneration + 1,
        evolutionState.getGeneration());
    lastCalledForGeneration = evolutionState.getGeneration();
  }
}