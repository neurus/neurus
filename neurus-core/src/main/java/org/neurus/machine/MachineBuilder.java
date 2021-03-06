package org.neurus.machine;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class MachineBuilder {

  private List<Operator> operators = Lists.newArrayList();
  private ConstantRegisters constantRegisters = new ConstantRegisters();
  private int calculationRegisters = -1;
  private int outputRegisters = -1;

  public Machine build() {
    Preconditions.checkState(
        operators.size() > 0, "You need to provide at least one operator");
    Preconditions.checkState(calculationRegisters >= 0,
        "You need to provide the number of calculation registers");
    Preconditions.checkState(outputRegisters >= 0,
        "You need to provide the number of output registers");
    Preconditions.checkState(outputRegisters <= calculationRegisters,
            "The number of output registers should be less or equal "
                + "to the number of calculation registers");
    Operator[] operatorArray = new Operator[operators.size()];
    operatorArray = operators.toArray(operatorArray);
    return new Machine(operatorArray, calculationRegisters, constantRegisters, outputRegisters);
  }

  public MachineBuilder withOperator(Operator operator) {
    Preconditions.checkNotNull(operator);
    operators.add(operator);
    return this;
  }

  public MachineBuilder withConstantRegisters(ConstantRegisters constantRegisters) {
    Preconditions.checkNotNull(constantRegisters);
    this.constantRegisters = constantRegisters;
    return this;
  }

  public MachineBuilder withCalculationRegisters(int calculationRegisters) {
    Preconditions.checkArgument(calculationRegisters >= 0);
    this.calculationRegisters = calculationRegisters;
    return this;
  }

  public MachineBuilder withOutputRegisters(int outputRegisters) {
    Preconditions.checkArgument(outputRegisters >= 0);
    this.outputRegisters = outputRegisters;
    return this;
  }
}
