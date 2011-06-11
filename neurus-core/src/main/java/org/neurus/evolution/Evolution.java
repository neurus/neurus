package org.neurus.evolution;

import org.neurus.instruction.Machine;
import org.neurus.rng.DefaultRandomNumberGenerator;
import org.neurus.rng.RandomNumberGenerator;

public class Evolution {

  // TODO all these constants should be configurable
  private static final int POPULATION_SIZE = 50;
  private static final int MIN_INITIALIZATION_PROGRAM_SIZE = 2;
  private static final int MAX_INITIALIZATION_PROGRAM_SIZE = 10;
  private static final int RNG_SEED = 10;
  private static final double PCONST = 0.5d;

  private RandomNumberGenerator rng;
  private Machine machine;
  private PopulationFactory populationInitializer;
  private IndividualInitializer individualInitializer;

  public Evolution(Machine machine) {
    this.machine = machine;
    this.rng = new DefaultRandomNumberGenerator(RNG_SEED);
    this.individualInitializer = new SimpleIndividualInitializer( this.machine, rng,
        MIN_INITIALIZATION_PROGRAM_SIZE, MAX_INITIALIZATION_PROGRAM_SIZE, PCONST);
    this.populationInitializer = new PopulationFactory(individualInitializer);
  }

  public void evolve() {
    populationInitializer.initialize(POPULATION_SIZE);
  }
}
