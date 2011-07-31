package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class EvolutionStateTest {

  @Test
  public void testInitialState() {
    EvolutionState evolutionState = new EvolutionState();
    assertEquals(evolutionState.getGeneration(), -1);
    assertEquals(evolutionState.getPopulation(), null);
  }

  @Test
  public void testNextGeneration() {
    EvolutionState evolutionState = new EvolutionState();
    evolutionState.incrementGeneration();
    assertEquals(0, evolutionState.getGeneration());
    evolutionState.incrementGeneration();
    assertEquals(1, evolutionState.getGeneration());
  }

  @Test
  public void testTakeSnapshot() {
    Population pop = TestPopulations.populationOfSize(5);
    Individual bestTraning = Mockito.mock(Individual.class);
    Individual bestValidation = Mockito.mock(Individual.class);
    EvolutionState evolutionState = new EvolutionState();
    evolutionState.incrementGeneration();
    evolutionState.incrementGeneration();
    evolutionState.setBestTrainingIndividual(bestTraning);
    evolutionState.setBestValidationIndividual(bestValidation);
    evolutionState.setPopulation(pop);

    EvolutionSnapshot snapshot = evolutionState.takeSnapshot();

    assertEquals(snapshot.getGenerationNumber(), 1);
    assertEquals(bestTraning, snapshot.getBestTrainingIndividual());
    assertEquals(bestValidation, snapshot.getBestValidationIndividual());
    assertNotSame(pop, snapshot.getPopulation());
    assertEquals(pop.size(), snapshot.getPopulation().size());
    assertEquals(pop.get(4), snapshot.getPopulation().get(4));
  }

  @Test
  public void testTakeSnapshotOnNullPopulation() {
    EvolutionState evolutionState = new EvolutionState();

    EvolutionSnapshot snapshot = evolutionState.takeSnapshot();

    assertEquals(-1, snapshot.getGenerationNumber());
    Assert.assertNull(snapshot.getPopulation());
  }
}
