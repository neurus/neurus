package org.neurus.machine;

public class BytecodeWriter {

  private Machine machine;
  private int bpi;
  private int pointer;
  private byte[] bytecode;

  public BytecodeWriter(Machine machine) {
    this.machine = machine;
    this.bpi = machine.getBytesPerInstruction();
  }

  public void initProgram(int length) {
    bytecode = new byte[length * bpi];
    pointer = 0;
  }

  public void writeInstruction(InstructionData instructionData) {
    bytecode[pointer++] = (byte) instructionData.operatorIndex;
    for (int i = 0; i < instructionData.inputRegisters.length; i++) {
      bytecode[pointer++] = (byte) instructionData.inputRegisters[i];
    }
    if (machine.hasDestinationRegister()) {
      bytecode[pointer++] = (byte) instructionData.destinationRegister;
    }
  }

  public void copyInstructions(byte[] original, int fromInstruction, int numberOfInstructions) {
    int bytesToCopy = numberOfInstructions * bpi;
    System.arraycopy(original, fromInstruction * bpi, bytecode, pointer, bytesToCopy);
    pointer += bytesToCopy;
  }

  public byte[] getBytecode() {
    return bytecode;
  }
}
