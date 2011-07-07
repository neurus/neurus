package org.neurus.evolution;

public interface TerminationCriteria {

  boolean shouldTerminate(EvolutionState evolutionState);
}
