package roquen.math.seq;

/**
 * Computes permuted (random) integers on [0, n) visiting each element exactly once.
 * <p>
 * Implementation is a simple rejection method built on top of {@link #PermutedSequencePOT}
 */
public class PermutedSequence extends PermutedSequencePOT
{
  private int length;

  public PermutedSequence(int length) {
    super();
    setLength(length);
  }

  /** Sets the length of the sequence */
  @Override
  public void setLength(int length)
  {
    int npow2 = roquen.math.Int32.ceilingLog2(length);

    this.length = length;

    super.setLength(npow2);
  }
  
  @Override
  public int next()
  {
    int r;
    
    // This will loop (on average) no more than twice per call.
    do {
      r = super.next();
      if (r < length)
        return r;
    } while(true);
  }
  
}
