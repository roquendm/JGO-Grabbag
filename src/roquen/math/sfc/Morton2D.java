package roquen.math.sfc;

import roquen.fake.Vect2i;

/**
 * Static utilities for 2-to-1 mappings based on Morton/Z-Order/Lebesgue curve.
 */
public enum Morton2D {
  ;
  
  

  /** bit positions of scattered X */
  protected static final int MASK_X = 0x55555555;

  /** bit positions of scattered Y */
  protected static final int MASK_Y = MASK_X<<1;

  /** Increase the represented Y coordinate by one. */
  public static int incY(int code)
  {
    //int r = (code | MASK_X) + 2;
    //return (r & MASK_Y) | (code & MASK_X);

    int mx = MASK_X;
    int x  = code & mx;
    int my = mx+mx;
    int y  = code & my;

    y |= mx; y += 2; y &= my;

    return x|y;
  }

  /** Decrease the represented Y coordinate by one. */
  public static int decY(int code)
  {
    //int r = (code & MASK_Y) - 2;
    //return (r & MASK_Y) | (code & MASK_X);

    int mx = MASK_X;
    int x  = code & mx;
    int my = mx+mx;
    int y  = code & my;

    y -= 2; y &= my;

    return x|y;
  }

  /** Increase the represented X coordinate by one. */
  public static int incX(int code)
  {
    //int r = (code | MASK_Y) + 1;
    //return (r & MASK_X) | (code & MASK_Y);

    int my = MASK_Y;
    int mx = my >>> 1;    // MASK_X
    int x  = code | my;   // set bits for carry
    int y  = code & my;   // isolate Y

    x += 1; x &= mx;      // add and isolate

    return x|y;           // rebuild code
  }

  /** Decrease the represented X coordinate by one. */
  public static int decX(int code)
  {
    //int r = (code & MASK_X) - 1;
    //return (r & MASK_X) | (code & MASK_Y);

    int mx = MASK_X;
    int x  = code & mx;   // isolate X
    int my = mx+mx;       // MASK_Y
    int y  = code & my;   // isolate Y

    x -= 1; x &= mx;      // sub and isolate

    return x|y;           // rebuild code
  }

  /**
   * (ax,ay)+(bx,by)
   */
  public static int add(int a, int b)
  {
    int x = (a | MASK_Y) + (b & MASK_X);
    int y = (a | MASK_X) + (b & MASK_Y);
    return  (x & MASK_X) | (y & MASK_Y);
  }

  /**
   * (ax,ay)-(bx,by)
   */
  public static int sub(int a, int b)
  {
    int x = (a & MASK_X) - (b & MASK_X);
    int y = (a & MASK_Y) - (b & MASK_Y);
    return  (x & MASK_X) | (y & MASK_Y);
  }

  /**
   * (min(ax,bx), min(ay,by))
   */
  public static int min(int a, int b)
  {
    int xdiff = (a & MASK_X) - (b & MASK_X);
    int ydiff = (a >> 1 & MASK_X) - (b >> 1 & MASK_X);

    int maskx = xdiff >> 31;
    int masky = ydiff >> 31;

    int xmin = (maskx & a) | (~maskx & b);
    int ymin = (masky & a) | (~masky & b);

    return (xmin & MASK_X) | (ymin & MASK_Y);
  }

  /**
   * (max(ax,bx), max(ay,by))
   */
  public static int max(int a, int b)
  {
    int xdiff = (a & MASK_X) - (b & MASK_X);
    int ydiff = (a >> 1 & MASK_X) - (b >> 1 & MASK_X);
    int maskx = (xdiff >> 31);
    int masky = (ydiff >> 31);
    int xmin = (~maskx & a) | (maskx & b);
    int ymin = (~masky & a) | (masky & b);
    return ((xmin & MASK_X) | (ymin & MASK_Y));
  }

  /** 
   * Scatter the lower 16-bits into even bit positions.
   * x on [0,0xffff].
   */
  public static int scatter1(int x)
  {
    //x = x & 0xFFFF;
    x = (x ^ (x <<  8)) & 0x00FF00FF; //16
    x = (x ^ (x <<  4)) & 0x0F0F0F0F; // 8
    x = (x ^ (x <<  2)) & 0x33333333; // 4
    x = (x ^ (x <<  1)) & MASK_X; 

    return x;
  }

  /**
   * Gather all the even bits into the lower 16. Odd
   * bits must be already zero.
   */
  public static int gather1_(int x)
  {
    x = (x ^ (x >>> 1)) & 0x33333333; 
    x = (x ^ (x >>> 2)) & 0x0F0F0F0F; 
    x = (x ^ (x >>> 4)) & 0x00FF00FF; 
    x = (x ^ (x >>> 8)) & 0x0000FFFF; 
    return x;
  }

  /**
   * Gather all the even bits into the lower 16.
   */
  public static int gather1(int x)
  {
    x &= MASK_X;
    x = (x ^ (x >>> 1)) & 0x33333333; 
    x = (x ^ (x >>> 2)) & 0x0F0F0F0F; 
    x = (x ^ (x >>> 4)) & 0x00FF00FF; 
    x = (x ^ (x >>> 8)) & 0x0000FFFF; 
    return x;
  }

  /** 
   *
   */
  public static int encode4(Vect2i v)
  {
    return encode4(v.x,v.y);
  }

  /** 
   * x,y on [0,0xF]
   */
  public static int encode4(int x, int y)
  {
    // dependency chain = 5 in 9 ops 
    x = x * 0x10101010 & 0x80402010;
    y = y * 0x10101010 & 0x80402010;
    x = x * 0x204081;
    y = y * 0x204081;
    x = x >>> 25;
    y = y >>> 24;
    return x|y;    
  }
  
  /**
   * 
   */
  public static void decode4(Vect2i p, int code)
  {
    int i = code+code;
    p.x = ((0xee44ee44 >> i) & 3);
    p.y = ((0xfafa5050 >> i) & 3);
  }

  /** 
   *
   */
  public static int encode5(Vect2i v)
  {
    return encode5(v.x, v.y);
  }

  /** 
   * x,y on [0,0x1F]
   */
  public static int encode5(int x, int y)
  {
    // dependency chain = 6 in 11 ops
    //x &= 0x0000001f; y &= 0x0000001f;
    x *= 0x01041041; y *= 0x01041041;
    x &= 0x10204081; y &= 0x10204081;
    x *= 0x00108421; y *= 0x00108421;
    x &= 0x15500000; y &= 0x15500000;
    return((x >>> 20) | (y >>> 19));
  }

  /** 
   */
  public static void decode5(Vect2i v, int code)
  {
    int x = code;
    int y = code >>> 1;
    x &= 0x00000155; y &= 0x00000155;
    x |= (x >>> 1);  y |= (y >>> 1);
    x &= 0x00000133; y &= 0x00000133;
    x |= (x >>> 2);  y |= (y >>> 2);
    x &= 0x0000010f; y &= 0x0000010f;
    x |= (x >>> 4);  y |= (y >>> 4);
    x &= 0x0000001f; y &= 0x0000001f;
    v.x = x;
    v.y = y;
  }

  /**
   * x,y on [0,0xFF]
   */
  public static final int encode8(int x, int y)
  {
    x = (int)((x * 0x0101010101010101L & 0x8040201008040201L) * 0x0102040810204081L >>> 49) & 0x5555;
    y = (int)((y * 0x0101010101010101L & 0x8040201008040201L) * 0x0102040810204081L >>> 48) & 0xAAAA;

    return x|y;
  }

  /** 
   * x,y on [0,0xFFFF]
   */
  public static int encode16(int x, int y)
  {
    //x &= 0x0000ffff; y &= 0x0000ffff;
    x |= (x << 8);   y |= (y << 8);
    x &= 0x00ff00ff; y &= 0x00ff00ff;
    x |= (x << 4);   y |= (y << 4);
    x &= 0x0f0f0f0f; y &= 0x0f0f0f0f;
    x |= (x << 2);   y |= (y << 2);
    x &= 0x33333333; y &= 0x33333333;
    x |= (x << 1);   y |= (y << 1);
    x &= MASK_X; y &= MASK_X;
    return(x|(y<<1));
  }

  /** 
   *
   */
  public final static void decode16(Vect2i v, int code)
  {
    int x = code;
    int y = code >>> 1;
    x &= MASK_X; y &= MASK_X;
    x |= (x >>> 1);  y |= (y >>> 1);
    x &= 0x33333333; y &= 0x33333333;
    x |= (x >>> 2);  y |= (y >>> 2);
    x &= 0x0f0f0f0f; y &= 0x0f0f0f0f;
    x |= (x >>> 4);  y |= (y >>> 4);
    x &= 0x00ff00ff; y &= 0x00ff00ff;
    x |= (x >>> 8);  y |= (y >>> 8);
    x &= 0x0000ffff; y &= 0x0000ffff;
    v.x = x;
    v.y = y;
  }
  
  @SuppressWarnings("boxing")
  public static void main(String[] args)
  {
    Vect2i p = new Vect2i();
    
    int px = 0;
    int py = 0;
    
    for(int i=0; i<16; i++) {
      int e = encode4(i&3,(i>>2)&3);
      px |= (i&3) << (e+e);
      py |= ((i>>2)&3) << (e+e);
      //System.out.printf("{%d,%d},",p.x,p.y);
    }
     
    System.out.printf("%s,%s\n",Integer.toBinaryString(px),Integer.toBinaryString(py));
    
    for(int i=0; i<16; i++) {
      decode4(p,i);
      System.out.printf("{%d,%d},",p.x,p.y);
    }
  }
}
