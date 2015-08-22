package roquen.info;

import roquen.math.Float32;

// java -cp bin -XX:+UnlockDiagnosticVMOptions -XX:-Inline -XX:+TraceClassLoading -XX:+LogCompilation -XX:+PrintAssembly roquen.info.HsIntrinsics
public class HsIntrinsics 
{
  private static roquen.math.rng.XorStar64 rng = new roquen.math.rng.XorStar64();
  
  public static void spDivision(float[] d, float[] s)
  {
    int len = d.length;
    int si  = 0;
    
    for(int i=0; i<len; i++) {
      d[i] = s[si++]/s[si++];
    }
  }
  
  public static void spMulInvertF(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = 1.f/s[i];
    }
  }
  
  // 1.8.0 (60) - converts to double, calls method & convert to single
  public static void spSin(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = (float)Math.sin(s[i]);
    }
  }
  
  public static float copySign(float r, float s)
  {
    int is = Float.floatToRawIntBits(r) >> 31;
    int ir = Float.floatToRawIntBits(r) << 1;
    int ix = is >>> 1;

    ir ^= is;
    ir  = (ir >>> 1) ^ ix;
    
    return Float.intBitsToFloat(ir);
  }
  
  public static void spCopySign(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = Math.copySign(d[i],s[i]);
    }
  }
  
  public static final float rsqrt_cl(float x)
  {
    int   i = 0x5f375a86 - (Float.floatToRawIntBits(x) >>> 1);
    float g = Float.intBitsToFloat(i);
    float h = 0.5f * x;
    g  = g*(1.5f-h*g*g);    
    return g;
  }
  
  public static final float rsqrt_cl2(float x)
  {
    int   i = 0x5f375a86 - (Float.floatToRawIntBits(x) >>> 1);
    float g = Float.intBitsToFloat(i);
    float h = 0.5f*g;
    g  = h*(3.f-x*g*g);    
    return g;
  }
  
  public static final float rsqrt_1(float x, float g)
  {
    float hx = x * 0.5f;
    g  = g*(1.5f-hx*g*g);    
    return g;
  }
  
  public static void spRawBits(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = rsqrt_cl2(s[i]);
    }
  }
  
  public static void spLocalAbs(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = Float32.abs(s[i]);
    }
  }
  
  public static void spLocalMin(float[] d, float[] s)
  {
    int len = d.length;
    int si  = 0;
    
    for(int i=0; i<len; i++) {
      d[i] = Float32.min(s[si++],s[si++]);
    }
  }
  
  private static final int SIZE = 1<<24;
  
  
  public static void spBinaryOpDriver()
  {
    float[] d = new float[SIZE];
    float[] s = new float[2*SIZE];
    
    for(int i=0; i<2*SIZE; i++) {
      s[i] = 100.f*(rng.nextFloat()+1.f);
    }
    
    for(int i=0; i<10; i++) {
      //spDivision(d,s);
      //spMulInvertF(d,s);
      //spCopySign(d,s);
      spLocalMin(d,s);
      
    }
    
  }
  
  
  public static void main(String[] args)
  {
    spBinaryOpDriver();
  }
  
  
}
