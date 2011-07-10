package org.neurus.machine;

import static org.neurus.util.Primitives.ubtoi;

import java.util.Arrays;

import com.google.common.base.Preconditions;

public class InterpreterRunner implements ProgramRunner {

  private Machine machine;
  private double[] cleanCalculationRegisters;
  private double[] registers;

  // cached machine information
  private boolean[] operatorHasDestinationRegisters;
  private int[] operatorNumberOfInputs;
  private boolean[] operatorIsBranching;

  // information used during execution
  private double[] cacheOutputRegisters;
  private int pointer;
  private int operatorIndexAtPointer;
  private Operator operatorAtPointer;
  private double[] instrInputs;

  // loaded program variables
  private boolean loaded;
  private byte[] bytecode;
  private int totalInstructions;

  public InterpreterRunner(Machine machine) {
    this.machine = machine;
    this.cleanCalculationRegisters = new double[machine.getNumberOfCalculationRegisters()];
    Arrays.fill(cleanCalculationRegisters, 0d);
    cacheOutputRegisters = new double[machine.getNumberOfOutputRegisters()];
    registers = new double[machine.getNumberOfCalculationRegisters()
        + machine.getNumberOfConstantRegisters()];

    // cache operator information
    operatorNumberOfInputs = new int[machine.size()];
    operatorHasDestinationRegisters = new boolean[machine.size()];
    operatorIsBranching = new boolean[machine.size()];
    for (int x = 0; x < machine.size(); x++) {
      operatorHasDestinationRegisters[x] = machine.getOperator(x).hasDestinationRegister();
      operatorNumberOfInputs[x] = machine.getOperator(x).getInputRegisters();
      operatorIsBranching[x] = machine.getOperator(x).isBranching();
    }

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
    pointer = -1;
    nextInstruction();
    while (pointer < totalInstructions) {
      updateOperatorInfoAtPointer();
      double result = executeInstruction();
      if (operatorIsBranching[operatorIndexAtPointer] && result == Operator.FALSE) {
        advanceToNextNonBranchingInstruction();
      }
      nextInstruction();
    }
    loadCachedOutputRegisters();
    return cacheOutputRegisters;
  }

  private void nextInstruction() {
    pointer++;
    if (pointer < totalInstructions) {
      updateOperatorInfoAtPointer();
    }
  }

  private void updateOperatorInfoAtPointer() {
    this.operatorIndexAtPointer = ubtoi(bytecode[pointerAddress()]);
    this.operatorAtPointer = machine.getOperator(operatorIndexAtPointer);
  }

  private int pointerAddress() {
    return pointer * machine.getBytesPerInstruction();
  }

  private void advanceToNextNonBranchingInstruction() {
    do {
      nextInstruction();
    } while(pointer < totalInstructions && operatorIsBranching[operatorIndexAtPointer]);
  }

  private void loadCachedOutputRegisters() {
    System.arraycopy(registers, 0, cacheOutputRegisters, 0, cacheOutputRegisters.length);
  }

  private double executeInstruction() {
    int address = pointerAddress();
    for (int i = 0; i < operatorNumberOfInputs[operatorIndexAtPointer]; i++) {
      int regIndex = ubtoi(bytecode[address + i + 1]);
      instrInputs[i] = registers[regIndex];
    }
    double result = operatorAtPointer.execute(instrInputs);
    if (operatorHasDestinationRegisters[operatorIndexAtPointer]) {
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
