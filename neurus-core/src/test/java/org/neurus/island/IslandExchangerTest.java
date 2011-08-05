package org.neurus.island;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.neurus.evolution.Evolution;
import org.neurus.evolution.Individual;
import org.neurus.evolution.Population;
import org.neurus.evolution.SelectionMethod;
import org.neurus.evolution.TestPopulations;
import org.neurus.rng.RandomNumberGenerator;

import com.google.common.collect.Lists;

public class IslandExchangerTest {

  SelectionMethod selector;
  IslandExchanger source;
  IslandExchanger outgoing;
  Population population;

  @Before
  public void setUp() {
    selector = Mockito.mock(SelectionMethod.class);
    source = new IslandExchanger(selector, 0.2, 5);
    outgoing = new IslandExchanger(selector, 0.2, 5);
    source.setOutgoingExchanger(outgoing);
    population = TestPopulations.populationOfSize(10);
  }

  @Test
  public void testCreateExchangersForIslands() {
    Evolution island0 = mockIsland();
    Evolution island1 = mockIsland();
    Evolution island2 = mockIsland();
    List<Evolution> islands = Lists.newArrayList(island0, island1, island2);

    List<IslandExchanger> exchangers = IslandExchanger.createExchangersForIslands(islands, 3, 0.4);

    // verify islands has correct exchangers
    verify(island0).setExchanger(exchangers.get(0));
    verify(island1).setExchanger(exchangers.get(1));
    verify(island2).setExchanger(exchangers.get(2));
    // verify exchangers where organized in a ring
    assertEquals(exchangers.get(1), exchangers.get(0).getOutgoingExchanger());
    assertEquals(exchangers.get(2), exchangers.get(1).getOutgoingExchanger());
    assertEquals(exchangers.get(0), exchangers.get(2).getOutgoingExchanger());
  }

  @Test
  public void testMigrateOnlyOnModuloGenerations() {
    // generation is not modulo 5, no migration
    source.migrateIndividuals(population, 2);
    assertTrue(outgoing.receiveIndividuals().isEmpty());

    // generation is modulo 5, then migrate individuals
    source.migrateIndividuals(population, 5);
    assertFalse(outgoing.receiveIndividuals().isEmpty());
  }

  @Test
  public void testMigrationSelectsIndividualsCorrectly() {
    when(selector.select(population)).thenReturn(2).thenReturn(7);

    source.migrateIndividuals(population, 10);

    List<Individual> expected = newArrayList(population.get(2), population.get(7));
    assertEquals(expected, outgoing.receiveIndividuals());
  }

  @Test
  public void testMigrationsAccumulates() {
    when(selector.select(population))
        .thenReturn(2).thenReturn(7)
        .thenReturn(3).thenReturn(8);

    source.migrateIndividuals(population, 10);
    source.migrateIndividuals(population, 15);

    List<Individual> expected = newArrayList(population.get(2), population.get(7));
    assertEquals(expected, outgoing.receiveIndividuals());

    List<Individual> expected2 = newArrayList(population.get(3), population.get(8));
    assertEquals(expected2, outgoing.receiveIndividuals());

    assertTrue(outgoing.receiveIndividuals().isEmpty());
  }

  @Test
  public void testMaxPendingBatches() {
    for (int x = 0; x < 20; x++) {
      source.migrateIndividuals(population, x * 5);
    }

    for (int x = 0; x < IslandExchanger.MAX_PENDING_BATCHES; x++) {
      assertFalse(outgoing.receiveIndividuals().isEmpty());
    }
    assertTrue(outgoing.receiveIndividuals().isEmpty());
  }

  private Evolution mockIsland() {
    RandomNumberGenerator rng = mock(RandomNumberGenerator.class);
    Evolution evo = mock(Evolution.class);
    when(evo.getRandomNumberGenerator()).thenReturn(rng);
    return evo;
  }

}
