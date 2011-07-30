package org.neurus.fitness;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.neurus.TestFitnessFunctions;
import org.neurus.evolution.Individual;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.Program;
import org.neurus.machine.ProgramRunner;

public class FitnessEvaluatorTest {

  FitnessFunction fitnessFunction;
  FitnessFunction validationFitnessFunction;
  ProgramRunner programRunner;
  EffectivenessAnalyzer analyzer;

  @Before
  public void setUp() {
    fitnessFunction = TestFitnessFunctions.constantFitnessFunction(0.5);
    validationFitnessFunction = TestFitnessFunctions.constantFitnessFunction(0.6);
    fitnessFunction = spy(fitnessFunction);
    programRunner = Mockito.mock(ProgramRunner.class);
    analyzer = Mockito.mock(EffectivenessAnalyzer.class);
  }

  @Test
  public void testEvaluateFitness() {
    FitnessEvaluator evaluator = new FitnessEvaluator(programRunner, analyzer, fitnessFunction);
    Individual individual = new Individual(new Program(new byte[] {}));

    evaluator.evaluateFitness(individual);
    assertEquals(0.5, individual.getFitness().getValue());

    InOrder order = Mockito.inOrder(analyzer, programRunner, fitnessFunction);
    // we should analyze the program before loading it
    order.verify(analyzer).analyzeProgram(individual.getProgram());
    // we should load the program before calling evaluate
    order.verify(programRunner).load(individual.getProgram(), true);
    // ensure at the end we call fitness evaluation
    order.verify(fitnessFunction).evaluate(programRunner, individual);
  }

  @Test
  public void testValidates() {
    FitnessEvaluator evaluatorWithoutValidation = new FitnessEvaluator(programRunner, analyzer,
        fitnessFunction);
    assertFalse(evaluatorWithoutValidation.validates());

    FitnessEvaluator evaluatorWithValidation = new FitnessEvaluator(programRunner, analyzer,
        fitnessFunction, validationFitnessFunction);
    assertTrue(evaluatorWithValidation.validates());
  }

  @Test
  public void testEvaluateValidationFitness() {
    FitnessEvaluator evaluator = new FitnessEvaluator(programRunner, analyzer, fitnessFunction,
        validationFitnessFunction);
    Individual individual = new Individual(new Program(new byte[] {}));

    evaluator.evaluateFitness(individual);
    assertEquals(0.5, individual.getFitness().getValue());
    assertNull(individual.getValidationFitness());

    evaluator.evaluateValidationFitness(individual);
    assertEquals(0.5, individual.getFitness().getValue());
    assertEquals(0.6, individual.getValidationFitness().getValue());
  }

  @Test(expected = IllegalStateException.class)
  public void testEvaluateValidationFitnessFailsWhenNoValidationFunction() {
    FitnessEvaluator evaluator = new FitnessEvaluator(programRunner, analyzer, fitnessFunction);
    Individual individual = new Individual(new Program(new byte[] {}));
    evaluator.evaluateValidationFitness(individual);
  }
}
