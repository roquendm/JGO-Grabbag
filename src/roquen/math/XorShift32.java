package roquen.math;



// Implementation notes:
//  Without Weyl generator: SmallCrush failures [13,17,5]:
//    1  BirthdaySpacings    eps  
//    2  Collision           1 - eps1
//    6  MaxOft              6.7e-16
//    8  MatrixRank          eps  
//   10  RandomWalk1 H       5.7e-7
// Adding in the counter will make it pass all
// SmallCrush tests.  You don't care. I promise.

/***
 * Basic XorShift (32-bit) generator.  This specific generator
 * predates generalized XorShifts and was previously called SHR3.
 * (This is one component of the once popular KISS generator)
 * The default configuration has a period of 2<sup>32</sup>-1,
 * although paranoid programmer may have extended it by adding
 * a Weyl generator (making it an XorWOW) with a period of 
 * 2<sup>64</sup>-2<sup>32</sup> ~= 2<sup>64</sup>
 * <p>
 * All ranges are shown in American notation: square bracket
 * is inclusive and paren is exclusive.
 * <p>
 * References: 
 * <list><li>"Xorshift RNGs", George Marsaglia, 2003.</li>
 * <li>"Some long-period random number generators
 * using shifts and xors", Richard P. Brent, 2006.</li></list>
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
