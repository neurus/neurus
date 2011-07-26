package org.neurus.data;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class InstanceTest {

  private Schema schema = new Schema.Builder()
      .addNominalAttribute("nominalAttribute", new String[] { "a", "b", "c" })
      .addTextAttribute("textAttribute")
      .addNumericAttribute("numericAttribute")
      .build();

  @Test
  public void testSetValueUsingString() {
    Instance instance = schema.newInstance();
    instance.setValue(0, "b");
    instance.setValue(1, "some random text");
    instance.setValue(2, "25.4");
    assertEquals(1d, instance.getValues()[0]);
    assertEquals(0d, instance.getValues()[1]);
    assertEquals(25.4d, instance.getValues()[2]);
  }

  public void testGetValueByAttributeName() {
    Instance instance = schema.newInstance();
    instance.setValue(0, "b");
    instance.setValue(1, "some random text");
    instance.setValue(2, "25.4");
    assertEquals(1d, instance.getValue("nominalAttribute"));
    assertEquals(0d, instance.getValue("textAttribute"));
    assertEquals(25.4d, instance.getValue("numericAttribute"));
  }

  @Test
  public void testCopy() {
    Instance original = schema.newInstance();
    original.setValue(0, "b");
    original.setValue(1, "some random text");
    original.setValue(2, "25.4");

    Instance copy = new Instance(schema, original);
    assertTrue(Arrays.equals(original.getValues(), copy.getValues()));
    assertNotSame(original.getValues(), copy.getValues());
    assertEquals(25.4d, copy.getValue("numericAttribute"));
  }
}
