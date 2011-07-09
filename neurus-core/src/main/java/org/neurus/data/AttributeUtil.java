package org.neurus.data;

public class AttributeUtil {

  private AttributeUtil() {
  }

  public static boolean isMissing(String strValue) {
    return "".equals(strValue) || "?".equals(strValue);
  }
}
