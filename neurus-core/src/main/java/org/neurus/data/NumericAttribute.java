package org.neurus.data;

import static org.neurus.data.AttributeUtil.isMissing;

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
    if (isMissing(strValue)) {
      return MISSING_VALUE;
    }
    return Double.valueOf(strValue);
  }

  @Override
  public String labelFor(double value) {
    if (Double.isNaN(value)) {
      return MISSING_VALUE_LABEL;
    }
    return String.valueOf(value);
  }
}
