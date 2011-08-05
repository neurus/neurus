package org.neurus.island;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.neurus.evolution.Evolution;
import org.neurus.evolution.EvolutionBuilder;

import com.google.common.collect.Lists;

public class MultiIslandEvolutionBuilder {

  public static final int DEFAULT_ISLANDS = 2;
  public static final int DEFAULT_MIGRATION_MODULO = 5;
  public static final double DEFAULT_MIGRATION_RATE = 0.05;

  private int numberOfIslands = DEFAULT_ISLANDS;
  private int migrationModulo = DEFAULT_MIGRATION_MODULO;
  private double migrationRate = DEFAULT_MIGRATION_RATE;
  private EvolutionBuilder evolutionBuilder;

  public MultiIslandEvolutionBuilder withEvolution(EvolutionBuilder evolutionBuilder) {
    this.evolutionBuilder = evolutionBuilder;
    return this;
  }

  public MultiIslandEvolutionBuilder withNumberOfIslands(int islands) {
    checkArgument(islands > 1, "Islands should be greater than 1");
    this.numberOfIslands = islands;
    return this;
  }

  public MultiIslandEvolutionBuilder withMigrationModulo(int migrationModulo) {
    checkArgument(migrationModulo > 0, "Migration modulo should be greater than 0");
    this.migrationModulo = migrationModulo;
    return this;
  }

  public MultiIslandEvolutionBuilder withMigrationRate(double migrationRate) {
    checkArgument(migrationModulo > 0 && migrationRate < 1,
        "Migration rate should be greater than 0 and less than 1");
    this.migrationRate = migrationRate;
    return this;
  }

  public MultiIslandEvolution build() {
    checkNotNull(evolutionBuilder, "EvolutionBuilder should be specified.");

    // create each island
    List<Evolution> islands = Lists.newArrayList();
    for (int x = 0; x < numberOfIslands; x++) {
      islands.add(evolutionBuilder.buildIsland(x));
    }

    IslandExchanger.createExchangersForIslands(islands, migrationModulo, migrationRate);

    return new MultiIslandEvolution(islands);
  }
}
