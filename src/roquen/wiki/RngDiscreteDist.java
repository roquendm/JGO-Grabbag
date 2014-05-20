package roquen.wiki;

/**
 * Example of discrete distribution generation by directly
 * using the PDF (linear search) and CDF (binary search).
 * These are not practical implementations.
 * <p>
 * 
 */
public enum RngDiscreteDist
{
  ;

  static java.util.Random rng = java.util.concurrent.ThreadLocalRandom.current();
  static float   weightSum;
  static int[]   weights;
  static float[] pdf;      //
  static float[] cdf;      //
  static float[] lhisto;   //
  static float[] bhisto;   //
  
  // 
  @SuppressWarnings("boxing")
  public static void generationTime()
  {
    int   e = weights.length;
    float s = 0;
    float t;
   
    // 1) Walk through the list create both a
    //    pdf and cdf.  pdf is used for a
    //    linear search and cdf for binary.
    for(int i=0; i<e; i++) {
      t      = weights[i]/weightSum;
      s     += t;
      pdf[i] = t;
      cdf[i] = s;
    }
    
    // demo stuff below this point
    System.out.print("SRC: ");
    for(int i=0; i<e; i++)
      System.out.printf("%2d ", weights[i]);
    
    System.out.print("\nPDF: ");
    for(int i=0; i<e; i++)
      System.out.printf("%4.4f ", pdf[i]);
    
    System.out.print("\nCDF: ");
    for(int i=0; i<e; i++)
      System.out.printf("%4.4f ", cdf[i]);
  }
  
  // Generate by a linear search of the PDF.
  // The thing to note is that this process
  // is exactly (or effectively) computing
  // the CDF as it walks this list. This
  // example version is effectively computing.
  public static int linear()
  {
    float u = rng.nextFloat();
    int   i = 0;
    
    do {
      u -= pdf[i++];
    } while(u > 0);
    
    return i-1;
  }
  
  // Generate by a binary search using
  // the CDF.
  public static int binary()
  {
    float u   = rng.nextFloat();
    int   len = cdf.length - 1;
    int   i   = 0;

    if (u > cdf[0]) {
      int j = len;

      while (i < j) {
        int m = (i + j) >>> 1;
        if (u > cdf[m])
          i = m + 1;
        else
          j = m;
      }
    }

    return i;
  }
  
  @SuppressWarnings("boxing")
  public static void main(String[] args)
  {
    int argc = args.length;
    int len;
    
    // 1) based on input method either create or
    // load in a list of weights.  At the same
    // time we'll also sum them up.
    
    if (argc >= 3) {
      // three or more: use input as explicit list of weights
      len = argc;
      weights = new int[len];
      
      for(int i=0; i<len; i++) {
        int wi = Integer.parseInt(args[i]);
        
        weights[i] = wi;
        weightSum += wi;
      }
        
    }
    else {
      // less than 3: making it up
      int max;
      
      if (argc == 0) {
        len = 3+rng.nextInt(10);
        max = 50;
      }
      else {
        len = Math.max(3, Integer.parseInt(args[0]));
        if (argc == 1)
          max = 50;
        else
          max = Math.max(10, Integer.parseInt(args[1]));
        }

      weights = new int[len];
      
      for(int i=0; i<len; i++) {
        int wi = 1+rng.nextInt(max);
        weights[i] = wi;
        weightSum += wi;
      }
    }
    
    // 2) create the actual data tables used
    pdf = new float[len];
    cdf = new float[len];
    generationTime();
    
    // 2a) Hack the last entry to nod for errors.
    // This is overkill.
    pdf[len-1] = 2.f;
    cdf[len-1] = 2.f;
    
    // 3) Generate some numbers and create a histogram
    // to show correctness for both example methods
    final int numSamples = 0x1FF_FFFF;
    
    lhisto = new float[len];
    bhisto = new float[len];
    
    for(int i=0; i<numSamples; i++) {
      int rl = linear();
      int rb = binary();
      
      lhisto[rl]++;
      bhisto[rb]++;
    }
    
    // demo stuff below this point
    System.out.print("\nlin: ");
    for(int i=0; i<len; i++)
      System.out.printf("%4.4f ", lhisto[i]*(1.0/numSamples));
    
    System.out.print("\nbin: ");
    for(int i=0; i<len; i++)
      System.out.printf("%4.4f ", bhisto[i]*(1.0/numSamples));
    System.out.println();
    
  }
}
