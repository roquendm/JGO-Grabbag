package roquen.math;

/**
 * Some basic math routines for 32-bit floating point.
 * <p>
 * <list>
 * <li>http://www.java-gaming.org/topics/atan2/28986/msg/264901/view.html</li>
 * <li>http://www.java-gaming.org/topics/exp-log-amp-pow/24240/msg/203361/view.html</li>
 * </list>
 */

public enum Float32 {

  ;

  static final int B_ONE = Float.floatToRawIntBits(1.f);

  /**
   * Returns abs(a)
   * <p>
   * This is a branch-free alternate of {@link java.lang.Math#abs(float)}
   */
  public static final float abs(float a)
  {
    return Float.intBitsToFloat((Float.floatToRawIntBits(a) & (-1>>>1)));
  }

  /**
   * Returns 'a' multiplied by the sign of 'b'.
   * <p>
   * equivalent to: {@linkplain java.lang.Math#copySign(float,float) copySign}(1,b)*a
   */
  public static final float mulSign(float a, float b)
  {
    int sb = Float.floatToRawIntBits(b) & 0x80000000;
    int ia = Float.floatToRawIntBits(a) ^ sb;
    return   Float.intBitsToFloat(ia);
  }

  public static int roundToInt(float x)
  {
    // TODO:
    return (int)(x + Math.copySign(0.5f,x));
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
  public static final float min(float a, float b)
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
  public static final float minNum(float a, float b)
  {
    if (b >= a) return a;      // a <= b and neither are NaN
    if (b == b) return b;      // b isn't NaN
    return a;
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
  public static final float max(float a, float b)
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
  public static final float maxNum(float a, float b)
  {
    if (a <= b) return b;      // b >= a and neither are NaN
    if (a == a) return a;      // a isn't NaN
    return b;
  }

  /** returns <i>true</i> if |d| <= epsilon.  If d is NaN will return false. */
  public final static boolean epsilonEquals(float d, float epsilon)
  {
    // if 'd' is NaN, returns false
    return abs(d) <= epsilon;
  }

  // TODO: these should be change to pre-computed correctly
  // rounded hex constants.

  public static final float PI        = 0x3.243f6cp0f; // (float)(Math.PI);
  public static final float PI_OVER_2 = 0x1.921fb6p0f; // (float)(Math.PI/2);
  public static final float PI_OVER_4 = 0xc.90fdbp-4f; // (float)(Math.PI/4);

  public static final float ONE_OVER_LOG_2 = 0x1.715476p0f; // (float)(1.0/Math.log(2));
  public static final float LOG_2 = 0xb.17218p-4f; // (float)Math.log(2);

  /** log(2)/log(10) */
  public static final float LOG_2_OVER_LOG_10 = 0x1.344136p-2f;

  public static final float SQRT_2 = 0x1.6a09e6p0f; //(float)Math.sqrt(2);

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
    // TODO: reduce
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
    return mulSign(r, xy);
  }

  /**
   * atan(y,x), where y >=0 and x >=0.
   * <p>
   * */
  // blah
  public static final float atanpp(float y, float x)
  {
    if (x <= y) return atan_5(y/x);

    return PI_OVER_2 - atan_5(x/y);
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
      //int ix = Float.floatToRawIntBits(x);
      //int ax = 0x7fffffff & ix;
      //int sz = B_ONE | (ix^ax); // 64-bit maybe - mask ix instead
      //x = Float.intBitsToFloat(ax);
      //x = atap(x);
      //x *= sx;

      // TODO: change to local and unfold
      float r = atanp(Math.abs(x));

      return Math.copySign(r, x); // TODO:
    }

    /**
     * <p>
     * NEVER CALL ME
     */
    /*
    public static final float asin(float a)
    {
      // range reduction
      int   ia = Float.floatToRawIntBits(a);
      int   aa = (0x7fffffff & ia);            // |a|
      int   sa = (0x80000000 & ia) | B_ONE;    // a < 0 ? -1 : 1
      float d  = Float.intBitsToFloat(aa);     // |a|
      float m  = Float.intBitsToFloat(sa);     // {-1,1}

      // atan(+,+);
      d = (float)Math.atan2(d, Math.sqrt((1.f+d)*(1.f-d)));

      return m*d;
    }
    */

    // NOTE: simply comment out higher order terms
    // in exp2 to use a lower quality/faster approximation

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
      float r = (y-SQRT_2)/(y+SQRT_2);
      float b = r * r;
      float a;

      // Approximate via ArcTanh:
      a = r*(LOG2_1 + b*(LOG2_2 + b*LOG2_3));

      return 0.5f + e + a;
    }

    /**
     * Calculates: e<sup>x</sup>
     * <p>
     */
    public static final float exp(float x)
    {
      return exp2(x * ONE_OVER_LOG_2);
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

    /**
     * Calculates: Log x
     */
    public static final float log(float x)
    {
      return log2(x) * LOG_2;
    }

    /**
     * Calculates: Log<sub>10</sub> x
     */
    public static final float log10(float x)
    {
      return log2(x) * LOG_2_OVER_LOG_10;
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

    /**
     * Calculates: 2<sup>x</sup> for inputs on [0, 30].
     * <p>
     * The result is exact for valid input values,
     * otherwise the result is undefined.
     */
    public static final float exp2ps(int x)
    {
      return 1<<x;
    }

    /**
     * Calculates: 2<sup>x</sup> for inputs on [-30, 0].
     * <p>
     * The result is exact for valid input values,
     * otherwise the result is undefined.
     */
    public static final float exp2ns(int x)
    {
      return 0x1.p-30f * (1<<(30+x));
    }


    /**
     * Returns the IEEE complaint bit format, converting any negative zero to zero.
     * Does not normalized NaNs. Intended for hashing.
     */
    public static final int toBits(float x)
    {
      return Float.floatToRawIntBits(0.f+x);
    }

    // Everything below here is very questionable in usefulness

    // HotSpot doesn't give access to various SIMD opcode sets
    // rsqrt approximations.

    /**
     * Make an initial guess for 1/sqrt(x) using Chris Lomont's "magic" number
     */
    public static final float rsqrtGuess(float x)
    {
      int   i = 0x5f375a86 - (Float.floatToRawIntBits(x) >>> 1);
      float g = Float.intBitsToFloat(i);
      return g;
    }

    /**
     * Approximate <tt>1/sqrt(x)</tt> from initial guess <tt>g</tt> using
     * 1 step of Newton's method.
     * <p>
     */
    public static final float rsqrt_1(float x, float g)
    {
      float hx = x * 0.5f;
      g  = g*(1.5f-hx*g*g);
      return g;
    }

    /**
     * Performs two Newton's steps.
     * <p>
     * @see #rsqrt_1(float, float)
     */
    public static final float rsqrt_2(float x, float g)
    {
      float hx = x * 0.5f;
      g  = g*(1.5f-hx*g*g);
      g  = g*(1.5f-hx*g*g);
      return g;
    }
}
