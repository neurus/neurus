package org.neurus.instruction;

import static org.neurus.util.Primitives.ubtoi;

import java.util.Arrays;

public class InterpreterRunner implements ProgramRunner {

  private Machine machine;
  private double[] cleanCalculationRegisters;
  private double[] registers;
  private double[] cacheOutputRegisters;

  public InterpreterRunner(Machine machine) {
    this.machine = machine;
    this.cleanCalculationRegisters = new double[machine.getNumberOfCalculationRegisters()];
    Arrays.fill(cleanCalculationRegisters, 0d);
    cacheOutputRegisters = new double[machine.getNumberOfCalculationRegisters()];
    registers = new double[machine.getNumberOfCalculationRegisters()
        + machine.getNumberOfConstantRegisters()];

    // copy constant values at the end of the registers array
    double[] constantValues = machine.getConstantRegisters().getValues();
    System.arraycopy(constantValues, 0, registers, machine.getNumberOfCalculationRegisters(),
        constantValues.length);
  }

  @Override
  public double[] run(Program program, double[] inputs) {
    byte[] bytecode = program.getBytecode();
    setInputsAndCleanCalculationRegisters(inputs);
    double[] instrInputs = new double[machine.getMaxInputsForASingleInstruction()];
    int totalInstructions = bytecode.length / machine.getBytesPerInstruction();
    int programLine = 0;
    while (programLine < totalInstructions) {
      int address = programLine * machine.getBytesPerInstruction();
      Instruction instr = machine.getInstruction(ubtoi(bytecode[address]));
      for (int i = 0; i < instr.getInputRegisters(); i++) {
        int regIndex = ubtoi(bytecode[address + i + 1]);
        instrInputs[i] = registers[regIndex];
      }
      double output = instr.execute(instrInputs);
      if (instr.hasOutputRegister()) {
        int regIndex = ubtoi(bytecode[address + machine.getBytesPerInstruction() - 1]);
        registers[regIndex] = output;
      }
      programLine++;
    }
    System.arraycopy(registers, 0, cacheOutputRegisters, 0, cacheOutputRegisters.length);
    return cacheOutputRegisters;
  }

  private void setInputsAndCleanCalculationRegisters(double[] inputs) {
    // copy inputs over calculation registers
    System.arraycopy(inputs, 0, registers, 0, inputs.length);
    // copy clean registers to the rest of calculation registers
    System.arraycopy(cleanCalculationRegisters, inputs.length, registers, inputs.length,
        cleanCalculationRegisters.length - inputs.length);
  }
}
