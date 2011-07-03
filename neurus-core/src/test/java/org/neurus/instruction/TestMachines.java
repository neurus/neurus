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
        .withOutputRegisters(1)
        .build();
  }

  public static Machine twoInstrnoInputsNoDestination() {
    return new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withInstruction(new FakeInstruction(0, false))
        .withInstruction(new FakeInstruction(0, false))
        .withOutputRegisters(1)
        .build();
  }
}
