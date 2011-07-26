package org.neurus.data.filter;

import org.neurus.data.Dataset;
import org.neurus.data.Instance;
import org.neurus.rng.RandomChoice;
import org.neurus.rng.RandomNumberGenerator;

import com.google.common.base.Preconditions;

public class DatasetSplitter {

  public static Dataset[] split(Dataset dataset, RandomNumberGenerator rng, double[] probabilities) {
    Preconditions.checkArgument(probabilities.length > 1,
        "You should provide at least 2 probabilities");
    RandomChoice choice = new RandomChoice(rng, probabilities);
    Dataset[] splits = new Dataset[probabilities.length];
    for (int x = 0; x < splits.length; x++) {
      splits[x] = new Dataset(dataset.getSchema().copy());
    }
    for (int x = 0; x < dataset.getInstances().size(); x++) {
      int i = choice.pickNext();
      Instance instanceCopy = splits[i].getSchema().newInstance(dataset.getInstances().get(x));
      splits[i].add(instanceCopy);
    }
    return splits;
  }
}
