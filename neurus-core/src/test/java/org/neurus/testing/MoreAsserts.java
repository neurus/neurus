package org.neurus.testing;

import junit.framework.Assert;

public class MoreAsserts {

  public static void assertStringPresent(String expectedSubstring, String str) {
    Assert.assertTrue("Expected String: '" + expectedSubstring + "' is not contained in String: '"
        + str + "'", str.contains(expectedSubstring));
  }
}
