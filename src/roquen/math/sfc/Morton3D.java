package roquen.math.sfc;

import roquen.fake.Vect3i;

/**
 * Static utilities for 3-to-1 mappings based on Morton/Z-Order/Lebesgue curve.
 */
public enum Morton3D {
  ;
  
  public static int encode5(Vect3i v)
  {
    return encode5(v.x,v.y,v.z);
  }
  
  public static int encode5(int x, int y, int z)
  {
  //x &= 0x0000001f; y &= 0x0000001f; z &= 0x0000001f;
    x *= 0x01041041; y *= 0x01041041; z *= 0x01041041;
    x &= 0x10204081; y &= 0x10204081; z &= 0x10204081;
    x *= 0x00011111; y *= 0x00011111; z *= 0x00011111;
    x &= 0x12490000; y &= 0x12490000; z &= 0x12490000;
    return((x >>> 16) | (y >>> 15) | (z >>> 14));
  }

  public static void decode5(Vect3i v, int code)
  {
    int x = code;
    int y = code >>> 1;
    int z = code >>> 2;
    x &= 0x00001249; y &= 0x00001249; z &= 0x00001249;
    x |= (x >>> 2);  y |= (y >>> 2);  z |= (z >>> 2);
    x &= 0x000010c3; y &= 0x000010c3; z &= 0x000010c3;
    x |= (x >>> 4);  y |= (y >>> 4);  z |= (z >>> 4);
    x &= 0x0000100f; y &= 0x0000100f; z &= 0x0000100f;
    x |= (x >>> 8);  y |= (y >>> 8);  z |= (z >>> 8);
    x &= 0x0000001f; y &= 0x0000001f; z &= 0x0000001f;
    v.x = x;
    v.y = y;
    v.z = z;
  }

  public static int encode10(Vect3i v)
  {
    return encode10(v.x,v.y,v.z);
  }
  
  public static int encode10(int x, int y, int z)
  {
  //x &= 0x000003ff; y &= 0x000003ff; z &= 0x000003ff;
    x |= (x << 16);  y |= (y << 16);  z |= (z << 16);
    x &= 0x030000ff; y &= 0x030000ff; z &= 0x030000ff;
    x |= (x << 8);   y |= (y << 8);   z |= (z << 8);
    x &= 0x0300f00f; y &= 0x0300f00f; z &= 0x0300f00f;
    x |= (x << 4);   y |= (y << 4);   z |= (z << 4);
    x &= 0x030c30c3; y &= 0x030c30c3; z &= 0x030c30c3;
    x |= (x << 2);   y |= (y << 2);   z |= (z << 2);
    x &= 0x09249249; y &= 0x09249249; z &= 0x09249249;
    return(x | (y << 1) | (z << 2));
  }
  
  public static void decode10(Vect3i v, int code)
  {
    int x = code;
    int y = code >>> 1;
    int z = code >>> 2;
    x &= 0x09249249; y &= 0x09249249; z &= 0x09249249;
    x |= (x >>> 2);  y |= (y >>> 2);  z |= (z >>> 2);
    x &= 0x030c30c3; y &= 0x030c30c3; z &= 0x030c30c3;
    x |= (x >>> 4);  y |= (y >>> 4);  z |= (z >>> 4);
    x &= 0x0300f00f; y &= 0x0300f00f; z &= 0x0300f00f;
    x |= (x >>> 8);  y |= (y >>> 8);  z |= (z >>> 8);
    x &= 0x030000ff; y &= 0x030000ff; z &= 0x030000ff;
    x |= (x >>> 16); y |= (y >>> 16); z |= (z >>> 16);
    x &= 0x000003ff; y &= 0x000003ff; z &= 0x000003ff;
    v.x = x;
    v.y = y;
    v.z = z;
  }
}
