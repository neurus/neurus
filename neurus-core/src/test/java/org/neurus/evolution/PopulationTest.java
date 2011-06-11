package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class PopulationTest {

  private Individual ind1 = new Individual(new byte[] {});
  private Individual ind2 = new Individual(new byte[] {});

  @Test
  public void testPopulationReturnsCorrectSize() {
    Population pop = new Population(new Individual[] { ind1, ind2 });
    assertEquals(2, pop.size());
  }

  @Test
  public void testGetReturnsInviduals() {
    Population pop = new Population(new Individual[] { ind1, ind2 });
    assertEquals(ind1, pop.get(0));
    assertEquals(ind2, pop.get(1));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetThrowsIndexOutOfBounds() {
    Population pop = new Population(new Individual[] { ind1, ind2 });
    pop.get(2);
  }
}
