package org.neurus.machine;

import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.neurus.machine.LogicOperators.ifEquals;
import static org.neurus.machine.LogicOperators.ifGreaterThan;
import static org.neurus.machine.LogicOperators.ifLessThan;
import static org.neurus.machine.MathOperators.addition;
import static org.neurus.machine.MathOperators.division;
import static org.neurus.machine.MathOperators.multiplication;
import static org.neurus.machine.MathOperators.substraction;
import static org.neurus.machine.TrigonometricOperators.sin;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;
import org.neurus.util.TestBitSetUtils;

public class EffectivenessAnalyzerTest {

  private Machine machine;

  private byte[] exampleProgramByteCode = new byte[] {
      0, 5, 10, 0, // r[0] = r[5] + 71;
      1, 0, 11, 7, // ** r[7] = r[0] - 59;
      5, 1, 12, 0, // if (r[1] > 0)
      5, 5, 13, 0, // if (r[5] > 2)
      2, 2, 1, 4, // r[4] = r[2] * r[1];
      0, 5, 4, 2, // ** r[2] = r[5] + r[4];
      2, 4, 14, 6, // r[6] = r[4] * 13;
      3, 3, 15, 1, // r[1] = r[3] / 2;
      5, 0, 1, 0, // ** if (r[0] > r[1])
      2, 5, 5, 3, // ** r[3] = r[5] * r[5];
      1, 6, 16, 7, // r[7] = r[6] - 2;
      0, 7, 17, 5, // ** r[5] = r[7] + 15;
      6, 1, 6, 0, // if (r[1] < r[6])
      7, 7, 0, 0, // r[0] = sin(r[7]);
  };

  private Program program;
  private EffectivenessAnalyzer analyzer;

  @Before
  public void setUp() {
    machine = new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withOperator(addition())
        .withOperator(substraction())
        .withOperator(multiplication())
        .withOperator(division())
        .withOperator(ifEquals())
        .withOperator(ifGreaterThan())
        .withOperator(ifLessThan())
        .withOperator(sin())
        .withOutputRegisters(1)
        .build();
    program = new Program(exampleProgramByteCode);
    analyzer = new EffectivenessAnalyzer(machine);
  }

  @Test
  public void testAnalyzeProgram() {
    analyzer.analyzeProgram(program);
    BitSet expected = TestBitSetUtils.valueOf("10111011001011");
    assertEquals(expected, program.getEffectiveInstructions());
  }

  @Test
  public void testSecondAnalysisIsANoOp() {
    analyzer.analyzeProgram(program);
    // if we pass the same program again, it should not be evaluated, this should be a no op
    BitSet previousEffectiveness = program.getEffectiveInstructions();
    analyzer.analyzeProgram(program);
    assertSame(previousEffectiveness, program.getEffectiveInstructions());
  }

  @Test
  public void testAnalyzeEffectiveRegistersAtInstruction() {
    // at the last instruction, only the output, r0 should be effective
    BitSet expected = TestBitSetUtils.valueOf("1000000000");
    BitSet effectiveRegisters = analyzer.analyzeEffectiveRegistersAtInstruction(program, 13);
    assertEquals(expected, effectiveRegisters);

    // at the previous, r0 and r7 should be effective
    effectiveRegisters = analyzer.analyzeEffectiveRegistersAtInstruction(program, 12);
    expected = TestBitSetUtils.valueOf("1000000100");
    assertEquals(expected, effectiveRegisters);

    // previous, r0, r1, r6 and r7 should be effective
    effectiveRegisters = analyzer.analyzeEffectiveRegistersAtInstruction(program, 11);
    expected = TestBitSetUtils.valueOf("1100001100");
    assertEquals(expected, effectiveRegisters);

    // previous, should stay the same
    effectiveRegisters = analyzer.analyzeEffectiveRegistersAtInstruction(program, 10);
    expected = TestBitSetUtils.valueOf("1100001100");
    assertEquals(expected, effectiveRegisters);
  }
}
