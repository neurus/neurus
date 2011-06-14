package org.neurus.instruction;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class ConstantRegistersTest {

  @Test
  public void testRangeConstructorReturnsCorrectValues() {
    ConstantRegisters cr = new ConstantRegisters(-0.2, 0.2, 0.1);
    assertTrue(Arrays.equals(new double[] { -0.2, -0.1, 0.0, 0.1, 0.2 }, cr.getValues()));
  }

  @Test
  public void testArrayConstructorReturnsCorrectValues() {
    ConstantRegisters cr = new ConstantRegisters(new double[] { -0.2, -0.1, 0.0, 0.1, 0.2 });
    assertTrue(Arrays.equals(new double[] { -0.2, -0.1, 0.0, 0.1, 0.2 }, cr.getValues()));
  }

  @Test
  public void testDefaultConstructorReturnsEmptyArrayAsValues() {
    ConstantRegisters cr = new ConstantRegisters();
    Assert.assertEquals(0, cr.getValues().length);
  }
  

  @Test
  public void testSize() {
    ConstantRegisters cr = new ConstantRegisters(new double[] {1.0, 1.5});
    Assert.assertEquals(2, cr.size());
    ConstantRegisters cr2 = new ConstantRegisters();
    Assert.assertEquals(0, cr2.size());
    ConstantRegisters cr3 = new ConstantRegisters(-0.2, 0.2, 0.1);
    Assert.assertEquals(5, cr3.size());
  }
}
