package roquen.fake;

import static roquen.hash.MurmurHash2.*;

public class Vect2f {
  public float x,y;
  
  public Vect2f set(float x, float y)
  {
    this.x=x; this.y=y;
    return this;
  }
  
  
  /** */
  private static final int HASH = hashMakeSeed(0);
  
  @Override
  public int hashCode()
  {
    return hashComplete(hashAdd(HASH,x,y));
  }
}
