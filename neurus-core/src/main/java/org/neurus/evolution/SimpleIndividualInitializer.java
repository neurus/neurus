package org.neurus.evolution;

import static org.neurus.utils.Primitives.ubtoi;

import org.neurus.instruction.Instruction;
import org.neurus.instruction.InstructionSet;
import org.neurus.rng.RandomNumberGenerator;

import com.google.common.base.Preconditions;

public class SimpleIndividualInitializer implements IndividualInitializer {

  private RandomNumberGenerator rng;
  private InstructionSet instrSet;
  private double pConst;
  private int minSize;
  private int maxSize;

  public SimpleIndividualInitializer(InstructionSet instrSet, RandomNumberGenerator rng,
      int minSize,
      int maxSize, double pConst) {
    this.instrSet = instrSet;
    this.rng = rng;
    this.minSize = minSize;
    this.maxSize = maxSize;
    Preconditions.checkArgument(pConst >= 0 && pConst <= 1,
        "pConst should be a value between 0 and 1");
    this.pConst = pConst;
  }

  public Individual newIndividual() {
    int size = randomSize();
    byte[] bytecode = new byte[size * instrSet.getBytesPerInstruction()];
    int writePos = 0;
    for (int i = 0; i < size; i++) {
      byte instrIndex = randomInstructionIndex();
      bytecode[writePos] = instrIndex;
      Instruction instruction = instrSet.getInstruction(ubtoi(instrIndex));
      if (instruction.getInputRegisters() > 0) {
        // there should be at least 1 calculation register
        bytecode[writePos + 1] = randomCalculationIndex();
        // the rest can be either calculation register or const depending on pConst
        for (int j = 1; j < instruction.getInputRegisters(); j++) {
          if (useConstant()) {
            bytecode[writePos + j + 1] = randomConstantIndex();
          } else {
            bytecode[writePos + j + 1] = randomCalculationIndex();
          }
        }
      }
      if (instruction.hasOutputRegister()) {
        bytecode[writePos + instrSet.getBytesPerInstruction() - 1] = randomCalculationIndex();
      }
      writePos += instrSet.getBytesPerInstruction();
    }
    return new Individual(bytecode);
  }

  private int randomSize() {
    return rng.nextInt(maxSize - minSize + 1) + minSize;
  }

  private byte randomInstructionIndex() {
    int instrIndex = rng.nextInt(instrSet.size());
    return (byte) instrIndex;
  }

  private byte randomCalculationIndex() {
    int calcIndex = rng.nextInt(instrSet.getNumberOfCalculationRegisters());
    return (byte) calcIndex;
  }

  private byte randomConstantIndex() {
    int constIndex = rng.nextInt(instrSet.getNumberOfConstantRegisters());
    int constRegIndex = constIndex + instrSet.getNumberOfCalculationRegisters();
    return (byte) constRegIndex;
  }

  private boolean useConstant() {
    return rng.nextDouble() < pConst;
  }
}
