package roquen.math.sfc;

import static roquen.math.sfc.Morton2D.*;
import roquen.fake.Vect2i;

/** implements manipulations of scattered coordinates */
abstract class Morton2DBase<T extends Morton2DBase<T>> extends Index2D<T>
{
  @Override
  public void set(T v)
  {
    sx = v.sx;
    sy = v.sy;
  }
  
  @Override
  public final void set(Vect2i v)
  {
   setX(v.x);
   setY(v.y);
  }
  
  @Override
  public final void setX(int x)
  {
    sx = scatter1(x);
  }
  
  @Override
  public final void setY(int y)
  {
    sy = scatter1(y) << 1;
  }
  
  @Override
  public final void get(Vect2i v)
  {
    v.x = gather1_(sx);
    v.y = gather1_(sy>>>1);
  }
  
  @Override
  public final int getX()
  {
    return gather1_(sx);
  }
  
  @Override
  public final int getY()
  {
    return gather1_(sy>>>1);
  }
  
  
  @Override
  public void incY()
  {
    sy +=MASK_X+2; sy &= MASK_Y; 
    //int mx = Morton2D.MASK_X;
    //int my = mx+mx;
    //sy |= mx; sy += 2; sy &= my;
  }
  
  @Override
  public void decY()
  {
    sy -= 2; sy &= Morton2D.MASK_Y;
  }
  
  protected final int INC_X = MASK_Y+1;
  
  @Override
  public void incX()
  {
    //sx |= MASK_Y; sx += 1; sx &= MASK_X;
    int a = INC_X;
    int m = a >>> 1;
    
    sx += a;
    sx &= m;
  }
  
  @Override
  public void decX()
  {
    sx -= 1; sx &= MASK_X;
  }

  @Override
  public void add(T v) {
    int mx = MASK_X;
    int my = mx << 1; // MASK_Y;
    sy += v.sy + mx;
    sx += v.sx + my;
    sy &= my;
    sx &= mx;
  }
  
  public void sub(T v)
  {
    int mx = MASK_X;
    int my = mx << 1; // MASK_Y;
    
    sx -= v.sx;
    sy -= v.sy;
    
    sy &= my;
    sx &= mx;
  }
  
}
