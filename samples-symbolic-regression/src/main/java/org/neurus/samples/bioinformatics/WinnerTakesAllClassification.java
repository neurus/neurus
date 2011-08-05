package org.neurus.samples.bioinformatics;

import org.neurus.data.Dataset;
import org.neurus.data.Instance;
import org.neurus.evolution.Individual;
import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.ProgramRunner;

public class WinnerTakesAllClassification implements FitnessFunction {

  private Dataset dataset;
  private int classAttributeIndex;

  public WinnerTakesAllClassification(Dataset dataset, int classAttributeIndex) {
    this.classAttributeIndex = classAttributeIndex;
    this.dataset = dataset;
  }

  @Override
  public Fitness evaluate(ProgramRunner programRunner, Individual individual) {
    double classError = 0;
    double[] inputs = new double[dataset.getSchema().getAttributeCount() - 1];
    for (Instance instance : dataset.getInstances()) {
      double[] values = instance.getValues();
      int c = 0;
      for (int x = 0; x < dataset.getSchema().getAttributeCount(); x++) {
        if (x != classAttributeIndex) {
          inputs[c] = values[x];
          c++;
        }
      }
      double[] result = programRunner.run(inputs);
      double expected = values[classAttributeIndex];
      
      
      double max = result[0];
      int maxIndex = 0;
      for (int i = 1; i < result.length; i++) {
        if(result[i] > max) {
          maxIndex = i;
          max = result[i];
        }
      }
      if (expected != maxIndex) {
        classError++;
      }
    }
    return new Fitness(classError);
  }
}
