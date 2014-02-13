package roquen.util;

public class IntPair<T> implements Comparable<IntPair<T>>
{
  public T   data;
  public int value;
  
  @Override
  public int compareTo(IntPair<T> o) {
    return value - o.value;
  }
 
  public static <T> void sort(java.util.List<IntPair<T>> list)
  {
    java.util.Collections.sort(list);
  }
  
  public static <T> IntPairArray<T> flatten(java.util.List<IntPair<T>> list) 
  {
    int len = list.size();
    int i   = 0;
    
    IntPairArray<T> r = new IntPairArray<>();
    r.t = Generics.newArray(len);
    r.f = new float[len];
    
    for(IntPair<T> e : list) {
      r.t[i] = e.data;
      r.f[i] = e.value;
      i++;
    }
    
    return r;
  }
  
  
}
