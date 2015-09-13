package roquen.pr0n;

import java.nio.file.*;

import sun.misc.Unsafe;
import static roquen.vm.UnsafeUtils.*;
//import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public enum Dissembler
{
  ;

  /**
   * <i>true</i> if VM oop headers appear to be in the expected layout.
   * <p>
   */
  public static final boolean hasExpectedOopHeaders = checkHeaders();

  private static final boolean checkHeaders()
  {
    // expecting:
    // mark   (native) = ADDRESS_SIZE
    // class  (4 for 32/64+coop, 8 for 64) = ARRAY_OBJECT_INDEX_SCALE
    // length (if array = 4)
    // pad    (if needed to align to 64-bits for 64-bit elements)

    int AS = Unsafe.ADDRESS_SIZE;              // mark work (native pointer)
    int PS = Unsafe.ARRAY_OBJECT_INDEX_SCALE;  // class pointer (perhaps compressed)

    // The 'length' entry for an array should match
    // 'value' of the single 32-bit field object
    Integer iw = null;

    for (int i=1; i<16; i++) {
      iw = Integer.valueOf(i);
      if (unsafe.getInt(iw, (long)(PS+AS)) != i)
        return false;
    }

    int  c0 = unsafe.getInt(iw, 0L);
    long c1 = getAddress(Integer.class);
    System.out.println(c0 + " " + c1);
    System.identityHashCode(iw);
    c0 = unsafe.getInt(iw, 0L);
    c1 = getAddress(Integer.class);
    System.out.println(c0 + " " + c1);


    // T {0=32 bit, 1=64+coop, 2=64}
    @SuppressWarnings("unused")
    int t = ((Unsafe.ARRAY_OBJECT_INDEX_SCALE+Unsafe.ADDRESS_SIZE) >> 2)-2;

    // on 64-bit we expect all array headers to be 24/16 (for coop)
    // on 32-bit: 12 for 32-bit elements and smaller, otherwise 16

    /*
    for (int i=0; i<3; i++) {
      Integer t0 = Integer.valueOf(i);
      int     ex = unsafe.getInt(t0, (long)(PS+AS));
      System.out.println(ex);
    }
    */

    // TODO: complete the check
    return true;
  }

  /** No protection ATM. Only call once, not thread-safe */
  public static void loadDeceiver(String base) throws Exception
  {
    Path path = FileSystems.getDefault().getPath(base, "roquen", "pr0n", "Deceiver.class");
    byte[] b = java.nio.file.Files.readAllBytes(path);

    // TODO: Not reliable.  Just a quick hack
    for(int i=0; i<b.length-5; i++) {

      // look for bytecode sequence: aload_0, checkcast(u16), areturn
      if (b[i] == 0x2a && b[i+1] == (byte)0xC0  && b[i+4] == (byte)0xb0) {
        b[i+1] = b[i+2] = b[i+3] = 0; // make checkcast nops
      }
    }

    // system ClassLoader and ProtectionDomain: go go evil!
    unsafe.defineClass("roquen.pr0n.Deceiver", b, 0, b.length, null, null);
  }



  public static int[] asI(Object o)
  {
    int[][]  i = new int[1][];
    Object[] t = new Object[] {o};

    // the loop is to handle the unlikely case that the GC
    // moves 'o' between getting the raw address and placing it.
    do {
      if (longPointer) {
        long a = unsafe.getLong(t, (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET);
        unsafe.putLong(i, (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET, a);
      } else {
        int a = unsafe.getInt(t,  (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET);
        unsafe.putInt(i,  (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET, a);
      }

      // This construction has the same potential problem as
      // Deceiver's.  The pointers can be seen to be aliases.
      if (i[0] == o)
        return i[0];

    } while(true);
  }

  public static float[] asF(Object o)
  {
    // SEE: asI
    float[][] f = new float[1][];
    Object[]  t = new Object[] {o};

    do {
      if (longPointer) {
        long a = unsafe.getLong(t, (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET);
        unsafe.putLong(f, (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET, a);
      } else {
        int a = unsafe.getInt(t,  (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET);
        unsafe.putInt(f,  (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET, a);
      }

      if (f[0] == o)
        return f[0];

    } while(true);
  }


  //*****************************

  private static String hexString(byte[] a)
  {
    int len = a.length - 1;

    StringBuilder b = new StringBuilder();
    b.append('[');

    for (int i = 0; ; i++) {
      int  v = a[i];
      int  t = (v >> 4) & 0xf;
      char c = (t <= 9) ? (char)('0'+t) : (char)('A'+t-10);

      b.append(c);
      t = a[i] & 0xf;
      c = (t <= 9) ? (char)('0'+t) : (char)('A'+t-10);
      b.append(c);

      if (i == len)
        return b.append(']').toString();

      b.append(", ");
    }
  }

  public static void main(String[] args) throws Exception
  {
    loadDeceiver("bin");
    System.out.println(hasExpectedOopHeaders);

    Integer wi = Integer.valueOf(666);
    int[]   zz = asI(wi);
    System.out.println(zz.length);

    java.util.ArrayList<byte[]> foo = new java.util.ArrayList<>();
    java.util.ArrayList<byte[]> bar;

    //System.out.println("address size: " + Unsafe.ADDRESS_SIZE);
    //System.out.println(Unsafe.ARRAY_OBJECT_BASE_OFFSET);
    //System.out.println(Unsafe.ARRAY_OBJECT_INDEX_SCALE);

    // allocate some space to remove
    for(int i=0; i<50000; i++) {
      foo.add(new byte[1024]);
    }

    // now the arrays we're going to lie about.
    byte[]  ba = new byte[16];
    int[]   ia = asI(ba);//Deceiver.asI(ba);
    float[] fa = asF(ba);//Deceiver.asF(ba);
  //long    x  = getAddress(ba);

  //System.out.println(Long.toHexString(x));

    ia[0] = 0xa;
    fa[3] = 1.f;

    System.out.println(hexString(ba));

    do {
      foo.remove(foo.size()-1); // free up some space

      System.out.println(hexString(ba));
      System.gc(); // try to request a full GC.

      // just in case that didn't work let's fill up the heap
      bar = new java.util.ArrayList<>();
      try {
        while(true) {
          bar.add(new byte[0xFFFFFF]);
        }
      }
      catch(Error e) {
        //
      }
      bar.clear();

      if ((Object)ia != (Object)ba) { System.out.println("x"); }

      ia[0]++;
      ia[1]++;
      ia[2]++;
      fa[3] += .5f;
      System.out.println(hexString(ba)
          + " " + Long.toHexString(getAddress(ba))
          + " " + Long.toHexString(getAddress(ba))
          );
    } while(!foo.isEmpty());

  //System.out.println(x);
  }





}
