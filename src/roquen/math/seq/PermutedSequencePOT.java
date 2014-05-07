package roquen.math.seq;


// http://www.java-gaming.org/topics/iterate-each-element-of-a-collection-randomly-once-only/26646/msg/234580/view.html#msg234580

/**
 * Computes permuted (random) integers on [0,2<sup>n</sup>) visiting each element exactly once.
 */
public class PermutedSequencePOT implements IntegerSequence
{
  private static roquen.math.rng.XorShift32 rng = new roquen.math.rng.XorShift32();
  
  private int m;
  private int a;
  private int mask;
  private int v;
  
  protected PermutedSequencePOT() {} 

  public PermutedSequencePOT(int length)
  {
    setLength(length);
  }

  /** Set the length of the sequence.  Must be a power-of-two.  */
  public void setLength(int length)
  {
    mask = length-1;
    reset();
    
    assert((length & mask)==0);
  }

  /** Create a new permutation of the current length. */
  public void reset()
  {
    // this is all overkill
    m = (rng.nextInt()<<3)|5;   // anything such that (m & 7)==5 
    a = (rng.nextInt()<<1)|1;   // any odd value
    v = (rng.nextInt());        // basically sets the offset that zero will appear
  }


  /** Returns the next integer in the sequence. */
  @Override
  public int nextInt()
  {
    v = (m*v + a) & mask;

    return v;
  }
  
}
