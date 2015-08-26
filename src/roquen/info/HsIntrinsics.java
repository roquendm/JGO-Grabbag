package roquen.info;

import roquen.math.Float32;

// java -cp bin -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading -XX:+LogCompilation -XX:+PrintAssembly roquen.info.HsIntrinsics
public class HsIntrinsics 
{
  private static roquen.math.rng.XorStar64 rng = new roquen.math.rng.XorStar64();
 
 
  // 1.8.0 (60) - performs the division (reasonable)
  public static void spMulInvert(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = 1.f/s[i];
    }
  }
  
  // 1.8.0 (60) - SSE converts to double, stores to memory,
  // load into x87, op, and back.
  //---
  // 
  public static void spSin(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = (float)Math.sin(s[i]);
    }
  }
  
  // 1.8.0 (60) - moving back and forth between SSE and x87 via memory - ouch
  //---
  // 
  public static void spMultiX87(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = (float)(Math.cos((Math.PI/2.0)*Math.sin(s[i])));
    }
  }
  
  // becomes a move, likewise for reverse
  public static void spRawBits(float[] d, float[] s)
  {
    int len = d.length;
    
    for(int i=0; i<len; i++) {
      d[i] = Float.floatToRawIntBits(s[i]);
    }
  }
  
  // Single precision functions (including casting result) from Math
  
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
  
  // 1.8.0 (60) -
  //  movd r11d, xmm1
  //  movd r10d, xmm0
  //  and  r11d, 0x7fffffff
  //  and  r10d, 0x80000000
  //  or   r10d, r11d
  //  movd xmm0, r10d
  public static void spCopySign(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = Math.copySign(d[i],s[i]); }
  }
  
  // branching as per java-source
  public static void spAbs(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = Math.abs(s[i]); }
  }
  
  // converts to double..and standardish mess from there
  public static void spFloor(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = (float)Math.floor(s[i]); }
  }
  
  // converts to double..and standardish mess from there
  public static void spCeiling(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = (float)Math.ceil(s[i]); }
  }
  
  // perfectly sane
  public static void spGetExp(int[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = Math.getExponent(s[i]); }
  }
  
  // transformed to: sqrtss
  public static void spSqrt(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = (float)Math.sqrt(s[i]); }
  }
  
  private static final int SIZE = 1<<24;
  
  
  public static void spBinaryOpDriver()
  {
    float[] d = new float[SIZE];
    float[] s = new float[2*SIZE];
    int[]   di = new int[SIZE];
    
    for(int i=0; i<2*SIZE; i++) {
      s[i] = 100.f*(rng.nextFloat()+1.f);
    }
    
    for(int i=0; i<10; i++) {
      spMulInvert(d,s);
      spCopySign(d,s);
      //spLocalMin(d,s);
      spSin(d,s);
      spFloor(d,s);
      spCeiling(d,s);
      spGetExp(di,s);
      spSqrt(d,s);
      spMultiX87(d,s);
    }
  }
  
  
  public static void main(String[] args)
  {
    spBinaryOpDriver();
  }
  
  
}
