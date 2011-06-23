package org.neurus.breeder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.neurus.evolution.Individual;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class CompositeBreederTest {

  private MockBreeder childBreeder1 = new MockBreeder(4, 2);
  private MockBreeder childBreeder2 = new MockBreeder(2, 3);
  private RandomNumberGenerator rng = new DefaultRandomNumberGenerator(1000);
  private CompositeBreeder breeder;

  @Before
  public void setUp() {
    breeder = new CompositeBreeder.Builder(rng)
        .withBreeder(childBreeder1, 0.7)
        .withBreeder(childBreeder2, 0.3)
        .build();
  }

  @Test
  public void testGivesCorrectNumberOfOffsprings() {
    assertEquals(3, breeder.getMaxNumberOfOffsprings());
  }

  @Test
  public void testGivesCorrectNumberOfParents() {
    assertEquals(4, breeder.getNumberOfParents());
  }

  @Test
  public void testBreeding() {
    for (int x = 0; x < 20; x++) {
      breeder.breed(new Individual[] {});
    }
    assertEquals(20, childBreeder1.breedInvocations + childBreeder2.breedInvocations);
    assertTrue(childBreeder2.breedInvocations > 0);
    assertTrue(childBreeder1.breedInvocations > childBreeder2.breedInvocations);
  }
}

class MockBreeder implements Breeder {

  private int offsprings;
  private int parents;
  int breedInvocations;

  MockBreeder(int parents, int offsprings) {
    this.parents = parents;
    this.offsprings = offsprings;
  }

  @Override
  public int getMaxNumberOfOffsprings() {
    return offsprings;
  }

  @Override
  public int getNumberOfParents() {
    return parents;
  }

  @Override
  public Individual[] breed(Individual[] parents) {
    breedInvocations++;
    return new Individual[] {};
  }
}
