package roquen.vm;

import sun.misc.Unsafe;

/** temp hack - hotspot only (more or less) */
public enum UnsafeUtils
{
  ;

  // temp testing hack -- of no use as is
  public enum ArrayType
  {
    BOOLEAN(boolean[].class),
    BYTE   (byte[].class),
    SHORT  (short[].class),
    CHAR   (char[].class),
    INT    (int[].class),
    FLOAT  (float[].class),
    LONG   (long[].class),
    DOUBLE (double[].class),
    OBJECT (Object[].class)
    ;

    public final Class<?> clazz;
    //private final int headerSize;
    //private final int elementSize;

    ArrayType(Class<?> c)
    {
      clazz = c;
      //headerSize  = unsafe.arrayBaseOffset(c);
      //elementSize = unsafe.arrayIndexScale(c);
    }

    public int getHeaderSize()
    {
      //return headerSize;
      return unsafe.arrayBaseOffset(clazz);
    }
    
    public int getElementSize()
    {
      //return elementSize;
      return unsafe.arrayIndexScale(clazz);
    }
    
  }
  
  
  public static final sun.misc.Unsafe unsafe;
  
  // fill-in unsafe
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
   * <i>true</i> if references are stored in 64-bits. 64-bit VM and no compressed oops.
   */
  public static final boolean longPointer = Unsafe.ARRAY_OBJECT_INDEX_SCALE == 8;
  
  /**
   * <i>true</i> if VM is 64-bit.
   */
  public static final boolean is64Bit = Unsafe.ADDRESS_SIZE == 8;
}
