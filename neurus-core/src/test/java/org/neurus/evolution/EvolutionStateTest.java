package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

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
}
