package org.neurus.evolution;

import org.neurus.breeder.Breeder;
import org.neurus.breeder.CompositeBreeder;
import org.neurus.breeder.MacroMutation;
import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.BytecodeWriter;
import org.neurus.instruction.InstructionRandomizer;
import org.neurus.instruction.Machine;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class EvolutionBuilder {

  private EvolutionParameters params = new EvolutionParameters();
  private Machine machine;
  private FitnessFunction fitnessFunction;  

  public EvolutionBuilder withFitnessFunction(FitnessFunction fitnessFunction) {
    this.fitnessFunction = fitnessFunction;
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
    RandomNumberGenerator rng = new DefaultRandomNumberGenerator(params.getRandomSeed());
    BytecodeWriter bytecodeWriter = new BytecodeWriter(machine);
    InstructionRandomizer instructionRandomizer = new InstructionRandomizer(machine, rng,
        params.getConstantProbability());
    IndividualInitializer individualInitializer = new SimpleIndividualInitializer(machine, rng,
        bytecodeWriter, instructionRandomizer, params.getMinInitializationProgramSize(),
        params.getMaxInitializationProgramSize());
    PopulationFactory populationFactory = new PopulationFactory(individualInitializer);
    TerminationStrategy termination = new DefaultTerminationStrategy();
    TournamentSelection selector = new TournamentSelection(rng,
        params.getTournamentSize());
    TournamentSelection deselector = new TournamentSelection(rng,
        params.getTournamentSize(), true);
    MacroMutation macroMutation = new MacroMutation(machine, rng, params.getInsertionProbability(),
        params.getMinProgramSize(), params.getMaxProgramSize(), bytecodeWriter,
        instructionRandomizer);
    EvolutionListener evolutionListener = new LoggingEvolutionListener();
    Breeder breeder = new CompositeBreeder.Builder(rng).withBreeder(macroMutation, 1).build();
    return new SteadyStateEvolution(machine, populationFactory, rng, fitnessFunction, termination,
        params, selector, deselector, breeder, evolutionListener);
  }
}
