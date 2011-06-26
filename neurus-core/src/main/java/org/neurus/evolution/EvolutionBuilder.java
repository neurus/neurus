package org.neurus.evolution;

import org.neurus.fitness.FitnessFunction;
import org.neurus.instruction.BytecodeWriter;
import org.neurus.instruction.InstructionRandomizer;
import org.neurus.instruction.Machine;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class EvolutionBuilder {

  private EvolutionParameters params;
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

  public EvolutionBuilder withEvolutionParameters(Machine machine) {
    this.machine = machine;
    return this;
  }

  public Evolution build() {
    RandomNumberGenerator rng = new DefaultRandomNumberGenerator(params.getRandomSeed());
    BytecodeWriter bytecodeWriter = new BytecodeWriter(machine);
    InstructionRandomizer instrRandomizer = new InstructionRandomizer(machine, rng,
        params.getConstProbability());
    IndividualInitializer individualInitializer = new SimpleIndividualInitializer(machine, rng,
        bytecodeWriter, instrRandomizer, params.getMinInitializationProgramSize(),
        params.getMaxInitializationProgramSize());
    PopulationFactory populationFactory = new PopulationFactory(individualInitializer);
    TerminationStrategy termination = new DefaultTerminationStrategy();
    return new SteadyStateEvolution(machine, populationFactory, rng, fitnessFunction, termination,
        params);
  }
}
