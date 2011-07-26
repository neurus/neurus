package org.neurus.data;

import static org.neurus.data.AttributeUtil.isMissing;

import java.util.Arrays;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class NominalAttribute implements Attribute {

  private String name;
  private String[] labels;
  private Map<String, Double> labelToValue = Maps.newTreeMap();

  public NominalAttribute(String name, String[] labels) {
    Preconditions.checkNotNull(name, "name cannot be null");
    Preconditions.checkNotNull(labels, "labels cannot be null");
    Preconditions.checkArgument(labels.length > 0, "labels cannot be empty");
    this.name = name;
    this.labels = labels.clone();
    for (int x = 0; x < labels.length; x++) {
      if (isMissing(labels[x])) {
        throw new IllegalArgumentException("Invalid label name: " + labels[x]
            + ". This are reserved to indicate missing attributes.");
      }
      Double previousValue = labelToValue.put(labels[x], Double.valueOf(x));
      if (previousValue != null) {
        throw new IllegalArgumentException("Repeated label: " + labels[x]);
      }
    }
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
    Double value = labelToValue.get(strValue);
    if (value == null) {
      throw new IllegalArgumentException("Cannot find label: '" + strValue
          + "' for nominal attribute: '" + name + "'");
    }
    return value.doubleValue();
  }

  @Override
  public String labelFor(double value) {
    if (Double.isNaN(value)) {
      return MISSING_VALUE_LABEL;
    }
    int index = (int) value;
    return labels[index];
  }

  public String[] getLabels() {
    return labels;
  }

  @Override
  public NominalAttribute copy() {
    return new NominalAttribute(name, Arrays.copyOf(labels, labels.length));
  }
}
