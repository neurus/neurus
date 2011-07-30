package org.neurus.evolution;

import org.neurus.breeder.CompositeBreeder;
import org.neurus.breeder.EffectiveMacroMutation;
import org.neurus.breeder.EffectiveMicroMutation;
import org.neurus.breeder.MacroMutation;
import org.neurus.fitness.FitnessEvaluator;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.BytecodeWriter;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.rng.MersenneTwister;
import org.neurus.rng.RandomNumberGenerator;

public class EvolutionBuilder {

  private EvolutionParameters params = new EvolutionParameters();
  private Machine machine;
  private FitnessFunction fitnessFunction;
  private FitnessFunction validationFitnessFunction;

  public EvolutionBuilder withFitnessFunction(FitnessFunction fitnessFunction) {
    this.fitnessFunction = fitnessFunction;
    return this;
  }

  public EvolutionBuilder withValidationFitnessFunction(FitnessFunction validationFitnessFunction) {
    this.validationFitnessFunction = validationFitnessFunction;
    return this;
  }

  public EvolutionBuilder withEvolutionParameters(EvolutionParameters evolutionParameters) {
    this.params = evolutionParameters;
    return this;
  }

  public EvolutionBuilder withMachine(Machine machine) {
    this.machine = machine;
    return this;
  }

  public Evolution build() {
    RandomNumberGenerator rng = new MersenneTwister(params.getRandomSeed());
    BytecodeWriter bytecodeWriter = new BytecodeWriter(machine);
    InstructionRandomizer instructionRandomizer = new InstructionRandomizer(machine, rng,
        params.getConstantProbability());
    IndividualInitializer individualInitializer = new SimpleIndividualInitializer(machine, rng,
        bytecodeWriter, instructionRandomizer, params.getMinInitializationProgramSize(),
        params.getMaxInitializationProgramSize());
    PopulationFactory populationFactory = new PopulationFactory(individualInitializer,
        params.getPopulationSize());
    TerminationCriteria termination = new DefaultTerminationCriteria(
        params.getMaxNumberOfGenerations(), params.getFitnessThreshold());
    TournamentSelection selector = new TournamentSelection(rng,
        params.getTournamentSize());
    TournamentSelection deselector = new TournamentSelection(rng,
        params.getTournamentSize(), true);
    EffectivenessAnalyzer effectivenessAnalyzer = new EffectivenessAnalyzer(machine);
    MacroMutation macroMutation = new EffectiveMacroMutation(machine, rng,
        params.getInsertionProbability(),
        params.getMinProgramSize(), params.getMaxProgramSize(), bytecodeWriter,
        instructionRandomizer, effectivenessAnalyzer);
    EffectiveMicroMutation effMicroMutation = new EffectiveMicroMutation(machine,
        instructionRandomizer, rng, effectivenessAnalyzer, params.getRegisterMutationProbability(),
        params.getOperatorMutationProbability(), params.getConstantMutationProbability());
    CompositeBreeder compositeBreeder = new CompositeBreeder.Builder(rng)
        .withBreeder(macroMutation, 1.0)
        .withBreeder(effMicroMutation, 1.0)
        .build();
    FitnessEvaluator fitnessEvaluator = new FitnessEvaluator(machine.createRunner(),
        effectivenessAnalyzer, fitnessFunction, validationFitnessFunction);
    EvolutionListener evolutionListener = new LoggingEvolutionListener();
    return new SteadyStateEvolution(populationFactory, fitnessEvaluator,
        termination, selector, deselector, compositeBreeder, evolutionListener);
  }
}
