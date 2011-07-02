package org.neurus.evolution;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class LoggingEvolutionListener implements EvolutionListener {

  private static Logger logger = Logger.getLogger(LoggingEvolutionListener.class.getName());
  private static DecimalFormat decimalFormat = new DecimalFormat("0.000000");

  @Override
  public void onNewGeneration(EvolutionState evolutionState) {
    double totalFitness = 0;
    double best = Double.MAX_VALUE;
    for (int x = 0; x < evolutionState.getPopulation().size(); x++) {
      double fit = evolutionState.getPopulation().get(x).getFitness().getValue();
      totalFitness += fit;
      best = Math.min(fit, best);
    }
    double average = totalFitness / evolutionState.getPopulation().size();
    logger.info("Generation: " + evolutionState.getGeneration()
        + ". Best: "  + decimalFormat.format(best)
        + " Average fitness: " + decimalFormat.format(average));
  }
}
