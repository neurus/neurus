package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class EvolutionStateTest {

  @Test
  public void testInitialState() {
    EvolutionState evolutionState = new EvolutionState();
    assertEquals(evolutionState.getGeneration(), -1);
    assertEquals(evolutionState.getPopulation(), null);
  }

  @Test
  public void testAdvanceGeneration() {
    EvolutionState evolutionState = new EvolutionState();
    Population pop1 = mock(Population.class);
    evolutionState.nextGeneration(pop1);
    assertEquals(pop1, evolutionState.getPopulation());
    assertEquals(0, evolutionState.getGeneration());
    Population pop2 = mock(Population.class);
    evolutionState.nextGeneration(pop2);
    assertEquals(pop2, evolutionState.getPopulation());
    assertEquals(1, evolutionState.getGeneration());
  }
}
