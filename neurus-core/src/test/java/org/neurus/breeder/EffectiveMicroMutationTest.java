package org.neurus.breeder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.neurus.machine.TestPrograms.EXAMPLE_BYTECODE_1;
import static org.neurus.machine.TestPrograms.EXAMPLE_BYTECODE_1_EFFECTIVENESS;
import static org.neurus.util.TestBitSetUtils.valueOf;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.neurus.evolution.Individual;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.machine.Program;
import org.neurus.machine.TestMachines;
import org.neurus.machine.TestPrograms;
import org.neurus.rng.RandomNumberGenerator;

public class EffectiveMicroMutationTest {

  private static final double pRegisterMut = 0.2;
  private static final double pOperatorMut = 0.7d;
  private static final double pConstantMut = 0.1d;
  private Individual parentIndividual;
  private EffectiveMicroMutation mutator;
  private Machine machine = TestMachines.calculator();
  private InstructionRandomizer instrRandomizer = mock(InstructionRandomizer.class);
  private RandomNumberGenerator rng = mock(RandomNumberGenerator.class);
  private EffectivenessAnalyzer effectivenessAnalyzer = mock(EffectivenessAnalyzer.class);

  @Before
  public void setUp() {
    Program parentProgram = new Program(EXAMPLE_BYTECODE_1);
    parentProgram.setEffectiveInstructions(EXAMPLE_BYTECODE_1_EFFECTIVENESS);
    parentIndividual = new Individual(parentProgram);
    mutator = new EffectiveMicroMutation(machine, instrRandomizer, rng, effectivenessAnalyzer,
        pRegisterMut, pOperatorMut, pConstantMut);
    when(effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(
        parentIndividual.getProgram(), 10))
        .thenReturn(TestPrograms.EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_10);
  }

  @Test
  public void testEffectivenessIsAnalyzed() {
    mutator.breed(new Individual[] { parentIndividual });
    verify(effectivenessAnalyzer).analyzeProgram(parentIndividual.getProgram());
  }

  @Test
  public void testBreederReturnsNullIfThereIsNoEffectiveProgram() {
    Program nonEffectiveProgram = new Program(new byte[] { 1, 1, 1, 1 });
    nonEffectiveProgram.setEffectiveInstructions(valueOf("0"));
    parentIndividual = new Individual(nonEffectiveProgram);
    assertBreedingProducesNoIndividuals();
  }

  @Test
  public void testBreederQuantities() {
    assertEquals(1, mutator.getMaxNumberOfOffsprings());
    assertEquals(1, mutator.getNumberOfParents());
  }

  @Test
  public void testDestinationRegisterMutation() {
    // chose register mutation type
    when(rng.nextDouble()).thenReturn(0.19);
    // chose the 7th effective instruction, { 1, 6, 16, 7 } // r[7] = r[6] - 2;
    when(rng.nextInt(EXAMPLE_BYTECODE_1_EFFECTIVENESS.cardinality())).thenReturn(6);
    // chose destination register mutation
    when(rng.nextInt(3)).thenReturn(2);
    // choose the 3rd eff dest register r6
    when(rng.nextInt(4)).thenReturn(2);

    Individual offspring = mutator.breed(new Individual[] { parentIndividual })[0];
    byte[] expected = EXAMPLE_BYTECODE_1.clone();
    expected[43] = 6;
    assertTrue(Arrays.equals(expected, offspring.getProgram().getBytecode()));
  }

  @Test
  public void testDestinationRegisterMutationReturnsNullToAvoidSameIndividual() {
    // chose register mutation type
    when(rng.nextDouble()).thenReturn(0.19);
    // chose the 7th effective instruction, { 1, 6, 16, 7 } // r[7] = r[6] - 2;
    when(rng.nextInt(EXAMPLE_BYTECODE_1_EFFECTIVENESS.cardinality())).thenReturn(6);
    // chose destination register mutation
    when(rng.nextInt(3)).thenReturn(2);
    // chose the same register, the fourth effective, r7
    when(rng.nextInt(4)).thenReturn(3);

    assertBreedingProducesNoIndividuals();
  }

  @Test
  public void testRegisterMutationOnFirstInputRegister() {
    // chose register mutation type
    when(rng.nextDouble()).thenReturn(0.19);
    // chose the 7th effective instruction, { 1, 6, 16, 7 } // r[7] = r[6] - 2;
    when(rng.nextInt(EXAMPLE_BYTECODE_1_EFFECTIVENESS.cardinality())).thenReturn(6);
    // chose the first calculation register for mutation
    when(rng.nextInt(3)).thenReturn(0);
    // now instr randomizer should be called for a calc register
    when(instrRandomizer.randomCalculationRegister()).thenReturn(3);

    Individual offspring = mutator.breed(new Individual[] { parentIndividual })[0];
    byte[] expected = EXAMPLE_BYTECODE_1.clone();
    expected[41] = 3;
    assertTrue(Arrays.equals(expected, offspring.getProgram().getBytecode()));
  }

  @Test
  public void testRegisterMutationOnSecondInputRegister() {
    // chose register mutation type
    when(rng.nextDouble()).thenReturn(0.19);
    // chose the 7th effective instruction, { 1, 6, 16, 7 } // r[7] = r[6] - 2;
    when(rng.nextInt(EXAMPLE_BYTECODE_1_EFFECTIVENESS.cardinality())).thenReturn(6);
    // chose the second calculation register for mutation
    when(rng.nextInt(3)).thenReturn(1);
    // now instr randomizer should be called for any type of register
    when(instrRandomizer.randomRegister()).thenReturn(9);

    Individual offspring = mutator.breed(new Individual[] { parentIndividual })[0];
    byte[] expected = EXAMPLE_BYTECODE_1.clone();
    expected[42] = 9;
    assertTrue(Arrays.equals(expected, offspring.getProgram().getBytecode()));
  }

  @Test
  public void testRegisterMutationReturnsNullToAvoidSameIndividual() {
    // chose register mutation type
    when(rng.nextDouble()).thenReturn(0.19);
    // chose the 7th effective instruction, { 1, 6, 16, 7 } // r[7] = r[6] - 2;
    when(rng.nextInt(EXAMPLE_BYTECODE_1_EFFECTIVENESS.cardinality())).thenReturn(6);
    // chose the second calculation register for mutation
    when(rng.nextInt(3)).thenReturn(1);
    // now instr randomizer should be called for any type of register
    when(instrRandomizer.randomRegister()).thenReturn(16);

    assertBreedingProducesNoIndividuals();
  }

  @Test
  public void testOperatorMutation() {
    // chose operator mutation type
    when(rng.nextDouble()).thenReturn(0.69);
    // chose the 7th effective instruction, { 1, 6, 16, 7 } // r[7] = r[6] - 2;
    when(rng.nextInt(EXAMPLE_BYTECODE_1_EFFECTIVENESS.cardinality())).thenReturn(6);
    // instr randomizer should be called for a random operator
    when(instrRandomizer.randomOperator()).thenReturn(3);

    Individual offspring = mutator.breed(new Individual[] { parentIndividual })[0];
    byte[] expected = EXAMPLE_BYTECODE_1.clone();
    expected[40] = 3;
    assertTrue(Arrays.equals(expected, offspring.getProgram().getBytecode()));
  }

  @Test
  public void testOperatorMutationReturnsNullToAvoidSameIndividual() {
    // chose operator mutation type
    when(rng.nextDouble()).thenReturn(0.69);
    // chose the 7th effective instruction, { 1, 6, 16, 7 } // r[7] = r[6] - 2;
    when(rng.nextInt(EXAMPLE_BYTECODE_1_EFFECTIVENESS.cardinality())).thenReturn(6);
    // instr randomizer should be called for a random operator
    when(instrRandomizer.randomOperator()).thenReturn(1);

    assertBreedingProducesNoIndividuals();
  }

  @Test
  public void testConstantMutation() {
    // chose operator mutation type
    when(rng.nextDouble()).thenReturn(0.99);
    // TODO expect no results because its not yet implemented
    assertBreedingProducesNoIndividuals();
  }

  private void assertBreedingProducesNoIndividuals() {
    assertEquals(0, mutator.breed(new Individual[] { parentIndividual }).length);
  }
}
