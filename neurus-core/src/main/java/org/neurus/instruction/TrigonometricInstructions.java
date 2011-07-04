package org.neurus.instruction;

public class TrigonometricInstructions {

  public static Instruction sin() {
    return new BaseInstruction(1, true) {

      @Override
      public double execute(double[] inputs) {
        return Math.sin(inputs[0]);
      }
    };
  }

  public static Instruction cos() {
    return new BaseInstruction(1, true) {

      @Override
      public double execute(double[] inputs) {
        return Math.cos(inputs[0]);
      }
    };
  }
}
