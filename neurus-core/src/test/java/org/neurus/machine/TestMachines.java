package org.neurus.machine;

import org.neurus.machine.ConstantRegisters;
import org.neurus.machine.Machine;
import org.neurus.machine.MachineBuilder;
import org.neurus.machine.MathOperators;

public class TestMachines {

  public static Machine calculator() {
    return new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withOperator(MathOperators.addition())
        .withOperator(MathOperators.substraction())
        .withOperator(MathOperators.multiplication())
        .withOperator(MathOperators.division())
        .withOutputRegisters(1)
        .build();
  }

  public static Machine twoInstrnoInputsNoDestination() {
    return new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withOperator(new FakeOperator(0, false))
        .withOperator(new FakeOperator(0, false))
        .withOutputRegisters(1)
        .build();
  }
}
