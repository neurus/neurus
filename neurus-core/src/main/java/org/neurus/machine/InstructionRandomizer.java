package org.neurus.machine;

import org.neurus.rng.RandomNumberGenerator;

import com.google.common.base.Preconditions;

public class InstructionRandomizer {

  private Machine machine;
  private RandomNumberGenerator rng;
  private double pConst;

  public InstructionRandomizer(Machine machine, RandomNumberGenerator rng, double pConst) {
    super();
    this.machine = machine;
    this.rng = rng;
    Preconditions.checkArgument(pConst >= 0 && pConst <= 1,
        "pConst should be a value between 0 and 1");
    this.pConst = pConst;
  }

  public void fillRandomInstruction(InstructionData instructionData) {
    instructionData.operatorIndex = randomOperator();
    if (instructionData.inputRegisters.length > 0) {
      // there should be at least 1 calculation register
      instructionData.inputRegisters[0] = randomCalculationRegister();
      // the rest can be either calculation register or const depending on pConst
      for (int j = 1; j < instructionData.inputRegisters.length; j++) {
        instructionData.inputRegisters[j] = randomRegister();
      }
    }
    if (machine.hasDestinationRegister()) {
      instructionData.destinationRegister = randomCalculationRegister();
    }
  }

  public int randomCalculationRegister() {
    int calcIndex = rng.nextInt(machine.getNumberOfCalculationRegisters());
    return calcIndex;
  }

  public int randomRegister() {
    if (rng.nextDouble() < pConst) {
      return randomConstantIndex();
    } else {
      return randomCalculationRegister();
    }
  }

  public int randomOperator() {
    int operatorIndex = rng.nextInt(machine.size());
    return operatorIndex;
  }

  private int randomConstantIndex() {
    int constIndex = rng.nextInt(machine.getNumberOfConstantRegisters());
    int constRegIndex = constIndex + machine.getNumberOfCalculationRegisters();
    return constRegIndex;
  }
}
