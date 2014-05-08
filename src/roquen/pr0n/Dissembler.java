package roquen.pr0n;

import java.lang.reflect.Field;
import java.nio.file.*;

import sun.misc.Unsafe;

public enum Dissembler
{
  ;
  public static sun.misc.Unsafe unsafe;

  static {
    Field f;
    try {
      f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      unsafe = (Unsafe) f.get(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
        b[i+1] = b[i+2] = b[i+3] = 0; // make nop
      }
    }
     
    // system ClassLoader and ProtectionDomain: go go evil!
    unsafe.defineClass("roquen.pr0n.Deceiver", b, 0, b.length, null, null);
  }
  
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

    System.out.println(Unsafe.ARRAY_OBJECT_BASE_OFFSET);
    
    // allocate some space to remove
    for(int i=0; i<50000; i++) {
      foo.add(new byte[1024]);
    }

    // now the arrays we're going to lie about.
    byte[]  ba = new byte[16];
    int[]   ia = Deceiver.asI(ba);
    float[] fa = Deceiver.asF(ba);
    
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
      System.out.println(hexString(ba));
    } while(!foo.isEmpty());
  }
  
}
