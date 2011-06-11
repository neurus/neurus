package org.neurus.samples.symbolicregression;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class Launcher {

  @Parameter(required = true, names = "--data_file",
      description = "The csv file that contains data to be used for symbolic regresion")
  public String dataFile;

  public void launch() throws IOException {
    List<double[]> inputs = readFile();
  }

  private List<double[]> readFile() throws FileNotFoundException, IOException {
    URL file = this.getClass().getClassLoader().getResource(dataFile);
    CSVReader csvReader = null;
    List<double[]> inputs = Lists.newArrayList();
    try {
      csvReader = new CSVReader(new FileReader(file.getFile()));
      String[] read;
      while ((read = csvReader.readNext()) != null) {
        double[] lineValues = new double[read.length];
        for (int x = 0; x < read.length; x++) {
          lineValues[x] = Double.parseDouble(read[x]);
        }
        inputs.add(lineValues);
      }
    } finally {
      if (csvReader != null) {
        csvReader.close();
      }
    }
    return inputs;
  }

  public static void main(String[] args) throws IOException {
    Launcher launcher = new Launcher();
    JCommander cliParser = new JCommander(launcher);
    cliParser.parse(args);
    launcher.launch();
  }
}
