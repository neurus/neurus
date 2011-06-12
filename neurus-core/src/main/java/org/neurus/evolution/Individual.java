package org.neurus.evolution;

import org.neurus.fitness.Fitness;
import org.neurus.instruction.Program;

public class Individual {

  private Program program;
  private Fitness fitness;

  public Individual(Program program) {
    this.program = program;
  }

  public Program getProgram() {
    return program;
  }

  public Fitness getFitness() {
    return fitness;
  }

  public void setFitness(Fitness fitness) {
    this.fitness = fitness;
  }
}
