package org.neurus.evolution;

import org.neurus.instruction.BytecodeWriter;
import org.neurus.instruction.InstructionData;
import org.neurus.instruction.InstructionRandomizer;
import org.neurus.instruction.Machine;
import org.neurus.instruction.Program;
import org.neurus.rng.RandomNumberGenerator;

public class SimpleIndividualInitializer implements IndividualInitializer {

  private RandomNumberGenerator rng;
  private InstructionRandomizer instrRandomizer;
  private int minSize;
  private int maxSize;
  private InstructionData instrData;
  private BytecodeWriter bytecodeWriter;

  public SimpleIndividualInitializer(Machine machine, RandomNumberGenerator rng,
      int minSize, int maxSize, double pConst) {
    this.rng = rng;
    this.minSize = minSize;
    this.maxSize = maxSize;
    this.instrRandomizer = new InstructionRandomizer(machine, rng, pConst);
    this.instrData = machine.createInstructionData();
    this.bytecodeWriter = new BytecodeWriter(machine);
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
