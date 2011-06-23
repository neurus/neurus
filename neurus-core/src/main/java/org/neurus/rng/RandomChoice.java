package org.neurus.rng;

import java.util.Arrays;

public class RandomChoice {

  private RandomNumberGenerator rng;
  private double[] distribution;

  public RandomChoice(RandomNumberGenerator rng, double[] probabilities) {
    super();
    this.rng = rng;
    // normalize weights from 0 to 1
    distribution = new double[probabilities.length];
    double totalProb = 0;
    for (int x = 0; x < probabilities.length; x++) {
      totalProb += probabilities[x];
    }
    distribution[0] = probabilities[0] / totalProb;
    for (int x = 1; x < probabilities.length; x++) {
      distribution[x] = probabilities[x] / totalProb + distribution[x - 1];
    }
  }

  public int pickNext() {
    double randomPoint = rng.nextDouble();
    int index = Arrays.binarySearch(distribution, randomPoint);
    if (index >= 0) {
      return index;
    }
    return -(index + 1);
  }
}
