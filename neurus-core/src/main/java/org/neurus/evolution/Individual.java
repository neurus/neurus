package org.neurus.evolution;

public class Individual {

  private byte[] bytecode;

  public Individual(byte[] bytecode) {
    this.bytecode = bytecode;
  }

  public byte[] getByteCode() {
    return bytecode;
  }
}
