package org.neurus.evolution;

import static org.neurus.util.Primitives.ubtoi;

import org.neurus.instruction.Instruction;
import org.neurus.instruction.Machine;
import org.neurus.instruction.Program;
import org.neurus.rng.RandomNumberGenerator;

import com.google.common.base.Preconditions;

public class SimpleIndividualInitializer implements IndividualInitializer {

  private RandomNumberGenerator rng;
  private Machine machine;
  private double pConst;
  private int minSize;
  private int maxSize;

  public SimpleIndividualInitializer(Machine machine, RandomNumberGenerator rng,
      int minSize,
      int maxSize, double pConst) {
    this.machine = machine;
    this.rng = rng;
    this.minSize = minSize;
    this.maxSize = maxSize;
    Preconditions.checkArgument(pConst >= 0 && pConst <= 1,
        "pConst should be a value between 0 and 1");
    this.pConst = pConst;
  }

  public Individual newIndividual() {
    int size = randomSize();
    byte[] bytecode = new byte[size * machine.getBytesPerInstruction()];
    int writePos = 0;
    for (int i = 0; i < size; i++) {
      byte instrIndex = randomInstructionIndex();
      bytecode[writePos] = instrIndex;
      Instruction instruction = machine.getInstruction(ubtoi(instrIndex));
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
        bytecode[writePos + machine.getBytesPerInstruction() - 1] = randomCalculationIndex();
      }
      writePos += machine.getBytesPerInstruction();
    }
    Program program = new Program(bytecode);
    return new Individual(program);
  }

  private int randomSize() {
    return rng.nextInt(maxSize - minSize + 1) + minSize;
  }

  private byte randomInstructionIndex() {
    int instrIndex = rng.nextInt(machine.size());
    return (byte) instrIndex;
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
