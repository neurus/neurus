package org.neurus.rng;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class DefaultRandomNumberGeneratorTest {

  @Test
  public void nextIntGeneratesRandomNumbers() {
    DefaultRandomNumberGenerator rng = new DefaultRandomNumberGenerator(1l);
    assertAllDifferent(randomInts(rng, 10, 100000));
  }

  @Test
  public void nextIntGeneratesSameNumbersForSameSeed() {
    DefaultRandomNumberGenerator rng1 = new DefaultRandomNumberGenerator(105l);
    DefaultRandomNumberGenerator rng2 = new DefaultRandomNumberGenerator(105l);
    assertTrue("both arrays should contain the same numbers",
        Arrays.equals(randomInts(rng1, 10, 100000), randomInts(rng2, 10, 100000)));
  }

  @Test
  public void nextIntGeneratesDiffNumbersForDiffSeed() {
    DefaultRandomNumberGenerator rng1 = new DefaultRandomNumberGenerator(105l);
    DefaultRandomNumberGenerator rng2 = new DefaultRandomNumberGenerator(45l);
    assertFalse("arrays should contain different numbers",
        Arrays.equals(randomInts(rng1, 10, 100000), randomInts(rng2, 10, 100000)));
  }

  @Test
  public void nextIntGeneratesNumbersInTheCorrectRange() {
    DefaultRandomNumberGenerator rng = new DefaultRandomNumberGenerator(1l);
    assertAllBetween(0, 10, randomInts(rng, 100, 10));
  }

  @Test
  public void nextDoubleGeneratesRandomNumbers() {
    DefaultRandomNumberGenerator rng = new DefaultRandomNumberGenerator(1l);
    assertAllDifferent(randomDoubles(rng, 10));
  }

  @Test
  public void nextDoubleGeneratesSameNumbersForSameSeed() {
    DefaultRandomNumberGenerator rng1 = new DefaultRandomNumberGenerator(105l);
    DefaultRandomNumberGenerator rng2 = new DefaultRandomNumberGenerator(105l);
    assertTrue("both arrays should contain the same numbers",
        Arrays.equals(randomDoubles(rng1, 10), randomDoubles(rng2, 10)));
  }

  @Test
  public void nextDoubleGeneratesDiffNumbersForDiffSeed() {
    DefaultRandomNumberGenerator rng1 = new DefaultRandomNumberGenerator(105l);
    DefaultRandomNumberGenerator rng2 = new DefaultRandomNumberGenerator(45l);
    assertFalse("arrays should contain different numbers",
        Arrays.equals(randomDoubles(rng1, 10), randomDoubles(rng2, 10)));
  }

  @Test
  public void nextDoubleGeneratesNumbersInTheCorrectRange() {
    DefaultRandomNumberGenerator rng = new DefaultRandomNumberGenerator(1l);
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


  private int[] randomInts(DefaultRandomNumberGenerator rng, int numberOfIntegers, int max) {
    int[] numbers = new int[numberOfIntegers];
    for (int x = 0; x < numberOfIntegers; x++) {
      numbers[x] = rng.nextInt(max);
    }
    return numbers;
  }

  private double[] randomDoubles(DefaultRandomNumberGenerator rng, int numberOfDoubles) {
    double[] numbers = new double[numberOfDoubles];
    for (int x = 0; x < numberOfDoubles; x++) {
      numbers[x] = rng.nextDouble();
    }
    return numbers;
  }
}
