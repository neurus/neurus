package org.neurus.evolution;

import org.neurus.rng.RandomNumberGenerator;

public interface Evolution {

  void evolve();

  EvolutionSnapshot getEvolutionSnapshot();

  RandomNumberGenerator getRandomNumberGenerator();

  void setEvolutionListener(EvolutionListener listener);

  void setExchanger(Exchanger exchanger);
}
