package roquen.pg.noise;

public final class ValueNoise extends Noise
{

  private static final float mix(int x)
  {
    int h = hashM2(x); 

    return normalizeI(h);
  }

  /** Sample 1D function */
  public static final float eval(float x)
  {
    int   ix = pseudoFloor(x);
    float dx = ease(x-ix);
    float r0 = mix(ix);
    float r1 = mix(ix+1);

    return lerp(dx, r0, r1);
  }

  private static final float mix(int x, int y)
  {
    return normalizeI(hashM2(x,y));
  }

  /** Sample 2D function */
  public static final float eval(float x, float y) 
  {
    // get the coordinate of the cell
    int ix = pseudoFloor(x);
    int iy = pseudoFloor(y);

    // compute the offset into the cell, then weight it
    x = ease(x-ix);
    y = ease(y-iy);

    // compute hash values for the four vertices of the cell and
    // convert into a uniform float
    float r00 = mix(ix,   iy);
    float r10 = mix(ix+1, iy);
    float r01 = mix(ix,   iy+1);
    float r11 = mix(ix+1, iy+1);

    float xb = lerp(x, r00, r10);  // lerp bottom edge
    float xt = lerp(x, r01, r11);  // lerp top edge

    return lerp(y, xb, xt);  // lerp the two edges into the final result
  }

  private static final float mix(int x, int y, int z)
  {
    int h = hashM2(x,y,z); 

    return normalizeI(h);
  }

  /** Sample 3D function */
  public static final float eval(float x, float y, float z) 
  {
    float t0,t1,t2,t3,t4;

    // lower left hand coordinate of cell
    int ix = pseudoFloor(x);
    int iy = pseudoFloor(y);
    int iz = pseudoFloor(z);

    // offset into cell, then convert to weighting value
    x = ease(x-ix);
    y = ease(y-iy);
    z = ease(z-iz);

    // bottom edge of forward face
    t0 = mix(ix,   iy, iz);
    t1 = mix(ix+1, iy, iz);
    t2 = lerp(x, t0, t1);

    // top edge of forward face
    t0 = mix(ix,   iy+1, iz);
    t1 = mix(ix+1, iy+1, iz);
    t3 = lerp(x, t0, t1);
    t3 = lerp(y, t2, t3);                // final result in forward face

    // bottom edge of back face
    t0 = mix(ix,   iy, iz+1);
    t1 = mix(ix+1, iy, iz+1);
    t2 = lerp(x, t0, t1);

    // top edge of back face
    t0 = mix(ix,   iy+1, iz+1);
    t1 = mix(ix+1, iy+1, iz+1);
    t4 = lerp(x, t0, t1);
    t4 = lerp(y, t2, t4);

    return lerp(z, t3, t4);
  }
}
