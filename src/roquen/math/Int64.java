package roquen.math;

public enum Int64
{
  ;
  
  // The basic bit-hacks can just be copied from Int32.
  // Note that HotSpot as of this writing performs less
  // optimizations for 64-bit integers than 32-bit..
  // sigh.
  
  public static long parity(long x)
  {
    if (Int32.hasPopCount)
      return Long.bitCount(x) & 1;
    
    long p;
    x = (x ^ (x >>> 1));
    x = (x ^ (x >>> 2)) & 0x1111111111111111L;
    x = x*0x1111111111111111L;
    p = (x >> 60) & 1;
    
    return p;
  }

  /** Computes 'a' such that a*x = 1 for odd integers 'x' */
  public static final long modInverse(long x)
  {
    // newton's
    long r = (x*x)+x - 1;
    long t = x*r;
    r *= 2-t; t = x*r;
    r *= 2-t; t = x*r;
    r *= 2-t; t = x*r;
    r *= 2-t;
    return r;
  }
}
