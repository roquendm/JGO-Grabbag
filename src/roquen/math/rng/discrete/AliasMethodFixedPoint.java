package roquen.math.rng.discrete;

import roquen.math.rng.PRNG;

/**
 * Example specialized version of {@link AliasMethod}. Flattens
 * the two internal arrays into a one like {@link AliasMethodFlat}
 * and stores probabilities in fixed point.  Drops a conversion
 * to floating point and a multiple in each {@link #nextInt(PRNG)}
 */
public class AliasMethodFixedPoint extends DiscreteMethod
{
  // 'nextInt' could loose the shift on the computation of 'p'
  // by either using unsigned comparisons of JDK8 or by biasing
  // the probability table entries (equivalent to unsigned comparisons).
  // this additionally improves the fixed-point representation by
  // giving it one more bit to work with.
  
  private final int[] data;
  private final int size;

  /** The range of the distribution. */
  public int getSize() {
    return size;
  }
  
  private AliasMethodFixedPoint(int[] d)
  {
    data = d;
    size = data.length >>> 1;
  }
  
  private static AliasMethodFixedPoint make_(double[] w, double sum)
  {
    int len = w.length;
    double scale = len / sum;
    int[] ss = new int[len];
    int[] ls = new int[len];
    
    int   si = 0;
    int   li = 0;

    for (int i = 0; i != len; i++) {
      
      double p = w[i] = w[i] * scale;

      if (p > 1)
        ls[li++] = i;
      else
        ss[si++] = i;
    }

    int[]  data  = new int[len*2];
    
    while (si != 0 && li != 0) {
      int ws = ss[--si];
      int wl = ls[--li];
      int id = ws<<1;
      
      data[id]   = (int)(w[ws]*Integer.MAX_VALUE); // see 'bias' above
      data[id+1] = wl;
      w[wl]      = (w[wl] + w[ws]) - 1;
      
      if (w[wl] > 1)
        ls[li++] = wl;
      else
        ss[si++] = wl;
    }

    while (si != 0) data[ss[--si]<<1] = Integer.MAX_VALUE; // see 'bias' above
    while (li != 0) data[ls[--li]<<1] = Integer.MAX_VALUE; // see 'bias' above
    
    return new AliasMethodFixedPoint(data);
  }

  public static AliasMethodFixedPoint make(double[] w)
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
  
  /** */
  public static AliasMethodFixedPoint make(float[] w)
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

  /** */
  public static AliasMethodFixedPoint make(int[] w)
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
  @Override
  public int nextInt(PRNG rng)
  { 
    int v = rng.nextInt(size);
    int i = v+v;
    int p = rng.nextInt()>>>1;           // see 'bias' above
    return p <= data[i] ? v : data[i+1]; // see 'bias' above
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
      AliasMethodFixedPoint select = make(w);
      
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
