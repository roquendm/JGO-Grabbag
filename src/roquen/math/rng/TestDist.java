package roquen.math.rng;

/**
 * Some distributions intended for testing purposes. Most are not
 * even approaching an optimal implementation.
 */
public enum TestDist {
  ;
  
  /**
   * Returns a number with one bit set from 'mask' with
   * equal probability.
   */
  public static int oneInMask(PRNG rng, int bits)
  {
    int len = Integer.bitCount(bits);
    int val = rng.nextIntFast(len);
    
    
    return 0;
  }
  
  public static int oneInPot(PRNG rng, int pow)
  {
    return 1 << (rng.nextInt() >>> (32-pow));
  }
  
  public static void main(String[] args)
  {
    XorStar64 rng = new XorStar64();
    
    for(int i=0; i<32; i++) {
      int r = oneInPot(rng, 2);
      System.out.printf("%32s\n", Integer.toBinaryString(r));
    }
  }
  
}
