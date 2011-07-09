package org.neurus.testing;

import java.util.Arrays;

import junit.framework.Assert;

public class MoreAsserts {

  public static void assertStringContains(String expectedSubstring, String str) {
    Assert.assertTrue("Expected sub-string: '" + expectedSubstring
        + "' is not contained in String: '"
        + str + "'", str.contains(expectedSubstring));
  }

  public static void assertEqualArrays(Object[] expected, Object[] actual) {
    Assert.assertTrue(
        "Expected array: " + Arrays.toString(expected) + ". Actual: " + Arrays.toString(actual),
        Arrays.equals(expected, actual));
  }
}
