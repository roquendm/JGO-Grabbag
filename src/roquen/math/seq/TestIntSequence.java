package roquen.math.seq;

/**
 * Integer array filling and modifying routines for testing. It uses a
 * static instance of a PRNG and therefore shouldn't be used concurrently
 * by multiple threads.
 */
public enum TestIntSequence
{
  ;
  private static roquen.math.rng.XorStar64 rng = new roquen.math.rng.XorStar64();
  
  public static long getSeed() { return rng.getSeed(); }
  public static void setSeed(long seed) { rng.setSeed(seed); }
  
  //
  
  /**
   * Modify <tt>data</tt> using a uniform distribution.
   * <p>
   * if <tt>update</tt> is <i>false</i> then logically:
   * <code>data[i] = i</code> initialization is performed.
   * <p>
   * <code>data[i] += rng(min,max)</code>
   * <p>
   * where <tt>rng(min,max)</tt> is uniform value on <tt>[min,max)</tt>
   */
  public static void uniform(int[] data, boolean update, int min, int max)
  {
    int d = max-min;
    int e = data.length;
    
    if (update) {
      for(int i=0; i<e; i++)
        data[i] += rng.nextInt(d) + min;
    }
    else {
      for(int i=0; i<e; i++)
        data[i] = rng.nextInt(d) + min + i;
    }
  }
  
  /**
   * Modify <tt>data</tt> using a geometric distribution.
   * <p>
   * if <tt>update</tt> is <i>false</i> then logically:
   * <code>data[i]=i</code> initialization is performed.
   * <p>
   * The modification is we start a counter at zero, then
   * repeated flip a coin.  If we get "tails" we increment
   * the counter and repeat until we get "heads".  Once
   * we have a count we again flip a coin to decided if
   * we want to negate the value which is then applied
   * to the array's value.
   * @see {@link roquen.math.rng.PRNG#nextToss()}
   */
  public static void geometric(int[] data, boolean update)
  {
    int e = data.length;
    
    if (update) {
      for(int i=0; i<e; i++) {
        int d = rng.nextToss();
        int s = rng.nextInt() >> 31;
        d ^= s;
        d -= s;
        
        data[i] += d;
      }
    }
    else {
      for(int i=0; i<e; i++) {
        int d = rng.nextToss();
        int s = rng.nextInt() >> 31;
        d ^= s;
        d -= s;
        
        data[i] = i + d;
      }
    }
  }
  
  /**
   * Modify <tt>data</tt> using a poisson distribution.
   * <p>
   * if <tt>update</tt> is <i>false</i> then logically:
   * <code>data[i]=i</code> initialization is performed.
   * <p>
   * At each element a Poisson distribution is computed
   * followed by a coin flip to determine if the value
   * is to be negated the is added to the element.
   * @see {@link roquen.math.rng.PRNG#nextPoisson(float)}
   */
  public static void poisson(int[] data, float mean, boolean update)
  {
    int e = data.length;
    float emean = (float)Math.exp(-mean);
    
    if (update) {
      for(int i=0; i<e; i++) {
        int d = rng.nextPoisson(emean);
        int s = rng.nextInt() >> 31;
        d ^= s;
        d -= s;
        
        data[i] += d;
      }
    }
    else {
      for(int i=0; i<e; i++) {
        int d = rng.nextPoisson(emean);
        int s = rng.nextInt() >> 31;
        d ^= s;
        d -= s;
        
        data[i] = i + d;
      }
    }
  }
  
  private static void swapL(int[] data, int a, int b)
  {
    int t   = data[a];
    data[a] = data[b];
    data[b] = t;
  }
  
  /**
   * Walks 'data' in order.  With a probability of 'prob' swaps the
   * current element with the one up to 'dist' forward (uniformly
   * chosen).
   */
  public static void hammingSwapUniform(int[] data, int dist, float prob)
  {
    int e = data.length;
    
    for(int i=0; i<e; i++) {
      float p = rng.nextFloat();
      if (p < prob) {
        int b = 1+rng.nextInt(dist)+i;
        if (b < e)
          swapL(data, i, b);
      }
    }
  }
  
  /**
   * Walks the array and swaps each element with one chosen
   * at a geometric distribution (p=.5) forward.
   * @see {@link roquen.math.rng.PRNG#nextToss()}
   */
  public static void geometricSwap(int[] data)
  {
    int e = data.length;
    
    for(int i=0; i<e; i++) {
      int b = rng.nextToss();
      
      if (b > 0) {
        b += i;
        if (b < e)
          swapL(data, i, b);
      }
    }
  }
  
  
  /**
   * Swaps 'n' entry pairs in data chosen uniformly
   * from its size.
   */
  public static void swapN(int[] data, int n)
  {
    int e = data.length;
    
    for(int i=0; i<n; i++) {
      // don't worry about repeats or self swaps
      int a = rng.nextInt(e);
      int b = rng.nextInt(e);
      swapL(data, a, b);
    }
  }
  
  /** Fill data with a seeded bit reversal sequence.  */
  public static void fillBRS(int[] data, int seed)
  {
    int e = data.length;
    int d = seed * 0xac549d55;
    int c;
    
    for(int i=0; i<e; i++) {
      c  = Integer.numberOfTrailingZeros(~i);
      d ^= 0x80000000 >>> c;
      data[i] = d;
    }
  }
  
  /*
  public static void main(String[] args) {
    int len = 22;
    int run = 10;
    int[] data = new int[len];
    
    for(int i=0; i<run; i++) {
      uniform(data, false, -2,2);
      //geometric(data, false);
      //hammingSwapUniform(data, 3, .7f);
      //geometricSwap(data);
      //poisson(data, 2.f, false);
      //swapN(data, 2);
      //fillBRS(data, i);
      System.out.println(java.util.Arrays.toString(data));
    }
  }
  */
}
