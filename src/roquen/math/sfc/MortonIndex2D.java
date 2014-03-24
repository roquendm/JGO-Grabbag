package roquen.math.sfc;

/**
 * 
 */
public class MortonIndex2D extends Morton2DBase<MortonIndex2D> 
{
  @Override
  public final void set(MortonIndex2D v)
  {
    sx = v.sx;
    sy = v.sy;
  }
  
  @Override
  public final int getIndex()
  {
    return sx|sy;
  }
}
