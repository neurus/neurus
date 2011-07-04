package org.neurus.machine;

public class TrigonometricOperators {

  public static Operator sin() {
    return new BaseOperator(1, true) {

      @Override
      public double execute(double[] inputs) {
        return Math.sin(inputs[0]);
      }
    };
  }

  public static Operator cos() {
    return new BaseOperator(1, true) {

      @Override
      public double execute(double[] inputs) {
        return Math.cos(inputs[0]);
      }
    };
  }
}
