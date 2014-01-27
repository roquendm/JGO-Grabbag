package roquen.math.lds;

/**
 * Sobol sequence generator in 2D for a predefined length.
 */
public final class SobolFixed2D
{
  // state data
  private int i,d0;
 
  private float rlength;
  
  private final void updateState()
  {
    d0 ^= 0x80000000 >>> Integer.numberOfTrailingZeros(~i);
    i  += 1;    
  }
  
  /** Set the needed length of the sequence */
  public final void setLength(int length)
  {
    rlength = 1.f/length;
  }
  
  /** Return current index into the stream. */
  public final int getPos() { return i; }
  
  public SobolFixed2D(int length)
  {
    setLength(length);
  }
  
  /** Sets seed value for the second dimension and resets the stream. */
  public final void seed(int s0)
  {
    d0 = s0;
    i  = 0;
  }
  
  /** Sets the two elements of 'v' starting of 'off' to the next value. */
  public final void next(float[] v, int off)
  {
    v[off++] = i * rlength;
    v[off++] = (d0 >>> 8) * 0x1p-24f;
    updateState();
  }
  
  /** Sets the first two elements of 'v' to the next value. */
  public final void next(float[] v) { next(v, 0); }
  
  /** Puts the next value (two elements) into 'fb' */
  public final void next(java.nio.FloatBuffer fb)
  {
    fb.put(i * rlength);
    fb.put((d0 >>> 8) * 0x1p-24f);
    updateState();
  }
}
