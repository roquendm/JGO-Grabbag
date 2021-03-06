package roquen.math.sfc;

import static roquen.math.sfc.Morton2D.*;
import roquen.fake.Vect2i;

/** implements manipulations of scattered coordinates */
abstract class Morton2DBase<T extends Morton2DBase<T>> extends Index2D<T>
{
  // probably would make sense to modify to prefer faster updates than
  // get operations.  scattered x,y in same bit positions and do the
  // shift for combine in get.
  
  @Override
  public void set(T v)
  {
    sx = v.sx;
    sy = v.sy;
  }
  
  @Override
  public final void set(Vect2i v)
  {
    //sx = (int)((v.x * 0x0101010101010101L & 0x8040201008040201L) * 0x0102040810204081L >>> 49) & 0x5555;
    //sy = (int)((v.y * 0x0101010101010101L & 0x8040201008040201L) * 0x0102040810204081L >>> 48) & 0xAAAA;
    
    setX(v.x);
    setY(v.y);
  }
  
  @Override
  public final void setX(int x)
  {
    //sx = (int)((x * 0x0101010101010101L & 0x8040201008040201L) * 0x0102040810204081L >>> 49) & 0x5555;
    sx = scatter1(x);
  }
  
  @Override
  public final void setY(int y)
  {
    //sy = (int)((y * 0x0101010101010101L & 0x8040201008040201L) * 0x0102040810204081L >>> 48) & 0xAAAA;
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
  
  @Override
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
