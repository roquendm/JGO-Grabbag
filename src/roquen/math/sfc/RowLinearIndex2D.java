package roquen.math.sfc;

import roquen.fake.Vect2i;

/**
 * Trivial indexing scheme for drop-in-replacement testing of apple-to-apples (almost)
 */
public final class RowLinearIndex2D extends Index2D<RowLinearIndex2D>
{ 
  private final int D;
  
  public RowLinearIndex2D(int dim)
  {
    D = dim;
  }
  
  @Override
  public final void set(Vect2i v)
  {
   sx = v.x; sy = v.y;
  }
  
  @Override
  public void set(RowLinearIndex2D v) 
  {
    sx = v.sx;
    sy = v.sy;
  }
  
  @Override
  public final void get(Vect2i v)
  {
    v.x = sx;
    v.y = sy;
  }
 
  @Override public final void setX(int x) { sx = x; }
  @Override public final void setY(int y) { sy = y; }
  @Override public final int  getX() { return sx; }
  @Override public final int  getY() { return sy; }
  @Override public final void incY() { sy += 1; }
  @Override public final void decY() { sy -= 1; }
  @Override public final void incX() { sx += 1; }
  @Override public final void decX() { sx -= 1; }

  @Override
  public void add(RowLinearIndex2D v)
  {
    sx += v.sx;
    sy += v.sy;
  }
  
  @Override
  public void sub(RowLinearIndex2D v)
  {
    sx -= v.sx;
    sy -= v.sy;
  }

  @Override
  public int getIndex() {
    return D*sy+sx;
  }


  
}
