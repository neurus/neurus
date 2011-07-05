package org.neurus.rng;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.BitSet;

import org.junit.Test;
import org.neurus.util.TestBitSetUtils;

public class RandomUtilsTest {

  private RandomNumberGenerator rng = mock(RandomNumberGenerator.class);

  @Test
  public void testRandomEnabledBitRetursMinusOneIfNoEnabledBit() {
    BitSet bs = TestBitSetUtils.valueOf("00000000");
    assertEquals(-1, RandomUtils.randomEnabledBit(rng, bs));
  }

  @Test
  public void testRandomEnabledBitRetursCorrectIndex() {
    BitSet bs = TestBitSetUtils.valueOf("10000010");
    when(rng.nextInt(2)).thenReturn(1);
    assertEquals(6, RandomUtils.randomEnabledBit(rng, bs));
  }
}
