package roquen.math;


/***
 * 64-bit XorShift generator combined with a Weyl.
 * <p>
 *  Period: 2<sup>128</sup>-2<sup>64</sup> ~= 2<sup>128</sup>
 * <p>
 * This generator requires 128 bits of state data.  So one
 * cannot retrieve and restore complete state via {@link #getSeed()}.
 * The second half of the state needs to be retrieved via {@link #getSeed2()}
 * and use {@link #setSeed(long, long)} to reset the stream to a given
 * position.
 */
public final class XorWow64 extends PRNG64
{
  private long data;
  private long weyl;
  
  /** Same constant as XorWow32, just scaled to 64-bits */
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
    data = seed | 1;
    weyl = 0x5db3d743 ^ (seed & 1);
  }

  void setSeed(long data1, long data2)
  {
    if (data1 == 0) data1 = Long.MIN_VALUE;
    data = data1;
    weyl = data2;
  }
  
  @Override
  long getSeed() {
    return data;
  }
  
  long getSeed2() { return weyl; }
}