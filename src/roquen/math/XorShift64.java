package roquen.math;


/*
 * SmallCrush failure: [13,7,17]
 *   8  MatrixRank eps
 * Adding weyl makes it pass all SmallCrush & Crush.
 */

public final class XorShift64 extends PRNG64
{
  private long data;
  
  @Override
  public final long nextLong()
  {
    data ^= (data << 13);
    data ^= (data >>> 7);
    data ^= (data << 17);
    return data;
  }

  @Override
  void setSeed(long seed) {
    data = seed;
  }

  @Override
  long getSeed() {
    return data;
  }
}