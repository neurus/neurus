package org.neurus.samples.bioinformatics;

import java.io.IOException;

import org.neurus.data.Schema;

public class WineSample extends BioInformaticsSample {

  @Override
  protected String getDataPath() {
    return "org/neurus/samples/bionformatics/wine.data.txt";
  }

  @Override
  protected Schema createDataSchema() {
    return new Schema.Builder()
        .addNominalAttribute("classes", new String[] { "1", "2", "3" })
        .addNumericAttribute("alcohol").addNumericAttribute("malic_acid")
        .addNumericAttribute("ash").addNumericAttribute("alcalinity_of_ash")
        .addNumericAttribute("magnesium").addNumericAttribute("total_phenols")
        .addNumericAttribute("flavanoids").addNumericAttribute("nonfalavanoid_phenols")
        .addNumericAttribute("proanthocyanins").addNumericAttribute("color_intensity")
        .addNumericAttribute("hue").addNumericAttribute("OD280_OD315")
        .addNumericAttribute("proline")
        .build();
  }

  public static void main(String[] args) throws IOException {
    new WineSample().run();
  }
}
