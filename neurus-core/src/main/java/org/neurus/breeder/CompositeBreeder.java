package org.neurus.breeder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.neurus.evolution.Individual;
import org.neurus.rng.RandomChoice;
import org.neurus.rng.RandomNumberGenerator;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;

public class CompositeBreeder implements Breeder {

  private int maxNumberOfOffsprings;
  private int numberOfParents;
  private Breeder[] breeders;
  private RandomChoice breederChoice;

  private CompositeBreeder(RandomNumberGenerator rng, Breeder[] breeders, double[] probabilities) {
    checkNotNull(breeders);
    checkArgument(breeders.length > 0);
    this.breeders = breeders;
    for (Breeder breeder : breeders) {
      maxNumberOfOffsprings = Math.max(maxNumberOfOffsprings, breeder.getMaxNumberOfOffsprings());
      numberOfParents = Math.max(numberOfParents, breeder.getNumberOfParents());
    }
    this.breederChoice = new RandomChoice(rng, probabilities);
  }

  @Override
  public Individual[] breed(Individual[] parents) {
    Breeder breeder = breeders[breederChoice.pickNext()];
    return breeder.breed(parents);
  }

  @Override
  public int getMaxNumberOfOffsprings() {
    return maxNumberOfOffsprings;
  }

  @Override
  public int getNumberOfParents() {
    return numberOfParents;
  }

  public static class Builder {
    private List<Breeder> breeders = Lists.newArrayList();
    private List<Double> probabilities = Lists.newArrayList();
    private RandomNumberGenerator rng;

    public Builder(RandomNumberGenerator rng) {
      this.rng = rng;
    }

    public Builder withBreeder(Breeder breeder, double probability) {
      breeders.add(breeder);
      probabilities.add(probability);
      return this;
    }

    public CompositeBreeder build() {
      Breeder[] breederArray = breeders.toArray(new Breeder[breeders.size()]);
      double[] probabilitiesArray = Doubles.toArray(probabilities);
      return new CompositeBreeder(rng, breederArray, probabilitiesArray);
    }
  }
}
