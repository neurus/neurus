package org.neurus.evolution;

public class EvolutionParameters {

  private static final int DEFAULT_POPULATION_SIZE = 50;
  private static final int DEFAULT_MIN_INITIALIZATION_PROGRAM_SIZE = 2;
  private static final int DEFAULT_MAX_INITIALIZATION_PROGRAM_SIZE = 10;
  private static final long DEFAULT_RNG_SEED = System.currentTimeMillis();
  private static final double DEFAULT_CONST_PROBABILITY = 0.5d;

  private int populationSize = DEFAULT_POPULATION_SIZE;
  private int minInitializationProgramSize = DEFAULT_MIN_INITIALIZATION_PROGRAM_SIZE;
  private int maxInitializationProgramSize = DEFAULT_MAX_INITIALIZATION_PROGRAM_SIZE;
  private long randomSeed = DEFAULT_RNG_SEED;
  private double constProbability = DEFAULT_CONST_PROBABILITY;

  public int getPopulationSize() {
    return populationSize;
  }

  public void setPopulationSize(int populationSize) {
    this.populationSize = populationSize;
  }

  public int getMinInitializationProgramSize() {
    return minInitializationProgramSize;
  }

  public void setMinInitializationProgramSize(int minInitializationProgramSize) {
    this.minInitializationProgramSize = minInitializationProgramSize;
  }

  public int getMaxInitializationProgramSize() {
    return maxInitializationProgramSize;
  }

  public void setMaxInitializationProgramSize(int maxInitializationProgramSize) {
    this.maxInitializationProgramSize = maxInitializationProgramSize;
  }

  public long getRandomSeed() {
    return randomSeed;
  }

  public void setRandomSeed(long randomSeed) {
    this.randomSeed = randomSeed;
  }

  public double getConstProbability() {
    return constProbability;
  }

  public void setConstProbability(double constProbability) {
    this.constProbability = constProbability;
  }
}
