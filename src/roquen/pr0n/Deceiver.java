package roquen.pr0n;

/**
 * Template class for lying to the JVM about the type of an
 * array.  Cannot be directly loaded, {@link Dissembler} must
 * loaded first.  It works converting the casting check into
 * nops and loading the class via 
 * {@link sun.misc.Unsafe#defineClass(String, byte[], int, int, ClassLoader, java.security.ProtectionDomain) Unsafe.defineClass}
 * to bypass the verifier.
 * <p>
 *  Doing this is very dangerous since once the compiler sees
 *  calling code it will be likely to notice that the references
 *  to multiple views of the same array are alias and removable.
 *  It will then be in untested cases of the backend compiler.
 */
public enum Deceiver 
{
  ;
  
  static {
    // Fail straight away if not loaded properly.
    ClassLoader loader = Deceiver.class.getClassLoader();
    if (loader != null)
      throw new Error("Dissembler must be loaded before Deceiver");
  }
  
  public static int[]   asI(Object obj) {  return (int[])obj; }
  public static float[] asF(Object obj) {  return (float[])obj; }
}
