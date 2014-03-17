package roquen.util;

public enum Generics 
{
  ;
  
  public static class Factory<T>
  {
    private final Class<T> clazz;
    
    public Factory(Class<T> type)
    {
      clazz = type;
    }
    
    public final Class<T> getType() { return clazz; }
    
    public T[] newArray(int len)
    {
      return Generics.newArray(clazz, len);
    }
    
    public T[] newFilledArray(int len)
    {
      T[] a = newArray(len);
      
      for(int i=0; i<len; i++)
        a[i] = newInstance();
   
      return a;
    }
    
    
    public T newInstance()
    {
      try {
        return clazz.newInstance();
      } catch (InstantiationException|IllegalAccessException e) {
        return null;
      }
    }
  }

  
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
