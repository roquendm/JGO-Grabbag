package roquen.math.rng;

/**
 * A 32-bit power-of-two LCG generator which can move both directions in the 
 * sequence.
 */
public class BidirectionalLCG extends PRNG32 {
  
  /** state data */
  private int data;
  
  /** true if moving forward in the stream and false if move backward. */
  private boolean forward = true;
  
  private final int mf;  // forward multiplier
  private final int mr;  // reverse multiplier
  private final int a;   // additive constant
  
  
  // length of this table must currently be a power-of-two.
  // each "pair" are connected such that the second "must"
  // be an inverse modulo of the first.
  protected static final int[] mult =
  {
    0xac549d55, 0xfe4677fd,
    0x01c8e815, 0x608fa73d,
    0x01ed0675, 0xd5c019dd,
    0x41c64e6d, 0xeeb9eb65,  
  };
  
  protected static final int mask  = (mult.length>>1)-1;
  protected static final int shift = 32-Integer.numberOfLeadingZeros(mask);
  
  /**
   * Number of multipliers in the set.
   */
  public static int getNumM()
  {
    return mult.length;
  }
  
  protected static final int getAdditive(int set)
  {
    int r;
    
    // drop the bits used to select the multiplier
    r = set >>> shift;
    
    // maybe a weyl generator would be a better choice.
    r = Integer.reverse(r);
    r = r + 1234;
    
    // the return value "must" be odd
    return r | 1;
  }
  
  /**
   * 
   */
  public BidirectionalLCG(int set)
  {
    int t = (set & mask) << 1;
    
    // Set the multiplicative constants.
    mf   = mult[t  ];
    mr   = mult[t+1];
   
    // The addition must be odd.
    a = getAdditive(set);
  }
  
  /**
   * 
   */
  public BidirectionalLCG(int set, int add)
  {
    set = (set & mask) << 1;
    mf  = mult[set  ];
    mr  = mult[set+1];
    a   = add | 1;
  }
  
  
  public final void setState(int seed, boolean forward)
  {
    this.data = seed;
    this.forward = forward;
  }

  /**
   * Returns the next 32-bit integer in the sequence in the
   * current direction.
   * <p>
   * Low order bits have a shorter period than higher order
   * ones.  Therefore using the highest bits possible is
   * desirable.
   */
  @Override
  public final int nextInt()
  {
    // TODO: this can be rewritten to be not
    if (forward)
      data = mf * data + a;
    else
      data = (data - a) * mr;
    
    return data;
  }
  
  /** Toggles the current direction of the sequence. */
  public final boolean flip()
  {
    nextInt();
    forward = !forward;
    
    return forward;
  }
  
  /** Returns true if the sequence is currently moving forward. */
  public final boolean isForward()
  {
    return forward;
  }

  @Override
  public final void setSeed(long seed) {
    data = (int)seed;
  }

  @Override
  public final long getSeed() {
    return data;
  }
}
