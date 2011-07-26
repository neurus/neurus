package org.neurus.data;


public class Instance {

  private Schema schema;
  private double[] values;

  Instance(Schema schema, double[] values) {
    this.schema = schema;
    this.values = values;
  }

  Instance(Schema schema, Instance copy) {
    this(schema, copy.values.clone());
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
