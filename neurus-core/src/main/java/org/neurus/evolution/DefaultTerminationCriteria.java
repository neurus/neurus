package org.neurus.evolution;

import com.google.common.base.Preconditions;

public class DefaultTerminationCriteria implements TerminationCriteria {

  private int stopAfterGenerationNumber;
  private double fitnessThreshold;

  public DefaultTerminationCriteria(int maxNumberOfGenerations, double fitnessThreshold) {
    Preconditions.checkArgument(maxNumberOfGenerations > 0,
        "maxNumberOfGenerations should be greater than 0");
    Preconditions.checkArgument(fitnessThreshold >= 0,
        "fitnessThreshold should be greater than 0");
    stopAfterGenerationNumber = maxNumberOfGenerations - 1;
    this.fitnessThreshold = fitnessThreshold;
  }

  @Override
  public boolean shouldTerminate(EvolutionState evolutionState) {
    // check for number of generations
    if (evolutionState.getGeneration() >= stopAfterGenerationNumber) {
      return true;
    }
    // check for best fitness
    // TODO We need to fill the best individual in the evolution algorithm and use that, may be also
    // check for the best validation error?
    double best = Double.MAX_VALUE;
    for (int x = 0; x < evolutionState.getPopulation().size(); x++) {
      double fit = evolutionState.getPopulation().get(x).getFitness().getValue();
      best = Math.min(fit, best);
    }
    if (best <= fitnessThreshold) {
      return true;
    }
    // no termination criteria met, continue
    return false;
  }
}
