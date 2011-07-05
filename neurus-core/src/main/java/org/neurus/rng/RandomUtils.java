package org.neurus.rng;

import java.util.BitSet;

public class RandomUtils {

  public static int randomEnabledBit(RandomNumberGenerator rng, BitSet bitSet) {
    int cardinality = bitSet.cardinality();
    if (cardinality == 0) {
      throw new IllegalArgumentException("No enabled bits in bitset: " + bitSet);
    }
    int randomBit = rng.nextInt(cardinality);
    int currBit = 1;
    for (int x = 0; x < randomBit; x++) {
      currBit = bitSet.nextSetBit(currBit + 1);
    }
    return currBit;
  }
}
