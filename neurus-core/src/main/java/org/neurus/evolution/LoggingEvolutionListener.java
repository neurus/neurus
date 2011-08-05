package org.neurus.evolution;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class LoggingEvolutionListener implements EvolutionListener {

  private static Logger logger = Logger.getLogger(LoggingEvolutionListener.class.getName());
  private static DecimalFormat decimalFormat = new DecimalFormat("0.000000");
  private String prefix;

  public LoggingEvolutionListener() {
    prefix = "";
  }

  public LoggingEvolutionListener(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public void onNewGeneration(EvolutionSnapshot evolutionSnapshot) {
    StringBuilder message = new StringBuilder();
    message.append(prefix);
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
    logger.info(message.toString());
  }
}
