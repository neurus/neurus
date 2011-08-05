package org.neurus.evolution;

import java.util.List;

public interface Exchanger {

  List<Individual> receiveIndividuals();

  void migrateIndividuals(Population population, int generationNumber);
}
