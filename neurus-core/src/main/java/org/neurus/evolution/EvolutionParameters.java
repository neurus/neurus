package org.neurus.evolution;

public class EvolutionParameters {

  private static final int DEFAULT_POPULATION_SIZE = 50;
  private static final int DEFAULT_MIN_INITIALIZATION_PROGRAM_SIZE = 2;
  private static final int DEFAULT_MAX_INITIALIZATION_PROGRAM_SIZE = 10;
  private static final int DEFAULT_MIN_PROGRAM_SIZE = 1;
  private static final int DEFAULT_MAX_PROGRAM_SIZE = 200;
  private static final long DEFAULT_RNG_SEED = System.currentTimeMillis();
  private static final double DEFAULT_CONSTANT_PROBABILITY = 0.5d;
  private static final double DEFAULT_INSERTION_PROBABILITY = 0.5d;
  private static final int DEFAULT_TOURNAMENT_SIZE = 2;
  private static final double DEFAULT_REGISTER_MUTATION_PROBABILITY = 0.33d;
  private static final double DEFAULT_OPERATOR_MUTATION_PROBABILITY = 0.33d;
  private static final double DEFAULT_CONSTANT_MUTATION_PROBABILITY = 0.33d;
  private static final int DEFAULT_MAX_NUMBER_OF_GENERATIONS = 500;
  private static final double DEFAULT_FITNESS_THRESHOLD = 0;

  private int populationSize = DEFAULT_POPULATION_SIZE;
  private int minInitializationProgramSize = DEFAULT_MIN_INITIALIZATION_PROGRAM_SIZE;
  private int maxInitializationProgramSize = DEFAULT_MAX_INITIALIZATION_PROGRAM_SIZE;
  private long randomSeed = DEFAULT_RNG_SEED;
  private int minProgramSize = DEFAULT_MIN_PROGRAM_SIZE;
  private int maxProgramSize = DEFAULT_MAX_PROGRAM_SIZE;
  private double insertionProbability = DEFAULT_INSERTION_PROBABILITY;
  private double constantProbability = DEFAULT_CONSTANT_PROBABILITY;
  private int tournamentSize = DEFAULT_TOURNAMENT_SIZE;
  private double registerMutationProbability = DEFAULT_REGISTER_MUTATION_PROBABILITY;
  private double operatorMutationProbability = DEFAULT_OPERATOR_MUTATION_PROBABILITY;
  private double constantMutationProbability = DEFAULT_CONSTANT_MUTATION_PROBABILITY;
  private int maxNumberOfGenerations = DEFAULT_MAX_NUMBER_OF_GENERATIONS;
  private double fitnessThreshold = DEFAULT_FITNESS_THRESHOLD;

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

  public double getConstantProbability() {
    return constantProbability;
  }

  public void setConstantProbability(double constProbability) {
    this.constantProbability = constProbability;
  }

  public int getTournamentSize() {
    return tournamentSize;
  }

  public void setTournamentSize(int tournamentSize) {
    this.tournamentSize = tournamentSize;
  }

  public int getMinProgramSize() {
    return minProgramSize;
  }

  public void setMinProgramSize(int minProgramSize) {
    this.minProgramSize = minProgramSize;
  }

  public int getMaxProgramSize() {
    return maxProgramSize;
  }

  public void setMaxProgramSize(int maxProgramSize) {
    this.maxProgramSize = maxProgramSize;
  }

  public double getInsertionProbability() {
    return insertionProbability;
  }

  public void setInsertionProbability(double insertionProbability) {
    this.insertionProbability = insertionProbability;
  }

  public double getRegisterMutationProbability() {
    return registerMutationProbability;
  }

  public void setRegisterMutationProbability(double registerMutationProbability) {
    this.registerMutationProbability = registerMutationProbability;
  }

  public double getOperatorMutationProbability() {
    return operatorMutationProbability;
  }

  public void setOperatorMutationProbability(double operatorMutationProbability) {
    this.operatorMutationProbability = operatorMutationProbability;
  }

  public double getConstantMutationProbability() {
    return constantMutationProbability;
  }

  public void setConstantMutationProbability(double constantMutationProbability) {
    this.constantMutationProbability = constantMutationProbability;
  }

  public int getMaxNumberOfGenerations() {
    return maxNumberOfGenerations;
  }

  public void setMaxNumberOfGenerations(int maxNumberOfGenerations) {
    this.maxNumberOfGenerations = maxNumberOfGenerations;
  }

  public double getFitnessThreshold() {
    return fitnessThreshold;
  }

  public void setFitnessThreshold(double fitnessThreshold) {
    this.fitnessThreshold = fitnessThreshold;
  }
}
