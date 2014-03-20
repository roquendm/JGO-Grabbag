package roquen.util;

public enum Generics 
{
  ;
  
  /** Helper class for creating arrays and instances of type T. */
  public static class Factory<T>
  {
    private final Class<T> clazz;
    
    public Factory(Class<T> type)
    {
      clazz = type;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Factory<T> get(T obj)
    {
      return (Factory<T>)new Factory<>(obj.getClass());
    }
    
    /** */
    public final Class<T> getType() { return clazz; }
    
    /** */
    public final T[] newArray(int len)
    {
      return Generics.newArray(clazz, len);
    }
    
    /** Returns an array of length 'n' filled with instances created by {@link #newInstance()}*/
    public T[] newFilledArray(int n)
    {
      T[] a = newArray(n);
      
      for(int i=0; i<n; i++)
        a[i] = newInstance();
   
      return a;
    }
    
    /** 
     * Instantiate an object of type 'T'. If not overloaded this method uses
     *  
     */
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
