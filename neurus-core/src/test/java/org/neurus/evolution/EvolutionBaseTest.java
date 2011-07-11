package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.neurus.TestFitnessFunctions;
import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.Machine;
import org.neurus.machine.Program;
import org.neurus.machine.ProgramRunner;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;
import org.neurus.util.TestBitSetUtils;

public class EvolutionBaseTest {

  private static final int POP_SIZE = 5;
  private TestEvolutionBaseImplementor evolution;
  private TerminationCriteria terminationCriteria;
  private PopulationFactory populationFactory;
  private Population population;
  private TestEvolutionListener evolutionListener;
  private EffectivenessAnalyzer effAnalyzer;
  private ProgramRunner programRunner;
  private FitnessFunction fitnessFunction;

  @Before
  public void setUp() {
    programRunner = mock(ProgramRunner.class);
    Machine machine = mock(Machine.class);
    when(machine.createRunner()).thenReturn(programRunner);
    EvolutionParameters params = new EvolutionParameters();
    params.setPopulationSize(POP_SIZE);
    population = TestPopulations.populationOfSize(POP_SIZE);
    terminationCriteria = mock(TerminationCriteria.class);
    fitnessFunction = TestFitnessFunctions.constantFitnessFunction(10d);
    fitnessFunction = spy(fitnessFunction);
    RandomNumberGenerator rng = new DefaultRandomNumberGenerator(1L);
    populationFactory = mock(PopulationFactory.class);
    effAnalyzer = mock(EffectivenessAnalyzer.class);
    when(populationFactory.initialize(POP_SIZE)).thenReturn(population);
    evolutionListener = new TestEvolutionListener();
    evolution = new TestEvolutionBaseImplementor(machine, populationFactory, rng,
        fitnessFunction, terminationCriteria, params, evolutionListener, effAnalyzer);
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
  public void testEvaluateIndividual() {
    stopRightAfterGeneration(0);
    storeEmptyBitSetWhenAnalyzedIsCalled();
    ensureAnalyzeWasCalledBeforeProgramLoading();
    ensureProgramIsLoadedBeforeEvaluation();
    evolution.evolve();
    EvolutionState evolutionState = evolution.getEvolutionState();
    assertAllIndividualsHaveFitness(evolutionState.getPopulation(), 10d);
  }

  private void ensureProgramIsLoadedBeforeEvaluation() {
    when(fitnessFunction.evaluate(same(programRunner), any(Individual.class)))
        .thenAnswer(new Answer<Fitness>() {
          @Override
          public Fitness answer(InvocationOnMock invocation) throws Throwable {
            Program p = ((Individual) invocation.getArguments()[1]).getProgram();
            assertEquals(TestBitSetUtils.valueOf("1"), p.getEffectiveInstructions());
            return (Fitness) invocation.callRealMethod();
          }
        });
  }

  private void ensureAnalyzeWasCalledBeforeProgramLoading() {
    doAnswer(new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        Program p = (Program) invocation.getArguments()[0];
        assertEquals(p.getEffectiveInstructions(), new BitSet());
        p.setEffectiveInstructions(TestBitSetUtils.valueOf("1"));
        return null;
      }
    }).when(programRunner).load(any(Program.class), eq(true));
  }

  private void storeEmptyBitSetWhenAnalyzedIsCalled() {
    doAnswer(new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        Program p = (Program) invocation.getArguments()[0];
        p.setEffectiveInstructions(new BitSet());
        return null;
      }
    }).when(effAnalyzer).analyzeProgram(any(Program.class));
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
    assertEquals(2, evolutionListener.lastCalledForGeneration);
  }

  private void stopRightAfterGeneration(int genNumber) {
    OngoingStubbing<Boolean> stub = when(terminationCriteria
        .shouldTerminate(any(EvolutionState.class)));
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
      EvolutionListener evolutionListener, EffectivenessAnalyzer effectivenessAnalyzer) {
    super(machine, populationFactory, rng, fitnessFunction, terminationStrategy, params,
        evolutionListener, effectivenessAnalyzer);
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