package org.neurus.breeder;

import org.neurus.evolution.Individual;

public interface Breeder {

  int getMaxNumberOfOffsprings();

  int getNumberOfParents();

  Individual[] breed(Individual[] parents);
}
