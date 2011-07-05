package org.neurus.rng;

import java.util.BitSet;

public class RandomUtils {

  public static int randomEnabledBit(RandomNumberGenerator rng, BitSet bitset) {
    int cardinality = bitset.cardinality();
    if (cardinality == 0) {
      return -1;
    }
    int randomBit = rng.nextInt(cardinality);

    int currBit = 1;
    for (int x = 0; x < randomBit; x++) {
      currBit = bitset.nextSetBit(currBit + 1);
    }
    return currBit;
  }
}
