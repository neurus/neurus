package org.neurus.evolution;

public class DefaultTerminationStrategy implements TerminationStrategy {

  @Override
  public boolean terminate(EvolutionState evolutionState) {
    return false;
  }
}
