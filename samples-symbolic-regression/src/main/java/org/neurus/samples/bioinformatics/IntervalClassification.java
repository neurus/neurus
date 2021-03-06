package org.neurus.samples.bioinformatics;

import org.neurus.data.Dataset;
import org.neurus.data.Instance;
import org.neurus.evolution.Individual;
import org.neurus.fitness.Fitness;
import org.neurus.fitness.FitnessFunction;
import org.neurus.machine.ProgramRunner;

public class IntervalClassification implements FitnessFunction {

  private Dataset dataset;
  private int classAttributeIndex;

  public IntervalClassification(Dataset dataset, int classAttributeIndex) {
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
      if (!(Math.abs(result[0] - expected) < 0.5)) {
        classError++;
      }
    }
    return new Fitness(classError);
  }
}
