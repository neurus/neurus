package org.neurus.machine;

import static junit.framework.Assert.assertTrue;
import static org.neurus.machine.LogicOperators.ifEquals;
import static org.neurus.machine.LogicOperators.ifGreaterThan;
import static org.neurus.machine.LogicOperators.ifLessThan;
import static org.neurus.machine.MathOperators.addition;
import static org.neurus.machine.MathOperators.division;
import static org.neurus.machine.MathOperators.multiplication;
import static org.neurus.machine.MathOperators.substraction;
import static org.neurus.machine.TrigonometricOperators.sin;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.neurus.machine.CodeEffectiveness;
import org.neurus.machine.ConstantRegisters;
import org.neurus.machine.Machine;
import org.neurus.machine.MachineBuilder;
import org.neurus.machine.Program;

public class CodeEffectivenessTest {

  private Machine machine;

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
