package org.neurus.util;

import java.util.BitSet;

public class TestBitSetUtils {

  public static BitSet valueOf(String strBits) {
    BitSet bitSet = new BitSet(strBits.length());
    for(int x = 0; x < strBits.length(); x++) {
      if(strBits.charAt(x) == '1') {
        bitSet.set(x);
      } else if (strBits.charAt(x) == '0') {
        bitSet.clear(x);
      } else {
        throw new IllegalArgumentException("Invalid bit string: " + strBits);
      }
    }
    return bitSet;
  }
}
