package roquen.math;


/** 
 * XorShift + Weyl generator with period ~2<sup>128</sup>.
 * <p>
 * Passes all of SmallCrush and Crush.
 */
public final class XorWow64 extends PRNG64
{
  private long data;
  private long weyl;
  
  private static final long WEYL = 0x61C8864680b583EBL;
  
  public XorWow64()
  {
    setSeed(mix.getAndDecrement(), System.nanoTime());
  }

  public XorWow64(long data1, long data2)
  {
    setSeed(data1, data2);
  }
  
  @Override
  public final long nextLong()
  {
    data ^= (data << 13);
    data ^= (data >>> 7);
    data ^= (data << 17);
    weyl += WEYL;
    
    // NOTE: other mixes are possible. See XorWow32.
    return data + weyl;
  }

  @Override
  void setSeed(long seed) {
    data = seed;
    weyl = 0x5db3d743;
  }

  void setSeed(long data1, long data2)
  {
    assert(data1 != 0);
    data = data1;
    weyl = data2;
  }
  
  @Override
  long getSeed() {
    return data;
  }
  
  long getSeed2() { return weyl; }
}