package roquen.math.lds;

public final class Sobol3D
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
  public Sobol3D() { d0=d1=d2=i=0; }
  
  /** */
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
  
  /** */
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

  
  /** */
  public final void next(float[] v, int off)
  {
    v[off++] = (d0 >>> 8) * 0x1p-24f;
    v[off++] = (d1 >>> 8) * 0x1p-24f;
    v[off++] = (d2 >>> 8) * 0x1p-24f;
    
    updateState();
  }
  
  /** */
  public final void next(float[] v) { next(v,0); }
  
  public final void next(java.nio.FloatBuffer fb)
  {
    fb.put((d0 >>> 8) * 0x1p-24f);
    fb.put((d1 >>> 8) * 0x1p-24f);
    fb.put((d2 >>> 8) * 0x1p-24f);
    updateState();
  }
}