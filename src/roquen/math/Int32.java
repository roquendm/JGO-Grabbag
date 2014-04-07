package roquen.math;


// NOTES: Includes some uber basics for those not familiar with their bit-twiddling.
// These will all be small and are highly likely to be inlined

// The final on methods is implicitly, explicitly marked
// because it can't hurt.
public enum Int32 {
  
  ;
  
  // HotSpot has popcount as an intrinsic, but we need a way
  // to test for hardware support...sigh.
  static final boolean hasPopCount = false;
  
  /** 
   * Returns <i>true</i> if 'a' and 'b' have the same sign.
   * Zero is considered to be positive.
   */
  public static final boolean sameSign(int a, int b)
  {
    return (a^b) >= 0;
  }
  
  /**
   * Returns <i>true<i> if 'x' is zero or a power-of-two.
   */
  public static final boolean isZeroOrPOT(int x)
  {
    return (x & (x-1)) == 0;
  }
 
  /** Returns 'x' with the lowest set bit zeroed */
  public static final int zeroLowBit(int x)
  {
    return x & (x-1);
  }
  
  /** */
  public static final int isolateLowBit(int x)
  {
    return x & -x;
  }
  
  /** */
  public static final int isolateLowZero(int x)
  {
    return ~x & (x+1);
  }
  
  /** Set all low order bits until a set bit is found. */
  public static final int fillLow(int x)
  {
    return x | (x-1);
  }
  
  /** Set the lowest zero to one. No effect in none. */
  public static final int setLowZero(int x)
  {
    return x | (x+1);
  }
  
  
  
  /** (a >= 0) ? a : 0 */
  public static final int clampPositive(int a)
  {
    return a & ~(a >> 31); // dep-chain=3
  }

  public static final int abs(int a)
  {
    // dep-chain=3
    int s = a >> 31;
    a ^= s;
    a -= s;
    return a;
  }

  public static final int min(int a, int b)
  {
    // dep-chain=4
    a -= b;
    a &= (a >> 31);
    a += b;

    return a;
  }

  /** */
  public static final int max(int a, int b)
  {
    // dep-chain=4
    a -= b;    
    a &= ~(a >> 31);
    a += b;    

    return a;
  }

  /** (a<b) ? c : d */
  public static final int select(int a, int b, int c, int d)
  {
    // dep-chain=4
    return (((a-b)>>31) & (c^d))^d;
  }
  
  public static final int parity(int x)
  {
    if (hasPopCount)
      return Integer.bitCount(x) & 1;
      
    int p;
    x = (x ^ (x >>> 1));
    x = (x ^ (x >>> 2)) & 0x11111111;
    x = x*0x11111111;
    p = (x >> 28) & 1;
    
    return p;
  }
  
  /** 
   * Returns 1 for positive, 0 for zero and -1
   * for negative inputs.
   */
  public static final int sign(int a)
  {
    return (-a >>> 31) | (a >> 31);
  }
  
  /** 
   * Returns 1 for input greater than zero, otherwise 0.
   */
  public static final int sgn(int a)
  {
    return (-a >>> 31);
  }
  
  /** 
   * Returns -1 for input greater than zero, otherwise 0.
   */
  public static final int sgnMask(int a)
  {
    return (-a >> 31);
  }
  
  /** 
   * Returns 1 for input greater or equal to zero, otherwise 0.
   */
  public static final int sgnz(int a)
  {
    return (~a >>> 31);
  }
  
  /** 
   * Returns -1 for input greater or equal to zero, otherwise 0.
   */
  public static final int sgnzMask(int a)
  {
    return (~a >> 31);
  }
  
  /** ceiling(log2(x)) */
  public static final int ceilingLog2(int x)
  {
    return 32-Integer.numberOfLeadingZeros(x-1);
  }
  
  /** floor(log2(x)) */
  public static final int floorLog2(int x)
  {
    return 31-Integer.numberOfLeadingZeros(x);
  }
  
  /** Returns next integer with the same population count as 'x'. */
  public static final int nextPop(int x)
  {
    int a = x & -x;
    int b = x + a;
    int c = x ^ b;
    int d = (2 + Integer.numberOfTrailingZeros(x));
    return b | (c >>> d);
  }
  
  /** Computes 'a' such that a*x = 1 for odd integers 'x' */
  public static final int modInverse(int x)
  {
    // newton's
    int r = (x*x)+x - 1;
    int t = x*r;
    r *= 2-t; t = x*r;
    r *= 2-t; t = x*r;
    r *= 2-t;
    return r;
  }
}
