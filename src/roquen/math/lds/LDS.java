package roquen.math.lds;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class LDS 
{
  /** atomic count-down for parameterless constructors */
  protected static AtomicInteger mix = new AtomicInteger(-1);
}
