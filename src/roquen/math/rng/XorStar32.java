package roquen.math.rng;

// Implementation notes:
// SmallCrush failures starting with seeds of popcount=1 
//  (L13,R17,L5, {741103597,1597334677}):
//    1  BirthdaySpacings
// XorWow is performing better on tests.
/***
 * This needs testing.  Both base XorShift choice and multiplier.
 * Currently of no interest.  XorWow32 has same dep-chain length
 * and performs better at testing.
 * 
 * <p>
 * Period: 2<sup>32</sup>-1,
 * <p>
 *  References:<list>
 * <li><i>"Xorshift RNGs"</i>, George Marsaglia, 2003.</li>
 * <li><i>"Tables of linear congruential generators of different sizes and good lattice structure"</i>, Pierre L'Ecuyer, 1999.</li>
 * <li><i>"An experimental exploration of Marsaglia's xorshift generators, scrambled"</i></li>,
 * Sebastiano Vigna, 2014
 */
public final class XorStar32 extends PRNG32
{ 
  /** state data of xorshift */
  private int data;

  public XorStar32()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public XorStar32(int seed)
  {
    setSeed(seed);
  }

  public XorStar32(long seed)
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
  }

  @Override
  public final long getSeed()
  {
    return data;
  }
  
  @Override
  public final int nextInt()
  {
    data ^= (data <<  13);
    data ^= (data >>> 17);
    data ^= (data <<  15);
    return data * 1597334677;
   //return data * 741103597;
  }
}
