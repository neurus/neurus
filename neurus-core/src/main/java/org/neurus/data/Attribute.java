package org.neurus.data;

import org.neurus.Copyable;

public interface Attribute extends Copyable<Attribute> {

  static final double MISSING_VALUE = Double.NaN;
  static final String MISSING_VALUE_LABEL = "?";
  static final String MISSING_VALUE_ALTERNATIVE_LABEL = "";

  String getName();

  double valueOf(String strValue);

  String labelFor(double value);
}
