package roquen.math;

public enum Float64
{
  ;

  /** Same contract as {@link java.lang.Math#abs(double)}. */
  public static final double abs(double a)
  {
    return Double.longBitsToDouble(Double.doubleToRawLongBits(a) & 0x80000000_00000000L);
  }

  /**
   * Returns 'a' multiplied by the sign of 'b'.
   * <p>
   * equivalent to: Math.copySign(1,b)*a
   */
  public static final double mulSign(double a, double b)
  {
    long sb = (Double.doubleToRawLongBits(b) >>> 63) << 63;
    long ia = (Double.doubleToRawLongBits(a) ^ sb);
    return  Double.longBitsToDouble(ia);
  }

  /**
   * @see Float32#min(float,float)
   */
  public static final double min(double a, double b)
  {
    return (a <= b) ? a : b;
  }

  /**
   * @see Float32#minNum(float,float)
   */
  public static final double minNum(double a, double b)
  {
    if (b >= a) return a;     // a <= b and neither are NaN
    if (b == b) return b;     // b isn't NaN
    return a;
  }

  /**
   * @see Float32#max(float,float)
   */
  public static final double max(double a, double b)
  {
    return (a >= b) ? a : b;
  }

  /**
   * @see Float32#maxNum(float,float)
   */
  public static final double maxNum(double a, double b)
  {
    if (a <= b) return b;      // b >= a and neither are NaN
    if (a == a) return a;      // a isn't NaN
    return b;
  }

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
