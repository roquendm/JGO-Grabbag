package roquen.info;

import roquen.math.Float32;

// java -cp bin -XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading -XX:+LogCompilation -XX:+PrintAssembly roquen.info.HsIntrinsics

//@SuppressWarnings("unused")
public class HsIntrinsics
{
  private static roquen.math.rng.XorStar64 rng = new roquen.math.rng.XorStar64();


  // 1.8.0 (60) - performs the division.  Is generating divps.
  private static void recip(float[] d, float[] s)
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
  private static void sin(float[] d, float[] s)
  {
    int len = d.length;

    for(int i=0; i<len; i++) {
      d[i] = (float)Math.sin(s[i]);
    }
  }

  // 1.8.0 (60) - does not fold, explicit individual calls to fsin & fcos
  // moving the parameter each time from SSE->Memory->x87.
  private static void spSinCos(float[] d, float[] s)
  {
    int len = d.length>>1;
    int di  = 0;

    for(int i=0; i<len; i++) {
      d[di++] = (float)Math.sin(s[i]);
      d[di++] = (float)Math.cos(s[i]);
    }
  }

  // 1.8.0 (60) - moving back and forth between SSE and x87 via memory - ouch
  //---
  //
  private static void spMultiX87(float[] d, float[] s)
  {
    int len = d.length;

    for(int i=0; i<len; i++) {
      d[i] = (float)(Math.cos((Math.PI/2.0)*Math.sin(s[i])));
    }
  }

  // becomes a move, likewise for reverse
  private static void getIntBit(float[] d, float[] s)
  {
    int len = d.length;

    for(int i=0; i<len; i++) {
      d[i] = Float.floatToRawIntBits(s[i]);
    }
  }

  // Single precision functions (including casting result) from Math

  private static void localAbs(float[] d, float[] s)
  {
    int len = d.length;

    for(int i=0; i<len; i++) {
      d[i] = Float32.abs(s[i]);
    }
  }

  private static void localMin(float[] d, float[] s)
  {
    int len = d.length;
    int si  = 0;

    for(int i=0; i<len; i++) {
      d[i] = Float32.min(s[si++],s[si++]);
    }
  }

  /** generates: addps */
  private static void sum(float[] d, float[] a, float[] b)
  {
    int len = d.length;

    for(int i=0; i<len; i++) {
      d[i] = a[i]+b[i];
    }
  }

  /** does not generate addps  */
  private static void sump4s(float[] d, float[] a, float[] b)
  {
    int len = d.length>>2;
    int i   = 0;

    while(i<len) {
      d[i] = a[i]+b[i]; i++;
      d[i] = a[i]+b[i]; i++;
      d[i] = a[i]+b[i]; i++;
      d[i] = a[i]+b[i]; i++;
    }
  }

  /** does not generate addps  */
  private static void sump4sa(float[] d, float[] a, float[] b)
  {
    int len = d.length>>2;
    int i   = 0;

    while(i<len) {
      d[i  ] = a[i]  +b[i  ];
      d[i+1] = a[i+1]+b[i+1];
      d[i+2] = a[i+2]+b[i+2];
      d[i+3] = a[i+3]+b[i+3];
      i += 4;
    }
  }

  // generates: mulps
  private static void mul(float[] d, float[] a, float[] b)
  {
    int len = d.length;

    for(int i=0; i<len; i++) {
      d[i] = a[i]*b[i];
    }
  }

  // 1.8.0 (60) -
  //  movd r11d, xmm1
  //  movd r10d, xmm0
  //  and  r11d, 0x7fffffff
  //  and  r10d, 0x80000000
  //  or   r10d, r11d
  //  movd xmm0, r10d
  private static void copySign(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = Math.copySign(d[i],s[i]); }
  }

  // branching as per java-source
  private static void abs(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = Math.abs(s[i]); }
  }

  // converts to double..and standardish mess from there
  private static void floor(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = (float)Math.floor(s[i]); }
  }

  // converts to double..and standardish mess from there
  private static void ceiling(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = (float)Math.ceil(s[i]); }
  }

  // perfectly sane
  private static void getExp(int[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = Math.getExponent(s[i]); }
  }

  // transformed to: sqrtss (no sqrtps)
  private static void sqrt(float[] d, float[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = (float)Math.sqrt(s[i]); }
  }

  private static final int SIZE = 1<<20;


  private static void floatOps()
  {
    float[] d = new float[SIZE];
    float[] s = new float[2*SIZE];
    int[]   di = new int[SIZE];

    for(int i=0; i<2*SIZE; i++) {
      s[i] = 100.f*(rng.nextFloat()+1.f);
    }

    for(int i=0; i<10; i++) {
      recip(d,s);
      copySign(d,s);
      localMin(d,s);
      localAbs(d,s);
      sin(d,s);
      spSinCos(d,s);
      floor(d,s);
      ceiling(d,s);
      getExp(di,s);
      sqrt(d,s);
      spMultiX87(d,s);
      abs(d, s);
      sum(d,s,s);
      sump4s(d,s,s);
      sump4sa(d,s,s);
      mul(d,s,s);
    }
  }

  // branching as per java-source
  private static void abs(double[] d, double[] s)
  {
    int len = d.length;
    for(int i=0; i<len; i++) { d[i] = Math.abs(s[i]); }
  }


  private static void doubleOps()
  {
    double[] d = new double[SIZE];
    double[] s = new double[2*SIZE];

    for(int i=0; i<2*SIZE; i++) {
      s[i] = 100.f*(rng.nextDouble()+1.f);
    }

    for(int i=0; i<10; i++) {
      abs(d, s);
    }
  }


  private static void max(int[] d, int[] s)
  {
    int len = d.length;
    int si  = 0;
    for(int i=0; i<len; i++) { d[i] = Math.max(s[si++],s[si++]); }
  }

  private static void min(int[] d, int[] s)
  {
    int len = d.length;
    int si  = 0;
    for(int i=0; i<len; i++) { d[i] = Math.max(s[si++],s[si++]); }
  }

  private static void intOps()
  {
    int[] d = new int[SIZE];
    int[] s = new int[2*SIZE];

    for(int i=0; i<2*SIZE; i++) {
      s[i] = rng.nextInt();
    }

    for(int i=0; i<10; i++) {
      max(d,s);
      min(d,s);
    }
  }

  public static void main(String[] args)
  {
    floatOps();
    doubleOps();
    intOps();
  }
}
