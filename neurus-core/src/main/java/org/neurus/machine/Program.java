package org.neurus.machine;

public class Program {

  private byte[] bytecode;

  public Program(byte[] bytecode) {
    super();
    this.bytecode = bytecode;
  }

  public byte[] getBytecode() {
    return bytecode;
  }
}
