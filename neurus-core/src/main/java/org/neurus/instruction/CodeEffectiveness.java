package org.neurus.instruction;

import static org.neurus.util.Primitives.ubtoi;

// TODO Pull some common functions that are repeated here and in InterpreterRunner
public class CodeEffectiveness {

  private final int bpi;
  private final Machine machine;

  public CodeEffectiveness(Machine machine) {
    bpi = machine.getBytesPerInstruction();
    this.machine = machine;
  }

  public boolean[] evaluate(Program program) {
    byte[] bytecode = program.getBytecode();
    int programLength = bytecode.length / bpi;
    boolean[] effectiveInstructions = new boolean[programLength];

    // at the beginning, only output registers are effective
    boolean[] effectiveRegisters = new boolean[machine.getNumberOfCalculationRegisters()];
    for (int x = 0; x < machine.getNumberOfOutputRegisters(); x++) {
      effectiveRegisters[x] = true;
    }

    // start at the last instruction and go backwards
    int linePointer = programLength - 1;
    while (linePointer >= 0) {
      // if this is already marked, it means its a branching instruction and we just need to mark
      // the input registers as effective
      if (effectiveInstructions[linePointer]) {
        markInputRegistersAsEffective(bytecode, linePointer, effectiveRegisters);
        linePointer--;
        continue;
      }
      // find instruction with destination register in Reff
      int destinationRegister = currentDestinationRegister(linePointer, bytecode);
      if (isEffectiveRegister(destinationRegister, effectiveRegisters)) {
        effectiveInstructions[linePointer] = true;
        int marked = markPreviousBranchingInstructions(bytecode, linePointer, effectiveInstructions);
        if (marked == 0) {
          effectiveRegisters[destinationRegister] = false;
        }
        markInputRegistersAsEffective(bytecode, linePointer, effectiveRegisters);
      }
      linePointer--;
    }
    return effectiveInstructions;
  }

  private void markInputRegistersAsEffective(byte[] bytecode, int pointer,
      boolean[] effectiveRegisters) {
    int address = address(pointer);
    Instruction instr = instructionAtPointer(pointer, bytecode);
    for (int i = 0; i < instr.getInputRegisters(); i++) {
      int regIndex = ubtoi(bytecode[address + i + 1]);
      if (regIndex < machine.getNumberOfCalculationRegisters()) {
        effectiveRegisters[regIndex] = true;
      }
    }
  }

  private int markPreviousBranchingInstructions(byte[] bytecode, int pointer,
      boolean[] effectiveInstructions) {
    int marked = 0;
    while (true) {
      pointer--;
      if (pointer < 0 || !instructionAtPointer(pointer, bytecode).isBranching()) {
        break;
      }
      effectiveInstructions[pointer] = true;
      marked++;
    }
    return marked;
  }

  private int currentDestinationRegister(int linePointer, byte[] bytecode) {
    Instruction instr = instructionAtPointer(linePointer, bytecode);
    if (!instr.hasDestinationRegister()) {
      return -1;
    }
    int destRegAddress = linePointer * bpi + bpi - 1;
    return ubtoi(bytecode[destRegAddress]);
  }

  private boolean isEffectiveRegister(int destinationRegister, boolean[] effectiveRegisters) {
    if (destinationRegister == -1) {
      return false;
    }
    return effectiveRegisters[destinationRegister];
  }

  private Instruction instructionAtPointer(int pointer, byte[] bytecode) {
    return machine.getInstruction(ubtoi(bytecode[address(pointer)]));
  }

  private int address(int pointer) {
    return pointer * bpi;
  }
}
