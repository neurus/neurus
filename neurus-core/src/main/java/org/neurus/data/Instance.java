package org.neurus.data;

public class Instance {

  private Schema schema;
  private double[] values;

  Instance(Schema schema, double[] values) {
    this.schema = schema;
    this.values = values;
  }

  public double[] getValues() {
    return values;
  }

  public void setValue(int x, String strValue) {
    values[x] = schema.getAttribute(x).valueOf(strValue);
  }

  public double getValue(String attributeName) {
    return values[schema.indexOfAttribute(attributeName)];
  }
}
