package org.neurus.data;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.neurus.testing.MoreAsserts.assertStringContains;

import java.util.Arrays;

import org.junit.Test;
import org.neurus.testing.MoreAsserts;

public class NominalAttributeTest {

  @Test
  public void testGetName() {
    String[] labels = new String[] { "label1", "label2" };
    NominalAttribute nominalAttribute = new NominalAttribute("someName", labels);
    assertEquals("someName", nominalAttribute.getName());
  }

  @Test
  public void testGetLabels() {
    String[] labels = new String[] { "label1", "label2" };
    NominalAttribute nominalAttribute = new NominalAttribute("someName", labels);
    assertTrue(Arrays.equals(labels, nominalAttribute.getLabels()));
  }

  @Test
  public void testValueOf() {
    String[] labels = new String[] { "label1", "label2", "label3" };
    NominalAttribute nominalAttribute = new NominalAttribute("someName", labels);
    assertEquals(0d, nominalAttribute.valueOf("label1"));
    assertEquals(1d, nominalAttribute.valueOf("label2"));
    assertEquals(2d, nominalAttribute.valueOf("label3"));
  }

  @Test
  public void testValueOfWithInvalidLabel() {
    String[] labels = new String[] { "label1", "label2", "label3" };
    NominalAttribute nominalAttribute = new NominalAttribute("someName", labels);
    try {
      nominalAttribute.valueOf("nonExistingLabel");
      fail();
    } catch (IllegalArgumentException ex) {
      assertStringContains("Cannot find label", ex.getMessage());
      assertStringContains("nonExistingLabel", ex.getMessage());
    }
  }

  @Test
  public void testLabelFor() {
    String[] labels = new String[] { "label1", "label2", "label3" };
    NominalAttribute nominalAttribute = new NominalAttribute("someName", labels);
    assertEquals("label1", nominalAttribute.labelFor(0));
    assertEquals("label2", nominalAttribute.labelFor(1));
    assertEquals("label3", nominalAttribute.labelFor(2));
  }

  @Test
  public void testConstructionFailsWhenLabelsAreRepeated() {
    String[] labels = new String[] { "repeatedLabel", "label1", "label2", "repeatedLabel", "label3" };
    try {
      new NominalAttribute("someName", labels);
      fail();
    } catch (IllegalArgumentException ex) {
      assertStringContains("Repeated label", ex.getMessage());
      assertStringContains("repeatedLabel", ex.getMessage());
    }
  }
}
