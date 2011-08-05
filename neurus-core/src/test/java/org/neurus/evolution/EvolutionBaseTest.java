package org.neurus.evolution;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessEvaluator;
import org.neurus.rng.RandomNumberGenerator;

public class EvolutionBaseTest {

  private static final int POP_SIZE = 5;

  private TestEvolutionBaseImplementor evolution;
  private TerminationCriteria terminationCriteria;
  private PopulationFactory populationFactory;
  private Population population;
  private FitnessEvaluator fitnessEvaluator;
  private SelectionMethod selector;
  private SelectionMethod deselector;
  private RandomNumberGenerator rng;

  @Before
  public void setUp() {
    terminationCriteria = mock(TerminationCriteria.class);
    populationFactory = mock(PopulationFactory.class);
    selector = mock(SelectionMethod.class);
    deselector = mock(SelectionMethod.class);
    rng = mock(RandomNumberGenerator.class);
    population = TestPopulations.populationOfSize(POP_SIZE);
    when(populationFactory.createPopulation()).thenReturn(population);
    fitnessEvaluator = mock(FitnessEvaluator.class);
    evolution = new TestEvolutionBaseImplementor(populationFactory, fitnessEvaluator,
        terminationCriteria, rng, selector, deselector);
  }

  @Test
  public void testFirstGeneration() {
    stopRightAfterGeneration(0);
    setupFitnesses(0.4, 0.6, 0.3, 0.8, 0.9);

    evolution.evolve();

    // verify that the evolution state is correct
    EvolutionSnapshot evolutionState = evolution.getEvolutionSnapshot();
    assertEquals(0, evolutionState.getGenerationNumber());
    assertEquals(5, evolutionState.getPopulation().size());
    assertEquals(0, evolution.evolveGenerationCounter);
    assertSame(population.get(2), evolutionState.getBestTrainingIndividual());
    assertAllIndividualsHaveFitness(evolutionState.getPopulation(), 0.4, 0.6, 0.3, 0.8, 0.9);
  }

  @Test
  public void testFirstGenerationWithValidationFitness() {
    when(fitnessEvaluator.validates()).thenReturn(true);
    stopRightAfterGeneration(0);
    setupFitnesses(0.4, 0.3, 0.5, 0.2, 0.1);
    setupValidationFitnesses(0.4, 0.5, 0, 0.3, 0.6);

    evolution.evolve();

    // best training individiual should be the last one
    EvolutionSnapshot evolutionSnapshot = evolution.getEvolutionSnapshot();
    assertSame(population.get(4), evolutionSnapshot.getBestTrainingIndividual());

    // best validation should be 0.3, because the one with 0 was never best training
    assertSame(population.get(3), evolutionSnapshot.getBestValidationIndividual());
  }

  @Test
  public void testEvolutionAdvancesGenerationsCorrectly() {
    stopRightAfterGeneration(2);
    setupFitnesses(0.4, 0.6, 0.7, 0.8, 0.9);

    evolution.evolve();

    // verify that the evolution state is correct
    EvolutionSnapshot evolutionSnapshot = evolution.getEvolutionSnapshot();
    assertEquals(2, evolutionSnapshot.getGenerationNumber());
    assertEquals(2, evolution.evolveGenerationCounter);
  }

  @Test
  public void testMigrateIndividualsOnEachGeneration() {
    stopRightAfterGeneration(2);
    setupFitnesses(0.4, 0.6, 0.7, 0.8, 0.9);
    Exchanger exchanger = mock(Exchanger.class);
    evolution.setExchanger(exchanger);

    evolution.evolve();

    verify(exchanger).migrateIndividuals(population, 1);
    verify(exchanger).migrateIndividuals(population, 2);
  }

  @Test
  public void testGetRandonNumberGenerator() {
    assertSame(rng, evolution.getRandomNumberGenerator());
  }

  @Test
  public void testReceiveImmigrantsOnEachGeneration() {
    stopRightAfterGeneration(1);
    Exchanger exchanger = mock(Exchanger.class);
    when(deselector.select(population)).thenReturn(0).thenReturn(1);
    setupFitnesses(0.4, 0.6, 0.7, 0.8, 0.9);
    evolution.setExchanger(exchanger);
    Population foreignPopulation = TestPopulations.populationWithFitnesses(0.2, 0.1);
    foreignPopulation.get(0).setValidationFitness(new Fitness(0.01));
    when(exchanger.receiveIndividuals())
        .thenReturn(newArrayList(foreignPopulation.get(0), foreignPopulation.get(1)))
        .thenThrow(new RuntimeException("Shouldn't have been called anymore"));

    evolution.evolve();

    // verify that the evolution state is correct
    EvolutionSnapshot evolutionSnapshot = evolution.getEvolutionSnapshot();
    assertSame(foreignPopulation.get(0), evolutionSnapshot.getPopulation().get(0));
    assertSame(foreignPopulation.get(1), evolutionSnapshot.getPopulation().get(1));
    assertEquals(foreignPopulation.get(1), evolutionSnapshot.getBestTrainingIndividual());
  }

  @Test
  public void testEvolutionCallsListenerAfterEachPopulation() {
    TestEvolutionListener evolutionListener = new TestEvolutionListener();
    evolution.setEvolutionListener(evolutionListener);
    stopRightAfterGeneration(2);
    setupFitnesses(0.4, 0.6, 0.7, 0.8, 0.9);

    evolution.evolve();

    // verify that the listener got called three times, initial pop + 2 evolutions
    assertEquals(2, evolutionListener.lastCalledForGeneration);
  }

  @Test
  public void testEmptyEvolutionSnapshot() {
    EvolutionSnapshot snapshot = evolution.getEvolutionSnapshot();

    Assert.assertEquals(-1, snapshot.getGenerationNumber());
  }

  private void stopRightAfterGeneration(int genNumber) {
    OngoingStubbing<Boolean> stub = when(terminationCriteria
        .shouldTerminate(any(EvolutionState.class)));
    for (int x = 0; x < genNumber; x++) {
      stub = stub.thenReturn(false);
    }
    stub.thenReturn(true);
  }

  private void assertAllIndividualsHaveFitness(Population pop, double... fitnessValues) {
    for (int x = 0; x < pop.size(); x++) {
      assertEquals(pop.get(x).getFitness().getValue(), fitnessValues[x]);
    }
  }

  private void setupFitnesses(final double... fit) {
    for (int x = 0; x < fit.length; x++) {
      final double fitness = fit[x];
      Mockito.doAnswer(new Answer<Void>() {

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
          ((Individual) invocation.getArguments()[0]).setFitness(new Fitness(fitness));
          return null;
        }
      }).when(fitnessEvaluator).evaluateFitness(population.get(x));
    }
  }

  private void setupValidationFitnesses(final double... fit) {
    for (int x = 0; x < fit.length; x++) {
      final double fitness = fit[x];
      Mockito.doAnswer(new Answer<Void>() {

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
          ((Individual) invocation.getArguments()[0]).setValidationFitness(new Fitness(fitness));
          return null;
        }
      }).when(fitnessEvaluator).evaluateValidationFitness(population.get(x));
    }
  }
}

class TestEvolutionBaseImplementor extends EvolutionBase {

  int evolveGenerationCounter;

  public TestEvolutionBaseImplementor(PopulationFactory populationFactory,
      FitnessEvaluator fitnessEvaluator, TerminationCriteria terminationStrategy,
      RandomNumberGenerator rng, SelectionMethod selector, SelectionMethod deselector) {
    super(populationFactory, fitnessEvaluator, terminationStrategy, rng, selector, deselector);
  }

  @Override
  protected void evolveOneGeneration() {
    evolveGenerationCounter++;
  }
}

class TestEvolutionListener implements EvolutionListener {

  int lastCalledForGeneration = -1;

  @Override
  public void onNewGeneration(EvolutionSnapshot evolutionSnapshot) {
    assertEquals("Was expecting a different generation", lastCalledForGeneration + 1,
        evolutionSnapshot.getGenerationNumber());
    lastCalledForGeneration = evolutionSnapshot.getGenerationNumber();
  }
}