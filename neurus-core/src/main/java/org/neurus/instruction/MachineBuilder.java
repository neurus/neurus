package org.neurus.instruction;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class MachineBuilder {

  private List<Instruction> instructions = Lists.newArrayList();
  private int constantRegisters = -1;
  private int calculationRegisters = -1;

  public Machine build() {
    Preconditions.checkState(
        instructions.size() > 0, "You need to provide at least one instruction");
    Preconditions.checkState(constantRegisters >= 0,
        "You need to provide the number of constant registers");
    Preconditions.checkState(calculationRegisters >= 0,
        "You need to provide the number of calculation registers");
    Instruction[] instrArray = new Instruction[instructions.size()];
    instrArray = instructions.toArray(instrArray);
    return new Machine(instrArray, calculationRegisters, constantRegisters);
  }

  public MachineBuilder withInstruction(Instruction instruction) {
    Preconditions.checkNotNull(instruction);
    instructions.add(instruction);
    return this;
  }

  public MachineBuilder withConstantRegisters(int registers) {
    Preconditions.checkArgument(registers >= 0);
    this.constantRegisters = registers;
    return this;
  }

  public MachineBuilder withCalculationRegisters(int registers) {
    Preconditions.checkArgument(registers >= 0);
    this.calculationRegisters = registers;
    return this;
  }
}
