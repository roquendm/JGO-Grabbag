package roquen.math;

/**
*
*/
public enum Trig64
{
  ;

  // SEE: the same Trig32 implementation for potentially more information

  /**
   * Reduces input angle on [0, 2pi] to equivalent value on [-pi,pi].
   * <p>
   * The reduction is exact.
   */
  public static final double reduceTwoPi(double a)
  {
    if (a <= Math.PI) return a;
    return 2*Math.PI - a;
  }


  /**
   * Returns sin(a) given cos(a) and a on [-pi,pi].
   * <p>
   * computed as: <tt>sign(sa) sqrt(1-cos<sup>2</sup>)</tt>
   */
  public static final double sinPmPi(double cos, double sa)
  {
    long   sx = (Double.doubleToRawLongBits(sa) >>> 63) << 63;
    double r  = Math.sqrt(1.0-cos*cos);
    long   ir = Double.doubleToRawLongBits(r) ^ sx;
    return  Double.longBitsToDouble(ir);
  }

  /**
   * Returns sin(a) given cos(a) and a on [-pi,pi].
   * <p>
   * computed as: <tt>sign(sa) sqrt((1-cos)(1+cos))</tt>
   * <p>
   * Result is within 1-ulp of correct
   */
  public static final double sinPmPiHq(double cos, double sa)
  {
    long   sx = (Double.doubleToRawLongBits(sa) >>> 63) << 63;
    double r  = Math.sqrt((1.0-cos)*(1.0+cos));
    long   ir = Double.doubleToRawLongBits(r) ^ sx;
    return  Double.longBitsToDouble(ir);
  }

  /**
   * Returns sin(a) given cos(a) and a on [0,pi].
   * <p>
   * computed as: <tt>sqrt(1-cos<sup>2</sup>)</tt>
   * @see {@link #sinPiHq(double)}
   */
  public static final double sinPi(double cos)
  {
    return Math.sqrt(1.0-cos*cos);
  }

  /**
   * Returns sin(a) given cos(a) and a on [0,pi].
   * <p>
   * computed as: <tt>sqrt((1-cos)(1+cos))</tt>
   * <p>
   * Result is within 1-ulp of correct and has
   * the same dependency chain length as {@link #sinPi(double)}
   */
  public static final double sinPiHq(double cos)
  {
    return Math.sqrt((1.0-cos)*(1.0+cos));
  }
}
