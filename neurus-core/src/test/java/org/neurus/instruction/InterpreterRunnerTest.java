package org.neurus.instruction;

import static junit.framework.Assert.assertEquals;
import static org.neurus.instruction.LogicInstructions.ifGreaterThan;
import static org.neurus.instruction.MathInstructions.addition;
import static org.neurus.instruction.MathInstructions.division;
import static org.neurus.instruction.MathInstructions.multiplication;
import static org.neurus.instruction.MathInstructions.substraction;

import org.junit.Before;
import org.junit.Test;

public class InterpreterRunnerTest {

  private Machine calculatorMachine;
  private Machine logicMachine;

  @Before
  public void setUp() {
    calculatorMachine = new MachineBuilder()
        .withCalculationRegisters(6)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withInstruction(addition())
        .withInstruction(substraction())
        .withInstruction(multiplication())
        .withInstruction(division())
        .withOutputRegisters(1)
        .build();
    logicMachine = new MachineBuilder()
        .withCalculationRegisters(6)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withInstruction(addition())
        .withInstruction(ifGreaterThan())
        .withOutputRegisters(1)
        .build();
  }

  @Test
  public void testExecutionOfTwoTimesThreePlusEightIsFourteen() {
    double[] inputs = new double[] { 2, 3, 8 };
    byte[] bytecode = new byte[] {
        2, 0, 1, 3, // r3 = r0 * r1
        0, 3, 2, 0, // r0 = r3 + r2
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(calculatorMachine);
    runner.load(program);
    double[] result = runner.run(inputs);
    assertEquals(14d, result[0]);
    // if I execute the same program again, it should still give the same result
    result = runner.run(inputs);
    assertEquals(14d, result[0]);
  }

  @Test
  public void testExecutionWithConstants() {
    double[] inputs = new double[] { 2, 3, 8 };
    byte[] bytecode = new byte[] {
        2, 0, 9, 3, // r3 = r0 * r9 --> r9 is c4=3
        0, 3, 8, 0, // r0 = r3 + r8 --> r8 is c3=2
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(calculatorMachine);
    runner.load(program);
    double[] result = runner.run(inputs);
    assertEquals(8d, result[0]);
    // if I execute the same program again, it should still give the same result
    result = runner.run(inputs);
    assertEquals(8d, result[0]);
  }

  @Test
  public void testExecutionOfBranches() {
    byte[] bytecode = new byte[] {
        1, 0, 9, 0, // if r0 > 3 (r9 is c4=3)
        0, 0, 8, 0, // r0 = r0 + 2 --> (r8 is c3=2)
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(logicMachine);
    runner.load(program);
    // execute with r0 = 1 --> result should be r0
    double[] result = runner.run(new double[] { 1 });
    assertEquals(1d, result[0]);
    // execute with r0 = 4 --> result should be r0 + 2
    double[] result2 = runner.run(new double[] { 4 });
    assertEquals(6d, result2[0]);
  }

  @Test
  public void testExecutionOfChainedBranches() {
    byte[] bytecode = new byte[] {
        1, 0, 9, 0, // if r0 > 3 (r9 is c4=3)
        1, 0, 9, 0, // if r0 > 1 (r9 is c4=3)
        0, 0, 8, 0, // r0 = r0 + 2 --> (r8 is c3=2)
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(logicMachine);
    runner.load(program);
    // execute with r0 = 2 --> last execution should be skipped because first branch is false,
    // doesn't matter that the second is true
    double[] result = runner.run(new double[] { 2 });
    assertEquals(2d, result[0]);
    // execute with r0 = 3 --> last execution should be skipped because second branch is false,
    double[] result2 = runner.run(new double[] { 2 });
    assertEquals(2d, result2[0]);
    // execute with r0 = 4 --> result should be r0 + 2
    // because this time both branches are true
    double[] result3 = runner.run(new double[] { 4 });
    assertEquals(6d, result3[0]);
  }

  @Test
  public void testExecutionOfOnlyBranchesProgram() {
    byte[] bytecode = new byte[] {
        1, 0, 9, 0, // if r0 > 3 (r9 is c4=3)
        1, 0, 9, 0, // if r0 > 1 (r9 is c4=3)
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(logicMachine);
    runner.load(program);
    // execute, all branches are false, output should be equals to the input
    double[] result = runner.run(new double[] { 2 });
    assertEquals(2d, result[0]);
    // execute all branches are true, output should be equals to the input 
    double[] result2 = runner.run(new double[] { 5 });
    assertEquals(5d, result2[0]);
  }
}
