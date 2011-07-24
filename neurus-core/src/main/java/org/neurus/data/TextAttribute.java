package org.neurus.data;

import java.util.HashMap;

import com.google.common.collect.Maps;

public class TextAttribute implements Attribute {

  private String name;
  private HashMap<String, Double> strToIndex = Maps.newHashMap();
  private HashMap<Double, String> indexToStr = Maps.newHashMap();
  private double nextIndex;

  public TextAttribute(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public double valueOf(String strValue) {
    Double index = strToIndex.get(strValue);
    if (index == null) {
      strToIndex.put(strValue, nextIndex);
      indexToStr.put(nextIndex, strValue);
      index = nextIndex;
      nextIndex++;
    }
    return index;
  }

  @Override
  public String labelFor(double value) {
    return indexToStr.get(value);
  }
}
