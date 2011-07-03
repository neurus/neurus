package org.neurus.breeder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.neurus.evolution.Individual;
import org.neurus.instruction.BytecodeWriter;
import org.neurus.instruction.InstructionData;
import org.neurus.instruction.InstructionRandomizer;
import org.neurus.instruction.Machine;
import org.neurus.instruction.Program;
import org.neurus.instruction.TestMachines;
import org.neurus.rng.RandomNumberGenerator;

public class MacroMutationTest {

  private Machine calculator = TestMachines.calculator();
  private BytecodeWriter bytecodeWriter = new BytecodeWriter(calculator);
  private InstructionRandomizer instructionRandomizer = Mockito.mock(InstructionRandomizer.class);
  private RandomNumberGenerator rng = mock(RandomNumberGenerator.class);
  private byte[] sampleProgram = new byte[] {
      0, 0, 0, 0,
      1, 1, 1, 1,
      2, 2, 2, 2,
      3, 3, 3, 3,
      4, 4, 4, 4
  };

  @Before
  public void setUp() {
    Mockito.doAnswer(fixedRandomInstructionAnswer()).when(instructionRandomizer)
        .fillRandomInstruction(Mockito.any(InstructionData.class));
  }

  @Test
  public void testDeletion() {
    when(rng.nextInt(5)).thenReturn(2); // remove the third line
    Program offspring = breedSampleProgramWithDeletion();
    byte[] expectedResult = new byte[] {
        0, 0, 0, 0,
        1, 1, 1, 1,
        3, 3, 3, 3,
        4, 4, 4, 4
    };
    Assert.assertTrue(Arrays.equals(expectedResult, offspring.getBytecode()));
  }

  @Test
  public void testDeletionOfFirstInstruction() {
    when(rng.nextInt(5)).thenReturn(0); // remove the third line
    Program offspring = breedSampleProgramWithDeletion();
    byte[] expectedResult = new byte[] {
        1, 1, 1, 1,
        2, 2, 2, 2,
        3, 3, 3, 3,
        4, 4, 4, 4
    };
    Assert.assertTrue(Arrays.equals(expectedResult, offspring.getBytecode()));
  }

  @Test
  public void testDeletionOfLastInstruction() {
    when(rng.nextInt(5)).thenReturn(4); // remove the third line
    Program offspring = breedSampleProgramWithDeletion();
    byte[] expectedResult = new byte[] {
        0, 0, 0, 0,
        1, 1, 1, 1,
        2, 2, 2, 2,
        3, 3, 3, 3,
    };
    Assert.assertTrue(Arrays.equals(expectedResult, offspring.getBytecode()));
  }

  @Test
  public void testInsertion() {
    when(rng.nextInt(6)).thenReturn(1); // insert after the first row
    Program offspring = breedSampleProgramWithInsertion();
    byte[] expectedResult = new byte[] {
        0, 0, 0, 0,
        0, 1, 2, 3,
        1, 1, 1, 1,
        2, 2, 2, 2,
        3, 3, 3, 3,
        4, 4, 4, 4
    };
    Assert.assertTrue(Arrays.equals(expectedResult, offspring.getBytecode()));
  }

  @Test
  public void testInsertionBeforeFirstInstruction() {
    when(rng.nextInt(6)).thenReturn(0); // insert after the first row
    Program offspring = breedSampleProgramWithInsertion();
    byte[] expectedResult = new byte[] {
        0, 1, 2, 3,
        0, 0, 0, 0,
        1, 1, 1, 1,
        2, 2, 2, 2,
        3, 3, 3, 3,
        4, 4, 4, 4
    };
    Assert.assertTrue(Arrays.equals(expectedResult, offspring.getBytecode()));
  }

  @Test
  public void testInsertionAfterLastInstruction() {
    when(rng.nextInt(6)).thenReturn(5); // insert after the first row
    Program offspring = breedSampleProgramWithInsertion();
    byte[] expectedResult = new byte[] {
        0, 0, 0, 0,
        1, 1, 1, 1,
        2, 2, 2, 2,
        3, 3, 3, 3,
        4, 4, 4, 4,
        0, 1, 2, 3,
    };
    Assert.assertTrue(Arrays.equals(expectedResult, offspring.getBytecode()));
  }

  private Program breedSampleProgramWithDeletion() {
    MacroMutation mutation = new MacroMutation(calculator, rng, 0 /* no insertions */, 1, 10,
        bytecodeWriter, instructionRandomizer);
    Individual parent = new Individual(new Program(sampleProgram));
    Individual[] offsprings = mutation.breed(new Individual[] { parent });
    return offsprings[0].getProgram();
  }

  private Program breedSampleProgramWithInsertion() {
    MacroMutation mutation = new MacroMutation(calculator, rng, 1 /* only insertions */, 1, 10,
        bytecodeWriter, instructionRandomizer);
    Individual parent = new Individual(new Program(sampleProgram));
    Individual[] offsprings = mutation.breed(new Individual[] { parent });
    return offsprings[0].getProgram();
  }

  private Answer<Object> fixedRandomInstructionAnswer() {
    return new Answer<Object>() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        InstructionData data = (InstructionData) invocation.getArguments()[0];
        data.instructionIndex = 0;
        data.inputRegisters[0] = 1;
        data.inputRegisters[1] = 2;
        data.destinationRegister = 3;
        return null;
      }
    };
  }
}
