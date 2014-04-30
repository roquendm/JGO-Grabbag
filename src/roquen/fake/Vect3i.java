package roquen.fake;

import static roquen.hash.MurmurHash2.*;

public class Vect3i {
  public int x,y,z;
  
  /** */
  private static final int HASH = hashMakeSeed(0);
  
  @Override
  public int hashCode()
  {
    return hashComplete(hashAdd(HASH,x,y,z));
  }
}
