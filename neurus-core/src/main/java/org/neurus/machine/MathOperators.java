package org.neurus.machine;

public class MathOperators {

  public static Operator addition() {
    return new BaseOperator(2, true) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] + inputs[1];
      }
    };
  }

  public static Operator substraction() {
    return new BaseOperator(2, true) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] - inputs[1];
      }
    };
  }


  public static Operator multiplication() {
    return new BaseOperator(2, true) {

      @Override
      public double execute(double[] inputs) {
        return protectedResult(inputs[0] * inputs[1]);
      }
    };
  }


  public static Operator division() {
    return new BaseOperator(2, true) {

      @Override
      public double execute(double[] inputs) {
        return protectedResult(inputs[0] / inputs[1]);
      }
    };
  }

  public static double protectedResult(double result) {
    if(Double.isInfinite(result) || Double.isNaN(result)) {
      return 0;
    }
    return result;
  }
}
