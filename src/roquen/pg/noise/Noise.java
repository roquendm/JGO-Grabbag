package roquen.pg.noise;


public abstract class Noise
{  
  public static final int pseudoFloor(float x)
  {
    //return (int)x;
    return x >= 0 ? (int)x : (int)x-1;
  }

  /** 3t<sup>2</sup> - 2t<sup>3</sup> */
  public static final float ease(float t)
  {
    return (t*t*(3.f-(t+t)));
  }
  
  /** 6t - 6t<sup>2</sup> */
  public static final float easeDt(float t)
  {
      return 6.f*t*(1-t);
  }

  /**
   * 10t<sup>3</sup> - 15t<sup>4</sup> + 6t<sup>5</sup>
   */
  public static final float iease(float t)
  { 
    return t*t*t*(t*(t*6.f-15.f)+10.f);
  }

  /** 30t<sup>2</sup> - 60t<sup>3</sup> + 30t<sup>4</sup> */
  public static final float ieaseDt(float t)
  {
    return 30.f*t*t*(t*(t-2.f)+1.f);
  }
  
  public static final float lerp(float t, float a, float b)
  {
    return a+t*(b-a);
  }


  /** map 32-bit integer to a single on [-1,1] */
  public static final float normalizeI(int h)
  {
    return (h>>>7)*0x1.0p-24f - 1;
  }

  //*************** HASHING FUNCTIONS

  private static final int M = 0x5bd1e995;


  public static final int postHashM2(int h)
  {
    h ^= h >>> 13; h *= M; h ^= h >>> 15;

    return h;
  }


  public static final int preHashM2(int x)
  {
    int h = 0x9747b28c; 

    x *= M; x ^= x >>> 24; x *= M;

    h ^= x; h *= M;

    return h;
  }

  public static final int hashM2(int x)
  {
    return postHashM2(preHashM2(x));
  }


  public static final int preHashM2(int x, int y)
  {
    int h = 0x9747b28c; 

    x *= M; x ^= x >>> 24; x *= M;
    y *= M; y ^= y >>> 24; y *= M;

    h ^= x; h *= M; h ^= y;

    return h;
  }

  public static final int hashM2(int x, int y)
  {
    return postHashM2(preHashM2(x,y));
  }

  public static final int preHashM2(int x, int y, int z)
  {
    int h = 0x9747b28c; 

    x *= M; x ^= x >>> 24; x *= M;
    y *= M; y ^= y >>> 24; y *= M;
    z *= M; z ^= z >>> 24; z *= M;

    h ^= x; h *= M; 
    h ^= y; h *= M; 
    h ^= z;

    return h;
  }

  public static final int hashM2(int x, int y, int z)
  {
    return postHashM2(preHashM2(x,y,z));
  }


  public static final int preHashJ(int x)
  {
    int h = 0x9747b28c;

    h += x; h += h << 10; h ^= h >>> 6;

    return h;
  }

  public static final int preHashJ(int x, int y)
  {
    int h = 0x9747b28c;

    h += x; h += h << 10; h ^= h >>> 6;
    h += y; h += h << 10; h ^= h >>> 6;

    return h;
  }

  public static final int hashJ(int x, int y)
  {
    return postHashJ(preHashJ(x,y));
  }
  
  public static final int preHashJ(int x, int y, int z)
  {
    int h = 0x9747b28c;

    h += x; h += h << 10; h ^= h >>> 6;
    h += y; h += h << 10; h ^= h >>> 6;
    h += z; h += h << 10; h ^= h >>> 6;

    return h;
  }
  
  public static final int hashJ(int x, int y, int z)
  {
    return postHashJ(preHashJ(x,y,z));
  }
  

  public static final int postHashJ(int h)
  {
    h += h <<   3;
    h ^= h >>> 11;
    h += h <<  15;

    return h;
  }

  public static final int hashTEA4(int x, int y)
  {
    int v0 = x;
    int v1 = y;

    v0 += ((v1<<4)+0xA341316C)^(v1+0x9e3779b9)^((v1>>>4)+0xC8013EA4);
    v1 += ((v0<<4)+0xAD90777D)^(v0+0x9e3779b9)^((v0>>>4)+0x7E95761E);
    v0 += ((v1<<4)+0xA341316C)^(v1+0x3c6ef372)^((v1>>>4)+0xC8013EA4);
    v1 += ((v0<<4)+0xAD90777D)^(v0+0x3c6ef372)^((v0>>>4)+0x7E95761E);
    v0 += ((v1<<4)+0xA341316C)^(v1+0xdaa66d2b)^((v1>>>4)+0xC8013EA4);
    v1 += ((v0<<4)+0xAD90777D)^(v0+0xdaa66d2b)^((v0>>>4)+0x7E95761E);
    v0 += ((v1<<4)+0xA341316C)^(v1+0x78dde6e4)^((v1>>>4)+0xC8013EA4);
    v1 += ((v0<<4)+0xAD90777D)^(v0+0x78dde6e4)^((v0>>>4)+0x7E95761E);

    return v0^v1;
  }

  public static final int hashZ(int x, int y)
  {
    // 8-bit input only
    int sx = (int)((x * 0x0101010101010101L & 0x8040201008040201L) * 
        0x0102040810204081L >>> 49) & 0x5555;

    int sy = (int)((y * 0x0101010101010101L & 0x8040201008040201L) * 
        0x0102040810204081L >>> 48) & 0xAAAA;

    return sx|sy;
  }
  
  public static final int hashWang(int seed)
  {
    seed = (seed ^ 61) ^ (seed >>> 16);
    seed *= 9;
    seed = seed ^ (seed >>> 4);
    seed *= 0x27d4eb2d;
    seed = seed ^ (seed >>> 15);
    return seed;
  }
  
  public static long hashWang(long key) {
    key = (~key) + (key << 21); // key = (key << 21) - key - 1;
    key = key ^ (key >>> 24);
    key = (key + (key << 3)) + (key << 8); // key * 265
    key = key ^ (key >>> 14);
    key = (key + (key << 2)) + (key << 4); // key * 21
    key = key ^ (key >>> 28);
    key = key + (key << 31);
    return key;
  }

  /*
  // inverse of the 64-bit wang hash function (still in C)
  uint64_t inverse_hash(uint64_t key) {
    uint64_t tmp;

    // Invert key = key + (key << 31)
    tmp = key-(key<<31);
    key = key-(tmp<<31);

    // Invert key = key ^ (key >> 28)
    tmp = key^key>>28;
    key = key^tmp>>28;

    // Invert key *= 21
    key *= 14933078535860113213u;

    // Invert key = key ^ (key >> 14)
    tmp = key^key>>14;
    tmp = key^tmp>>14;
    tmp = key^tmp>>14;
    key = key^tmp>>14;

    // Invert key *= 265
    key *= 15244667743933553977u;

    // Invert key = key ^ (key >> 24)
    tmp = key^key>>24;
    key = key^tmp>>24;

    // Invert key = (~key) + (key << 21)
    tmp = ~key;
    tmp = ~(key-(tmp<<21));
    tmp = ~(key-(tmp<<21));
    key = ~(key-(tmp<<21));

    return key;
  }
  */
  
  
  //*************** DOT WITH RANDOM VECTOR FUNCTIONS



  public static final float grad7(int h, float x, float y)
  {
    switch (h >>> (32-3)) {
      case 0x0: return  x+y;
      case 0x1: return -x+y;
      case 0x2: return  x-y;
      case 0x3: return -x-y;
      case 0x4: return  x;
      case 0x5: return -x;
      case 0x6: return  y;
      case 0x7: return -y;
      default: return 0; // never happens
    }
  }

  // perlin's simplex reference
  public static final float gradPSR(int h, float x, float y, float z)
  {
    int b5 = h >> 5 & 1;
    int b4 = h >> 4 & 1;
    int b3 = h >> 3 & 1;
    int b2 = h >> 2 & 1;
    int b = h & 3;

    float p = b == 1 ? x : b == 2 ? y : z;
    float q = b == 1 ? y : b == 2 ? z : x;
    float r = b == 1 ? z : b == 2 ? x : y;

    p = b5 == b3 ? -p : p;
    q = b5 == b4 ? -q: q;
    r = b5 != (b4^b3) ? -r : r;

    return (p + (b == 0 ? q + r : b2 == 0 ? q : r));
  }

  public static final float gradF(int h, float x, float y, float z)
  {
    // TODO: change to shift instead?
    switch(h & 0xF) {
      case 0x0: return  x+y;
      case 0x1: return -x+y;
      case 0x2: return  x-y;
      case 0x3: return -x-y;
      case 0x4: return  x+z;
      case 0x5: return -x+z;
      case 0x6: return  x-z;
      case 0x7: return -x-z;
      case 0x8: return  y+z;
      case 0x9: return -y+z;
      case 0xA: return  y-z;
      case 0xB: return -y-z;
      case 0xC: return  x+y;
      case 0xD: return -x+y;
      case 0xE: return -y+z;
      case 0xF: return -y-z;
      default:  return 0;
    }
  }


  //public abstract float eval(float x);
}

