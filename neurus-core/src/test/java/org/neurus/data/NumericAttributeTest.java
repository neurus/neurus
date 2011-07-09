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
  }

  @Test
  public void testLabelFor() {
    NumericAttribute att = new NumericAttribute("someName");
    assertEquals("3.45", att.labelFor(3.45d));
  }
}
