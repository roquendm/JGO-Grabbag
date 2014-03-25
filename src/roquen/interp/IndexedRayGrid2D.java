package roquen.interp;

import roquen.fake.Vect2f;
import roquen.math.sfc.*;

/**
 * An example abstraction for testing if using alternate indexing is a win.
 */

public class IndexedRayGrid2D // could extends WhatEver once an indexing scheme is choice
{
  private double  dx,dy;
  private double  cx,cy;
  private double  ax,ay;
  private boolean ux,uy;
 
  // TODO: go way in production if this was used
  private final Index2D<?> map;
  
  // TODO: go way in production if this was used
  public IndexedRayGrid2D(Index2D<?> map)
  {
    this.map = map;
  }
 
  public void set(Vect2f p0, Vect2f p1)
  {
    // the coordinate of the first cell
    // TODO: change to floor if for some reason you want negative coordinates
    int x = (int)p0.x; //(int)Math.floor(p0.x);
    int y = (int)p0.y; //(int)Math.floor(p0.y);
 
    map.setX(x);
    map.setY(y);
 
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
    
    // movement directions
    ux = dx >= 0;
    uy = dy >= 0;
  }
  
  public void update()
  {
    // the next cell is the one with the smallest
    // crossing time.
    if (ax < ay) {
      ax += cx;
      if (ux) map.incX(); else map.decX();
    } else {
      ay += cy;
      if (uy) map.incY(); else map.decY();      
    }
  }
  
  
}
