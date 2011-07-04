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
    instructionData.operatorIndex = randomOperatorIndex();
    if (instructionData.inputRegisters.length > 0) {
      // there should be at least 1 calculation register
      instructionData.inputRegisters[0] = randomCalculationIndex();
      // the rest can be either calculation register or const depending on pConst
      for (int j = 1; j < instructionData.inputRegisters.length; j++) {
        if (useConstant()) {
          instructionData.inputRegisters[j] = randomConstantIndex();
        } else {
          instructionData.inputRegisters[j] = randomCalculationIndex();
        }
      }
    }
    if (machine.hasDestinationRegister()) {
      instructionData.destinationRegister = randomCalculationIndex();
    }
  }

  private byte randomOperatorIndex() {
    int operatorIndex = rng.nextInt(machine.size());
    return (byte) operatorIndex;
  }

  private byte randomCalculationIndex() {
    int calcIndex = rng.nextInt(machine.getNumberOfCalculationRegisters());
    return (byte) calcIndex;
  }

  private byte randomConstantIndex() {
    int constIndex = rng.nextInt(machine.getNumberOfConstantRegisters());
    int constRegIndex = constIndex + machine.getNumberOfCalculationRegisters();
    return (byte) constRegIndex;
  }

  private boolean useConstant() {
    return rng.nextDouble() < pConst;
  }
}
