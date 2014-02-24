package roquen.math.lds;

/**
 * 
 */
public final class Sobol1D extends LDS
{
  // state data
  private int i,d0;
  
  private final void updateState()
  {
    int c = Integer.numberOfTrailingZeros(~i);
    d0 ^= 0x80000000 >>> c;
    i  += 1;    
  }
  
  /** Return current index into the stream. */
  public final int getPos() { return i; }
  
  /** Move 'num' positions forward or backward in the stream. */
  public final void seek(int num)
  {
    int n = i + num;
    int a = i ^ (i >>> 1);
    int b = n ^ (n >>> 1);
    int d = a ^ b;
    
    int c = 0;
    
    while(d != 0) {
      if ((d & 1) != 0) {
        d0 ^= 0x80000000 >>> c;
      }
      d >>>= 1;
      c   += 1;
    }
    
    i = n;    
  }
  
  public Sobol1D() { seed((int)(mix.getAndDecrement() ^ System.nanoTime())); }
  
  public Sobol1D(int seed)
  {
    seed(seed);
  }
  
  /** Sets the seed and resets the stream. */
  public final void seed(int s0)
  {
    d0 = s0;
    i  = 0;
  }
  
  /** Returns the next value in the sequence. */
  public final float next()
  {
    float r = (d0 >>> 8) * 0x1p-24f;
    updateState();
    return r;
  }
}
