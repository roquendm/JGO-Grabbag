package roquen.vm;

import java.io.*;
import java.lang.management.*;

import com.sun.management.*;

// tossed together for some testing...might be useful in the future.
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
  
  public static boolean getBooleanOption(String name)
  {
    return Boolean.parseBoolean(getOption(name).getValue());
  }
 
  public static int getIntOption(String name)
  {
    return Integer.parseInt(getOption(name).getValue());
  }
  
  public static String getStringOption(String name)
  {
    return getOption(name).getValue();
  }
  
  public static boolean isCompressedOops() 
  {
    return getBooleanOption("UseCompressedOops");
  }
  
  public static boolean isCompressedClassPointer() 
  {
    return getBooleanOption("UseCompressedClassPointers");
  }
  
  //------------
  public static void dumpAll()
  {
    javax.management.MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    java.util.Set<javax.management.ObjectInstance> list = server.queryMBeans(null, null);
  
    for(javax.management.ObjectInstance oi : list) {
      System.out.println("class:  " + oi.getClassName());
      System.out.println("object: " + oi.getObjectName());
    }
  }
  
  
  public static void main(String[] args)
  {
    dumpAll();
    //System.out.println(getOption("PrintC"));
    System.out.println(getOption("PrintCompilation2"));
  }
}
