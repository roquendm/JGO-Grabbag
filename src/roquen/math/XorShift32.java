package roquen.math;

import java.util.concurrent.atomic.AtomicLong;


// Implementation notes:
//  Without Weyl generator: SmallCrush failures [13,17,5]:
//    1  BirthdaySpacings    eps  
//    2  Collision           1 - eps1
//    6  MaxOft              6.7e-16
//    8  MatrixRank          eps  
//   10  RandomWalk1 H       5.7e-7
// Adding in the counter will make it pass all
// SmallCrush tests.  You don't care. I promise.

/***
 * Basic XorShift (32-bit) generator.  This specific generator
 * predates generalized XorShifts and was previously called SHR3.
 * (This is one component of the once popular KISS generator)
 * The default configuration has a period of 2<sup>32</sup>-1,
 * although paranoid programmer may have extended it by adding
 * a Weyl generator (making it an XorWOW) with a period of 
 * 2<sup>64</sup>-2<sup>32</sup> ~= 2<sup>64</sup>
 * <p>
 * All ranges are shown in American notation: square bracket
 * is inclusive and paren is exclusive.
 * <p>
 * References: 
 * <list><li>"Xorshift RNGs", George Marsaglia, 2003.</li>
 * <li>"Some long-period random number generators
 * using shifts and xors", Richard P. Brent, 2006.</li></list>
 */
public final class XorShift32
{ 
  /** state data of xorshift */
  private int data;

  //*********************** For the paranoid section (start)
  
  /** If this is true, then we're XorWOW and paranoid ;) */
  public static final boolean PARANOID = true;
  
  // just in case we happen to have two threads on different
  // processors make two non-seeded generators within the
  // same nanosecond (currently CPU tick) and that somehow
  // really matters.  Or we're in the future of this writing
  // and computers are really fast and we succeed in creating
  // two non-seeded generators in a row within the same
  // nanosecond and it matters.  You never know. (Seriously,
  // why not...doesn't cost you anything)
  private static AtomicLong mix = new AtomicLong();

  // counter (weyl) stuff...only for the paranoid.
  /** state data of Weyl */
  private int weyl;
  
  /** 
   * Weyl increment.  This value must be odd, but some
   * choices are much better than others.
   * The choice here is from Richard Brent (see paper)
   * <p>
   * 2<sup>31</sup>(3-sqrt(5)) = 0x61c88647
   */
  private static final int WEYL = 0x61c88647;

  //*********************** For the paranoid section (end)

  public XorShift32()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public XorShift32(int seed)
  {
    setSeed(seed);
  }

  public XorShift32(long seed)
  {
    setSeed(seed);
  }

  /**
   * Sets the current set of the generator. Some
   * seeds set the generator into an identical
   * state.
   */
  public final void setSeed(long seed)
  {
    int lo = (int)(seed);

    // xorshift state must never be zero.
    if (lo==0) lo = 0x92d68ca2;
    
    data = lo;

    if (PARANOID) {
      int hi = (int)(seed >>> 32);
      
      data = lo;
      weyl = hi|1; // must be odd
    }
  }

  /**
   * Returns the current state data of the generator.
   */
  public final long getSeed()
  {
    long r = data;

    if (PARANOID)
      r |= weyl << 32;
    
    return r;
  }
  
  /** Returns a uniform 32-bit integer. */
  public final int nextInt()
  {
    data ^= (data <<  13);
    data ^= (data >>> 17);
    data ^= (data <<   5);
    
    if (PARANOID) {
      weyl += WEYL;
      return data + weyl;
    }
    return data;
  }
  
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

  /** Returns a uniform 64-bit integer. */
  public final long nextLong()
  {
    long r = nextInt();
    return (r<<32) | nextInt();
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
   * Returns a 'n' bit random number, [0, 2<sup>n</sup>)
   */
  public final int nextBits(int n)
  {
    return nextInt() >>> (32-n);
  }

  /**
   * Returns a 'n' bit random number, [2<sup>n/2</sup>, 2<sup>n/2</sup>)
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
   * Returns a random number on [0, 0xFFFF] with a
   * triangular distribution.
   */
  public final float nextFloatTriangle()
  {
    int r0,r1;
    
    r1 = nextInt();
    r0 = r1 >>> 8;          // 24-bit
    r1 = r1 & 0xFFFFFF;     // 24-bit
    
    return (r0+r1+1)>>1;
  }


  @SuppressWarnings("boxing")
  public static void main(String[] args)
  { 
    double m = 0x1p-51;
    double hi,lo;
    
    m = m+Math.ulp(m);
    
    lo = ((Long.MIN_VALUE >> 12)+0.5) * m;
    hi = ((Long.MAX_VALUE >> 12)+0.5) * m;
    
    lo = mapToPMOx(Long.MIN_VALUE);
    hi = mapToPMOx(Long.MAX_VALUE);
    
    //System.out.println(Double.toHexString(m));
    //System.out.println(Double.toHexString(1+Math.ulp(1.0)));
    System.out.println(Double.toHexString(1-Math.ulp(1.0)));
    
    System.out.printf("(%f,%f) (%s,%s)\n", lo, hi,
        Double.toHexString(lo),
        Double.toHexString(hi));
    
  }


}
