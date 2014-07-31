package roquen.math.rng;

/***
 * <p>
 * Period: 2<sup>128</sup>-1,
 * <p>
 * This generator requires 128 bits of state data.  So one
 * cannot retrieve and restore complete state via {@link #getSeed()}.
 * The second half of the state needs to be retrieved via {@link #getSeed2()}
 * and use {@link #setSeed(long, long)} to reset the stream to a given
 * position.
 * <p>
 *  References:<list>
 * <li><i>"Xorshift RNGs"</i>, George Marsaglia, 2003.</li>
 * <li><i>"Tables of linear congruential generators of different sizes and good lattice structure"</i>, Pierre L'Ecuyer, 1999.</li>
 * <li><i>"An experimental exploration of Marsaglia's xorshift generators, scrambled"</i></li>,
 * Sebastiano Vigna, 2014
 * <li><i>"Further scramblings of Marsaglia’s xorshift generators"</i>, Sebastiano Vigna, 2014
 * </list>
 */

public final class XorPlus128 extends PRNG64
{
  private long data0;
  private long data1;
  
  public XorPlus128()
  {
    setSeed(mix.getAndDecrement(), System.nanoTime());
  }

  public XorPlus128(long seed)
  {
    setSeed(seed);
  } 
  
  public XorPlus128(long seed0, long seed1)
  {
    setSeed(seed0, seed1);
  } 
  
  @Override
  public final long nextLong()
  {
    long s1 = data0;
    long s0 = data1;
    
    data0  = s0;
    s1    ^= s1 << 23;
    data1  = (s1 ^ s0 ^ (s1 >>> 17) ^ (s0 >>> 26)) + s0;
   
    return data1;
  }

  @Override
  public final void setSeed(long seed) 
  {
    setSeed(seed, 0x61C8864680b583EBL);
  }
  
  public final void setSeed(long seed0, long seed1) 
  {
    if (seed0 == 0) seed0 = Long.MIN_VALUE;
    data0 = seed0;
    data1 = seed1;
  }

  @Override
  public final long getSeed()
  {
    return data0;
  }
  
  public final long getSeed2()
  {
    return data1;
  }
}