package org.neurus.rng;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public abstract class RandomNumberGeneratorTest {

  protected abstract RandomNumberGenerator createRandomNumberGenerator(long seed);

  @Test
  public void nextIntGeneratesRandomNumbers() {
    RandomNumberGenerator rng = createRandomNumberGenerator(1L);
    assertAllDifferent(randomInts(rng, 10, 100000));
  }

  @Test
  public void nextIntGeneratesSameNumbersForSameSeed() {
    RandomNumberGenerator rng1 = createRandomNumberGenerator(105l);
    RandomNumberGenerator rng2 = createRandomNumberGenerator(105l);
    assertTrue("both arrays should contain the same numbers",
        Arrays.equals(randomInts(rng1, 10, 100000), randomInts(rng2, 10, 100000)));
  }

  @Test
  public void nextIntGeneratesDiffNumbersForDiffSeed() {
    RandomNumberGenerator rng1 = createRandomNumberGenerator(105l);
    RandomNumberGenerator rng2 = createRandomNumberGenerator(45l);
    assertFalse("arrays should contain different numbers",
        Arrays.equals(randomInts(rng1, 10, 100000), randomInts(rng2, 10, 100000)));
  }

  @Test
  public void nextIntGeneratesNumbersInTheCorrectRange() {
    RandomNumberGenerator rng = createRandomNumberGenerator(1l);
    assertAllBetween(0, 10, randomInts(rng, 100, 10));
  }

  @Test
  public void nextDoubleGeneratesRandomNumbers() {
    RandomNumberGenerator rng = createRandomNumberGenerator(1l);
    assertAllDifferent(randomDoubles(rng, 10));
  }

  @Test
  public void nextDoubleGeneratesSameNumbersForSameSeed() {
    RandomNumberGenerator rng1 = createRandomNumberGenerator(105l);
    RandomNumberGenerator rng2 = createRandomNumberGenerator(105l);
    assertTrue("both arrays should contain the same numbers",
        Arrays.equals(randomDoubles(rng1, 10), randomDoubles(rng2, 10)));
  }

  @Test
  public void nextDoubleGeneratesDiffNumbersForDiffSeed() {
    RandomNumberGenerator rng1 = createRandomNumberGenerator(105l);
    RandomNumberGenerator rng2 = createRandomNumberGenerator(45l);
    assertFalse("arrays should contain different numbers",
        Arrays.equals(randomDoubles(rng1, 10), randomDoubles(rng2, 10)));
  }

  @Test
  public void nextDoubleGeneratesNumbersInTheCorrectRange() {
    RandomNumberGenerator rng = createRandomNumberGenerator(1l);
    assertAllBetween(0d, 1d, randomDoubles(rng, 100));
  }

  private void assertAllDifferent(int[] items) {
    int[] newArray = Arrays.copyOf(items, items.length);
    Arrays.sort(newArray);
    for (int x = 0; x < newArray.length - 1; x++) {
      assertFalse(newArray[x] == newArray[x + 1]);
    }
  }

  private void assertAllDifferent(double[] items) {
    double[] newArray = Arrays.copyOf(items, items.length);
    Arrays.sort(newArray);
    for (int x = 0; x < newArray.length - 1; x++) {
      assertFalse(newArray[x] == newArray[x + 1]);
    }
  }

  private void assertAllBetween(int min, int max, int[] items) {
    for (int x = 0; x < items.length; x++) {
      assertTrue(items[x] >= min);
      assertTrue(items[x] < max);
    }
  }

  private void assertAllBetween(double min, double max, double[] items) {
    for (int x = 0; x < items.length; x++) {
      assertTrue(items[x] >= min);
      assertTrue(items[x] < max);
    }
  }

  private int[] randomInts(RandomNumberGenerator rng, int numberOfIntegers, int max) {
    int[] numbers = new int[numberOfIntegers];
    for (int x = 0; x < numberOfIntegers; x++) {
      numbers[x] = rng.nextInt(max);
    }
    return numbers;
  }

  private double[] randomDoubles(RandomNumberGenerator rng, int numberOfDoubles) {
    double[] numbers = new double[numberOfDoubles];
    for (int x = 0; x < numberOfDoubles; x++) {
      numbers[x] = rng.nextDouble();
    }
    return numbers;
  }
}
