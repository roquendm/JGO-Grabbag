package roquen.interp;

/**
 * For performing in-order walks of cells on a uniform 2D grid which
 * are crossed by a line segment or ray.
 * <p>
 * Passed in values make the assumption that the extent of cells are
 * of one unit.  User code scaling is required for other sizes.
 * <p>
 * The name is actually a misnomer as the computation is in floating
 * point.  This drastically reduces the complexity of the update.
 */
public class DDA2D {
  // NOTE: Computation is in doubles.  Usage requires little memory
  // motion and the extra bits allow for larger grids.
  
  //------ SNIP: Placeholder classes, replace with the real thing
  public static class Vect2f
  {
    public float x,y;
  }
  //------ SNIP
  
  /** 
   * use-case specific cell offset. default=0 (lower left).
   * set to .5 for center of cell for example.
   */
  private double offset;
  
  /** current number of steps (integer) */
  private double step;
  
  /** inverse the number of steps (one rounding error) */
  private double in;
  
  /** coordinate of cell containing the first point (integer) */
  private double x0,y0;
  
  /** integer displacement from first to last cell (integer) */
  private double dx,dy;
  
  /** current step (cell) coordinate, includes fractional */
  public double x,y;
  
  /**
   * Sets the instance to iterate over all cells covered
   * by the line segment from p0 to p1 in order.  Return
   * the number of steps required.
   * <p>
   * The fields ({@link #x}, {@link #y}) are
   * set to the first cell coordinate.
   */
  public int set(Vect2f p0, Vect2f p1)
  {
    // find the cell coordinate of the end points
    x0 = Math.floor(p0.x);
    y0 = Math.floor(p0.y);
    
    double x1 = Math.floor(p1.x);
    double y1 = Math.floor(p1.y);
    
    // initialize current cell coordinate
    x = x0 + offset;
    y = y0 + offset;
    step = 1;
    
    // calculate the deltas and find the maximum magnitude
    dx = x1-x0;
    dy = y1-y0;
    
    double ax = Math.abs(dx);
    double ay = Math.abs(dy);
    double n  = ax > ay ? ax : ay;

    // calculate the step values
    in = 1.0/n;
    
    return (int)n;
  }
  
  public final void setCenteredMode(boolean mode)
  {
    if (mode)
      offset = 0.5;
    else
      offset = 0.0;
  }
  
  /**
   * Updates ({@link #x}, {@link #y}) to the next cell 
   * coordinate, including fractional parts.
   * <p>
   * In the default mode (no center) taking the floor is the
   * coordinate of the cell. If they are insured to be positive, 
   * then truncate (cast) to integers.
   * <p>
   * Calling more times than the required number of steps will
   * continue to visit cells in the same direction.
   */
  public void next()
  {
    // formulation for no compounding of errors across steps
    double n = step*in;
    x    = x0 + n*dx;
    y    = y0 + n*dy;
    step += 1;
  }
}