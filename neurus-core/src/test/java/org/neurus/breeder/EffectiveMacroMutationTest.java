package org.neurus.breeder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.neurus.machine.TestPrograms.EXAMPLE_BYTECODE_1;
import static org.neurus.machine.TestPrograms.EXAMPLE_BYTECODE_1_EFFECTIVENESS;
import static org.neurus.machine.TestPrograms.EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_10;
import static org.neurus.machine.TestPrograms.EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_11;

import java.util.Arrays;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.neurus.evolution.Individual;
import org.neurus.machine.BytecodeWriter;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.InstructionData;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.machine.Program;
import org.neurus.machine.TestMachines;
import org.neurus.machine.TestPrograms;
import org.neurus.rng.RandomNumberGenerator;
import org.neurus.util.TestBitSetUtils;

public class EffectiveMacroMutationTest {

  private static final BitSet NO_EFF_INSTRUCTIONS = TestBitSetUtils.valueOf("0000000000000");

  private InstructionRandomizer instructionRandomizer = mock(InstructionRandomizer.class);
  private Machine machine = TestMachines.calculatorAndLogic();
  private BytecodeWriter bytecodeWriter = new BytecodeWriter(machine);
  private RandomNumberGenerator rng = mock(RandomNumberGenerator.class);
  private EffectivenessAnalyzer effectivenessAnalyzer = mock(EffectivenessAnalyzer.class);
  private Individual parentIndividual;
  private EffectiveMacroMutation mutation;

  @Before
  public void setUp() {
    mutation = new EffectiveMacroMutation(machine, rng, 0.5,
        10, 15, bytecodeWriter, instructionRandomizer, effectivenessAnalyzer);

    Program parentProgram = new Program(EXAMPLE_BYTECODE_1);
    parentProgram.setEffectiveInstructions(EXAMPLE_BYTECODE_1_EFFECTIVENESS);
    parentIndividual = new Individual(parentProgram);

    doAnswer(effectiveInstructions(EXAMPLE_BYTECODE_1_EFFECTIVENESS))
        .when(effectivenessAnalyzer).analyzeProgram(parentIndividual.getProgram());
  }

  @Test
  public void testNoIndividualIsGeneratedIfNoEffectiveInstructions() {
    doAnswer(effectiveInstructions(NO_EFF_INSTRUCTIONS))
        .when(effectivenessAnalyzer).analyzeProgram(parentIndividual.getProgram());
    assertEquals(0, mutation.breed(new Individual[] { parentIndividual }).length);
  }

  @Test
  public void testInstructionDeletion() {
    when(rng.nextDouble()).thenReturn(0.51d); // do deletion
    when(rng.nextInt(9)).thenReturn(3); // remove the fourth effective line

    Individual[] offsprings = mutation.breed(new Individual[] { parentIndividual });

    byte[] expected = new byte[] {
        0, 5, 10, 0, // r[0] = r[5] + 71;
        1, 0, 11, 7, // ** r[7] = r[0] - 59;
        5, 1, 12, 0, // if (r[1] > 0)
        5, 5, 13, 0, // if (r[5] > 2)
        // deleted 2, 2, 1, 4, // r[4] = r[2] * r[1];
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
    assertTrue(Arrays.equals(expected, offsprings[0].getProgram().getBytecode()));
  }

  @Test
  public void testInstructionInsertion() {
    when(rng.nextDouble()).thenReturn(0.49d); // do insertion
    when(rng.nextInt(15)).thenReturn(11); // insert between 11th and 12th instr
    when(
        effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(parentIndividual.getProgram(),
            10)).thenReturn(EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_10);
    doAnswer(allOnesInstruction()).when(instructionRandomizer)
        .fillRandomInstruction(Mockito.any(InstructionData.class),
            Mockito.eq(EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_10));
    Individual[] offsprings = mutation.breed(new Individual[] { parentIndividual });

    byte[] expected = new byte[] {
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
        1, 1, 1, 1, // new instruction
        0, 7, 17, 5, // ** r[5] = r[7] + 15;
        6, 1, 6, 0, // if (r[1] < r[6])
        7, 7, 0, 0, // r[0] = sin(r[7]);
    };
    assertTrue(Arrays.equals(expected, offsprings[0].getProgram().getBytecode()));
  }

  @Test
  public void testInstructionInsertionOfBranchOperator() {
    when(rng.nextDouble()).thenReturn(0.49d); // do insertion
    when(rng.nextInt(15)).thenReturn(11); // insert between 11th and 12th instr
    when(
        effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(parentIndividual.getProgram(),
            10)).thenReturn(EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_10);
    when(
        effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(parentIndividual.getProgram(),
            11)).thenReturn(EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_11);
    doAnswer(branchingInstruction()).when(instructionRandomizer)
        .fillRandomInstruction(Mockito.any(InstructionData.class),
            Mockito.eq(EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_10));
    when(rng.nextInt(4)).thenReturn(3); // pick the last eff register for the instr following the
                                        // new branch instruction 
    Individual[] offsprings = mutation.breed(new Individual[] { parentIndividual });

    byte[] expected = new byte[] {
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
        5, 1, 1, 1, // new instruction
        0, 7, 17, 7, // dest reg changed to 7 r[5] = r[7] + 15;
        6, 1, 6, 0, // if (r[1] < r[6])
        7, 7, 0, 0, // r[0] = sin(r[7]);
    };
    assertTrue(Arrays.equals(expected, offsprings[0].getProgram().getBytecode()));
  }

  @Test
  public void testNoIndividualIsGeneratedWhenNoEffectiveRegisters() {
    when(rng.nextDouble()).thenReturn(0.49d); // do insertion
    when(rng.nextInt(15)).thenReturn(11); // insert between 11th and 12th instr
    when(
        effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(parentIndividual.getProgram(),
            10)).thenReturn(TestBitSetUtils.valueOf("0000000000"));
    assertEquals(0, mutation.breed(new Individual[] { parentIndividual }).length);
  }

  private Answer<Object> allOnesInstruction() {
    return new Answer<Object>() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        InstructionData instrData = (InstructionData) invocation.getArguments()[0];
        instrData.destinationRegister = 1;
        instrData.operatorIndex = 1;
        Arrays.fill(instrData.inputRegisters, 1);
        return null;
      }
    };
  }

  private Answer<Object> branchingInstruction() {
    return new Answer<Object>() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        InstructionData instrData = (InstructionData) invocation.getArguments()[0];
        instrData.destinationRegister = 1;
        instrData.operatorIndex = 5;
        Arrays.fill(instrData.inputRegisters, 1);
        return null;
      }
    };
  }

  private Answer<Object> effectiveInstructions(final BitSet bitset) {
    return new Answer<Object>() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        Program program = (Program) invocation.getArguments()[0];
        program.setEffectiveInstructions(bitset);
        return null;
      }
    };
  }

}
