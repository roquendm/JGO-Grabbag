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
    private final int headerSize;
    private final int elementSize;

    ArrayType(Class<?> c)
    {
      clazz = c;
      headerSize  = unsafe.arrayBaseOffset(c);
      elementSize = unsafe.arrayIndexScale(c);
    }

    /** */
    public int getHeaderSize()
    {
      return headerSize;
      //return unsafe.arrayBaseOffset(clazz);
    }
    
    /** */
    public int getElementSize()
    {
      return elementSize;
      //return unsafe.arrayIndexScale(clazz);
    }
    
    /** */
    public long sizeof(int len)
    {
      return getHeaderSize() + ((long)getElementSize())*len;
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
  
  /**
   * <i>true</i> if hardware stores data in little endian format
   */
  public static final boolean isLittleEndian;
  
  static {
    long a = unsafe.allocateMemory(4);
    unsafe.putAddress(a, 0x12345678);
    byte b = unsafe.getByte(a);
    unsafe.freeMemory(a);
    isLittleEndian = b == (byte)0x78;
  }
  
  public static final int instanceHeaderSize;
  
  static {
    // TODO: this assumes hotspot...add some checking
    instanceHeaderSize = Unsafe.ADDRESS_SIZE + Unsafe.ARRAY_OBJECT_INDEX_SCALE; 
  }
  
  
  //public static java.lang.reflect.Constructor<?> c;
  //public static final java.lang.invoke.MethodHandle mh;
  
  static {
    java.lang.reflect.Constructor<?> m;
    
    try {
      Class<?> dbb = ClassLoader.getSystemClassLoader().loadClass("java.nio.DirectByteBuffer");
      m = dbb.getDeclaredConstructor(long.class, int.class, Object.class);
      m.setAccessible(true);
    //mh = java.lang.invoke.MethodHandles.lookup().unreflectConstructor(m).asType(java.lang.invoke.MethodType.methodType(java.nio.ByteBuffer.class, long.class, int.class, Object.class));
    }
    catch(Throwable t) {/**/}
  }
  
  //riven
  /**
   *  Convert a raw address into a reference. The GC could have moved
   *  the actual object between if the value is 'on-heap'.
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
}
