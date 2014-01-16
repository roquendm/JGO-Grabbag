package roquen.math;

// http://www.java-gaming.org/topics/atan2/28986/msg/264901/view.html
// http://www.java-gaming.org/topics/exp-log-amp-pow/24240/msg/203361/view.html
  
public final class Float32 {
  
  private Float32() {}

  private static final float PI        = (float)(Math.PI);
  private static final float PI_OVER_2 = (float)(Math.PI/2);
  private static final float PI_OVER_4 = (float)(Math.PI/4);

  /** Return 'x' multiplied by the sign of 'y'. */
  // copySign isn't yet an intrinsic, so this isn't a desirable function to call.
  public static float mulsign(float x, float y) { return (Math.copySign(1, y) * x); }

  /**
   * arc tangent of x on [-1,1]
   * <p>
   * 5th order minimax approximation (minimizing abs error).  Banged out quickly,
   * a better version could be produced.  Error bound of ~0.0006 @
   * x = +/-{0.205219, 0.59347, 0.888196, 1.0}
   */
  public static final float atan_5(float x)
    {
      // quality (of either abs or rel error) can be improved at same cost.
      // Additionally (by using Estrin's) one could probably produce a higher
      // order approximation at same cost (by reducing dependency chains). YMMV.
      float x2 = x*x;
      return x*(0.995354f+x2*(-0.288769f+0.079331f*x2));
    }

  /** */
  public static final float atan(float y, float x)
  {
    float ay = Math.abs(y);
    float xy = x*y; // this is xor of sign of x & y...probably removable.
    float r  = 0;
      
    // perform argument reduction.  probably can be reduced
    if (x  < 0) { x = -x; r = -PI; }
    if (ay > x) { float t = x; x = ay; ay = -t; r += PI_OVER_2; }
      
    // perform the approximation..increasing speed or accuracy by 
    // trading the base approximation possible
    r += atan_5(ay/x);

    // xor the sign of reduced range with the input for final result..
    // again all of this could probably be cleaned up with some thought.
    return mulsign(r, xy);
  }

    /**
     * atan of x on [0,Inf]
     */
    public static final float atanp(float x)
    {
      // ignoring numeric issues around '1' due to the subtract,
      // same error bound as core routine, stretched out over
      // the interval.
      return PI_OVER_4 + atan_5((x-1)/(x+1));
    }
    
    /** atan of x on [-Inf, Inf] */
    public static final float atan(float x)
    {
      float r = atanp(Math.abs(x));
      
      return Math.copySign(r, x);
    }
  
    // 3rd order: 14 good bits
    /**
    private static final float EXP2_0 = 0xf.ff8fcp-4f;
    private static final float EXP2_1 = 0xb.24b09p-4f;
    private static final float EXP2_2 = 0x3.96e3dp-4f;
    private static final float EXP2_3 = 0x1.446bap-4f;
    */
    
    // 4th order: 17 good bits
    /**
    private static final float EXP2_0 = 0x1.00002cp0f;
    private static final float EXP2_1 = 0x1.62d166p-1f;
    private static final float EXP2_2 = 0x1.ee798ap-3f;
    private static final float EXP2_3 = 0x1.aa13fp-5f;
    private static final float EXP2_4 = 0x1.bb7cd4p-7f;
    */

    
    // 5th order: 21 good bits
    private static final float EXP2_0 = 0xf.ffffep-4f;
    private static final float EXP2_1 = 0xb.1729ap-4f;
    private static final float EXP2_2 = 0x3.d79c1p-4f;
    private static final float EXP2_3 = 0xe.4d4cp-8f;
    private static final float EXP2_4 = 0x2.4a14p-8f;
    private static final float EXP2_5 = 0x7.c45p-12f;
   
    
    /**
     * Calculates: 2<sup>x</sup>
     */
   
    public static final float exp2(float x)
    {
      int   a = (int)x;
      float b = x-a;
      float r = 0;
      
    //r = b*(r + EXP2_6);
      r = b*(r + EXP2_5);
      r = b*(r + EXP2_4);
      r = b*(r + EXP2_3);
      r = b*(r + EXP2_2);
      r = b*(r + EXP2_1);
      r =   (r + EXP2_0);

      return exp2(a)*r;
    }
    
    
    // Result accurate to 22 bits (on primary range).
    private static final float LOG2_1 = 0x1.71547ap1f;
    private static final float LOG2_2 = 0x1.ec554ep-1f;
    private static final float LOG2_3 = 0x1.310a2cp-1f;
    
    
    /**
     * Calculates: Log<sub>2</sub> x
     * <p>
     * Accuracy: 22 bits (in normal range)
     */
    
    public static final float log2(float x)
    {
      int   i = Float.floatToRawIntBits(x);
      int   e = ((i >>> 23) & 0xff)-127;
      float y = Float.intBitsToFloat(i - (e<<23));
      float r = (y-sqrtOf2f)/(y+sqrtOf2f);
      float b = r * r;
      float a;
      
      // Approximate via ArcTanh:
      a = r*(LOG2_1 + b*(LOG2_2 + b*LOG2_3));
      
      return 0.5f + e + a;
    }
    
    
    // change to pre-calculated correctly round hex constant
    private static final float oneOverLog2 = (float)(1.0/Math.log(2));
    
    /**
     * Calculates: e<sup>x</sup>
     * <p>
     */
    public static final float exp(float x)
    {
      return exp2(x * oneOverLog2);
    }
    
    /**
     * Calculates: x<sup>y</sup>
     */
    public static final float powf(float x, float y)
    {
      return exp2(y * log2(x));
    }

    /**
     * Calculates: x<sup>n</sup>
     */
    
    public static final float pow(float x, int n)
    {
      float r = 1.f;
      int   e = n;
      
      if (n < 0)
        e = -n;

      do {
        if ((e & 1) != 0) r *= x;
        
        e >>>= 1;
        
        if (e != 0) {
          x *= x;
          continue;
        }
        
        if (n >= 0)
          return r;
        
        return 1/r;
        
      } while(true);
    }
    
    // hastings style
    private static final float sqrtOf2f = (float)Math.sqrt(2);
    

    
    // TODO: change to precalculated exact constant
    private static final float logOf2 = (float)Math.log(2);
    
    /**
     * Calculates: Log x
     */
    public static final float log(float x)
    {
      return log2(x) * logOf2;
    }
    
    // Log[2]/Log[10]
    private static final float log10Convert = 0x1.344136p-2f;
    
    /**
     * Calculates: Log<sub>10</sub> x
     */
    public static final float log10(float x)
    {
      return log2(x) * log10Convert;
    }
    
    /**
     * Calculates: 2<sup>x</sup> for inputs on [-127, 128].
     * <p>
     * The result is exact for valid input values,
     * otherwise the result is undefined.
     */
    public static final float exp2(int x)
    {
      return Float.intBitsToFloat((x+127)<<23);
    }
}
