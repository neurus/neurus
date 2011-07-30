package org.neurus.fitness;

import com.google.common.base.Preconditions;

public class Fitness implements Comparable<Fitness> {

  private double value;

  public Fitness(double value) {
    Preconditions.checkArgument(value >= 0);
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public boolean betterThan(Fitness other) {
    return compareTo(other) < 0;
  }

  @Override
  public int compareTo(Fitness o) {
    return Double.compare(value, o.value);
  }
}
