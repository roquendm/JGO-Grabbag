package roquen.math;

public enum Float64
{
  ;

  // SEE: Float32 implementations for potentially addition comments

  static final long B_ONE = Double.doubleToRawLongBits(1.f);

  /**
   * This is a branch-free alternate of {@link java.lang.Math#abs(double)}.
   */
  public static final double abs(double a)
  {
    return Double.longBitsToDouble((Double.doubleToRawLongBits(a)<<1)>>>1);
  }

  /**
   * Returns 'a' multiplied by the sign of 'b'.
   * <p>
   * equivalent to: {@linkplain java.lang.Math#copySign(double,double) copySign}(1,b)*a
   */
  public static final double mulSign(double a, double b)
  {
    long sb = (Double.doubleToRawLongBits(b) >>> 63) << 63;
    long ia = (Double.doubleToRawLongBits(a) ^ sb);
    return  Double.longBitsToDouble(ia);
  }

  /**
   * Returns the smallest of the two inputs without special casing for NaNs and -0
   * <pre>
   * min(-0, 0)  = -0
   * min( 0,-0)  =  0
   * min(NaN, x) =  x
   * min(x, NaN) =  NaN
   * </pre>
   */
  public static final double min(double a, double b)
  {
    return (a <= b) ? a : b;
  }

  /**
   * Returns the smaller of 'a' and 'b'.
   * <p>
   * No special casing -0, only returns NaN if both
   * inputs are NaN.
   * <pre>
   * minNum(-0, 0)  = -0
   * minNum( 0,-0)  =  0
   * minNum(NaN, x) =  x
   * minNum(x, NaN) =  x
   * </pre>
   */
  public static final double minNum(double a, double b)
  {
    if (b >= a) return a;     // a <= b and neither are NaN
    if (b == b) return b;     // b isn't NaN
    return a;                 // both are NaN
  }

  /**
   * Returns the largest of the two inputs without special casing for NaNs and -0
   * <pre>
   * max(-0, 0)  = -0
   * max( 0,-0)  =  0
   * max(NaN, x) =  x
   * max(x, NaN) =  NaN
   * </pre>
   */
  public static final double max(double a, double b)
  {
    return (a >= b) ? a : b;
  }

  /**
   * Returns the larger of 'a' and 'b'.
   * <p>
   * No special casing -0, only returns NaN if both
   * inputs are NaN.
   * <pre>
   * maxNum(-0, 0)  =  0
   * maxNum( 0,-0)  = -0
   * maxNum(NaN, x) =  x
   * maxNum(x, NaN) =  x
   * </pre>
   */
  public static final double maxNum(double a, double b)
  {
    if (a <= b) return b;      // b >= a and neither are NaN
    if (a == a) return a;      // a isn't NaN
    return b;
  }

  /**
   * <p>
   * NEVER CALL ME
   */
  /*
  public static final double asin(double a)
  {
    // range reduction
    long   ia = Double.doubleToRawLongBits(a);
    long   aa = (ia<<1)>>>1;
    long   sa = B_ONE | (ia^aa);
    double d  = Double.longBitsToDouble(aa);
    double m  = Double.longBitsToDouble(sa);

    // atan(+,+);
    d = Math.atan2(d, Math.sqrt((1.0+d)*(1.0-d)));

    return m*d;
  }
  */

  // HotSpot doesn't give access to various SIMD opcode sets
  // rsqrt approximations.

  /** Make an initial guess for 1/sqrt(x) using Matthew Robertson's "magic" number */
  public static final double rsqrtGuess(double x)
  {
    long   i = 0x5fe6eb50c7b537a9L - (Double.doubleToRawLongBits(x) >>> 1);
    double g = Double.longBitsToDouble(i);
    return g;
  }

  /**
   * Approximate <tt>1/sqrt(x)</tt> from initial guess <tt>g</tt> using
   * 1 step of Newton's method.
   */
  public static final double rsqrt_1(double x, double g)
  {
    double hx = x * 0.5;
    g  = g*(1.5-hx*g*g);
    return g;
  }

  /**
   * Approximate <tt>1/sqrt(x)</tt> from initial guess <tt>g</tt> using
   * 2 steps of Newton's method.
   */
  public static final double rsqrt_2(double x, double g)
  {
    double hx = x * 0.5;
    g  = g*(1.5-hx*g*g);
    g  = g*(1.5-hx*g*g);
    return g;
  }

  /**
   * Approximate <tt>1/sqrt(x)</tt> from initial guess <tt>g</tt> using
   * 3 step of Newton's method.
   */
  public static final double rsqrt_3(double x, double g)
  {
    double hx = x * 0.5;
    g  = g*(1.5-hx*g*g);
    g  = g*(1.5-hx*g*g);
    g  = g*(1.5-hx*g*g);
    return g;
  }

  /**
   * Returns the IEEE complaint bit format, converting any negative zero to zero.
   * Does not normalized NaNs. Intended for hashing.
   */
  public static final long toBits(double x)
  {
    return Double.doubleToRawLongBits(0.0+x);
  }

}
