package roquen.pr0n;

import java.lang.reflect.Field;
import java.nio.file.*;

import sun.misc.Unsafe;

public enum Dissembler
{
  ;
  public static sun.misc.Unsafe unsafe;
  public static Deceiver deceiver;

  static {
    Field f;
    try {
      f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      unsafe = (Unsafe) f.get(null);
      
      build();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private static void build() throws Exception
  {
    Path path = FileSystems.getDefault().getPath("bin", "roquen", "pr0n", "Deceiver.class");
    byte[] b = java.nio.file.Files.readAllBytes(path);
    
    // TODO: Not reliable.  Just a quick hack
    for(int i=0; i<b.length-5; i++) {
      
      // look for bytecode sequence: aload_0, checkcast(u16), areturn
      if (b[i] == 0x2a && b[i+1] == (byte)0xC0  && b[i+4] == (byte)0xb0) {
        b[i+1] = b[i+2] = b[i+3] = 0; // make nop
      }
    }
    
    
    // system ClassLoader and ProtectionDomain: go go evil!
    Class<?> clazz = unsafe.defineClass("roquen.pr0n.Deceiver", b, 0, b.length, null, null);
    deceiver = (Deceiver)clazz.newInstance();
  }
  
  
  public static void main(String[] args)
  {
    java.util.ArrayList<byte[]> foo = new java.util.ArrayList<>();
    java.util.ArrayList<byte[]> bar;

    // allocate some space to remove
    for(int i=0; i<50000; i++) {
      foo.add(new byte[1024]);
    }

    // now the arrays we're going to lie about.
    byte[] ba = new byte[16];
    int[]  ia = Deceiver.asI(ba);
    
    ia[0] = 0xdeadbeef;
    
    do {
      foo.remove(foo.size()-1); // free up some space
      
      System.out.println(java.util.Arrays.toString(ba));
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

      ia[0]++;
      ia[1]++;
      ia[2]++;
      System.out.println(java.util.Arrays.toString(ba));
    } while(!foo.isEmpty());
  }
  
}
