package roquen.interp;

import roquen.fake.Vect2f;

/**
 * For performing in-order walks of cells on a uniform 2D grid which
 * are crossed by a line segment or ray. All covered cells are visited
 * however it does not provide supercoverage.  Supercoverage is when
 * all four cells are visited in the case where the ray passes through
 * a corner.  For a corner case this will visit three out of four. At
 * each step the next cell will be either a horizontal or vertical move
 * so in the extreme pathological case this will visit "false positive"
 * cells, the worst case situation begin a 45 degree angle ray
 * perfectly through corners where half the visited cells will be
 * "false positives".  These false positives only occur when hitting
 * a corner so is of no concern unless the use-case is something like
 * rayshooting from the center of a cell in only horizontal, vertical or
 * diagonal directions which isn't the intended usage.
 * <p>
 * Passed in values make the assumption that the extent of cells are
 * of one unit.  User code scaling is required for other sizes.
 */
public class RayGrid2D 
{
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
    double dx = p1.x-p0.x;
    double dy = p1.y-p0.y;
    
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
