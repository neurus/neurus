package org.neurus.machine;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class ConstantRegisters {

  private double[] values;

  public ConstantRegisters(double minValue, double maxValue, double interval) {
    checkArgument(minValue <= maxValue, "minValue should be less or equal than max value");
    checkArgument(interval >= 0, "interval should be greater than 0");
    List<Double> constants = Lists.newArrayList();
    double value = minValue;
    while (value <= maxValue) {
      constants.add(value);
      value += interval;
    }
    values = new double[constants.size()];
    for (int x = 0; x < constants.size(); x++) {
      values[x] = constants.get(x);
    }
  }

  public ConstantRegisters(double[] values) {
    checkNotNull(values, "Values cannot be null");
    this.values = Arrays.copyOf(values, values.length);
  }

  public ConstantRegisters() {
    this.values = new double[0];
  }

  public double[] getValues() {
    return values;
  }

  public int size() {
    return values.length;
  }
}
