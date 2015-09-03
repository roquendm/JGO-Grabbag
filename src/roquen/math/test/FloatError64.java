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
  public static final double ulpDiff(double e, double r)
  {
    // ignores the rounding produced by the subtraction
    return Float64.abs(e-r)/Math.ulp(e);
  }

  /**
   * Checks if a returns result 'r' is with 'n' ulp of the expected 'e'
   * <p>
   * <tt>abs(e-r) <= ulp(e)</tt>
   */
  public static final boolean withinULP(double e, double r, double n)
  {
    // ignores the rounding produced by the subtraction
     return Float64.abs(e-r) <= n*Math.ulp(e);
  }

  // 2^-53 + ulp(2^-53)
  private static final double ULP_0 = 0x1.0000000000001p-53;

  /**
   * Returns the size of the ulp in 'f' if |f| >= 2^-969
   */
  public static strictfp double approxUlp(double f)
  {
    return (f+f*ULP_0)-f;
  }

  /**
   * Checks if a returns result 'r' is with 'n' ulp of the expected 'e'.
   * <p>
   * <tt>abs(e-r) <= {@link #approxUlp(double) approxUlp(e)}</tt>
   */
  public static boolean withinApproxULP(double e, double r, double n)
  {
    // ignores the rounding produced by the subtraction
     return Float64.abs(e-r) <= n*approxUlp(e);
  }

  /** Returns <tt>sqrt(a<sup>2</sup>+b<sup>2</sup>)</tt> without overflow or underflow. */
  public static final double pythagoreanSum(double a, double b)
  {
    return Math.hypot(a, b);
  }

  /**
   *  Returns <tt>sqrt(sum(a[i]<sup>2</sup>)</tt> without intermediate overflow or underflow.
   *  <p>
   *  Explodes if: <tt>a.length < 2</tt>
   *  <p>
   *  @see Math#hypot(double, double)
   */
  public static final double pythagoreanSum(double[] a)
  {
    double s = Math.hypot(a[0], a[1]);
    int    e = a.length;

    for(int i=2; i<e; i++)
      s = Math.hypot(s, a[i]);

    return s;
  }

  /** Returns <tt>sqrt(a<sup>2</sup>-b<sup>2</sup>)</tt> without intermediate overflow or underflow. */
  public static final double pythagoreanDiff(double a, double b)
  {
    b = b/a;
    return Math.abs(a)*Math.sqrt((1.0-b)*(1.0+b));
  }
}
