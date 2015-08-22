package roquen.math;

public enum Float64
{
  ;
  
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
    double t = a+b;
    
    if (t == t)
       return (a >= b) ? a : b;
    
    if (a == a) return a;
    
    return b;
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
