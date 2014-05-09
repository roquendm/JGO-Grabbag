package roquen.pr0n;

import java.nio.file.*;

import sun.misc.Unsafe;
import static roquen.vm.UnsafeUtils.*;
//import sun.misc.Unsafe;

public enum Dissembler
{
  ;

  
  /** No protection ATM. Only call once, not thread-safe */
  public static void loadDeceiver(String base) throws Exception
  {
    Path path = FileSystems.getDefault().getPath(base, "roquen", "pr0n", "Deceiver.class");
    byte[] b = java.nio.file.Files.readAllBytes(path);
    
    // TODO: Not reliable.  Just a quick hack
    for(int i=0; i<b.length-5; i++) {
      
      // look for bytecode sequence: aload_0, checkcast(u16), areturn
      if (b[i] == 0x2a && b[i+1] == (byte)0xC0  && b[i+4] == (byte)0xb0) {
        b[i+1] = b[i+2] = b[i+3] = 0; // make nop
      }
    }
     
    // system ClassLoader and ProtectionDomain: go go evil!
    unsafe.defineClass("roquen.pr0n.Deceiver", b, 0, b.length, null, null);
  }
  
  
  //riven
  /**
   *  Convert a raw address into a reference. The GC could have moved
   *  the actual object between.
   */
  public static Object getReference(long a)
  {
     Object[] t = new Object[1];

    if (longPointer)
      unsafe.putLong(t, (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET, a);
    else
      unsafe.putInt(t,  (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET, (int)a);

    return t[0];
 }

  // riven
  /** Convert a reference into a raw address. */
  public static long getAddress(Object o)
  {
    Object[] t = new Object[] {o};
    long a;
    
    if (longPointer)
      a = unsafe.getLong(t, (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET);
    else 
      a = unsafe.getInt(t,  (long)Unsafe.ARRAY_OBJECT_BASE_OFFSET) & 0xFFFF_FFFFL;
  
    return a;
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
    
    java.util.ArrayList<byte[]> foo = new java.util.ArrayList<>();
    java.util.ArrayList<byte[]> bar;

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
          + " " + Long.toHexString(Dissembler.getAddress(ba))
          + " " + Long.toHexString(Dissembler.getAddress(ba))
          );
    } while(!foo.isEmpty());
    
  //System.out.println(x);
  }




  
}
