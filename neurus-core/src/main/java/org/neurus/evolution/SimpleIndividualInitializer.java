package org.neurus.evolution;

import org.neurus.machine.BytecodeWriter;
import org.neurus.machine.InstructionData;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.machine.Program;
import org.neurus.rng.RandomNumberGenerator;

public class SimpleIndividualInitializer implements IndividualInitializer {

  private RandomNumberGenerator rng;
  private InstructionRandomizer instrRandomizer;
  private int minSize;
  private int maxSize;
  private InstructionData instrData;
  private BytecodeWriter bytecodeWriter;

  public SimpleIndividualInitializer(Machine machine, RandomNumberGenerator rng,
      BytecodeWriter bytecodeWriter, InstructionRandomizer instrRandomizer,
      int minSize, int maxSize) {
    this.rng = rng;
    this.minSize = minSize;
    this.maxSize = maxSize;
    this.instrRandomizer = instrRandomizer;
    this.instrData = machine.createInstructionData();
    this.bytecodeWriter = bytecodeWriter;
  }

  public Individual newIndividual() {
    int size = randomSize();
    bytecodeWriter.initProgram(size);
    for (int i = 0; i < size; i++) {
      instrRandomizer.fillRandomInstruction(instrData);
      bytecodeWriter.writeInstruction(instrData);
    }
    return new Individual(new Program(bytecodeWriter.getBytecode()));
  }

  private int randomSize() {
    return rng.nextInt(maxSize - minSize + 1) + minSize;
  }
}
