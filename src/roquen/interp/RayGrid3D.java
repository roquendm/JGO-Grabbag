package roquen.interp;

import roquen.fake.Vect3f;

public class RayGrid3D {
  private double dx,dy,dz;
  private double cx,cy,cz;
  private double ax,ay,az;
  private int    ux,uy,uz;
  
  /** Current cell coordinate */
  public int x,y,z;

  public void set(Vect3f p0, Vect3f p1)
  {
    // the coordinate of the first cell
    x = (int)Math.floor(p0.x);
    y = (int)Math.floor(p0.y);
    z = (int)Math.floor(p0.y);
 
    // direction of the ray
    dx = p1.x-p0.x;
    dy = p1.y-p0.y;
    dz = p1.z-p0.z;
    
    // amount to move in each direction to cross a
    // cell boundary.
    cx = 1/dx;
    cy = 1/dy;
    cz = 1/dz;
    
    // cross time accumulators. Initialized to
    // the first cross time.
    ax = cx * (1.0-(p0.x-x));
    ay = cy * (1.0-(p0.y-y));
    az = cz * (1.0-(p0.z-z));
    
    // step amounts for each direction
    ux = dx >= 0 ? 1 : -1;
    uy = dy >= 0 ? 1 : -1;
    uz = dz >= 0 ? 1 : -1;
  }
  
  public void update()
  {
    // the next cell is the one with the smallest
    // crossing time.
    if (ax < ay) {
      if (ax < az) {
        ax += cx;
        x  += ux;
      }
    } else if (ax < az) {
      ay += cy;
      y  += uy;
      return;
    }
    az += cz;
    z  += uz;
  }
}
