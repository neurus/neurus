package org.neurus.machine;

import junit.framework.Assert;

import org.junit.Test;
import org.neurus.machine.ConstantRegisters;
import org.neurus.machine.Machine;
import org.neurus.machine.MachineBuilder;
import org.neurus.machine.Operator;

public class MachineBuilderTest {

  private Operator fakeOperator1 = new FakeOperator();
  private Operator fakeOperator2 = new FakeOperator();

  @Test
  public void testbuildCreatesAMachine() {
    Machine machine = new MachineBuilder()
        .withConstantRegisters(new ConstantRegisters(0, 9, 1))
        .withCalculationRegisters(8)
        .withOperator(fakeOperator1)
        .withOperator(fakeOperator2)
        .withOutputRegisters(3)
        .build();
    Assert.assertEquals(2, machine.size());
    Assert.assertEquals(fakeOperator1, machine.getOperator(0));
    Assert.assertEquals(fakeOperator2, machine.getOperator(1));
    Assert.assertEquals(8, machine.getNumberOfCalculationRegisters());
    Assert.assertEquals(10, machine.getNumberOfConstantRegisters());
    Assert.assertEquals(3, machine.getNumberOfOutputRegisters());
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildFailsWhenThereAreNoOperators() {
    new MachineBuilder().withCalculationRegisters(10).withOutputRegisters(3).build();
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildFailsWhenRegistersNotSpecified() {
    new MachineBuilder().withOperator(fakeOperator1).withOutputRegisters(3).build();
  }

  @Test(expected = NullPointerException.class)
  public void testWithOperatorFailsWhenGivenNullOperator() {
    new MachineBuilder().withOperator(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildFailsWhenOutputRegistersAreGreaterThanCalculationRegisters() {
    new MachineBuilder().withCalculationRegisters(5).withOutputRegisters(6).build();
  }
}
