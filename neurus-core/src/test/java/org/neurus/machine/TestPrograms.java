package org.neurus.machine;

import java.util.BitSet;

import org.neurus.util.TestBitSetUtils;

public class TestPrograms {

  public static final byte[] EXAMPLE_BYTECODE_1 = new byte[] {
      0, 5, 10, 0, // r[0] = r[5] + 71;
      1, 0, 11, 7, // ** r[7] = r[0] - 59;
      5, 1, 12, 0, // if (r[1] > 0)
      5, 5, 13, 0, // if (r[5] > 2)
      2, 2, 1, 4, // r[4] = r[2] * r[1];
      0, 5, 4, 2, // ** r[2] = r[5] + r[4];
      2, 4, 14, 6, // r[6] = r[4] * 13;
      3, 3, 15, 1, // r[1] = r[3] / 2;
      5, 0, 1, 0, // ** if (r[0] > r[1])
      2, 5, 5, 3, // ** r[3] = r[5] * r[5];
      1, 6, 16, 7, // r[7] = r[6] - 2;
      0, 7, 17, 5, // ** r[5] = r[7] + 15;
      6, 1, 6, 0, // if (r[1] < r[6])
      7, 7, 0, 0, // r[0] = sin(r[7]);
  };

  public static final BitSet EXAMPLE_BYTECODE_1_EFFECTIVENESS =
      TestBitSetUtils.valueOf("10111011001011");

  public static final BitSet EXAMPLE_BYTECODE_1_EFFECTIVEREGISTERS_AT_INSTR_10 =
    TestBitSetUtils.valueOf("1100001100");
}
