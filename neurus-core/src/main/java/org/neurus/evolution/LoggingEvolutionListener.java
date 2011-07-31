package org.neurus.evolution;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class LoggingEvolutionListener implements EvolutionListener {

  private static Logger logger = Logger.getLogger(LoggingEvolutionListener.class.getName());
  private static DecimalFormat decimalFormat = new DecimalFormat("0.000000");

  @Override
  public void onNewGeneration(EvolutionSnapshot evolutionSnapshot) {
    double totalFitness = 0;
    for (int x = 0; x < evolutionSnapshot.getPopulation().size(); x++) {
      double fit = evolutionSnapshot.getPopulation().get(x).getFitness().getValue();
      totalFitness += fit;
    }
    double average = totalFitness / evolutionSnapshot.getPopulation().size();

    StringBuilder message = new StringBuilder();
    message.append("Generation: ").append(evolutionSnapshot.getGenerationNumber()).append(".");
    message
        .append(" Best Training: ")
        .append(decimalFormat
                .format(evolutionSnapshot.getBestTrainingIndividual().getFitness().getValue()))
        .append(".");
    if (evolutionSnapshot.getBestValidationIndividual() != null) {
      message
          .append(" Best Validation: ")
          .append(decimalFormat.format(evolutionSnapshot.getBestValidationIndividual()
                  .getValidationFitness().getValue()))
          .append(".");
    }
    message.append(" Average fitness: ").append(decimalFormat.format(average)).append(".");

    logger.info(message.toString());
  }
}
