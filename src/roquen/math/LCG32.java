package roquen.math;

/***
 * Basic power-of-two 32-bit LCG.
 * References: 
 * <list><li>"Tables of linear congruential generators of different sizes and good lattice structure", Pierre L'Ecuyer, 1999.</li></list>
 */
public final class LCG32 extends PRNG32
{ 
  /** state data */
  private int data;

  public LCG32()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public LCG32(int seed)
  {
    setSeed(seed);
  }

  public LCG32(long seed)
  {
    setSeed(seed);
  }

  @Override
  public final void setSeed(long seed)
  {
    data = (int)seed;
  }

  @Override
  public final long getSeed()
  {
    return data;
  }
  
  @Override
  public final int nextInt()
  {
    data = 0xac549d55 * data + 0x61c88647;
    
    return data;
  }
}
