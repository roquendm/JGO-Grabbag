package roquen.math.rng.discrete;

import roquen.math.rng.PRNG;
import roquen.math.rng.XorStar64;


/**
 * 
 * <p>
 * <i>"A Linear Algorithm For Generating Random Numbers With a Given Distribution"</i>, 
 * Michael D. Vose, 1991.
 * <p>
 * @see http://www.keithschwarz.com/darts-dice-coins/
 */
public class AliasMethod 
{
  // merge into a single table
  /** Probability that the original column should be chosen.  */
  private final float[] prob;

  /** The second rectangle */
  private final int[] alias;

  /** */
  public int getSize() {
    return alias.length;
  }

  private AliasMethod(float[] prob, int[] alias)
  {
    this.prob  = prob;
    this.alias = alias;
  }
  
  private static AliasMethod make_(double[] w, double sum)
  {
    int len = w.length;
    double scale = len / sum;
    int[] ss = new int[len];
    int[] ls = new int[len];
    
    int   si = 0;
    int   li = 0;

    // convert weights into scaled probabilities and partition
    // the input into a big and small set.
    for (int i = 0; i != len; i++) {
      
      double p = w[i] = w[i] * scale;

      if (p > 1)
        ls[li++] = i;
      else
        ss[si++] = i;
    }

    float[]  prob  = new float[len];
    int[]    alias = new int[len];
    
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

    while (si != 0)
      prob[ss[--si]] = 1;

    while (li != 0)
      prob[ls[--li]] = 1;
    
    return new AliasMethod(prob, alias);
  }

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

  /** Returns the next value in the distribution. */
  public int nextInt(PRNG random)
  {
    // Deviation from Vose here.  Generate two 
    // uniforms instead of one.
    final int    j = random.nextInt(alias.length);
    final double p = random.nextDouble();
    return p <= prob[j] ? j : alias[j];
  }
  
  
  @SuppressWarnings("boxing")
  public static void main(String[] args)
  {
    XorStar64 rng = new XorStar64();
    
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
  
  
  
}
