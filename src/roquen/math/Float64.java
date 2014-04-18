package roquen.math;

public enum Float64
{
  ;
  
  /**
   * Returns the IEEE complaint bit format, converting any negative zero to zero.
   * Does not normalized NaNs. Intended for hashing.
   */
  public static final long toBits(double x)
  {
    return Double.doubleToRawLongBits(0.0+x);
  }
}
