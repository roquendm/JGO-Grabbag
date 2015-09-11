package roquen.math;

/**
 *
 */
public enum Trig32
{
  ;

  private static final int B_PI  = Float.floatToRawIntBits((float)Math.PI);
  private static final int B_2PI = Float.floatToRawIntBits((float)(2*Math.PI));

  /**
   * Reduces input angle on [-2pi, 2pi] to equivalent value on [-pi,pi]
   */
  /*
  public static final float reducePmPi(float a)
  {
    int ia = Float.floatToRawIntBits(a);
    int aa = ia & 0x7fffffff;

    // if |a| <= pi, then already in output range so return
    if (aa <= B_PI) return a;

    // Sterbenz's theorem: if (x & y >= 0) and (x/2 <= y <= 2x)
    // then: floating point op (y-x) is exact.

    // x = |a|, x on [pi, 2pi]
    // y = 2pi
    // a = 2pi - a, a > 0
    // a =

    float s = Float.intBitsToFloat(aa);// TODO: complete
    float b = (float)(2*Math.PI)-Float.intBitsToFloat(aa);

    //return a;
  }
  */

  /**
   * Reduces input angle on [0, 2pi] to equivalent value on [-pi,pi].
   * <p>
   * The reduction is exact.
   */
  public static final float reduceTwoPi(float a)
  {
    if (a <= (float)Math.PI) return a;

    // Sterbenz theorem show this subtraction is exact
    return (float)(2*Math.PI) - a;
  }


  /**
   * Returns sin(a) given cos(a) and a on [-pi,pi], where
   * 'sa' is any value with same sign as 'a'.
   * <p>
   * computed as: <tt>sign(sa) sqrt(1-cos<sup>2</sup>)</tt>
   */
  public static final float sinPmPi(float cos, float sa)
  {
    // TODO: examine the multiply version as well
    int   sx = Float.floatToRawIntBits(sa) & 0x80000000;
    float r  = (float)Math.sqrt(1.f-cos*cos);
    int   ir = Float.floatToRawIntBits(r) ^ sx;
    return Float.intBitsToFloat(ir);
  }


  /**
   * Returns sin(a) given cos(a) and a on [-pi,pi], where
   * 'sa' is any value with same sign as 'a'.
   * <p>
   * computed as: <tt>sign(sa) sqrt((1-cos)(1+cos))</tt>
   * <p>
   * Result is within 1-ulp of correct
   */

  public static final float sinPmPiHq(float cos, float sa)
  {
    // SEE: sinPmPi - multiply maybe
    int   sx = Float.floatToRawIntBits(sa) & 0x80000000;
    float r  = (float)Math.sqrt((1.f-cos)*(1.f+cos));
    int   ir = Float.floatToRawIntBits(r) ^ sx;
    return Float.intBitsToFloat(ir);
  }

  /**
   * Returns sin(a) given cos(a) and a on [0,pi].
   * <p>
   * computed as: <tt>sqrt(1-cos<sup>2</sup>)</tt>
   * @see {@link #sinPiHq(float)}
   */
  public static final float sinPi(float cos)
  {
    return (float)Math.sqrt(1.f-cos*cos);
  }

  /**
   * Returns sin(a) given cos(a) and a on [0, pi].
   * <p>
   * computed as: <tt>sqrt((1-cos)(1+cos))</tt>
   * <p>
   * Result is within 1-ulp of correct and has
   * the same dependency chain length as
   * {@link #sinPi(float)}
   */
  public static final float sinPiHq(float cos)
  {
    return (float)Math.sqrt((1.f-cos)*(1.f+cos));
  }
}
