package roquen.vm;

import java.io.*;
import java.lang.management.*;

import com.sun.management.*;

public enum VMQuery
{
  ;
  private static final HotSpotDiagnosticMXBean bean;
  
 private static VMOption none = new VMOption("none", "null", false, VMOption.Origin.DEFAULT);
  
 static {
   HotSpotDiagnosticMXBean r = null;

   try {
     r = ManagementFactory.newPlatformMXBeanProxy(
         ManagementFactory.getPlatformMBeanServer(),
         "com.sun.management:type=HotSpotDiagnostic",
         HotSpotDiagnosticMXBean.class);
   } catch (IOException e) {

     e.printStackTrace();
   }
   bean = r;
  }
  
  public static VMOption getOption(String name)
  {
    try {
      return bean.getVMOption(name);
    }
    catch(Throwable t) { /* */ }
    
    return none;
  }
  
  @SuppressWarnings("boxing")
  public static boolean getBooleanOption(String name)
  {
    return Boolean.valueOf(getOption(name).getValue());
  }
 
  @SuppressWarnings("boxing")
  public static int getIntOption(String name)
  {
    return Integer.valueOf(getOption(name).getValue());
  }
  
  public static String getStringOption(String name)
  {
    return getOption(name).getValue();
  }
  
  public static VMOption getCompressOops()
  {
    return getOption("UseCompressedOops");
  }
  
  public static boolean isCompressedOops() 
  {
    return getBooleanOption("UseCompressedOops");
  }
  
  public static boolean isCompressedClassPointer() 
  {
    return getBooleanOption("UseCompressedClassPointers");
  }
  
  public static void main(String[] args)
  {
    System.out.println(getOption("UseCompressedOops"));
    System.out.println(getOption("UseCompressedClassPointers"));
  }
}
