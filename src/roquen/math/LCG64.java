package roquen.math;

/***
 * Basic power-of-two 64-bit LCG.
 * References: 
 * <list><li>"Tables of linear congruential generators of different sizes and good lattice structure", Pierre L'Ecuyer, 1999.</li></list>
 */
public final class LCG64 extends PRNG64
{ 
  /** state data */
  private long data;

  public LCG64()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public LCG64(long seed)
  {
    setSeed(seed);
  }

  @Override
  public final void setSeed(long seed)
  {
    data = seed;
  }

  @Override
  public final long getSeed()
  {
    return data;
  }
  
  @Override
  public final long nextLong()
  {
    data = 3935559000370003845L * data + 0x61C8864680b583EBL;
    
    return data;
  }
}
