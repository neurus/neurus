package org.neurus.instruction;

import static junit.framework.Assert.assertTrue;
import static org.neurus.instruction.LogicInstructions.ifEquals;
import static org.neurus.instruction.LogicInstructions.ifGreaterThan;
import static org.neurus.instruction.LogicInstructions.ifLessThan;
import static org.neurus.instruction.MathInstructions.addition;
import static org.neurus.instruction.MathInstructions.division;
import static org.neurus.instruction.MathInstructions.multiplication;
import static org.neurus.instruction.MathInstructions.substraction;
import static org.neurus.instruction.TrigonometricInstructions.sin;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class CodeEffectivenessTest {

  private Machine machine;

  @Before
  public void setUp() {
    machine = new MachineBuilder()
        .withCalculationRegisters(10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withInstruction(addition())
        .withInstruction(substraction())
        .withInstruction(multiplication())
        .withInstruction(division())
        .withInstruction(ifEquals())
        .withInstruction(ifGreaterThan())
        .withInstruction(ifLessThan())
        .withInstruction(sin())
        .withOutputRegisters(1)
        .build();
  }

  @Test
  public void testCodeEffectiveness() {
    byte[] bytecode = new byte[] {
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
    Program program = new Program(bytecode);
    CodeEffectiveness codeEffectiveness = new CodeEffectiveness(machine);
    boolean[] effectiveInstructions = codeEffectiveness.evaluate(program);
    boolean[] expected = new boolean[] {
        true, false, true, true, true, false, true,
        true, false, false, true, false, true, true };
    assertTrue(Arrays.equals(expected, effectiveInstructions));
  }
}
