package roquen.hash;

import static roquen.math.Float32.toBits;
import static roquen.math.Float64.toBits;

/** 
 * Static methods for constructing hashing based on 32-bit Murmurhash2.
 * <p>
 * This hash has a flaw (<a href="https://code.google.com/p/smhasher/wiki/MurmurHash2Flaw">link</a>)
 * which is very unlikely to be a concern.
 * <p>
 * <a href="http://en.wikipedia.org/wiki/MurmurHash">Wikipedia</a>
 */

// TODO: nothing has been testing...just quickly banged out
public enum MurmurHash2
{
  ;
  
  private static final int M = 0x5bd1e995;
  
  /** A starting value for hash */
  public static final int hashInit()
  {
    return 0x9747b28c;
  }
  
  /** Final mixing of the hash */
  public static final int hashComplete(int h)
  {
    h ^= h >>> 13; h *= M; h ^= h >>> 15;

    return h;
  }
  
  /** */
  public static final int hashAdd(int h, int x)
  {
    x *= M; x ^= x >>> 24; x *= M;

    h ^= x; h *= M;

    return h;
  }

  /** */
  public static final int hashAdd(int h, int x, int y)
  {
    x *= M; x ^= x >>> 24; x *= M;
    y *= M; y ^= y >>> 24; y *= M;

    h ^= x; h *= M; h ^= y;

    return h;
  }

  /** */
  public static final int hashAdd(int h, int x, int y, int z)
  {
    x *= M; x ^= x >>> 24; x *= M;
    y *= M; y ^= y >>> 24; y *= M;
    z *= M; z ^= z >>> 24; z *= M;

    h ^= x; h *= M; 
    h ^= y; h *= M; 
    h ^= z; h *= M; 

    return h;
  }
  
  /** */
  public static final int hashAdd(int h, int x, int y, int z, int w)
  {
    x *= M; x ^= x >>> 24; x *= M;
    y *= M; y ^= y >>> 24; y *= M;
    z *= M; z ^= z >>> 24; z *= M;
    w *= M; w ^= w >>> 24; w *= M;

    h ^= x; h *= M; 
    h ^= y; h *= M; 
    h ^= z; h *= M; 

    return h;
  }
  
  /** */
  public static final int hashAdd(int h, long x)
  {
    return hashAdd(h, (int)x, (int)(x>>>32));
  }
  
  /** */
  public static final int hashAdd(int h, long x, long y)
  {
    return hashAdd(h, (int)x, (int)(x>>>32),
                      (int)y, (int)(y>>>32));
  }
  
  /** */
  public static final int hashAdd(int h, float x)
  {
    return hashAdd(h, toBits(x));
  }
  
  /** */
  public static final int hashAdd(int h, float x, float y)
  {
    return hashAdd(h, toBits(x), toBits(y));
  }
  
  /** */
  public static final int hashAdd(int h, float x, float y, float z)
  {
    return hashAdd(h, toBits(x), toBits(y), toBits(z));
  }
  
  /** */
  public static final int hashAdd(int h, float x, float y, float z, float w)
  {
    return hashAdd(h, toBits(x), toBits(y), toBits(z), toBits(w));
  }
  
  /** */
  public static final int hashAdd(int h, double x)
  {
    return hashAdd(h, toBits(x));
  }
  
  /** */
  public static final int hashAdd(int h, double x, double y)
  {
    return hashAdd(h, toBits(x), toBits(y));
  }
  
  /** */
  public static final int hashAdd(int h, int[] a)
  {
    int c = a.length;
    int i = 0;
    
    while(c >= 4) {
      h = hashAdd(h, a[i], a[i+1], a[i+2], a[i+3]);
      i += 4;
      c -= 4;
    }
    
    while(i < c)
      h = hashAdd(h, a[i++]);
    
    return h;
  }
  
  /** */
  public static final int hashAdd(int h, float[] a)
  {
    int c = a.length;
    int i = 0;
    
    while(c >= 4) {
      h = hashAdd(h, a[i], a[i+1], a[i+2], a[i+3]);
      i += 4;
      c -= 4;
    }
    
    while(i < c)
      h = hashAdd(h, a[i++]);
    
    return h;
  }
  
  /** */
  public static final int hashAdd(int h, long[] a)
  {
    int c = a.length;
    int i = 0;
    
    while(c >= 2) {
      h = hashAdd(h, a[i], a[i+1]);
      i += 2;
      c -= 2;
    }
    
    if (i != c)
      h = hashAdd(h, a[i]);
    
    return h;
  }
  
  /** */
  public static final int hashAdd(int h, double[] a)
  {
    int c = a.length;
    int i = 0;
    
    while(c >= 2) {
      h = hashAdd(h, a[i], a[i+1]);
      i += 2;
      c -= 2;
    }
    
    if (i != c)
      h = hashAdd(h, a[i]);
    
    return h;
  } 
}

