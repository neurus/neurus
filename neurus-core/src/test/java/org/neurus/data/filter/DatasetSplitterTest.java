package org.neurus.data.filter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.neurus.data.Dataset;
import org.neurus.data.Instance;
import org.neurus.data.Schema;
import org.neurus.rng.RandomNumberGenerator;

public class DatasetSplitterTest {

  private Schema schema = new Schema.Builder()
      .addNumericAttribute("att1")
      .build();
  private RandomNumberGenerator rng = Mockito.mock(RandomNumberGenerator.class);

  @Test
  public void testSplit() {
    Dataset ds = new Dataset(schema);
    ds.add(newInstance(1d));
    ds.add(newInstance(2d));
    ds.add(newInstance(3d));
    ds.add(newInstance(4d));
    when(rng.nextDouble()).thenReturn(0.05d).thenReturn(0.2d).thenReturn(0.91d)
        .thenReturn(0.85d);

    Dataset[] dss = DatasetSplitter.split(ds, rng, new double[] { 0.1, 0.8, 0.1 });

    assertDs(dss[0], new double[] { 1d });
    assertDs(dss[1], new double[] { 2d, 4d });
    assertDs(dss[2], new double[] { 3d });
  }

  private void assertDs(Dataset dataset, double[] instances) {
    assertEquals(dataset.getInstances().size(), instances.length);
    for (int x = 0; x < instances.length; x++) {
      Assert.assertEquals(instances[x], dataset.getInstances().get(x).getValues()[0]);
    }
  }

  private Instance newInstance(double value) {
    Instance i = schema.newInstance();
    i.getValues()[0] = value;
    return i;
  }
}
