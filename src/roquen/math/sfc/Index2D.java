package roquen.math.sfc;

import roquen.fake.Vect2i;

/**
 * Base class of maintaining a 2-to-1 representation of a 2D coordinate.
 */
public abstract class Index2D<T extends Index2D<T>>
{
  /** Scattered version of x. Publicly visible for copy/storage/restoring. */
  public int sx;
  
  /** Scattered version of y. Publicly visible for copy/storage/restoring. */
  public int sy;
  
  /** Returns the current index */
  public abstract int  getIndex();
  
  /** Copies the represented coordinate 'v' into this. */
  public abstract void set(T v);
  
  /** Set the represented 2D coordinate */
  public abstract void set(Vect2i v);
  
  /** Sets the represented X coordinate */
  public abstract void setX(int x);
  
  /** Sets the represented X coordinate */
  public abstract void setY(int y);
  
  public void set(int x, int y)
  {
    setX(x); setY(y);
  }
  
  /** Fill 'v' with the represented 2D coordinate */
  public abstract void get(Vect2i v);
  
  /** Gets the represented X coordinate */
  public abstract int getX();
  
  /** Gets the represented Y coordinate */
  public abstract int getY();
  
  /** Increment the represented X coordinate: (x, y) -> (x+1, y) */
  public abstract void incX();
  
  /** Decrement the represented X coordinate: (x, y) -> (x+1, y) */
  public abstract void decX();
  
  /** Increment the represented Y coordinate: (x, y) -> (x, y+1) */
  public abstract void incY();
  
  /** Decrement the represented Y coordinate: (x, y) -> (x, y+1) */
  public abstract void decY();
  
  /** */
  public abstract void add(T v);
  
  /** */
  public abstract void sub(T v);
}
