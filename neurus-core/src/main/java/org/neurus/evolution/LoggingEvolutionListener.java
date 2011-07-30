package org.neurus.evolution;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class LoggingEvolutionListener implements EvolutionListener {

  private static Logger logger = Logger.getLogger(LoggingEvolutionListener.class.getName());
  private static DecimalFormat decimalFormat = new DecimalFormat("0.000000");

  @Override
  public void onNewGeneration(EvolutionState evolutionState) {
    double totalFitness = 0;
    for (int x = 0; x < evolutionState.getPopulation().size(); x++) {
      double fit = evolutionState.getPopulation().get(x).getFitness().getValue();
      totalFitness += fit;
    }
    double average = totalFitness / evolutionState.getPopulation().size();

    StringBuilder message = new StringBuilder();
    message.append("Generation: ").append(evolutionState.getGeneration()).append(".");
    message
        .append(" Best Training: ")
        .append(decimalFormat
                .format(evolutionState.getBestTrainingIndividual().getFitness().getValue()))
        .append(".");
    if (evolutionState.getBestValidationIndividual() != null) {
      message
          .append(" Best Validation: ")
          .append(decimalFormat.format(evolutionState.getBestValidationIndividual()
                  .getValidationFitness().getValue()))
          .append(".");
    }
    message.append(" Average fitness: ").append(decimalFormat.format(average)).append(".");

    logger.info(message.toString());
  }
}
