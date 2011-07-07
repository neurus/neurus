package org.neurus.evolution;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class DefaultTerminationCriteriaTest {

  private EvolutionState evolutionState = mock(EvolutionState.class);

  @Test
  public void testNumberOfGenerationsCriteria() {
    DefaultTerminationCriteria terminationCriteria = new DefaultTerminationCriteria(500, 0);
    Population notSoGoodPop = TestPopulations.populationWithFitnesses(new double[] { 0.45 });
    when(evolutionState.getPopulation()).thenReturn(notSoGoodPop);
    when(evolutionState.getGeneration()).thenReturn(0);
    assertFalse(terminationCriteria.shouldTerminate(evolutionState));
    when(evolutionState.getGeneration()).thenReturn(498);
    assertFalse(terminationCriteria.shouldTerminate(evolutionState));
    when(evolutionState.getGeneration()).thenReturn(499);
    assertTrue(terminationCriteria.shouldTerminate(evolutionState));
    when(evolutionState.getGeneration()).thenReturn(500);
    assertTrue(terminationCriteria.shouldTerminate(evolutionState));
  }

  @Test
  public void testFitnessThresholdCriteria() {
    DefaultTerminationCriteria terminationCriteria = new DefaultTerminationCriteria(500, 0.4);
    Population notSoGoodPop =
        TestPopulations.populationWithFitnesses(new double[] { 0.45, 0.41, 0.5 });
    when(evolutionState.getPopulation()).thenReturn(notSoGoodPop);

    Population goodPopulation =
        TestPopulations.populationWithFitnesses(new double[] { 0.45, 0.4, 0.5 });
    when(evolutionState.getPopulation()).thenReturn(goodPopulation);
    assertTrue(terminationCriteria.shouldTerminate(evolutionState));

    Population greatPopulation =
        TestPopulations.populationWithFitnesses(new double[] { 0, 0.4, 0.5 });
    when(evolutionState.getPopulation()).thenReturn(greatPopulation);
    assertTrue(terminationCriteria.shouldTerminate(evolutionState));
  }
}
