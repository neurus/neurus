package org.neurus.rng;

import java.util.Random;

public class DefaultRandomNumberGenerator implements RandomNumberGenerator {

  private Random random;

  public DefaultRandomNumberGenerator(long seed) {
    random = new Random(seed);
  }

  @Override
  public int nextInt(int n) {
    return random.nextInt(n);
  }

  @Override
  public double nextDouble() {
    return random.nextDouble();
  }
}
