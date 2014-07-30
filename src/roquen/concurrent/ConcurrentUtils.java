package roquen.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * http://g.oswego.edu/dl/jmm/cookbook.html 
 */
public enum ConcurrentUtils 
{
  ;
  // 
  private static volatile int dummy;
  private static final AtomicInteger ai = new AtomicInteger();
  
  
  public static int loadLoadBarrier()
  {
    return dummy;
  }
  
  public static void loadStoreBarrier()
  {
    dummy = 0;
  }
  
  public static void storeStoreBarrier()
  {
    ai.lazySet(-1);
  }
}
