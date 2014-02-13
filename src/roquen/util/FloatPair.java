package roquen.util;

public class FloatPair<T> implements Comparable<FloatPair<T>>
{
  public T     data;
  public float value;
  
  public FloatPair(T e, float v)
  {
    data  = e;
    value = v;
  }

  @Override
  public int compareTo(FloatPair<T> o) {
    float d = value - o.value;
    if (d < 0) return -1;
    if (d > 0) return  1;
    return 0;
  }
 
  public static <T> void sort(java.util.List<FloatPair<T>> list)
  {
    java.util.Collections.sort(list);
  }
  
  public static <T> FloatPairArray<T> flatten(java.util.List<FloatPair<T>> list) 
  {
    int len = list.size();
    int i   = 0;
    
    FloatPairArray<T> r = new FloatPairArray<>();
    r.t = Generics.newArray(len);
    r.f = new float[len];
    
    for(FloatPair<T> e : list) {
      r.t[i] = e.data;
      r.f[i] = e.value;
      i++;
    }
    
    return r;
  }
  
}
