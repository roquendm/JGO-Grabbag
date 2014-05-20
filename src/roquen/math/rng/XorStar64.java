package roquen.math.rng;

/***
 * <p>
 * Period: 2<sup>64</sup>-1,
 * <p>
 *  References:<list>
 * <li><i>"Xorshift RNGs"</i>, George Marsaglia, 2003.</li>
 * <li><i>"Tables of linear congruential generators of different sizes and good lattice structure"</i>, Pierre L'Ecuyer, 1999.</li>
 * <li><i>"An experimental exploration of Marsaglia's xorshift generators, scrambled"</i></li>,
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