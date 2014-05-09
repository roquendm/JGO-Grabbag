package roquen.vm;

import sun.misc.Unsafe;

/** temp hack - hotspot only (more or less) */
public enum UnsafeUtils
{
  ;

  public static final sun.misc.Unsafe unsafe;
  
  static {
    java.lang.reflect.Field f;
    Unsafe u = null;
    try {
      f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      u = (Unsafe) f.get(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    unsafe = u;
  }
  
  /** 
   * <i>true</i> if pointers are 64-bits wide. 64-bit VM and no compressed oops.
   */
  public static final boolean longPointer = Unsafe.ARRAY_OBJECT_INDEX_SCALE == 8;

}
