package org.neurus.data;

public interface Attribute {

  static final double MISSING_VALUE = Double.NaN;
  static final String MISSING_VALUE_LABEL = "?";
  static final String MISSING_VALUE_ALTERNATIVE_LABEL = "";

  String getName();

  double valueOf(String strValue);

  String labelFor(double value);
}
