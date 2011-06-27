package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class PopulationTest {

  private Individual ind1 = mock(Individual.class);
  private Individual ind2 = mock(Individual.class);
  private Individual ind3 = mock(Individual.class);

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

  @Test
  public void testCopyConstructor() {
    Population pop = new Population(new Individual[] { ind1, ind2 });
    Population clonedPop = new Population(pop);
    assertEquals(2, clonedPop.size());
    assertSame(ind1, clonedPop.get(0));
    assertSame(ind2, clonedPop.get(1));
  }

  @Test
  public void testReplace() {
    Population pop = new Population(new Individual[] { ind1, ind2 });
    pop.replace(0, ind3);
    pop.replace(1, ind1);
    assertSame(ind3, pop.get(0));
    assertSame(ind1, pop.get(1));
  }

  @Test
  public void testReplacingOnClonedPopulationDoNotAffectOriginal() {
    Population pop = new Population(new Individual[] { ind1, ind2 });
    Population clonedPop = new Population(pop);
    clonedPop.replace(0, ind3);
    assertSame(ind1, pop.get(0));
  }
}
