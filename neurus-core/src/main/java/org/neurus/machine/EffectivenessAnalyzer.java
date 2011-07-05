package org.neurus.machine;

import static org.neurus.util.Primitives.ubtoi;

import java.util.BitSet;

// TODO Pull some common functions that are repeated here and in InterpreterRunner
public class EffectivenessAnalyzer {

  private final int bpi;
  private final Machine machine;

  public EffectivenessAnalyzer(Machine machine) {
    bpi = machine.getBytesPerInstruction();
    this.machine = machine;
  }

  public void analyzeProgram(Program program) {
    // if we already studied a program effectiveness, we don't have to do it again
    if (program.getEffectiveInstructions() != null) {
      return;
    }
    BitSet effectiveInstructions = newEffectiveInstructionsBitSet(program);
    doEvaluation(program, -1, effectiveInstructions, newEffectiveRegistersArray());
    program.setEffectiveInstructions(effectiveInstructions);
  }

  public BitSet analyzeEffectiveRegistersAtInstruction(Program program, int instruction) {
    BitSet effectiveRegisters = newEffectiveRegistersArray();
    doEvaluation(program, instruction, newEffectiveInstructionsBitSet(program), effectiveRegisters);
    return effectiveRegisters;
  }

  private void doEvaluation(Program program, int stopAtLine, BitSet effectiveInstructions,
      BitSet effectiveRegisters) {
    
    byte[] bytecode = program.getBytecode();
    int programLength = bytecode.length / bpi;

    // at the beginning, only output registers are effective
    for (int x = 0; x < machine.getNumberOfOutputRegisters(); x++) {
      effectiveRegisters.set(x);
    }

    // start at the last instruction and go backwards
    int linePointer = programLength - 1;
    while (linePointer > stopAtLine) {
      // if this is already marked, it means its a branching instruction and we just need to mark
      // the input registers as effective
      if (effectiveInstructions.get(linePointer)) {
        markInputRegistersAsEffective(bytecode, linePointer, effectiveRegisters);
        linePointer--;
        continue;
      }
      // find instruction with destination register in Reff
      int destinationRegister = currentDestinationRegister(linePointer, bytecode);
      if (isEffectiveRegister(destinationRegister, effectiveRegisters)) {
        effectiveInstructions.set(linePointer);
        int marked = markPreviousBranchingInstructions(bytecode, linePointer, effectiveInstructions);
        if (marked == 0) {
          effectiveRegisters.clear(destinationRegister);
        }
        markInputRegistersAsEffective(bytecode, linePointer, effectiveRegisters);
      }
      linePointer--;
    }
  }

  private BitSet newEffectiveRegistersArray() {
    return new BitSet(machine.getNumberOfCalculationRegisters());
  }

  private BitSet newEffectiveInstructionsBitSet(Program program) {
    byte[] bytecode = program.getBytecode();
    int programLength = bytecode.length / bpi;
    return new BitSet(programLength);
  }

  private void markInputRegistersAsEffective(byte[] bytecode, int pointer,
      BitSet effectiveRegisters) {
    int address = address(pointer);
    Operator operator = operatorAtPointer(pointer, bytecode);
    for (int i = 0; i < operator.getInputRegisters(); i++) {
      int regIndex = ubtoi(bytecode[address + i + 1]);
      if (regIndex < machine.getNumberOfCalculationRegisters()) {
        effectiveRegisters.set(regIndex);
      }
    }
  }

  private int markPreviousBranchingInstructions(byte[] bytecode, int pointer,
      BitSet effectiveInstructions) {
    int marked = 0;
    while (true) {
      pointer--;
      if (pointer < 0 || !operatorAtPointer(pointer, bytecode).isBranching()) {
        break;
      }
      effectiveInstructions.set(pointer);
      marked++;
    }
    return marked;
  }

  private int currentDestinationRegister(int linePointer, byte[] bytecode) {
    Operator operator = operatorAtPointer(linePointer, bytecode);
    if (!operator.hasDestinationRegister()) {
      return -1;
    }
    int destRegAddress = linePointer * bpi + bpi - 1;
    return ubtoi(bytecode[destRegAddress]);
  }

  private boolean isEffectiveRegister(int destinationRegister, BitSet effectiveRegisters) {
    if (destinationRegister == -1) {
      return false;
    }
    return effectiveRegisters.get(destinationRegister);
  }

  private Operator operatorAtPointer(int pointer, byte[] bytecode) {
    return machine.getOperator(ubtoi(bytecode[address(pointer)]));
  }

  private int address(int pointer) {
    return pointer * bpi;
  }
}
