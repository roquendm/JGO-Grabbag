package roquen.math.test;

import roquen.math.Float32;

public enum FloatError32
{
  ;

  /**
   * Given 32 uniformly distributed bits 'bit' returns
   * a double uniformly distributed on [-{@link Float#MAX_VALUE}, {@link Float#MAX_VALUE}]
   */
  public static final float finiteDomain(int bits)
  {
    bits &= 0xBFFFFFFF;
    return Float.intBitsToFloat(bits);
  }

  /**
   * Given 32 uniformly distributed bits 'bit' returns
   * a double uniformly distributed on [0, {@link Float#MAX_VALUE}]
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

  // 2^-24 + ulp(2^24)
  private static final float ULP_0 = 0x1.000002p-24f;

  /**
   * Returns the size of the ulp in 'f' if |f| >= 2^-101
   */
  public static strictfp float approxUlp(float f)
  {
    return (f+f*ULP_0)-f;
  }


  /**
   * Checks if a returns result 'r' is with 'n' ulp of the expected 'e'
   * <p>
   * <tt>abs(e-r) <= ulp(e)</tt>
   */
  public static boolean withinULP(float e, float r, float n)
  {
    // ignores the rounding produced by the subtraction
     return Float32.abs(e-r) <= n*Math.ulp(e);
  }

  /**
   * Checks if a returns result 'r' is with 'n' ulp of the expected 'e'
   * <p>
   * <tt>abs(e-r) <= {@link #approxUlp(float) approxUlp(e)}</tt>
   */
  public static boolean withinApproxULP(float e, float r, float n)
  {
    // ignores the rounding produced by the subtraction
     return Float32.abs(e-r) <= n*approxUlp(e);
  }

  /** Returns <tt>sqrt(a<sup>2</sup>+b<sup>2</sup>)</tt> without overflow or underflow. */
  public static final strictfp float pythagoreanSum(float a, float b)
  {
    double da = a;
    double db = b;
    return (float)Math.sqrt(da*da + db*db);
  }

  /**
   *  Returns <tt>sqrt(sum(a[i]<sup>2</sup>)</tt> without intermediate overflow or underflow.
   *  <p>
   *  Explodes if: <tt>a.length < 2</tt>
   */
  public static final float pythagoreanSum(float[] a)
  {
    double da = a[0];
    double db = a[1];
    double s  = Math.sqrt(da*da + db*db);
    int    e  = a.length;

    for(int i=2; i<e; i++)
      s = Math.sqrt(s*s+a[i]*a[i]);

    return (float)s;
  }
}
