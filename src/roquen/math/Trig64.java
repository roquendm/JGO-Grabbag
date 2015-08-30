package roquen.math;

public enum Trig64 
{
  ;
  
  /**
   * Returns sin(a) given cos(a) and x on [-pi,pi].
   * <p>
   * computed as: sign(sa) sqrt(1-cos<sup>2</sup>)
   */
  public static final double sinPt0(double cos, double a)
  {
    long   sx = (Double.doubleToRawLongBits(a) >>> 63) << 63;
    double r  = Math.sqrt(1.f-cos*cos);
    long   ir = Double.doubleToRawLongBits(r) ^ sx;
    return  Double.longBitsToDouble(ir);
  }
  
  /**
   * Returns sin(a) given cos(a) and x on [-pi,pi].
   * <p>
   * computed as: sign(sa) sqrt((1-cos)(1+cos))
   */
  public static final double sinPt1(double cos, double a)
  {
    long   sx = (Double.doubleToRawLongBits(a) >>> 63) << 63;
    double r  = Math.sqrt(1.f-cos*cos);
    long   ir = Double.doubleToRawLongBits(r) ^ sx;
    return  Double.longBitsToDouble(ir);
  }
}
