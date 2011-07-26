package org.neurus.data;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class NumericAttributeTest {

  @Test
  public void testGetName() {
    NumericAttribute att = new NumericAttribute("someName");
    assertEquals("someName", att.getName());
  }

  @Test
  public void testValueOf() {
    NumericAttribute att = new NumericAttribute("someName");
    assertEquals(3.45d, att.valueOf("3.45"));
    assertEquals(Double.NaN, att.valueOf(""));
    assertEquals(Double.NaN, att.valueOf("?"));
  }

  @Test
  public void testLabelFor() {
    NumericAttribute att = new NumericAttribute("someName");
    assertEquals("3.45", att.labelFor(3.45d));
    assertEquals("?", att.labelFor(Double.NaN));
  }

  @Test
  public void testCopy() {
    NumericAttribute original = new NumericAttribute("numeric_att");
    NumericAttribute copy = original.copy();
    assertEquals(original.getName(), copy.getName());
  }
}
