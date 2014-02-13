package roquen.util;

public enum Generics 
{
  ;

  
  public static<T> T[] newArray(T obj, int len)
  {
    Class<?> clazz = obj.getClass();
    
    @SuppressWarnings("unchecked")
    T[] r = (T[])java.lang.reflect.Array.newInstance(clazz, len);
    
    return r;
  }
  
  public static<T> T[] newArray(Class<T> clazz, int len)
  {
    @SuppressWarnings("unchecked")
    T[] r = (T[])java.lang.reflect.Array.newInstance(clazz, len);
    
    return r;
  }

  /** 
   * Only for weak type requirements. Actual type of the
   * array is Object[].
   */
  public static <T> T[] newArray(int len) 
  {
    @SuppressWarnings("unchecked")
    T[] r   = (T[])new Object[len];
    
    return r;
  }
}
