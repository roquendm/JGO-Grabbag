package roquen.math;

public enum Trig32 
{
  ;

  /**
   * Returns sin(a) given cos(a) and a on [-pi,pi].
   * <p>
   * computed as: sign(sa) sqrt(1-cos<sup>2</sup>)
   */
  public static final float sinPt0(float cos, float sa)
  {
    int   sx = Float.floatToRawIntBits(sa) & 0x80000000;
    float r  = (float)Math.sqrt(1.f-cos*cos);
    int   ir = Float.floatToRawIntBits(r) ^ sx;
    return Float.intBitsToFloat(ir);
  }
  
  /**
   * Returns sin(a) given cos(a) and a on [-pi,pi].
   * <p>
   * computed as: sign(sa) sqrt((1-cos)(1+cos))
   */
  public static final float sinPt1(float cos, float sa)
  {
    int   sx = Float.floatToRawIntBits(sa) & 0x80000000;
    float r  = (float)Math.sqrt((1.f-cos)*(1.f+cos));
    int   ir = Float.floatToRawIntBits(r) ^ sx;
    return Float.intBitsToFloat(ir);
  }
  
}
