package roquen.pg.noise;

public class SimplexNoise3D extends Noise
{
  /**
   * If <i>true</i> the valid input set to noise is restricted
   * to positive numbers.  This will produce serious defects
   * if any of the input coordinates are negative.
   */
  private static final boolean restrictToPositive = false;
  

  /**
   * Compile time selection between Perlin's simplex vectors and
   * the older vector set from Improved (gradient) noise.
   */
  private static final boolean perlinVectors = false;
  
  /**
   * Compile time selection: If <i>true</i> vectors are expanded
   * to a switch table instead of conditional moves.
   */
  private static final boolean expandVectors = true;
  
  
  /**
   * Final scaling factor.  Depends on vector set.
   */
  private static final float finalScale;
  
  static {
    if (perlinVectors)
      finalScale = 8;
    else
      finalScale = 32;
  }
  

  /**
   * Completes coordinate hashing and performs the dot product with the selected vector.
   */
  private static float dotRandVector(int h, float x, float y, float z)
  {   
    // complete hash generation (see eval)
    h = posthash(h) >>> 24;

    if (perlinVectors) {
      float S;

      if (false) {
        int b5 = h >> 5 & 1;
        int b4 = h >> 4 & 1;
        int b3 = h >> 3 & 1;
        int b2 = h >> 2 & 1;
        int b  = h & 3;

        float P,Q,R;

        switch (b) {
          case 1:  P=x; Q=y; R=z; break;
          case 2:  P=y; Q=z; R=x; break;
          default: P=z; Q=x; R=y; break;
        }
        if (true) {
          if (b5 == b3) P = -P;
          if (b5 == b4) Q = -Q;
          if (b5 != (b4^b3)) R = -R; 
        } else {
          P = b5 == b3 ? -P : P;
          Q = b5 == b4 ? -Q: Q;
          R = b5 != (b4^b3) ? -R : R;
        }

        S = (b == 0 ? Q + R : b2 == 0 ? Q : R);


        return (P + S);
      }
      else {
        S = 0;//tempHack(h,x,y,z);

        return S;
      }
    }
    else {
      // Use the "older" vector set from Improved Gradient Noise.
      
      if (expandVectors) {
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
          case 0xC: return  y+x;
          case 0xD: return -y+z;
          case 0xE: return  y-x;
          case 0xF: return -y-z;
          default: return 0; // never happens
        }
      }
      else {
        h &= 0xF;
    
        float u = (h < 8) ? x : y;
        float v = (h < 4) ? y : ((h == 12 || h == 14) ? x : z);
    
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
      }
    }
  }
  
  /** Properly rounded: 1/3 */
  private static final float K0 = 0x1.555556p-2f;
  
  /** Properly rounded: 1/6 */
  private static final float K1 = 0x1.555556p-3f; 

  private static final boolean simpleHash = true;
  private static final int M = 0x5bd1e995;
  
  private static final int prehash(int i, int j, int k)
  {
    if (simpleHash)
      return (i<<16)^(j<<8)^(k);
    else {
      int h; 
      
      i *= M; i ^= i >>> 24; i *= M;
      j *= M; j ^= j >>> 24; j *= M;
      k *= M; k ^= k >>> 24; k *= M;
      
      h  = i; h *= M; 
      h ^= j; h *= M; 
      h ^= k;
      
      return h;
    }
  }

  private static final int posthash(int h)
  {
    if (simpleHash)
      h *= 0xB5262C85;
    else {
      h ^= h >>> 13; h *= M; h ^= h >>> 15;
    }
    return h;
  }

  
  
  /**
   *
   */
  public static float eval(float x, float y, float z)
  {
    // project the input coordinate (via skew)
    float s = (x + y + z) * K0;
    int   i, j, k;

    if (restrictToPositive) {
      // This is possible if input is restricted to positive values.
      // The center of the image in the example is (0,0,0), so will
      // show the (serious) defects if used and the inputs are negative.
      //
      // The major upside is that the code will run significantly
      // faster if this input limitation is required.
      i = (int)(x+s);
      j = (int)(y+s);
      k = (int)(z+s);
    }
    else {
      i = pseudoFloor(x+s);
      j = pseudoFloor(y+s);
      k = pseudoFloor(z+s);
    }
    
    // unproject the coordinate back to Euclidean space.
    float t  = (i + j + k) * K1;
    float x0 = x - (i-t);
    float y0 = y - (j-t);
    float z0 = z - (k-t);

    // locate the simplex of the input coordinate.
    int i1, j1, k1; 
    int i2, j2, k2; 

    if (x0 >= y0) {
      i2 = 1;
      j1 = 0;
      
      if (y0 >= z0) {
        i1 = j2 = 1;
        k1 = k2 = 0;
      }
      else {
        j2 = 0;
        k2 = 1;
        
        if (x0 >= z0)
          i1 = 1;
        else
          i1 = 0;
        
        k1 = i1 ^ 1;
      } 
    }
    else {
      i1 = 0;
      j2 = 1;
      
      if (y0 < z0) {
        j1 = i2 = 0;
        k1 = k2 = 1;
      }
      else {
        j1 = 1;
        k1 = 0;
        
        if (x0 < z0) {
          i2 = 0;
          k2 = 1;
        }
        else {
          i2 = 1;
          k2 = 0;
        }
      }
    }

    // calculate the four coordinates
    float x1 = x0 - i1 + K1; 
    float y1 = y0 - j1 + K1;
    float z1 = z0 - k1 + K1;

    float x2 = x0 - i2 + 2*K1; 
    float y2 = y0 - j2 + 2*K1;
    float z2 = z0 - k2 + 2*K1;

    float x3 = x0 - 1 + 3*K1; 
    float y3 = y0 - 1 + 3*K1;
    float z3 = z0 - 1 + 3*K1;

    // Step 1 of hash generation, if needed the hashing
    // is completed in 'doRandVector'.
    // This could be improved
    int h0 = prehash(i,    j,    k   );
    int h1 = prehash(i+i1, j+j1, k+k1);
    int h2 = prehash(i+i2, j+j2, k+k2);
    int h3 = prehash(i+ 1, j+ 1, k+ 1);
    
    float n = 0;
    
    // Calculate and sum the results of the four coordinates.
    
    t = 0.6f - x0*x0 - y0*y0 - z0*z0;

    if (t > 0){
      t *= t;
      n += t*t * dotRandVector(h0, x0, y0, z0);
    }

    t = 0.6f - x1*x1 - y1*y1 - z1*z1;

    if (t > 0) {
      t *= t;
      n += t*t * dotRandVector(h1, x1, y1, z1);
    }

    t = 0.6f - x2*x2 - y2*y2 - z2*z2;

    if (t > 0) {
      t *= t;
      n += t*t * dotRandVector(h2, x2, y2, z2);
    }

    t = 0.6f - x3*x3 - y3*y3 - z3*z3;

    if (t > 0) {
      t *= t;
      n += t*t * dotRandVector(h3, x3, y3, z3);
    }
    
    return finalScale * n;
  }
}