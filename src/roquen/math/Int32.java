package roquen.math;

public final class Int32 {
  private Int32() {}
  
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
