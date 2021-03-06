package roquen.math.rng;


/* Implementation notes:
 * SmallCrush failure: [13,7,17]
 *   8  MatrixRank eps
 */

/***
 * Basic XorShift (64-bit) generator.
 * <p>
 * Period: 2<sup>64</sup>-1,
 * <p>
 */

public final class XorShift64 extends PRNG64
{
  private long data;
  
  public XorShift64()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public XorShift64(long seed)
  {
    setSeed(seed);
  } 
  
  @Override
  public final long nextLong()
  {
    data ^= (data << 13);
    data ^= (data >>> 7);
    data ^= (data << 17);
    return data;
  }

  @Override
  public void setSeed(long seed) {
    if (seed == 0) seed = Long.MIN_VALUE;
    data = seed;
  }

  @Override
  public long getSeed() {
    return data;
  }
}