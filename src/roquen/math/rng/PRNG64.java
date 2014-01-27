package roquen.math.rng;

/**
 * Basic class of random number generators that operate on 64-bit
 * integer data chunks.
 */
public abstract class PRNG64 extends PRNG 
{
  @Override
  public final int nextInt() { return (int)(nextLong() >>> 32); }
}
