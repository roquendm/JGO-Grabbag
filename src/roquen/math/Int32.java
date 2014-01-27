package roquen.math;

public final class Int32 {
  private Int32() {}
  
  /** 
   * Returns 1 for positive, 0 for zero and -1
   * for negative inputs.
   */
  public static final int sign(int a)
  {
    return (-a >>> 31) | (a >> 31);
  }
  
  /** 
   * Returns 1 for input greater than zero, otherwise 0.
   */
  public static final int sgn(int a)
  {
    return (-a >>> 31);
  }
  
  /** ceiling(log2(x)) */
  public static final int ceilingLog2(int x)
  {
    return 32-Integer.numberOfLeadingZeros(x-1);
  }
  
  /** floor(log2(x)) */
  public static final int floorLog2(int x)
  {
    return 31-Integer.numberOfLeadingZeros(x);
  }
  
}
