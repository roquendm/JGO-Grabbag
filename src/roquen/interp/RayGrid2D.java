package roquen.interp;

import roquen.fake.Vect2f;

public class RayGrid2D 
{
  private double dx,dy;
  private double cx,cy;
  private double ax,ay;
  private int    ux,uy;
  
  /** Current cell coordinate */
  public int x,y;

  public void set(Vect2f p0, Vect2f p1)
  {
    // the coordinate of the first cell
    // TODO: change to floor if for some reason you want negative coordinates
    x = (int)p0.x; //(int)Math.floor(p0.x);
    y = (int)p0.y; //(int)Math.floor(p0.y);
 
    // direction of the ray
    dx = p1.x-p0.x;
    dy = p1.y-p0.y;
    
    // amount to move in each direction to cross a
    // cell boundary.
    cx = 1/dx;
    cy = 1/dy;
    
    // cross time accumulators. Initialized to
    // the first cross time.
    ax = cx * (1.0-(p0.x-x));
    ay = cy * (1.0-(p0.y-y));
    
    // step amounts for each direction
    ux = dx >= 0 ? 1 : -1;
    uy = dy >= 0 ? 1 : -1;
  }
  
  public void update()
  {
    // the next cell is the one with the smallest
    // crossing time.
    if (ax < ay) {
      ax += cx;
      x  += ux;
    } else {
      ay += cy;
      y  += uy;      
    }
  }
  
  
}
