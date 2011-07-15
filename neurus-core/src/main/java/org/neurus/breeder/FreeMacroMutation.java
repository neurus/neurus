package org.neurus.breeder;

import org.neurus.machine.BytecodeWriter;
import org.neurus.machine.InstructionData;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.machine.Program;
import org.neurus.rng.RandomNumberGenerator;

public class FreeMacroMutation extends MacroMutation {

  public FreeMacroMutation(Machine machine, RandomNumberGenerator rng, double pInsertion,
      int minProgramLength, int maxProgramLength, BytecodeWriter bytecodeWriter,
      InstructionRandomizer instructionRandomizer) {
    super(machine, rng, pInsertion, minProgramLength, maxProgramLength, bytecodeWriter,
        instructionRandomizer);
  }

  @Override
  protected boolean prepareProgramForMutation(Program parentProgram) {
    return true;
  }

  @Override
  protected int randomDeletionPos(Program parentProgram) {
    return rng.nextInt(programLength(parentProgram));
  }

  @Override
  protected boolean randomizeInstruction(InstructionData instructionData, Program oldProgram,
      int insertionPosition) {
    instructionRandomizer.fillRandomInstruction(instructionData);
    return true;
  }

  @Override
  protected void postProcessAfterInsertion(Program parentProgram, int insertionPos,
      byte[] offspringBytecode) {
    // do nothing
  }
}
