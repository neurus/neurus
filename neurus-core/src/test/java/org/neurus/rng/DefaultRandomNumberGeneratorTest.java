package org.neurus.rng;


public class DefaultRandomNumberGeneratorTest extends RandomNumberGeneratorTest {

  @Override
  protected RandomNumberGenerator createRandomNumberGenerator(long seed) {
    return new DefaultRandomNumberGenerator(seed);
  }
}
