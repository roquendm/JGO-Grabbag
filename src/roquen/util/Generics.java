package roquen.util;

//import java.lang.reflect.*;
import java.util.function.Function;

public enum Generics 
{
  ;
  
  /** Helper class for creating arrays and instances of type T. */
  public static class Factory<T>
  {
    private final Class<T> clazz;
    private final Function<Class<T>,T> alloc; 
    
    /** */
    public Factory(Class<T> type)
    {
      clazz = type;
      alloc = Factory::defAlloc;
    }
    
    /** Specify the method of instance allocation */
    public Factory(Class<T> type, Function<Class<T>,T> gen)
    {
      clazz = type;
      alloc = gen != null ? gen : Factory::defAlloc; 
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
    
    private static<T> T defAlloc(Class<T> clazz)
    {
      try {
        return clazz.newInstance();
      } catch (InstantiationException|IllegalAccessException e) {
        return null;
      }
    }
    
    /** 
     * Instantiate an object of type 'T'.
     */
    public T newInstance()
    {
      return alloc.apply(clazz);
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
  
  /*
  // Don't look at me: thinking about evil things here
  @SuppressWarnings("unchecked")
  public static <T> T fooBar(Object obj) throws Exception
  {
    ParameterizedType superclass = (ParameterizedType)obj.getClass().getGenericSuperclass();
    Class<T> object = (Class<T>)superclass.getActualTypeArguments()[0];
    Class<?>[] params = {};
    Constructor<T> cons = object.getDeclaredConstructor(params);
    Object[] args = {};
    
    return cons.newInstance(args);
  }
  */
  
  
 
  public static void main(String[] args)
  {
    Factory<?> intFactory = new Factory<>(int.class);
    
    Object bar = intFactory.newArray(10);
    int[]  bari = (int[])bar;
    
    System.out.println(bari.getClass());
    
    //Factory<roquen.math.lds.Sobol1D> sobelGen = new Factory<>(roquen.math.lds.Sobol1D.class);
    //roquen.math.lds.Sobol1D lds = sobelGen.newInstance();
    
    //System.out.println(lds.next());
  }
  
}
