package org.neurus.island;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.neurus.evolution.Evolution;

import com.google.common.collect.Lists;

public class MultiIslandEvolutionTest {

  @Test
  public void testEvolve() {
    Evolution island0 = Mockito.mock(Evolution.class);
    Evolution island1 = Mockito.mock(Evolution.class);
    Evolution island2 = Mockito.mock(Evolution.class);
    List<Evolution> islands = Lists.newArrayList(island0, island1, island2);

    MultiIslandEvolution multiIsland = new MultiIslandEvolution(islands);
    multiIsland.evolve();

    verify(island0).evolve();
    verify(island1).evolve();
    verify(island2).evolve();
  }
}
