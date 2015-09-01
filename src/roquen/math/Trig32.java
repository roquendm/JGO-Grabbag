package roquen.math;

public enum Trig32
{
  ;

  /**
   * Returns sin(a) given cos(a) and a on [-pi,pi], where
   * 'sa' is any value with same sign as 'a'.
   * <p>
   * computed as: <tt>sign(sa) sqrt(1-cos<sup>2</sup>)</tt>
   */
  public static final float sinPmPi(float cos, float sa)
  {
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
