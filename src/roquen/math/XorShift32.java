package roquen.math;

// Implementation notes:
// SmallCrush failures [13,17,5]:
//    1  BirthdaySpacings    eps  
//    2  Collision           1 - eps1
//    6  MaxOft              6.7e-16
//    8  MatrixRank          eps  
//   10  RandomWalk1 H       5.7e-7

/***
 * Basic XorShift (32-bit) generator.
 * <p>
 * Period: 2<sup>32</sup>-1,
 * <p>
 * References: 
 * <list><li>"Xorshift RNGs", George Marsaglia, 2003.</li>
 * <li>"Some long-period random number generators
 * using shifts and xors", Richard P. Brent, 2006.</li>
 * <li>"On the Xorshift Random Number Generators", Francois Panneton and Pierre L’Ecuyer</li></list>
 */
public final class XorShift32 extends PRNG32
{ 
  /** state data of xorshift */
  private int data;

  public XorShift32()
  {
    setSeed((mix.getAndDecrement() ^ System.nanoTime()));
  }

  public XorShift32(int seed)
  {
    setSeed(seed);
  }

  public XorShift32(long seed)
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
    data ^= (data <<   5);
    return data;
  }
}
