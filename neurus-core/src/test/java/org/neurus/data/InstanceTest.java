package org.neurus.data;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class InstanceTest {

  Schema schema = new Schema.Builder()
    .addNominalAttribute("nominalAttribute", new String[] {"a", "b", "c"})
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
}
