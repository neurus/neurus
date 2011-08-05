package org.neurus.island;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.neurus.evolution.Evolution;
import org.neurus.evolution.Exchanger;
import org.neurus.evolution.Individual;
import org.neurus.evolution.Population;
import org.neurus.evolution.SelectionMethod;
import org.neurus.evolution.TournamentSelection;

import com.google.common.collect.Lists;

public class IslandExchanger implements Exchanger {

  public static final int MAX_PENDING_BATCHES = 10;
  private static final int TOURNAMENT_SIZE = 10;

  private IslandExchanger outgoingExchanger;
  private ConcurrentLinkedQueue<ExchangeBatch> batches = new ConcurrentLinkedQueue<ExchangeBatch>();
  private SelectionMethod selector;
  private double migrationRate;
  private int migrationModulo;

  public IslandExchanger(SelectionMethod selector, double migrationRate, int migrationModulo) {
    super();
    this.selector = selector;
    this.migrationRate = migrationRate;
    this.migrationModulo = migrationModulo;
  }

  @Override
  public List<Individual> receiveIndividuals() {
    ExchangeBatch batch = batches.poll();
    if (batch == null) {
      return Lists.newArrayList();
    }
    return batch.getIndividuals();
  }

  @Override
  public void migrateIndividuals(Population population, int generation) {
    // show we do the migration?
    if (generation % migrationModulo != 0) {
      return;
    }

    // select individuals
    int numberOfIndividuals = (int) (population.size() * migrationRate);
    List<Individual> individuals = Lists.newArrayList();
    for (int x = 0; x < numberOfIndividuals; x++) {
      int selected = selector.select(population);
      individuals.add(population.get(selected));
    }

    // send batch to the outgoing exchanger
    outgoingExchanger.addBatch(new ExchangeBatch(individuals));
  }

  protected void addBatch(ExchangeBatch batch) {
    if (batches.size() >= MAX_PENDING_BATCHES) {
      batches.poll();
    }
    batches.add(batch);
  }

  public void setOutgoingExchanger(IslandExchanger exchanger) {
    outgoingExchanger = exchanger;
  }

  IslandExchanger getOutgoingExchanger() {
    return outgoingExchanger;
  }

  public static List<IslandExchanger> createExchangersForIslands(List<Evolution> islands, int migrationModulo,
      double migrationRate) {
    // create the exchangers and connect each island with it
    List<IslandExchanger> exchangers = Lists.newArrayList();
    for (int x = 0; x < islands.size(); x++) {
      TournamentSelection selector = new TournamentSelection(islands.get(x)
          .getRandomNumberGenerator(), TOURNAMENT_SIZE);
      IslandExchanger exchanger = new IslandExchanger(selector, migrationRate, migrationModulo);
      exchangers.add(exchanger);
      islands.get(x).setExchanger(exchanger);
    }

    // connect the ring of exchangers
    for (int x = 0; x < exchangers.size() - 1; x++) {
      exchangers.get(x).setOutgoingExchanger(exchangers.get(x + 1));
    }
    exchangers.get(exchangers.size() - 1).setOutgoingExchanger(exchangers.get(0));

    return exchangers;
  }
}

class ExchangeBatch {

  private List<Individual> individuals;

  public ExchangeBatch(List<Individual> individuals) {
    super();
    this.individuals = individuals;
  }

  public List<Individual> getIndividuals() {
    return individuals;
  }
}