package org.neurus.machine;

public class LogicOperators {

  public static Operator ifEquals() {
    return new BranchingOperator(2) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] == inputs[1] ? TRUE : FALSE;
      }
    };
  }

  public static Operator ifGreaterThan() {
    return new BranchingOperator(2) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] > inputs[1] ? TRUE : FALSE;
      }
    };
  }

  public static Operator ifLessThan() {
    return new BranchingOperator(2) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] < inputs[1] ? TRUE : FALSE;
      }
    };
  }
}
