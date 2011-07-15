package org.neurus.breeder;

import static org.neurus.rng.RandomUtils.randomEnabledBit;
import static org.neurus.util.Primitives.ubtoi;

import java.util.BitSet;

import org.neurus.machine.BytecodeWriter;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.InstructionData;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.machine.Operator;
import org.neurus.machine.Program;
import org.neurus.rng.RandomNumberGenerator;

public class EffectiveMacroMutation extends MacroMutation {

  private EffectivenessAnalyzer effectivenessAnalyzer;

  public EffectiveMacroMutation(Machine machine, RandomNumberGenerator rng, double pInsertion,
      int minProgramLength, int maxProgramLength, BytecodeWriter bytecodeWriter,
      InstructionRandomizer instructionRandomizer, EffectivenessAnalyzer effectivenessAnalyzer) {
    super(machine, rng, pInsertion, minProgramLength, maxProgramLength, bytecodeWriter,
        instructionRandomizer);
    this.effectivenessAnalyzer = effectivenessAnalyzer;
  }

  @Override
  protected boolean prepareProgramForMutation(Program parentProgram) {
    effectivenessAnalyzer.analyzeProgram(parentProgram);
    // can't pick any effective instructions in a totally non effective program
    return parentProgram.getEffectiveInstructions().cardinality() > 0;
  }

  @Override
  protected int randomDeletionPos(Program parentProgram) {
    return randomEnabledBit(rng, parentProgram.getEffectiveInstructions());
  }

  @Override
  protected boolean randomizeInstruction(InstructionData instructionData, Program oldProgram,
      int insertionPosition) {
    BitSet effectiveRegisters =
        effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(oldProgram,
            insertionPosition - 1);
    if (effectiveRegisters.cardinality() == 0) {
      return false;
    }
    instructionRandomizer.fillRandomInstruction(instructionData, effectiveRegisters);
    return true;
  }

  @Override
  protected void postProcessAfterInsertion(Program parentProgram, int insertionPos,
      byte[] offspringBytecode) {
    // we only do postprocessing if inserted instr is branching
    Operator op = operatorAtLine(offspringBytecode, insertionPos);
    if (!op.isBranching()) {
      return;
    }

    int programLength = programLength(parentProgram);
    byte[] parentBytecode = parentProgram.getBytecode();
    int pos = insertionPos;
    while (true) {
      if (pos >= programLength) {
        break;
      }
      if (!operatorAtLine(parentBytecode, pos).isBranching()) {
        BitSet effRegisters = effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(
            parentProgram, pos);
        if(effRegisters.cardinality() == 0) {
          break;
        }
        int writeAddress = address(pos + 1) + machine.getBytesPerInstruction() - 1;
        offspringBytecode[writeAddress] = (byte) randomEnabledBit(rng, effRegisters);
        break;
      }
      pos++;
    }
  }

  private Operator operatorAtLine(byte[] bytecode, int line) {
    return machine.getOperator(ubtoi(bytecode[address(line)]));
  }

  private int address(int pointer) {
    return pointer * machine.getBytesPerInstruction();
  }
}
