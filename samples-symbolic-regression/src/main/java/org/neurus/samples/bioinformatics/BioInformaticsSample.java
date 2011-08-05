package org.neurus.samples.bioinformatics;

import static org.neurus.machine.LogicOperators.ifGreaterThan;
import static org.neurus.machine.LogicOperators.ifLessThanOrEquals;
import static org.neurus.machine.MathOperators.addition;
import static org.neurus.machine.MathOperators.division;
import static org.neurus.machine.MathOperators.multiplication;
import static org.neurus.machine.MathOperators.pow;
import static org.neurus.machine.MathOperators.substraction;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.neurus.data.CsvLoader;
import org.neurus.data.Dataset;
import org.neurus.data.Schema;
import org.neurus.evolution.EvolutionBuilder;
import org.neurus.evolution.EvolutionParameters;
import org.neurus.island.MultiIslandEvolution;
import org.neurus.island.MultiIslandEvolutionBuilder;
import org.neurus.machine.ConstantRegisters;
import org.neurus.machine.Machine;
import org.neurus.machine.MachineBuilder;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

public abstract class BioInformaticsSample {

  public void run() {
    Dataset dataset = loadDataset();
    EvolutionParameters params = new EvolutionParameters();
    params.setRandomSeed(System.currentTimeMillis());
    params.setPopulationSize(1000);
    params.setMaxProgramSize(200);
    params.setMaxNumberOfGenerations(500);
    params.setMinInitializationProgramSize(10);
    params.setMaxInitializationProgramSize(30);
    EvolutionBuilder evolutionBuilder = new EvolutionBuilder()
        .withMachine(createMachine(dataset.getSchema().getAttributeCount() - 1))
        .withFitnessFunction(new WinnerTakesAllClassification(dataset, 0))
        .withEvolutionParameters(params);
    MultiIslandEvolution evolution = new MultiIslandEvolutionBuilder()
        .withEvolution(evolutionBuilder)
        .withNumberOfIslands(4)
        .build();
    evolution.evolve();
  }

  private Dataset loadDataset() {
    try {
      URL dataUrl = Resources.getResource(getDataPath());
      InputSupplier<InputStreamReader> readerSupplier =
          Resources.newReaderSupplier(dataUrl, Charsets.UTF_8);
      CsvLoader csvLoader = new CsvLoader(createDataSchema());
      Dataset dataset = csvLoader.load(readerSupplier.getInput(), false);
      return dataset;
    } catch (IOException ex) {
      throw Throwables.propagate(ex);
    }
  }

  protected abstract String getDataPath();

  protected abstract Schema createDataSchema();

  private Machine createMachine(int numberOfInputs) {
    return new MachineBuilder()
        .withOperator(addition())
        .withOperator(substraction())
        .withOperator(multiplication())
        .withOperator(division())
        .withOperator(pow())
        .withOperator(ifGreaterThan())
        .withOperator(ifLessThanOrEquals())
        .withCalculationRegisters(numberOfInputs + 10)
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withOutputRegisters(19)
        .build();
  }
}
