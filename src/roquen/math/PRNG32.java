package roquen.math;

/**
 * Basic class of random number generators that operate on 32-bit
 * integer data chunks.
 */
public abstract class PRNG32 extends PRNG {
  
  
  public final long nextLong()
  {
    long r = nextInt();
    return (r<<32) | nextInt();
  }

}
