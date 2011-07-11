package org.neurus.rng;

public class MersenneTwisterTest extends RandomNumberGeneratorTest {

  @Override
  protected RandomNumberGenerator createRandomNumberGenerator(long seed) {
    return new MersenneTwister(seed);
  }
}
