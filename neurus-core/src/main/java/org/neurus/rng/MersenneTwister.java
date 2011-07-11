package org.neurus.rng;

public strictfp class MersenneTwister implements RandomNumberGenerator {

  // Serialization
  private static final long serialVersionUID = -8219700664442619525L; // locked as of Version 15

  // Period parameters
  private static final int N = 624;
  private static final int M = 397;
  private static final int MATRIX_A = 0x9908b0df; // private static final * constant vector a
  private static final int UPPER_MASK = 0x80000000; // most significant w-r bits
  private static final int LOWER_MASK = 0x7fffffff; // least significant r bits

  // Tempering parameters
  private static final int TEMPERING_MASK_B = 0x9d2c5680;
  private static final int TEMPERING_MASK_C = 0xefc60000;

  private int mt[]; // the array for the state vector
  private int mti; // mti==N+1 means mt[N] is not initialized
  private int mag01[];

  /**
   * Constructor using a given seed. Though you pass this seed in as a long, it's best to make sure
   * it's actually an integer.
   */
  public MersenneTwister(final long seed) {
    setSeed(seed);
  }

  /**
   * Initalize the pseudo random number generator. Don't pass in a long that's bigger than an int
   * (Mersenne Twister only uses the first 32 bits for its seed).
   */
  synchronized public void setSeed(final long seed) {
    mt = new int[N];

    mag01 = new int[2];
    mag01[0] = 0x0;
    mag01[1] = MATRIX_A;

    mt[0] = (int) (seed & 0xffffffff);
    for (mti = 1; mti < N; mti++) {
      mt[mti] =
                (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
      /* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
      /* In the previous versions, MSBs of the seed affect */
      /* only MSBs of the array mt[]. */
      /* 2002/01/09 modified by Makoto Matsumoto */
      mt[mti] &= 0xffffffff;
      /* for >32 bit machines */
    }
  }

  /**
   * Returns a random double in the half-open range from [0.0,1.0). Thus 0.0 is a valid result but
   * 1.0 is not.
   */
  public final double nextDouble() {
    int y;
    int z;

    if (mti >= N) // generate N words at one time
    {
      int kk;
      final int[] mt = this.mt; // locals are slightly faster
      final int[] mag01 = this.mag01; // locals are slightly faster

      for (kk = 0; kk < N - M; kk++) {
        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
      }
      for (; kk < N - 1; kk++) {
        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
      }
      y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
      mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

      mti = 0;
    }

    y = mt[mti++];
    y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
    y ^= (y << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
    y ^= (y << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
    y ^= (y >>> 18); // TEMPERING_SHIFT_L(y)

    if (mti >= N) // generate N words at one time
    {
      int kk;
      final int[] mt = this.mt; // locals are slightly faster
      final int[] mag01 = this.mag01; // locals are slightly faster

      for (kk = 0; kk < N - M; kk++) {
        z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + M] ^ (z >>> 1) ^ mag01[z & 0x1];
      }
      for (; kk < N - 1; kk++) {
        z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + (M - N)] ^ (z >>> 1) ^ mag01[z & 0x1];
      }
      z = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
      mt[N - 1] = mt[M - 1] ^ (z >>> 1) ^ mag01[z & 0x1];

      mti = 0;
    }

    z = mt[mti++];
    z ^= z >>> 11; // TEMPERING_SHIFT_U(z)
    z ^= (z << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(z)
    z ^= (z << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(z)
    z ^= (z >>> 18); // TEMPERING_SHIFT_L(z)

    /* derived from nextDouble documentation in jdk 1.2 docs, see top */
    return ((((long) (y >>> 6)) << 27) + (z >>> 5)) / (double) (1L << 53);
  }

  /**
   * Returns an integer drawn uniformly from 0 to n-1. Suffice it to say, n must be > 0, or an
   * IllegalArgumentException is raised.
   */
  public final int nextInt(final int n) {
    if (n <= 0)
      throw new IllegalArgumentException("n must be positive, got: " + n);

    if ((n & -n) == n) // i.e., n is a power of 2
    {
      int y;

      if (mti >= N) // generate N words at one time
      {
        int kk;
        final int[] mt = this.mt; // locals are slightly faster
        final int[] mag01 = this.mag01; // locals are slightly faster

        for (kk = 0; kk < N - M; kk++) {
          y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
          mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
        }
        for (; kk < N - 1; kk++) {
          y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
          mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
        }
        y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
        mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

        mti = 0;
      }

      y = mt[mti++];
      y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
      y ^= (y << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
      y ^= (y << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
      y ^= (y >>> 18); // TEMPERING_SHIFT_L(y)

      return (int) ((n * (long) (y >>> 1)) >> 31);
    }

    int bits, val;
    do {
      int y;

      if (mti >= N) // generate N words at one time
      {
        int kk;
        final int[] mt = this.mt; // locals are slightly faster
        final int[] mag01 = this.mag01; // locals are slightly faster

        for (kk = 0; kk < N - M; kk++) {
          y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
          mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
        }
        for (; kk < N - 1; kk++) {
          y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
          mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
        }
        y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
        mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

        mti = 0;
      }

      y = mt[mti++];
      y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
      y ^= (y << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
      y ^= (y << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
      y ^= (y >>> 18); // TEMPERING_SHIFT_L(y)

      bits = (y >>> 1);
      val = bits % n;
    } while (bits - val + (n - 1) < 0);
    return val;
  }
}