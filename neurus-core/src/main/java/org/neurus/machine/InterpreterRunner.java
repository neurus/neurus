package org.neurus.machine;

import static org.neurus.util.Primitives.ubtoi;

import java.util.Arrays;

import com.google.common.base.Preconditions;

public class InterpreterRunner implements ProgramRunner {

  private Machine machine;
  private double[] cleanCalculationRegisters;
  private double[] registers;
  private double[] cacheOutputRegisters;

  // loaded program variables
  private boolean loaded;
  private byte[] bytecode;
  private double[] instrInputs;
  private int totalInstructions;
  private int pointer;

  public InterpreterRunner(Machine machine) {
    this.machine = machine;
    this.cleanCalculationRegisters = new double[machine.getNumberOfCalculationRegisters()];
    Arrays.fill(cleanCalculationRegisters, 0d);
    cacheOutputRegisters = new double[machine.getNumberOfOutputRegisters()];
    registers = new double[machine.getNumberOfCalculationRegisters()
        + machine.getNumberOfConstantRegisters()];

    // copy constant values at the end of the registers array
    double[] constantValues = machine.getConstantRegisters().getValues();
    System.arraycopy(constantValues, 0, registers, machine.getNumberOfCalculationRegisters(),
        constantValues.length);
  }

  public void load(Program program) {
    bytecode = program.getBytecode();
    instrInputs = new double[machine.getMaxInputsForASingleInstruction()];
    totalInstructions = bytecode.length / machine.getBytesPerInstruction();
    loaded = true;
  }

  @Override
  public double[] run(double[] inputs) {
    Preconditions.checkState(loaded, "You should load a program before calling run()");
    Preconditions.checkState(inputs.length <= machine.getNumberOfCalculationRegisters(),
        "Not enough calculation registers for that many inputs");
    setInputsAndCleanCalculationRegisters(inputs);
    pointer = 0;
    while (pointer < totalInstructions) {
      Operator operator = operatorAtPointer();
      double result = executeInstruction(operator);
      if (operator.isBranching() && result == Operator.FALSE) {
        skipNextNonBranchingInstruction();
      }
      pointer++;
    }
    loadCachedOutputRegisters();
    return cacheOutputRegisters;
  }

  private int pointerAddress() {
    return pointer * machine.getBytesPerInstruction();
  }

  private void skipNextNonBranchingInstruction() {
    do {
      pointer++;
    } while (pointer < totalInstructions && operatorAtPointer().isBranching());
  }

  private Operator operatorAtPointer() {
    return machine.getOperator(ubtoi(bytecode[pointerAddress()]));
  }

  private void loadCachedOutputRegisters() {
    System.arraycopy(registers, 0, cacheOutputRegisters, 0, cacheOutputRegisters.length);
  }

  private double executeInstruction(Operator operator) {
    int address = pointerAddress();
    for (int i = 0; i < operator.getInputRegisters(); i++) {
      int regIndex = ubtoi(bytecode[address + i + 1]);
      instrInputs[i] = registers[regIndex];
    }
    double result = operator.execute(instrInputs);
    if (operator.hasDestinationRegister()) {
      int regIndex = ubtoi(bytecode[address + machine.getBytesPerInstruction() - 1]);
      registers[regIndex] = result;
    }
    return result;
  }

  private void setInputsAndCleanCalculationRegisters(double[] inputs) {
    // copy inputs over calculation registers
    System.arraycopy(inputs, 0, registers, 0, inputs.length);
    // copy clean registers to the rest of calculation registers
    System.arraycopy(cleanCalculationRegisters, inputs.length, registers, inputs.length,
        cleanCalculationRegisters.length - inputs.length);
  }
}
