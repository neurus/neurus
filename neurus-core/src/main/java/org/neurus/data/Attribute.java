package org.neurus.data;

public interface Attribute {

  String getName();

  double valueOf(String strValue);

  String labelFor(double value);
}
