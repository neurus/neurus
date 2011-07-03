package org.neurus.instruction;

public class LogicInstructions {

  public static Instruction ifEquals() {
    return new BranchingInstruction(2) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] == inputs[1] ? TRUE : FALSE;
      }
    };
  }

  public static Instruction ifGreaterThan() {
    return new BranchingInstruction(2) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] > inputs[1] ? TRUE : FALSE;
      }
    };
  }

  public static Instruction ifLessThan() {
    return new BranchingInstruction(2) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] < inputs[1] ? TRUE : FALSE;
      }
    };
  }
}
