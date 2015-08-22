package roquen.wiki.complex;

/**
 * Toy complex number implementation to accompany the JGO wiki entry:
 * <a href=http://www.java-gaming.org/topics/complex-number-cookbook/28991/view.html>"Complex Number Cookbook"</a>
 * <link></link>. 
 * <p>
 * Not geared for performance or accuracy.  Values are immutable
 * to simplify the API.
 */
public class C2D
{
  public static final C2D ONE  = new C2D(1,0);
  public static final C2D I    = new C2D(0,1);
  public static final C2D ZERO = new C2D(0,0);
  
  /** 
   */
  public final float x;
  
  /**
   */
  public final float y;
  
  public C2D(float x, float y)
  {
    this.x = x;
    this.y = y;
  }
  
  /**
   * Returns a unit complex with the specified angle (in radians).
   * <p>
   * <code>(cos θ, sin θ)</code>
   */
  public static C2D unit(float a)
  {
    return new C2D((float)Math.cos(a), (float)Math.sin(a));
  }
  
  
  /**
   * Returns the conjugation of input.
   * <p>
   * For unit complex numbers the conjugate is equivalent to inversion (c<sup>-1</sup>) and faster to compute.
   * <code><pre>(x, y)<sup>*</sup> = (x, -y)</pre></code>
   * <p>
   * <code><pre>m(cos θ, sin θ)<sup>*</sup> = m(cos -θ, sin -θ)</pre></code>
   * <p>
   * Partial list of interpretations:
   * <table>
   * <li>Performs a reflection about the line of reals.</li>
   * <li>Negates the angle and maintains magnitude.</li>
   * <table>
   * <table>
   * </table>
   * @see #inv(Complex)
   */
  public C2D conj()
  {
    return c2d(x,-y);
  }
  
  /**
   * Returns 'this' negated.
   * <p>
   * <p>
   */
  public C2D neg()
  {
    return c2d(-x,-y);
  }
  
  
  /**
   * Returns the complex number orthogonal (in a right handed sense) to the input.
   * <p>
   * <code>ortho(x, y) = (-y, x)</code>
   * <p>
   * Equivalent to multiply by <code>(0,1)</code>, so is a Pi/2 (90 degree) 
   * counter-clockwise rotation of the input.
   */
  public C2D ortho()
  {
    return c2d(-y,x);
  }
  
  /**
   * Returns the multiplicative inverse of the input.<code><pre>
   * c<sup>-1</sup> = c/(cc<sup>*</sup>)
   * (x,y)<sup>-1</sup> = (x,-y)/(x<sup>2</sup>+y<sup>2</sup>)
   * (m(cos a, sin a))<sup>-1</sup> = (1/m)(cos -a, sin -a)</pre></code>
   * <p>
   * Inverts the magnitude and negates the angle.
   * <p>
   * Does not deal with overflow or underflow.
   * @see #conj()
   */
  public C2D inv()
  {
    float s = invNormBiased();
    
    // the zero minus is to filter out negative zero results.
    return c2d(s*x, -s*y);
  }
  
  /**
   * Returns this+c
   * <p>
   * Partial list of interpretations:
   * <table>
   * <li>Translation</li>
   * <table>
   */
  public C2D add(C2D z)
  {
    return c2d(x+z.x, y+z.y);
  }
  
  /**
   * Returns this-c
   */
  public C2D sub(C2D z)
  {
    return c2d(x-z.x, y-z.y);
  }
  
  
  /**
   * Returns c-this
   */
  public C2D rsub(C2D z)
  {
    return c2d(z.x-x, z.y-y);
  }
  
  /**
   * Returns this multiplied by the input. Scales the input.
   */
  public C2D mul(float s)
  {
    return c2d(s*x,s*y);
  }
  
  /** 
   * Returns the product with this and the input.
   * <pre><code>
   *  (a,b)(c,d) = (ac-bd,bc+ad)
   *  m(cos a, sin a) n(cos b, sin b) = mn(cos(a+b), sin(a+b))
   *  m(cosθ, sinθ)(x, y) = m(x cosθ - y sinθ, y cosθ + x sinθ)
   * </code></pre>
   * Product of the magnitudes and sums the angles.
   * <p>
   * Partial list of interpretations:
   * <table>
   * <li>Rotation (one is a rotation, other is a coordinate)</li>
   * <li>Composition of two rotations (both represent rotations)
   * <table>
   * @see #mulc(C2D)
   */
  public C2D mul(C2D z) 
  {
    float rx = x*z.x - y*z.y;
    float ry = x*z.y + y*z.x;
    
    return c2d(rx, ry);
  }
  
  /** 
   * Returns the product of this with the conjugate of the input.
   * <p>
   * Specifically the following two statements are the same:<code><pre>
   * c = a.mulc(b);
   * c = a.mul(b.conj());</pre></code>
   * <p>
   * <code><pre>
   *  (a,b)(c,d)<sup>*</sup> = (ac+bd, bc-ad)
   *  m(cos a, sin a) n(cos b, sin b)<sup>*</sup> = mn(cos(a-b), sin(a-b))
   *  AB<sup>*</sup> = (A.B, -AxB) = (A.B, BxA)</pre></code>
   * Product of the magnitudes and subtracts the angles (this-input).
   * <p>
   * Partial list of interpretations:
   * <table>
   * <li>Clockwise rotation (input is a rotation, this is a coordinate)</li>
   * <li>Rotates the input to the line of reals. So the output angle
   * is related to the line of reals in the same way that the angle 'this' is
   * related to the input.</li>
   * <li>Dot and cross product</li>
   * <li>Performs a scaled reflection by a line with half the angle of 'this'
   * through the origin.
   * <table>
   * @see #conj()
   * @see #mul(C2D)
   */
  public C2D mulc(C2D z) 
  {
    float rx = x*z.x + y*z.y;
    float ry = y*z.x - x*z.y;
    
    return c2d(rx, ry);
  }
  
  /**
   * Returns this multiplied by 'a' followed by adding 'b'.
   * <p>
   * Specifically the following statements are the same:<code><pre>
   * a.mul(b).add(c);
   * a.madd(b,c);</pre></code>
   * <p>
   * Partial list of interpretations:
   * <table>
   * <li>Rotation about a point.</li>
   * <li>Rotation about origin plus translation.</li>
   * <table>
   */
  public C2D madd(C2D a, C2D b) 
  {
    float rx = x*a.x - y*a.y;
    float ry = x*a.y + y*a.x;
    
    return c2d(rx+b.x, ry+b.y);
  }
  
  /** 
   * Returns the product of this with the conjugate of 'a' then
   * adds 'b'.
   * <p>
   * Specifically the following statements are the same:<code><pre>
   * x.mulc(y).add(z)
   * x.maddc(y,z)</pre></code>
   * Partial list of interpretations:
   * <table>
   * <li>Reflection about a line.</li>
   * <table>
   * @see #conj()
   * @see #mul(C2D)
   */
  public C2D maddc(C2D a, C2D b) 
  {
    float rx = x*a.x + y*a.y + b.x;
    float ry = y*a.x - x*a.y + b.y;
    
    return c2d(rx, ry);
  }
  
  
  /**
   * Returns this squared.<code><pre>
   * (x,y)<sup>2</sup> = (x<sup>2</sup>-y<sup>2</sup>, 2xy)
   * (m(cos a, sin a))<sup>2</sup> = m<sup>2</sup>(cos 2a, sin 2a)</pre></code>
   * <p>
   * Squares the magnitude and doubles the angle.
   */
  public C2D sq()
  {
    float rx = x*x - y*y;
    float ry = x*y;
    
    return c2d(rx, ry+ry);
  }
  
  /**
   * Returns this (assumed unit) squared.
   * <p>
   * Computes: <code>(1</sup>-2y<sup>2</sup>, 2xy)</code>
   * @see #sq()
   */
  public C2D usq()
  {
    float y2 = y+y;   // 2y
    float yy = y*y2;  // 2yy
    float xy = x*y2;  // 2xy
    
    return c2d(1-yy, xy);
  }
  
  
  /**
   * Returns the square root. This is assumed to be
   * unit magnitude.
   * <p>
   * (cos a, sin a)<sup>0.5</sup> = (cos a/2, sin a/2)
   */
  public C2D usqrt() 
  {
    float m = x+1;
    float rx,ry;

    m  = (float)Math.sqrt(m+m);
    rx = 0.5f * m;
    ry = y/(m + Float.MIN_NORMAL);
    
    return c2d(rx, ry);
  }
  
  /**
   * Returns the inner product.
   * <p>
   * @see #dot(C2D)
   */
  public C2D eip(C2D z)
  {
    return c2d(dot(z), 0);
  }
  
  /**
   * Returns the outer product.
   * <p>
   * @see #cross(C2D)
   */
  public C2D eop(C2D z)
  {
    return c2d(0, cross(z));
  }
  
  /**
   * Returns the inner product as a float.
   * <p>
   * The following code sequences are equivalent:
   * <code><pre>
   * a.dot(b);
   * b.dot(a);
   * a.mulc(b).x;</pre></code>
   * <p>
   * Partial list of interpretations:
   * <table>
   * <li>Parallel projection.</li>
   * <table>
   * @see #mulc(C2D)
   * @see #eip(C2D)
   */
  public float dot(C2D z)
  {
    return x*z.x + y*z.y;
  }

  
  /**
   * Returns the outer product as a float. This is equivalent
   * to the vector cross product in 2D.
   * <p>
   * The following code sequences are equivalent:
   * <p>
   * <code><pre>
   * a.cross(b);
   * -b.cross(a);
   * a.mulc(b).y;</pre></code>
   * <p>
   * Partial list of interpretations:
   * <table>
   * <li>Orthogonal projection.</li>
   * <table>
   * @see #mulc(C2D)
   * @see #eop(C2D)
   */
  public float cross(C2D z)
  {
    return z.y*x - z.x*y;
  }
  
  /**
   * Returns the angle formed with the line of reals
   * (in radians).
   */
  public float angle()
  {
    return (float)Math.atan2(y,x);
  }
  
  /**
   * Returns the inverse of the norm. Result is clamped
   * to a finite result.
   * <p>
   * <code>1/det(x,y) = 1/(x<sup>2</sup>+y<sup>2</sup>)</code>
   * <p>
   * Logically equivalent to:
   * <p>
   * <code>1/(A.A);</code>
   */
  public float invNormBiased()
  {
    return 1/(x*x+y*y+Float.MIN_NORMAL);
  }
  
  /**
   * Returns the magnitude (L<sup>2</sup> norm).
   * <p>
   * <code>magnitude(x,y) = (x<sup>2</sup>+y<sup>2</sup>)<sup>1/2</sup></code>
   * <p>
   * Does not deal with overflow or underflow.
   */
  public float magnitude()
  {
    return (float)Math.sqrt(x*x+y*y);
  }
  
  @Override
  public String toString()
  {
    // adding zero to nuke any negative zeroes from display
    return "(" + 0+x + ", " + 0+y + ")";
  }
  
  public String toHexString()
  {
    return "(" + Float.toHexString(x) + ", " + Float.toHexString(y) + ")";
  }
  
  
  @SuppressWarnings({ "hiding", "static-method" })
  public C2D c2d(float x, float y)
  {
    return new C2D(x,y);
  }
  
  // BELOW HERE: Example code for goofing around
  
  // Reflection examples
  // * These are all for point reflections. For 'bounce' like
  //   reflections the result is negated.
  
  /**
   * Reflects a coordinate (input) about line in direction of 'this'.
   * <p>
   * If we call this L and the input P. Then it reflects P about
   * a line through the origin with the direction of L.
   * <p>
   * Logically performs the following, assuming L is unit:
   * <p>
   * <code>L<sup>2</sup>P<sup>*</sup></code>
   * <p>
   * The actual computation is actually this:
   * <p>
   * <code>L<sup>-1</sup>L<sup>*</sup>P<sup>*</sup> = (L<sup>2</sup>P<sup>*</sup>)/(L.L)</code>
   * <p>
   * Which reflect about L without scaling and doesn't require
   * normalization (i.e. no square root)
   */
  public C2D ref(C2D p)
  {
    // op count: 1 div, 9 mul, 6 add
    float x2 = x*x;
    float y2 = y*y;
    
    // Calculate L*L
    float ry = x*y;
    float rx = x2 - y2;
    
    ry += ry;
    
    // Scale factor to deal with any non-unit L
    // bias prevents division by zero.
    float s  = 1/(x2 + y2 + Float.MIN_NORMAL);
    
    // L*L*conj(P)
    rx = rx*p.x + ry*p.y;
    ry = ry*p.x - rx*p.y;
    
    return c2d(s*rx, s*ry);
  }
  
  /**
   * Reflects a coordinate (input) about line in direction of 'this'.
   * <p>
   * The output is scaled if this is not unit.
   * @see #ref(C2D)
   */
  public C2D sref(C2D p)
  {
    // 7 mul, 4 add
    
    // Calculate L*L
    float ry = x*y;
    float rx = x*x - y*y;
    
    ry += ry;
    
    // L*L*conj(P)
    rx = rx*p.x + ry*p.y;
    ry = ry*p.x - rx*p.y;
    
    return c2d(rx, ry);
  }
  
  
  /**
   * Reflects a coordinate (input) about line in direction of 'this'.
   * <p>
   * This must be unit magnitude.
   */
  public C2D uref(C2D p)
  {
    // op count: 4 mul, 5 add
    float d  = dot(p);
    float rx = x*d;
    float ry = y*d;
    
    rx += rx;
    ry += ry;
    
    return c2d(rx-p.x, ry-p.y);
  }
}
