package roquen.math.test;

import roquen.math.Float64;

public enum FloatError64 
{
  ;
  
  /** 
   * Given 64 uniformly distributed bits 'bit' returns
   * a double uniformly distributed on [-{@link Double#MAX_VALUE}, {@link Double#MAX_VALUE}]
   */
  public static final double finiteDomain(long bits)
  {
    bits &= 0xBFFFFFFF_FFFFFFFFL;
    return Double.longBitsToDouble(bits);
  }
  
  /** 
   * Given 64 uniformly distributed bits 'bit' returns
   * a double uniformly distributed on [0, {@link Double#MAX_VALUE}]
   */
  public static final double positiveFiniteDomain(long bits)
  {
    bits >>>= 2;
    return Double.longBitsToDouble(bits);
  }
  
  /**
   * Returns the approximate number of ulp's that returned result 'r'
   * is with respect to expected result 'e'
   * <p>
   * <tt>abs(e-r)/ulp(e)</tt>
   */
  public static double ulpDiff(double e, double r)
  {
    // ignores the rounding produced by the subtraction
    return Float64.abs(e-r)/Math.ulp(e);
  }
  
  /**
   * Checks if a returns result 'r' is with 'n' ulp of the expected 'e'
   * <p>
   * <tt>abs(e-r) <= ulp(e)</tt>
   */
  public static boolean withULP(double e, double r, double n)
  {
    // ignores the rounding produced by the subtraction
     return Float64.abs(e-r) <= n*Math.ulp(e);
  }
}
