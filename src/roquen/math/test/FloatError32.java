package roquen.math.test;

import roquen.math.Float32;

public enum FloatError32
{
  ;
  
  /** 
   * Given 64 uniformly distributed bits 'bit' returns
   * a double uniformly distributed on [-{@link Double#MAX_VALUE}, {@link Double#MAX_VALUE}]
   */
  public static final float finiteDomain(int bits)
  {
    bits &= 0xBFFFFFFF;
    return Float.intBitsToFloat(bits);
  }
  
  /** 
   * Given 64 uniformly distributed bits 'bit' returns
   * a double uniformly distributed on [0, {@link Double#MAX_VALUE}]
   */
  public static final float positiveFiniteDomain(int bits)
  {
    bits >>>= 2;
    return Float.intBitsToFloat(bits);
  }
  
  /**
   * Returns the approximate number of ulp's that returned result 'r'
   * is with respect to expected result 'e'
   * <p>
   * <tt>abs(e-r)/ulp(e)</tt>
   */
  public static float ulpDiff(float e, float r)
  {
    // ignores the rounding produced by the subtraction
    return Float32.abs(e-r)/Math.ulp(e);
  }
  
  /**
   * Checks if a returns result 'r' is with 'n' ulp of the expected 'e'
   * <p>
   * <tt>abs(e-r) <= ulp(e)</tt>
   */
  public static boolean withULP(float e, float r, float n)
  {
    // ignores the rounding produced by the subtraction
     return Float32.abs(e-r) <= n*Math.ulp(e);
  }
  
  // complete
  private static int significantBits(float e, float r)
  {
    int ie = Float.floatToRawIntBits(e);
    int ir = Float.floatToRawIntBits(r);
    int t  = ie ^ ir;
    
    if (t > 0) {
      //
    } else if (t == 0) return 24;
    
    
    return 0;
  }
}
