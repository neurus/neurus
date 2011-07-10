package org.neurus.samples.bioinformatics;

import org.neurus.data.Schema;

public class SoybeanSample extends BioInformaticsSample {

  private String[] CLASSES = new String[] {
      "diaporthe-stem-canker", "charcoal-rot", "rhizoctonia-root-rot",
      "phytophthora-rot", "brown-stem-rot", "powdery-mildew",
      "downy-mildew", "brown-spot", "bacterial-blight",
      "bacterial-pustule", "purple-seed-stain", "anthracnose",
      "phyllosticta-leaf-spot", "alternarialeaf-spot",
      "frog-eye-leaf-spot", "diaporthe-pod-&-stem-blight",
      "cyst-nematode", "2-4-d-injury", "herbicide-injury" };

  @Override
  protected String getDataPath() {
    return "org/neurus/samples/bionformatics/soybean-large.data.txt";
  }

  @Override
  protected Schema createDataSchema() {
    return new Schema.Builder()
        .addNominalAttribute("classes", CLASSES)
        .addNumericAttribute("date").addNumericAttribute("plant-stand")
        .addNumericAttribute("precip").addNumericAttribute("temp")
        .addNumericAttribute("hail").addNumericAttribute("crop-hist")
        .addNumericAttribute("area-damaged").addNumericAttribute("severity")
        .addNumericAttribute("seed-tmt").addNumericAttribute("germination")
        .addNumericAttribute("plant-growth").addNumericAttribute("leaves")
        .addNumericAttribute("leafspots-halo").addNumericAttribute("leafspots-marg")
        .addNumericAttribute("leafspot-size").addNumericAttribute("leaf-shread")
        .addNumericAttribute("leaf-malf").addNumericAttribute("leaf-mild")
        .addNumericAttribute("stem").addNumericAttribute("lodging")
        .addNumericAttribute("stem-cankers").addNumericAttribute("canker-lesion")
        .addNumericAttribute("fruiting-bodies").addNumericAttribute("external decay")
        .addNumericAttribute("mycelium").addNumericAttribute("int-discolor")
        .addNumericAttribute("sclerotia").addNumericAttribute("fruit-pods")
        .addNumericAttribute("fruit spots").addNumericAttribute("seed")
        .addNumericAttribute("mold-growth").addNumericAttribute("seed-discolor")
        .addNumericAttribute("seed-size").addNumericAttribute("shriveling")
        .addNumericAttribute("roots")
        .build();
  }

  public static void main(String[] args) {
    new SoybeanSample().run();
  }
}
