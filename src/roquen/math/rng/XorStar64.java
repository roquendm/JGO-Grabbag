package roquen.math.rng;

/***
 * <p>
 * <i>"An experimental exploration of Marsaglia's xorshift generators, scrambled"</i>,
 * Sebastiano Vigna, 2014
 */

public final class XorStar64 extends PRNG64
{
  private long data;
  
  public XorStar64()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public XorStar64(long seed)
  {
    setSeed(seed);
  } 
  
  @Override
  public final long nextLong()
  {
    data ^= (data >>> 12);
    data ^= (data <<  25);
    data ^= (data >>> 27);
    return data * 2685821657736338717L;
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