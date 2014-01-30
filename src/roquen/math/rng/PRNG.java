package roquen.math.rng;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Base class
 * <p>
 * All ranges are shown in American notation: square bracket
 * is inclusive and parentheses is exclusive.
 */
public abstract class PRNG {
  /** Sets the current state data of the generator. */
  abstract void setSeed(long seed);
  
  /** Returns the current state data of the generator. */
  abstract long getSeed();
  
  /** Returns a uniform 32-bit integer. */
  abstract int nextInt();
  
  /** Returns a uniform 64-bit integer. */
  abstract long nextLong();
  
  // just in case we happen to have two threads on different
  // processors make two non-seeded generators within the
  // same nanosecond (currently CPU tick) and that somehow
  // really matters.  Or we're in the future of this writing
  // and computers are really fast and we succeed in creating
  // two non-seeded generators in a row within the same
  // nanosecond and it matters.  You never know. (Seriously,
  // why not...doesn't cost you anything)
  protected static AtomicLong mix = new AtomicLong(-1);
  
  //***************************************
  //** Derived stuff all below this point
  //** some assumptions based on 32-bit
  //** base generator.
  //***************************************

  /** Maps a 32-bit integer to [0,1) single. */
  public static final float mapToZO(int n)
  {
    // max value is 1-ulp(1)
    return (n >>> 8) * 0x1p-24f;
  }

  /** Maps a 32-bit integer to (0,1] single. */
  public static final float mapToZOi(int n)
  {
    return ((n >>> 8)+1) * 0x1p-24f;
  }
  
  /** Maps a 64-bit integer to [0,1) double. */
  public static final double mapToZO(long n)
  {
    // max value is 1-ulp(1)
    return (n >>> 12) * 0x1p-52;
  }

  /** Maps a 32-bit integer to [-1,1] single. */
  public static final float mapToPMO(int n)
  {
    return ((n >> 8)+0.5f) * 0x1.000002p-23f;
  }

  /** Maps a 64-bit integer to [-1,1] double. */
  public static final double mapToPMO(long n)
  {
    return ((n >> 12)+0.5)*0x1.0000000000001p-51;
  }
  
  /** Maps a 32-bit integer to (-1,1) single. */
  public static final float mapToPMOx(int n)
  {
    return ((n >> 8)+0.5f) * 0x1p-23f;
  }

  /** Maps a 64-bit integer to (-1,1) double. */
  public static final double mapToPMOx(long n)
  {
    return ((n >> 12)+0.5)*0x1p-51;
  }


  /** Returns a uniform boolean */
  public final boolean nextBoolean()
  {
    return nextInt() < 0;
  }

  /**
   * Returns a uniform integer on [0, n).
   * <p>
   * Range 'n' legal on [0, 0x8000].
    */
  public final int nextInt(int n)
  {
    // not quite uniform..you shouldn't care.
    // If you're really paranoid change to a
    // rejection method.
    return ((nextInt()>>>15) * n) >>> 17;
  }

  /** Returns a uniform integer on [0, n). */
  public final int nextIntBig(int n)
  {
    // See notes in nextInt. This is 
    // (on average) a better choice for
    // 64-bit VMs.
    long r = nextInt() >>> 1;

    // sign doesn't matter here
    r = (r * n) >> 31;

    return (int)r;
  }



  /** 
   * Returns a uniform float on [0, 1) = [0, 1-ulp(1)]
   * <p>
   * @see #mapToZO(int)
   */
  public final float nextFloat()
  {
    return mapToZO(nextInt());
  }

  /**
   * Returns a uniform single on [min,max)
   */
  public final float nextFloat(float min, float max)
  {
    // NOTE: Not the soundest method..you shouldn't care.
    return min+nextFloat()*(max-min);
  }

  /** 
   * Returns a uniform double on [0, 1) = [0, 1-ulp(1)]
   * <p>
   * @see #mapToZO(long)
   */
  public final double nextDouble()
  {
    return mapToZO(nextLong());
  }

  /**
   * Returns a uniform double on [min,max)
   */
  public final double nextDouble(double min, double max)
  {
    // NOTE: Not the soundest method..you shouldn't care.
    return min+nextDouble()*(max-min);
  }

  
  /**
   * Returns a 'n' bit random number, [0, 2<sup>n</sup>), with
   * n on [1,32]
   */
  public final int nextBits(int n)
  {
    return nextInt() >>> (32-n);
  }
  
  
  /**
   * Returns a 'n' bit random number, [0, 2<sup>n</sup>), with
   * n on [1,64]
   */
  public final long nextLongBits(int n)
  {
    return nextLong() >>> (64-n);
  }

  /**
   * Returns a 'n' bit random number, [2<sup>n/2</sup>, 2<sup>n/2</sup>),
   * with n on [1,32].
   */
  public final int nextSignedBits(int n)
  {
    return nextInt() >> (32-n);
  }
  
  /**
   * Returns a random number on [0, 0xFFFF] with a
   * triangular distribution.
   */
  public final int nextIntTriangle()
  {
    int r0,r1;
    
    r1 = nextInt();
    r0 = r1 >>> 16;
    r1 = r1 & 0xFFFF;
    
    return (r0+r1+1)>>1;
  }

  /**
   * Returns a random number on [0, 1) with a
   * triangular distribution.
   */
  public final float nextFloatTriangle()
  {
    long r0,r1;
    
    r1 = nextLong();
    r0 = r1 >>> 40;         // 24-bit
    r1 = r1 & 0xFFFFFF;     // 24-bit
    
    return (r0+r1) * 0x1p-25f;
  }

  /**
   * Returns a random integer with a Poisson distribution. The
   * input 'eMean' is exp(-mean).
   * <p>
   * Implementation is suitable for small means.
   */
  public final int nextPoisson(float eMean)
  {
    int   r = 1;
    float t = nextFloat();

    while (t > eMean) {
      r++;
      t *= nextFloat();
    }
    return r-1;
  }
  
  /**
   * Geometric distribution with p = 1/2.
   * <p>
   * Returns a random integer on [0,32] with probability of (1/2)<sup>n+1</sup>.
   * <p>
   * Number of times I toss a coin until I see a "heads" (minus one).  So we get
   * 0,1,2...(50%, 25%, 12.5%...)
   */
  public final int nextToss()
  {
    return Integer.numberOfLeadingZeros(nextInt());
  }
  
  /**
   * Geometric distribution with p = 1/2.  Result on [0,64]
   * @see #nextToss()
   */
  public final int nextLongToss()
  {
    return Long.numberOfLeadingZeros(nextLong());
  }
  
}
