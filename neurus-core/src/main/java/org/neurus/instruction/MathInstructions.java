package org.neurus.instruction;

public class MathInstructions {

  public static Instruction addition() {
    return new BaseInstruction(2, true) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] + inputs[1];
      }
    };
  }

  public static Instruction substraction() {
    return new BaseInstruction(2, true) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] - inputs[1];
      }
    };
  }


  public static Instruction multiplication() {
    return new BaseInstruction(2, true) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] * inputs[1];
      }
    };
  }


  public static Instruction division() {
    return new BaseInstruction(2, true) {

      @Override
      public double execute(double[] inputs) {
        return inputs[0] * inputs[1];
      }
    };
  }
}
