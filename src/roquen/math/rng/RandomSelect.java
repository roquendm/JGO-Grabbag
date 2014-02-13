package roquen.math.rng;

import java.util.*;

import roquen.util.FloatPair;
import roquen.util.Generics;

public class RandomSelect<E>
{
  /** cumulative probabilities of the entries */
  private final float[] cdf; 

  /** table of entries */
  private final E[] data;

  /** cdf[median-1] < 1/2 <= cdf[median] */
  private final int median;

  /**
   * <p>
   * This class is not thread-safe.
   */

  public static class Builder<E>
  {
    /** list of weighted entries */
    ArrayList<FloatPair<E>> w = new ArrayList<>();

    // temp storage for init -- not thread safe anyway
    private int median;


    /** Adds an entry. */
    public void add(E data, float weight)
    {
      if (weight > 0) {
        w.add(new FloatPair<>(data, weight));
        return;
      }

      // TODO: warning/error for invalid weight
    }

    // all temp hack
    private float[] init(float[] pr, float s) {
      // temp hacks
      int len = pr.length;
      float[] cdf = new float[len];
      float   curr;
      int max = len - 1;
      curr = cdf[0] = s*pr[0];

      int i = 0;

      // For numeric precision the cumulative probabilities are broken
      // into two regions.  The lower part of the array stores forward
      // up to 1/2.  The variable 'median' notes this position in the
      // array.  The upper part stores backward.

      while (i < max && curr < 0.5) {
        i++;
        cdf[i] = curr = (s*pr[i] + curr);
      }

      median = i;

      cdf[max] = curr = s*pr[max];

      i = max-1;
      
      while (i > median) {
        cdf[i] = curr = (s*pr[i] + curr);
        i--;
      }

      return cdf;
    }

    /** */
    public RandomSelect<E> build()
    { 
      // NOTE: The building process makes no nods to performance
      // this could be greatly improved.
      int len = w.size();
      int i = 0;
      float totalW = 0.f;
      float[] cdf;
      E[]     entries = Generics.newArray(len);

      FloatPair.sort(w);

      float[] p = new float[len];

      for(FloatPair<E> e : w) {
        p[i] = e.value;
        entries[i++] = e.data;
        totalW += e.value;
      }

      totalW = 1.f/totalW;

      cdf = init(p, totalW);

      return new RandomSelect<>(cdf, entries, median);
    }

    /** Reset the builder to empty */
    public void clear()
    {
      w.clear();
    }

  }

  protected RandomSelect(float[] p, E[] v, int m)
  {
    cdf    = p;
    data   = v;
    median = m;
  }

  public static <E> Builder<E> getBuilder()
  {
    return new Builder<>();
  }

  public int size()
  {
    return cdf.length;
  }

  /**
   * Transform uniform value 'u' into the represented distribution.
   */
  public E eval(float u)
  {
    int i, j, k;
    int max = cdf.length - 1;

    // cumulative probabilities are broken into two ranges.
    if (u <= cdf[median]) {

      // TODO: flip
      if (u <= cdf[0])
        return data[0];

      i = 0;
      j = median - 0;

      while (i < j) {
        k = (i + j) >>> 1;
        if (u > cdf[k])
          i = k + 1;
        else
          j = k;
      }
    }
    else {
      u = 1 - u;

      // TODO: flip
      if (u < cdf[max])
        return data[max];

      i = median + 1;
      j = max;

      while (i < j) {
        k = (i + j) >>> 1;
        if (u < cdf[k])
          i = k + 1;
        else
          j = k;
      }
      i--;
    }

    return data[i];
  }

  // debugging aid
  @Override
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    int len = cdf.length;

    sb.append(getClass().getSimpleName());
    sb.append('[');

    for(int i=0; i<len; i++) {
      sb.append('(');
      sb.append(data[i]);
      sb.append(',');
      sb.append(cdf[i]);
      sb.append(')');
      if (i == median) sb.append('|');
    }

    sb.append(']');

    return sb.toString();
  }

  // -----
  public static void main(String[] args)
  {
    Builder<String> builder = RandomSelect.getBuilder();

    builder.add("A",  1);
    builder.add("B",  1);
    builder.add("C",  1);
    builder.add("D", 1);
    builder.add("E",  1);
    builder.add("F",  1);
    builder.add("G",  2);

    RandomSelect<String> select = builder.build();
    System.out.println(select);
    
    java.util.concurrent.ThreadLocalRandom rng = java.util.concurrent.ThreadLocalRandom.current();
    
    // no time based hashing for the default constructor ATM..
    // equal to passing zero (as here).  The first value is
    // then insured to be zero so even a tiny probability entry
    // will always appear first.
    roquen.math.lds.Sobol1D lds = new roquen.math.lds.Sobol1D(0);
    
    for(int i=0; i<10; i++) {
      float p0 = rng.nextFloat();
      float p1 = lds.next();
      String s0 = select.eval(p0);
      String s1 = select.eval(p1);
      System.out.println(s0 + ", " + s1);
    }



  }



}
