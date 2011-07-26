package org.neurus.fitness;

import com.google.common.base.Preconditions;

public class Fitness {

  private double value;

  public Fitness(double value) {
    Preconditions.checkArgument(value >= 0);
    this.value = value;
  }

  public double getValue() {
    return value;
  }
}
