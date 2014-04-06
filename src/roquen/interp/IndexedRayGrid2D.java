package roquen.interp;

import roquen.fake.Vect2f;
import roquen.math.sfc.*;

/**
 * An example abstraction for testing if using alternate indexing is a win.
 */

public class IndexedRayGrid2D // could extends WhatEver once an indexing scheme is choice
{
  private double  cx,cy;
  private double  ax,ay;
  private boolean ux,uy;
 
  // TODO: go away in production if this was used
  private final Index2D<?> map;
  
  // TODO: go away in production if this was used
  public IndexedRayGrid2D(Index2D<?> map)
  {
    this.map = map;
  }
 
  public int set(float x0, float y0, float x1, float y1)
  {
    // the coordinate of the first cell
    // TODO: change to floor if for some reason you want negative coordinates
    int x = (int)x0; //(int)Math.floor(p0.x);
    int y = (int)y0; //(int)Math.floor(p0.y);
 
    map.setX(x);
    map.setY(y);
 
    // direction of the ray
    double dx = x1-x0;
    double dy = y1-y0;
    
    // amount to move in each direction to cross a
    // cell boundary.
    cx = 1/dx;
    cy = 1/dy;
    
    // cross time accumulators. Initialized to
    // the first cross time.
    ax = cx * (1.0-(x0-x));
    ay = cy * (1.0-(y0-y));
    
    // movement directions
    ux = dx >= 0;
    uy = dy >= 0;
    
    // TODO: temp hack
    int ex = (int)x1;
    int ey = (int)y1;
    int nx = Math.abs(ex-x);
    int ny = Math.abs(ey-y);
    int n;
    
    // todo: compute number of off major axis steps
    if (nx > ny) {
      n = nx;
    } else {
      n = ny;
    }
    
    return n;
  }
  
  public int set(Vect2f p0, Vect2f p1)
  {
    return set(p0.x,p0.y,p1.x,p1.y);
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
  
  public int getIndex()
  {
    return map.getIndex();
  }
  
  
}
