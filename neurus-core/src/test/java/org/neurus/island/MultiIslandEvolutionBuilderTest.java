package org.neurus.island;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.neurus.island.MultiIslandEvolutionBuilder.DEFAULT_MIGRATION_MODULO;
import static org.neurus.island.MultiIslandEvolutionBuilder.DEFAULT_MIGRATION_RATE;
import static org.neurus.testing.MoreAsserts.assertStringContains;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.neurus.evolution.Evolution;
import org.neurus.evolution.EvolutionBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IslandExchanger.class)
public class MultiIslandEvolutionBuilderTest {

  EvolutionBuilder evolutionBuilder = mock(EvolutionBuilder.class);

  @Test
  public void testDefaults() {
    Evolution island0 = mock(Evolution.class);
    Evolution island1 = mock(Evolution.class);
    when(evolutionBuilder.buildIsland(0)).thenReturn(island0);
    when(evolutionBuilder.buildIsland(1)).thenReturn(island1);
    PowerMockito.mockStatic(IslandExchanger.class);

    MultiIslandEvolution multiIsland = new MultiIslandEvolutionBuilder()
        .withEvolution(evolutionBuilder)
        .build();

    // Verify that createExchangerForIslands was called
    PowerMockito.verifyStatic();
    IslandExchanger.createExchangersForIslands(
        Mockito.eq(newArrayList(island0, island1)),
        Mockito.eq(DEFAULT_MIGRATION_MODULO), Mockito.eq(DEFAULT_MIGRATION_RATE));

    // verify a new multiisland is returned with the correct number of islands
    assertEquals(2, multiIsland.getIslands().size());
  }

  @Test
  public void testNonDefaults() {
    Evolution island0 = mock(Evolution.class);
    Evolution island1 = mock(Evolution.class);
    Evolution island2 = mock(Evolution.class);
    Evolution island3 = mock(Evolution.class);
    when(evolutionBuilder.buildIsland(0)).thenReturn(island0);
    when(evolutionBuilder.buildIsland(1)).thenReturn(island1);
    when(evolutionBuilder.buildIsland(2)).thenReturn(island2);
    when(evolutionBuilder.buildIsland(3)).thenReturn(island3);
    PowerMockito.mockStatic(IslandExchanger.class);

    MultiIslandEvolution multiIsland = new MultiIslandEvolutionBuilder()
        .withEvolution(evolutionBuilder)
        .withNumberOfIslands(4)
        .withMigrationRate(0.35)
        .withMigrationModulo(3)
        .build();

    // Verify that createExchangerForIslands was called
    PowerMockito.verifyStatic();
    IslandExchanger.createExchangersForIslands(
        Mockito.eq(newArrayList(island0, island1, island2, island3)),
        Mockito.eq(3), Mockito.eq(0.35d));

    // verify a new multiisland is returned with the correct number of islands
    assertEquals(4, multiIsland.getIslands().size());
  }

  @Test
  public void testNoEvolutionBuilder() {
    try {
      new MultiIslandEvolutionBuilder()
          .build();
      fail("Exception expected");
    } catch (NullPointerException ex) {
      assertStringContains("EvolutionBuilder should be specified", ex.getMessage());
    }
  }
}
