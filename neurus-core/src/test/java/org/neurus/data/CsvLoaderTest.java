package org.neurus.data;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.neurus.testing.MoreAsserts.assertStringContains;

import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Joiner;

public class CsvLoaderTest {

  private Schema ageAndGenderSchema = new Schema.Builder()
      .addNumericAttribute("age")
      .addNominalAttribute("gender", new String[] { "M", "F" })
      .build();

  @Test
  public void testSuccessfulLoad() {
    String csvFile = Joiner.on('\n').join("age,gender", "9,M", "12,F");
    CsvLoader csvLoader = new CsvLoader(ageAndGenderSchema);
    Dataset ds = csvLoader.load(new StringReader(csvFile), true);
    Assert.assertSame(ageAndGenderSchema, ds.getSchema());
    assertEquals(2, ds.getInstances().size());

    Instance instance = ds.getInstances().get(0);
    assertEquals(9d, instance.getValues()[0]);
    assertEquals(0d, instance.getValues()[1]);

    instance = ds.getInstances().get(1);
    assertEquals(12d, instance.getValues()[0]);
    assertEquals(1d, instance.getValues()[1]);
  }

  @Test
  public void testSuccessfulLoadWithNoHeaders() {
    String csvFile = Joiner.on('\n').join("9,M", "12,F");
    CsvLoader csvLoader = new CsvLoader(ageAndGenderSchema);
    Dataset ds = csvLoader.load(new StringReader(csvFile), false);
    assertEquals(2, ds.getInstances().size());
  }

  @Test
  public void testAvoidHeaderValidation() {
    String csvFile = Joiner.on('\n').join("wrongHeader1,wrongHeader2", "9,M", "12,F");
    CsvLoader csvLoader = new CsvLoader(ageAndGenderSchema);
    csvLoader.setValidateHeadersIfPresent(false);
    Dataset ds = csvLoader.load(new StringReader(csvFile), true);
    assertEquals(2, ds.getInstances().size());
  }

  @Test
  public void testCsvLoaderComplainsAboutEmptyFile() {
    String csvFile = "";
    try {
      CsvLoader csvLoader = new CsvLoader(ageAndGenderSchema);
      csvLoader.load(new StringReader(csvFile), true);
      fail();
    } catch (RuntimeException ex) {
      assertStringContains("File is empty?", ex.getMessage());
    }
  }

  @Test
  public void testCsvLoaderValidatesNumberOfAttributes() {
    String csvFile = Joiner.on('\n').join("age", "9,M");
    try {
      CsvLoader csvLoader = new CsvLoader(ageAndGenderSchema);
      csvLoader.load(new StringReader(csvFile), true);
      fail();
    } catch (RuntimeException ex) {
      assertStringContains("headers expected", ex.getMessage());
    }
  }

  @Test
  public void testCsvLoaderValidatesAttributeNames() {
    String csvFile = Joiner.on('\n').join("age,wrongheader", "9,M");
    try {
      CsvLoader csvLoader = new CsvLoader(ageAndGenderSchema);
      csvLoader.load(new StringReader(csvFile), true);
      fail();
    } catch (RuntimeException ex) {
      assertStringContains("Schema attributes do not match", ex.getMessage());
      assertStringContains("wrongheader", ex.getMessage());
    }
  }

  @Test
  public void testInvalidAttributeCountForData() {
    String csvFile = Joiner.on('\n').join("age,gender", "9,M,idontbelonghere");
    try {
      CsvLoader csvLoader = new CsvLoader(ageAndGenderSchema);
      csvLoader.load(new StringReader(csvFile), true);
      fail();
    } catch (Exception ex) {
      assertStringContains("Incorrect number of attributes", ex.getMessage());
    }
  }
}
