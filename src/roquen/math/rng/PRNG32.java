package roquen.math.rng;

/**
 * Basic class of random number generators that operate on 32-bit
 * integer data chunks.
 */
public abstract class PRNG32 extends PRNG 
{  
  @Override
  public final long nextLong()
  {
    long r = nextInt();
    return (r<<32) | nextInt();
  }
}
