package roquen.math.lds;

/**
 * 
 */
public final class Sobol3D extends LDS
{
  // state data for the sequence
  private int i,d0,d1,d2;

  // direction table
  private static final int[] D;
  
  // build the direction tables
  static {
    // TODO: 'i' = -1 is not handled
    
    D = new int[65];

    // second dimension
    int c = 1 << 31;
    
    D[0] = c;
    
     for(int i=2; i<64; i+=2) {
       c = c ^ (c >>> 1);
       D[i] = c;
     }
     
     // third dimension
     int s0,s1,t;
     
     s0 = D[1] = D[0];
     s1 = D[3] = D[2];
     
     for (int i=4; i<64; i+=2) {
       t   = s1;
       s1 ^= s0 ^ (s0 >>> 2);
       s0  = t;
       
       D[i+1] = s1;
     }
  }
  
  /** */
  public Sobol3D() {
    long t = System.nanoTime();
    int  m = mix.getAndDecrement();
    int  x = (int)t;
    int  y = (int)(t >> 32);
    int  z = (int)(t * 2685821657736338717L);
    
    // TODO: y&z values sucks...hash
    seed(m^x, m^y, m^z); 
  }
  
  /** Sets seed values for the three dimensions and resets the stream. */
  public final void seed(int s0, int s1, int s2)
  {
    d0 = s0;
    d1 = s1;
    d2 = s2;
    i  = 0;
  }

  /** Return current index into the stream. */
  public final int getPos() { return i; }
  
  /** */
  private final void updateState()
  {
    int c = Integer.numberOfTrailingZeros(~i);
    int o = c+c;
    
    d0 ^= 0x80000000 >>> c;
    d1 ^= D[o];
    d2 ^= D[o+1];
    i  += 1;
  }
  
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
        d1 ^= D[c+c];
        d2 ^= D[c+c+1];
      }
      
      d >>>= 1;
      c   += 1;
    }
    
    i = n;    
  }

  public final void next(roquen.fake.Vect3f v)
  {
    v.x = (d0 >>> 8) * 0x1p-24f;
    v.y = (d1 >>> 8) * 0x1p-24f;
    v.z = (d2 >>> 8) * 0x1p-24f;
  }
  
  /** Sets the three elements of 'v' starting of 'off' to the next value. */
  public final void next(float[] v, int off)
  {
    v[off++] = (d0 >>> 8) * 0x1p-24f;
    v[off++] = (d1 >>> 8) * 0x1p-24f;
    v[off++] = (d2 >>> 8) * 0x1p-24f;
    
    updateState();
  }
  
  /** Sets the first three elements of 'v' to the next value. */
  public final void next(float[] v) { next(v,0); }
  
  /** Puts the next value (three elements) into 'fb' */
  public final void next(java.nio.FloatBuffer fb)
  {
    fb.put((d0 >>> 8) * 0x1p-24f);
    fb.put((d1 >>> 8) * 0x1p-24f);
    fb.put((d2 >>> 8) * 0x1p-24f);
    updateState();
  }
  
  /** Puts the next value (three elements) into 'fb' at the specified offset. */
  public final void next(java.nio.FloatBuffer fb, int off)
  {
    fb.put(off++, (d0 >>> 8) * 0x1p-24f);
    fb.put(off++, (d1 >>> 8) * 0x1p-24f);
    fb.put(off,   (d2 >>> 8) * 0x1p-24f);
    updateState();
  }
}