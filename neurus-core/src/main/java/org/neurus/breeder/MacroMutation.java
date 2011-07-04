package org.neurus.breeder;

import org.neurus.evolution.Individual;
import org.neurus.machine.BytecodeWriter;
import org.neurus.machine.InstructionData;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.machine.Program;
import org.neurus.rng.RandomNumberGenerator;

public class MacroMutation implements Breeder {

  private BytecodeWriter bytecodeWriter;
  private int minProgramLength;
  private int maxProgramLength;
  private double pInsertion;
  private Machine machine;
  private RandomNumberGenerator rng;
  private InstructionData instructionData;
  private InstructionRandomizer instructionRandomizer;

  public MacroMutation(Machine machine, RandomNumberGenerator rng,
       double pInsertion, int minProgramLength, int maxProgramLength,
      BytecodeWriter bytecodeWriter, InstructionRandomizer instructionRandomizer) {
    this.instructionRandomizer = instructionRandomizer;
    this.bytecodeWriter = bytecodeWriter;
    this.machine = machine;
    this.rng = rng;
    this.pInsertion = pInsertion;
    this.instructionData = machine.createInstructionData();
    this.minProgramLength = minProgramLength;
    this.maxProgramLength = maxProgramLength;
  }

  @Override
  public Individual[] breed(Individual[] parents) {
    Program parentProgram = parents[0].getProgram();
    Program offspringProgram = null;
    if (shouldDoInsertion(parentProgram)) {
      offspringProgram = doInsertion(parentProgram);
    } else {
      offspringProgram = doDeletion(parentProgram);
    }
    return new Individual[] { new Individual(offspringProgram) };
  }

  private Program doInsertion(Program parentProgram) {
    int oldProgramLength = programLength(parentProgram);
    bytecodeWriter.initProgram(oldProgramLength + 1);
    int insertionPos = rng.nextInt(oldProgramLength + 1);
    bytecodeWriter.copyInstructions(parentProgram.getBytecode(), 0, insertionPos);
    instructionRandomizer.fillRandomInstruction(instructionData);
    bytecodeWriter.writeInstruction(instructionData);
    bytecodeWriter.copyInstructions(parentProgram.getBytecode(), insertionPos,
        oldProgramLength - insertionPos);
    return new Program(bytecodeWriter.getBytecode());
  }

  private Program doDeletion(Program parentProgram) {
    int oldProgramLength = programLength(parentProgram);
    bytecodeWriter.initProgram(oldProgramLength - 1);
    int deletionPos = rng.nextInt(oldProgramLength);
    if (deletionPos > 0) {
      bytecodeWriter.copyInstructions(parentProgram.getBytecode(), 0, deletionPos);
    }
    if (deletionPos < oldProgramLength - 1) {
      bytecodeWriter.copyInstructions(parentProgram.getBytecode(), deletionPos + 1,
          oldProgramLength - deletionPos - 1);
    }
    return new Program(bytecodeWriter.getBytecode());
  }

  private int programLength(Program program) {
    return program.getBytecode().length / machine.getBytesPerInstruction();
  }

  public boolean shouldDoInsertion(Program parentProgram) {
    int programLength = programLength(parentProgram);
    if (programLength >= maxProgramLength || pInsertion == 0) {
      return false;
    } else if (programLength <= minProgramLength) {
      return true;
    } else {
      return rng.nextDouble() <= pInsertion;
    }
  }

  @Override
  public int getMaxNumberOfOffsprings() {
    return 1;
  }

  @Override
  public int getNumberOfParents() {
    return 1;
  }
}
