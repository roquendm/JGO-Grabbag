package roquen.math.lds;

/**
 * 
 */
public final class Sobol2D extends LDS
{
  // state data
  private int i,d0,d1;

  // direction table
  private static final int[] D;
  
  static {
    D = new int[33];
    
    int c = D[0] = 1 << 31;
    
     for(int i=1; i<32; i++) {
       c = c ^ (c >>> 1);
       D[i] = c;
     }
     
     // TODO: what about -1? index 32
  }
  
  private final void updateState()
  {
    int c = Integer.numberOfTrailingZeros(~i);
    d1 ^= D[c];
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
        d1 ^= D[c];
      }
      d >>>= 1;
      c   += 1;
    }
    
    i = n;    
  }
  
  
  public Sobol2D() 
  {
    long t = System.nanoTime();
    int  m = mix.getAndDecrement();
    int  x = (int)t;
    int  y = (int)(t * 2685821657736338717L);
    
    // TODO: y value sucks...hash
    seed(m^x, m^y); 
  }
  
  public Sobol2D(int seedX, int seedY)
  {
    seed(seedX, seedY);
  }
  
  /** Sets seed values for the two dimensions and resets the stream. */
  public final void seed(int s0, int s1)
  {
    d0 = s0;
    d1 = s1;
    i  = 0;
  }
  
  public final void next(roquen.fake.Vect2f v)
  {
    v.x = (d0 >>> 8) * 0x1p-24f;
    v.y = (d1 >>> 8) * 0x1p-24f;
  }
  
  
  /** Sets the two elements of 'v' starting of 'off' to the next value. */
  public final void next(float[] v, int off)
  {
    v[off++] = (d0 >>> 8) * 0x1p-24f;
    v[off++] = (d1 >>> 8) * 0x1p-24f;
    updateState();
  }
  
  /** Sets the first two elements of 'v' to the next value. */
  public final void next(float[] v) { next(v, 0); }
  
  /** Puts the next value (two elements) into 'fb' */
  public final void next(java.nio.FloatBuffer fb)
  {
    fb.put((d0 >>> 8) * 0x1p-24f);
    fb.put((d1 >>> 8) * 0x1p-24f);
    updateState();
  }
  
  /** Puts the next value (two elements) into 'fb' at the specified offset. */
  public final void next(java.nio.FloatBuffer fb, int off)
  {
    fb.put(off,   (d0 >>> 8) * 0x1p-24f);
    fb.put(off+1, (d1 >>> 8) * 0x1p-24f);
    updateState();
  }
}
