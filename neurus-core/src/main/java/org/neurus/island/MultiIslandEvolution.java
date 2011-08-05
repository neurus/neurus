package org.neurus.island;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.neurus.evolution.Evolution;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

public class MultiIslandEvolution {

  private List<Evolution> islands;

  public MultiIslandEvolution(List<Evolution> islands) {
    this.islands = ImmutableList.copyOf(islands);
  }

  public List<Evolution> getIslands() {
    return islands;
  }

  public void evolve() {
    ExecutorService executor = Executors.newFixedThreadPool(islands.size());
    for (int x = 0; x < islands.size(); x++) {
      executor.submit(new IslandTask(islands.get(x)));
    }
    executor.shutdown();
    try {
      executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      throw Throwables.propagate(e);
    }
  }
}

class IslandTask implements Runnable {

  private Evolution island;

  public IslandTask(Evolution island) {
    this.island = island;
  }

  @Override
  public void run() {
    island.evolve();
  }
}