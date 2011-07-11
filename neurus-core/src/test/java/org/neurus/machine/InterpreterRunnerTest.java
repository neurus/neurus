package org.neurus.machine;

import static junit.framework.Assert.assertEquals;
import static org.neurus.machine.LogicOperators.ifGreaterThan;
import static org.neurus.machine.MathOperators.addition;
import static org.neurus.machine.MathOperators.division;
import static org.neurus.machine.MathOperators.multiplication;
import static org.neurus.machine.MathOperators.substraction;

import org.junit.Before;
import org.junit.Test;
import org.neurus.machine.ConstantRegisters;
import org.neurus.machine.InterpreterRunner;
import org.neurus.machine.Machine;
import org.neurus.machine.MachineBuilder;
import org.neurus.machine.Program;
import org.neurus.util.TestBitSetUtils;

public class InterpreterRunnerTest {

  private Machine calculatorMachine;
  private Machine logicMachine;

  @Before
  public void setUp() {
    calculatorMachine = new MachineBuilder()
        .withCalculationRegisters(6)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withOperator(addition())
        .withOperator(substraction())
        .withOperator(multiplication())
        .withOperator(division())
        .withOutputRegisters(1)
        .build();
    logicMachine = new MachineBuilder()
        .withCalculationRegisters(6)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withOperator(addition())
        .withOperator(ifGreaterThan())
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
    runner.load(program, false);
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
    runner.load(program, false);
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
    runner.load(program, false);
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
        0, 0, 9, 0, // r0 = r0 + 3 --> (r9 is c3=3)
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(logicMachine);
    runner.load(program, false);
    // execute with r0 = 2 --> +2 should be skipped because first branch is false,
    double[] result = runner.run(new double[] { 2 });
    assertEquals(5d, result[0]);
    // execute with r0 = 3 --> +2 should be skipped because second branch is false,
    double[] result2 = runner.run(new double[] { 3 });
    assertEquals(6d, result2[0]);
    // execute with r0 = 4 --> result should be r0 + 2 + 3
    // because this time both branches are true
    double[] result3 = runner.run(new double[] { 4 });
    assertEquals(9d, result3[0]);
  }

  @Test
  public void testExecutionOfOnlyBranchesProgram() {
    byte[] bytecode = new byte[] {
        1, 0, 9, 0, // if r0 > 3 (r9 is c4=3)
        1, 0, 9, 0, // if r0 > 1 (r9 is c4=3)
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(logicMachine);
    runner.load(program, false);
    // execute, all branches are false, output should be equals to the input
    double[] result = runner.run(new double[] { 2 });
    assertEquals(2d, result[0]);
    // execute all branches are true, output should be equals to the input 
    double[] result2 = runner.run(new double[] { 5 });
    assertEquals(5d, result2[0]);
  }

  @Test
  public void testExecutionOfEffectiveInstructions() {
    double[] inputs = new double[] { 2, 3, 8 };
    byte[] bytecode = new byte[] {
        2, 0, 9, 2, // r2 = r0 * r9  (r9=3)
        0, 2, 9, 0, // r0 = r2 + r9  (r9=3)
    };
    Program program = new Program(bytecode);
    program.setEffectiveInstructions(TestBitSetUtils.valueOf("01"));
    InterpreterRunner runner = new InterpreterRunner(calculatorMachine);
    
    // run with innefective code, both instructions are executed so r0 should be 9
    runner.load(program, false);
    double[] result = runner.run(inputs);
    assertEquals(9d, result[0]);
    
    // run with eff code, only second instruction is executed so r0 should be 11
    runner.load(program, true);
    double[] result2 = runner.run(inputs);
    assertEquals(11d, result2[0]);
  }

  @Test
  public void testBranchesOnEffectiveProgram() {
    byte[] bytecode = new byte[] {
        1, 0, 9, 0, // (eff) if r0 > 3 (r9 is c4=3)
        0, 2, 9, 0, // (eff) r0 = r2 + r9  (r9=3)
        0, 2, 9, 0, // (noneff) r0 = r2 + r9  (r9=3)
        0, 2, 9, 0, // (eff)r0 = r0 + r7  (r7=1)
    };
    Program program = new Program(bytecode);
    InterpreterRunner runner = new InterpreterRunner(logicMachine);
    program.setEffectiveInstructions(TestBitSetUtils.valueOf("1101"));
    runner.load(program, true);
    // execute, branch is false so last instr is executed
    double[] result = runner.run(new double[] { 2 });
    assertEquals(3d, result[0]);
  }
}
