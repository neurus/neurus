package org.neurus.machine;

import java.util.BitSet;

public class Program {

  private byte[] bytecode;
  private BitSet effectiveInstructions;

  public Program(byte[] bytecode) {
    super();
    this.bytecode = bytecode;
  }

  public byte[] getBytecode() {
    return bytecode;
  }

  public void setEffectiveInstructions(BitSet effectiveInstructions) {
    this.effectiveInstructions = effectiveInstructions;
  }

  public BitSet getEffectiveInstructions() {
    return effectiveInstructions;
  }
}
