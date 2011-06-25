package org.neurus.instruction;

public class TestMachines {

  public static Machine calculator() {
    return new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withInstruction(MathInstructions.addition())
        .withInstruction(MathInstructions.substraction())
        .withInstruction(MathInstructions.multiplication())
        .withInstruction(MathInstructions.division())
        .build();
  }

  public static Machine twoInstrnoInputsNoOutputs() {
    return new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withInstruction(new FakeInstruction(0, false))
        .withInstruction(new FakeInstruction(0, false))
        .build();
  }
}