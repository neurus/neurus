package org.neurus.rng;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class RandomChoiceTest {

  private RandomNumberGenerator rng = Mockito.mock(RandomNumberGenerator.class);

  @Test
  public void testRandomChoiceSelectsCorrectItem() {
    // normalizing this the prob are 0.2, 0.3, 0.5
    // distribution is [0...0.2...0.5...1]
    double[] probs = new double[] { 2, 3, 5 };
    RandomChoice randomChoice = new RandomChoice(rng, probs);
    Mockito.when(rng.nextDouble()).thenReturn(0d);
    Assert.assertEquals(0, randomChoice.pickNext());
    Mockito.when(rng.nextDouble()).thenReturn(0.2d);
    Assert.assertEquals(0, randomChoice.pickNext());
    Mockito.when(rng.nextDouble()).thenReturn(0.2001d);
    Assert.assertEquals(1, randomChoice.pickNext());
    Mockito.when(rng.nextDouble()).thenReturn(0.5d);
    Assert.assertEquals(1, randomChoice.pickNext());
    Mockito.when(rng.nextDouble()).thenReturn(0.51d);
    Assert.assertEquals(2, randomChoice.pickNext());
    Mockito.when(rng.nextDouble()).thenReturn(0.9999d);
    Assert.assertEquals(2, randomChoice.pickNext());
  }
}
