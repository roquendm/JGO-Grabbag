package roquen.math;

// Implementation notes:
// Passes SmallCrush

/***
 * 32-bit XorShift generator combined with a Weyl.
 * <p>
 *  Period: 2<sup>64</sup>-2<sup>32</sup> ~= 2<sup>64</sup>
 */
public final class XorWow32 extends PRNG32 
{
  /** state data of xorshift */
  private int data;

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

  public XorWow32()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public XorWow32(int seed)
  {
    setSeed(seed);
  }

  public XorWow32(long seed)
  {
    setSeed(seed);
  }

  @Override
  public final void setSeed(long seed)
  {
    int lo = (int)(seed);

    // xorshift state must never be zero.
    if (lo==0) lo = 0x92d68ca2;
    
    data = lo;

    int hi = (int)(seed >>> 32);
      
    data = lo;
    weyl = hi|1; // must be odd
  }


  @Override
  public final long getSeed()
  {
    long r = data;

    r |= weyl << 32;
    
    return r;
  }
  
  @Override
  public final int nextInt()
  {
    data ^= (data <<  13);
    data ^= (data >>> 17);
    data ^= (data <<   5);
    weyl += WEYL;
    
    return data + weyl;
  }
}
