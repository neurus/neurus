package org.neurus.data;

import java.io.IOException;
import java.io.Reader;

import org.neurus.util.Closeables;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class CsvLoader {

  private boolean validateHeadersIfPresent = true;
  private Schema schema;

  public CsvLoader(Schema schema) {
    Preconditions.checkNotNull(schema, "Schema cannot be null");
    this.schema = schema;
  }

  // TODO document that we close the reader
  public Dataset load(Reader reader, boolean fileHasHeaders) {
    Preconditions.checkNotNull(reader, "Reader cannot be null");
    CSVReader csvReader = null;
    try {
      csvReader = new CSVReader(reader);
      String[] line;

      // read headers and check that they match the defined schema
      if (fileHasHeaders) {
        line = csvReader.readNext();
        if (validateHeadersIfPresent) {
          checkHeaders(schema, line);
        }
      }

      // now load each data intances
      Dataset dataset = new Dataset(schema);
      int lineCounter = 1;
      while ((line = csvReader.readNext()) != null) {
        Instance instance = createInstance(schema, line, lineCounter);
        dataset.add(instance);
        lineCounter++;
      }
      return dataset;
    } catch (IOException e) {
      throw Throwables.propagate(e);
    } finally {
      Closeables.close(csvReader);
    }
  }

  public void setValidateHeadersIfPresent(boolean validateHeadersIfPresent) {
    this.validateHeadersIfPresent = validateHeadersIfPresent;
  }

  private Instance createInstance(Schema schema, String[] line, int lineCounter) {
    Instance instance = schema.newInstance();
    int attributeCount = schema.getAttributeCount();
    if (line.length != attributeCount) {
      throw new RuntimeException("Incorrect number of attributes in data file for line "
          + lineCounter + ". Expected: " + attributeCount + " but found: " + line.length);
    }
    for (int x = 0; x < attributeCount; x++) {
      instance.setValue(x, line[x]);
    }
    return instance;
  }

  private void checkHeaders(Schema schema, String[] line) {
    if (line == null) {
      throw new RuntimeException("No header line present in the file. File is empty?");
    }
    if (line.length != schema.getAttributeCount()) {
      throw new RuntimeException(schema.getAttributeCount() + " headers expected, found "
          + line.length);
    }
    for (int x = 0; x < schema.getAttributeCount(); x++) {
      if (!schema.getAttribute(x).getName().equals(line[x])) {
        throw new RuntimeException(
            "Schema attributes do not match with data file. Expected attribute: '"
                + schema.getAttribute(x).getName() + "' but found: '" + line[x] + "'");
      }
    }
  }
}
