package org.neurus.data;

public class NumericAttribute implements Attribute {

  private String name;

  public NumericAttribute(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public double valueOf(String strValue) {
    return Double.valueOf(strValue);
  }

  @Override
  public String labelFor(double value) {
    return String.valueOf(value);
  }
}
