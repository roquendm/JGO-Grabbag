package roquen.math.sfc;

import roquen.fake.Vect2i;

public abstract class Index2D<T extends Index2D<T>>
{
  /** Scattered version of x. Publicly visible for copy/storage/restoring. */
  public int sx;
  
  /** Scattered version of y. Publicly visible for copy/storage/restoring. */
  public int sy;
  
  /** returns the current index */
  public abstract int  getIndex();
  
  public abstract void set(T v);
  
  /** Set the represented 2D coordinate */
  public abstract void set(Vect2i v);
  
  public abstract void setX(int x);
  
  public abstract void setY(int y);
  
  /** Fill 'v' with the represented 2D coordinate */
  public abstract void get(Vect2i v);
  
  public abstract int getX();
  public abstract int getY();
  
  /** (x, y) -> (x+1, y) */
  public abstract void incX();
  
  /** (x, y) -> (x+1, y) */
  public abstract void decX();
  
  /** (x, y) -> (x, y+1) */
  public abstract void incY();
  
  /** (x, y) -> (x, y+1) */
  public abstract void decY();
  
  /** */
  public abstract void add(T v);  
}
