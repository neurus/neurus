package org.neurus.data;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class TextAttributeTest {

  @Test
  public void testTextAttribute() {
    String str0 = "First String";
    String str1 = "Second String";
    String str2 = "Third String";

    TextAttribute att = new TextAttribute("att");
    assertEquals("att", att.getName());

    assertEquals(att.valueOf(str0), 0d);
    assertEquals(att.valueOf(str1), 1d);
    assertEquals(att.valueOf(str0), 0d);
    assertEquals(att.valueOf(str1), 1d);
    assertEquals(att.valueOf(str2), 2d);

    assertEquals(att.labelFor(0d), str0);
    assertEquals(att.labelFor(1d), str1);
    assertEquals(att.labelFor(2d), str2);
  }

  @Test
  public void testCopy() {
    String str0 = "First String";
    String str1 = "Second String";
    String str2 = "Third String";    
    TextAttribute att = new TextAttribute("att");
    att.valueOf(str0);
    att.valueOf(str1);
    att.valueOf(str2);

    TextAttribute copy = att.copy();
    assertEquals(att.getName(), copy.getName());
    assertEquals(1d, copy.valueOf(str1));
    assertEquals(str2, copy.labelFor(2d));
    assertEquals(3d, copy.valueOf("new string"));
  }
}
