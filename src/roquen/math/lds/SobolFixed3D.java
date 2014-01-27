package roquen.math.lds;

/**
 * Sobol sequence generator in 3D for a predefined length.
 */
public final class SobolFixed3D
{
  // state data
  private int i,d0,d1;

  // direction table
  private static final int[] D;
  
  private float rlength;
  
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
  
  /** Set the needed length of the sequence */
  public final void setLength(int length)
  {
    rlength = 1.f/length;
  }
  
  public SobolFixed3D(int length)
  {
    setLength(length);
  }
  
  /** Sets seed values for the second and third dimensions and resets the stream. */
  public final void seed(int s0, int s1)
  {
    d0 = s0;
    d1 = s1;
    i  = 0;
  }
  
  /** Sets the three elements of 'v' starting of 'off' to the next value. */
  public final void next(float[] v, int off)
  {
    v[off++] = i * rlength;
    v[off++] = (d0 >>> 8) * 0x1p-24f;
    v[off++] = (d1 >>> 8) * 0x1p-24f;
    updateState();
  }
  
  /** Sets the first three elements of 'v' to the next value. */
  public final void next(float[] v) { next(v, 0); }
  
  /** Puts the next value (two elements) into 'fb' */
  public final void next(java.nio.FloatBuffer fb)
  {
    fb.put(i * rlength);
    fb.put((d0 >>> 8) * 0x1p-24f);
    fb.put((d1 >>> 8) * 0x1p-24f);
    updateState();
  }
}
