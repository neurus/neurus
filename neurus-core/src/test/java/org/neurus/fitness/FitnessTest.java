package org.neurus.fitness;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class FitnessTest {

  @Test
  public void testGetValue() {
    Fitness f = new Fitness(0.5d);
    assertEquals(0.5d, f.getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFitnessConstructorFailsWithFitnessBelowZero() {
    new Fitness(-0.1d);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFitnessConstructorFailsWithNan() {
    new Fitness(Double.NaN);
  }

  @Test
  public void testBetterThan() {
    assertTrue(new Fitness(0.5d).betterThan(new Fitness(0.6d)));
    assertFalse(new Fitness(0.6d).betterThan(new Fitness(0.5d)));
    assertFalse(new Fitness(0.5d).betterThan(new Fitness(0.5d)));
  }
}
