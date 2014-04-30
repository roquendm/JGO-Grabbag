package roquen.fake;

import static roquen.hash.MurmurHash2.*;

public class Vect2i {
  public int x, y;
  
  /** */
  private static final int HASH = hashMakeSeed(0);
  
  @Override
  public int hashCode()
  {
    return hashComplete(hashAdd(HASH,x,y));
  }
}
