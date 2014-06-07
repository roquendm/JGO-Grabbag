package roquen.math.rng.discrete;

import roquen.math.rng.PRNG;

/**
 * An instance of this class represents a fixed arbitrary discrete
 * probability distribution function via Vose's alias method.  
 * <p>
 * Specifically a set of 'n' weights are provided at generation
 * time.  These values are converted into probabilities and an
 * efficient representation is created to return a random integer
 * on the range [0,n) with these probabilities.  Generating the
 * data tables requires linear time and once constructed performing
 * a query is constant time.  Storage cost is 2n.
 * <p>
 * <p>
 * <i>"A Linear Algorithm For Generating Random Numbers With a Given Distribution"</i>, 
 * Michael D. Vose, 1991.
 * <p>
 * @see http://www.keithschwarz.com/darts-dice-coins/
 */
public class AliasMethod extends DiscreteMethod
{
  // NOTES: If you've found this class by a web-search then
  // you're probably doing scientific computation and you
  // need to modify it. Notably change 'prob' to a more
  // appropriate type and matching PRNG generation in
  // 'nextInt'.
  
  /** Probability that the original column should be chosen.  */
  private final float[] prob;

  /** The second rectangle */
  private final int[] alias;

  /** The range of the distribution. */
  public int getSize() {
    return alias.length;
  }

  private AliasMethod(float[] prob, int[] alias)
  {
    this.prob  = prob;
    this.alias = alias;
  }
  
  /** Core routine for building the tables. This could be optimized. */
  private static AliasMethod make_(double[] w, double sum)
  {
    int len = w.length;
    double scale = len / sum;
    int[] ss = new int[len];
    int[] ls = new int[len];
    
    int   si = 0;
    int   li = 0;

    // convert weights into scaled probabilities and partition
    // the input into a big and small set. The temp arrays
    // can be eliminated.
    for (int i = 0; i != len; i++) {
      
      double p = w[i] = w[i] * scale;

      if (p > 1)
        ls[li++] = i;
      else
        ss[si++] = i;
    }

    float[]  prob  = new float[len];
    int[]    alias = new int[len];
    
    // construct the probability & alias tables. SEE: paper
    // or above provided linked.
    while (si != 0 && li != 0) {
      int ws = ss[--si];
      int wl = ls[--li];
      prob[ws]  = (float)w[ws];
      alias[ws] = wl;
      w[wl]    = (w[wl] + w[ws]) - 1;
      
      if (w[wl] > 1)
        ls[li++] = wl;
      else
        ss[si++] = wl;
    }

    // if either/both of list are not empty then
    // fill the associated prob with insured selection
    // (no alias for that slot).
    while (si != 0) prob[ss[--si]] = 1;
    while (li != 0) prob[ls[--li]] = 1;
    
    return new AliasMethod(prob, alias);
  }

  /**
   * Returns an AliasMethod instance which represents
   * the specified weights.
   * <p>
   */
  public static AliasMethod make(double[] w)
  {
    int      len = w.length;
    double   sum = 0;
    double[] sa  = new double[len];

    // Compute the total weight of the input set and
    // copy the original array.
    for (int i = 0; i != len; i++) {
      double wi = w[i];
      
      if (wi < 0)
        throw new IllegalArgumentException("AliasMethod: negative weight");
      
      sa[i] = wi;
      sum  += wi;
    }

    return make_(sa, sum);
  }
  
  /** @see #make(double[]) */
  public static AliasMethod make(float[] w)
  {
    int      len = w.length;
    double   sum = 0;
    double[] sa  = new double[len];

    // Compute the total weight of the input set and
    // copy the original array.
    for (int i = 0; i != len; i++) {
      double wi = w[i];
      
      if (wi < 0)
        throw new IllegalArgumentException("AliasMethod: negative weight");
      
      sa[i] = wi;
      sum  += wi;
    }

    return make_(sa, sum);
  }

  /** @see #make(double[]) */
  public static AliasMethod make(int[] w)
  {
    int      len = w.length;
    double   sum = 0;
    double[] sa  = new double[len];

    // Compute the total weight of the input set and
    // copy the original array.
    for (int i = 0; i != len; i++) {
      double wi = w[i];
      
      if (wi < 0)
        throw new IllegalArgumentException("AliasMethod: negative weight");
      
      sa[i] = wi;
      sum  += wi;
    }

    return make_(sa, sum);
  }
  
  
  /** 
   * Returns the next value in the distribution.
   * <p>
   * The result is on [0, {@link #getSize()})
   * <p>
   * Requires generating two uniform samples from
   * 'rng' and one or two table lookups and
   * completes in constant time.
   */
  @Override
  public int nextInt(PRNG rng)
  {
    // Vose's version
 //   double p = alias.length*rng.nextDouble();
 //   int    i = (int)p;
 //   return p-i <= prob[i] ? i : alias[i];
    
    // Deviation from Vose here.  Generate two 
    // uniforms instead of one.
    int   i = rng.nextIntFast(alias.length);
    float p = rng.nextFloat(); 
    return p <= prob[i] ? i : alias[i];
  }
  
  /*
  @SuppressWarnings("boxing")
  public static void main(String[] args)
  {  
    roquen.math.rng.XorStar64 rng = new roquen.math.rng.XorStar64(1);
    
    for(int t=0; t<13; t++) {
      int len = rng.nextInt(5)+2;
      double[] w = new double[len];
      int[]    h = new int[len];
      
      double sum = 0;
      for(int i=0;i<len;i++) {
        w[i] = rng.nextDouble();
        sum += w[i];
      }
      
      double isum = 1.0/sum;
      for(int i=0;i<len;i++)
        System.out.printf("%f, ", isum*w[i]);
      
      System.out.println();
      
      final int trials = 1000000;
      AliasMethod select = make(w);
      
      for(int i=0; i<trials; i++) {
        h[select.nextInt(rng)]++;
      }
      
      double itrials = 1.0/trials;
      for(int i=0;i<len;i++) {
        System.out.printf("%f, ",h[i]*itrials);
      }
      
      System.out.println();
      
      for(int i=0;i<len;i++) {
        double d = Math.abs(isum*w[i]- h[i]*itrials);
        
        if (d < 0.001)
          System.out.printf("%f, ", d);
        else
          System.out.printf("*%f*, ", d);
      }
      
      System.out.println();System.out.println();
    }
  }
  */
}
