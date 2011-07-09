package org.neurus.data;

import java.util.List;

import com.google.common.collect.Lists;

public class Dataset {

  private Schema schema;
  private List<Instance> instances = Lists.newArrayList();

  public Dataset(Schema schema) {
    this.schema = schema;
  }

  public void add(Instance instance) {
    this.instances.add(instance);
  }

  public Schema getSchema() {
    return schema;
  }

  public List<Instance> getInstances() {
    return instances;
  }
}
