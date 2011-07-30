package org.neurus.fitness;

import org.neurus.evolution.Individual;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.ProgramRunner;

public class FitnessEvaluator {

  private final FitnessFunction fitnessFunction;
  private final FitnessFunction validationFitnessFunction;
  private final ProgramRunner programRunner;
  private final EffectivenessAnalyzer effectivenessAnalyzer;

  public FitnessEvaluator(ProgramRunner programRunner, EffectivenessAnalyzer effectivenessAnalyzer,
      FitnessFunction fitnessFunction, FitnessFunction validationFitnessFunction) {
    super();
    this.fitnessFunction = fitnessFunction;
    this.validationFitnessFunction = validationFitnessFunction;
    this.programRunner = programRunner;
    this.effectivenessAnalyzer = effectivenessAnalyzer;
  }

  public FitnessEvaluator(ProgramRunner programRunner, EffectivenessAnalyzer effectivenessAnalyzer,
      FitnessFunction fitnessFunction) {
    this(programRunner, effectivenessAnalyzer, fitnessFunction, null);
  }

  public void evaluateFitness(Individual individual) {
    individual.setFitness(doEvaluation(individual, fitnessFunction));
  }

  public void evaluateValidationFitness(Individual individual) {
    if (!validates()) {
      throw new IllegalStateException(
          "This evaluator was not provided with a validation fitness function");
    }
    individual.setValidationFitness(doEvaluation(individual, validationFitnessFunction));
  }

  public boolean validates() {
    return validationFitnessFunction != null;
  }

  private Fitness doEvaluation(Individual individual, FitnessFunction aFitnessFunction) {
    effectivenessAnalyzer.analyzeProgram(individual.getProgram());
    programRunner.load(individual.getProgram(), true);
    Fitness fitness = aFitnessFunction.evaluate(programRunner, individual);
    return fitness;
  }
}
