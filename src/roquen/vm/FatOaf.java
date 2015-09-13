package roquen.vm;

import static sun.misc.Unsafe.*;
import static roquen.vm.UnsafeUtils.*;

/**
 * Fat off-heap Overlaid Array Format
 */

// Experimental evil hack for comparison vs. put/get and direct buffers
@SuppressWarnings("restriction")
public class FatOaf
{
  /** */
  public final byte[]  b;

  /** */
  public final int[]   i;

  /** */
  public final float[] f;

  private final long baseAddress;

  // just to shorten things a bit
  private static final int F = ARRAY_FLOAT_BASE_OFFSET;
  private static final int I = ARRAY_INT_BASE_OFFSET;
  private static final int B = ARRAY_BYTE_BASE_OFFSET;

  private static final int HEADER_LEN = F+I+B;

  /**  */
  private static final long template;

  // TODO: correctly build these
  private static final int F_LEN_OFF = instanceHeaderSize;
  private static final int I_LEN_OFF = F+instanceHeaderSize;
  private static final int B_LEN_OFF = F+I+instanceHeaderSize;

  /** initial index of the data for the integer array */
  public static final int I_OFF = B >> 2;

  /** initial index of the data for the float array */
  public static final int F_OFF = I_OFF + (I >> 2);

  /** initial index of the data for the byte array */
  public static final int B_OFF = 0;

  static {
    long a = template = unsafe.allocateMemory(HEADER_LEN);
    float[] f = new float[3];
    int[]   i = new int[4];
    byte[]  b = new byte[5];

    // TODO: verify length offset
    unsafe.copyMemory(f, 0, null, a, F); a += F;
    unsafe.copyMemory(i, 0, null, a, I); a += I;
    unsafe.copyMemory(b, 0, null, a, B);

    int lf = unsafe.getInt(template + F_LEN_OFF);
    int li = unsafe.getInt(template + I_LEN_OFF);
    int lb = unsafe.getInt(template + B_LEN_OFF);

    if (lf == 3 && li == 4 && lb == 5) {
      unsafe.putInt(template + F_LEN_OFF, Integer.MAX_VALUE);
      unsafe.putInt(template + I_LEN_OFF, Integer.MAX_VALUE);
      unsafe.putInt(template + B_LEN_OFF, Integer.MAX_VALUE);
    }
    else throw new RuntimeException("oops");
  }

  // TODO: make temp arrays if not expected headers found

  public FatOaf(int len, boolean lie)
  {
    len = (len + 3) & ~3;

    long size = len + HEADER_LEN;

    baseAddress = unsafe.allocateMemory(size);

    unsafe.copyMemory(null, template, null, baseAddress, HEADER_LEN);

    // TODO: temp hack
    f = (float[])getReference(baseAddress);
    i = (int[])  getReference(baseAddress + F);
    b = (byte[]) getReference(baseAddress + F+I);

    if (!lie) {
      unsafe.putInt(baseAddress + F_LEN_OFF, (len >> 2) + I_OFF);
      unsafe.putInt(baseAddress + I_LEN_OFF, (len >> 2) + F_OFF);
      unsafe.putInt(baseAddress + B_LEN_OFF, len + B_OFF);
    }

  }

  public static void main(String[] args)
  {
    FatOaf oaf = new FatOaf(44, true);
    int[]   i = oaf.i;
    byte[]  b = oaf.b;
    float[] f = oaf.f;

    System.out.println(F + " " + I + " " + B);

    System.out.println(f.getClass() + " " + f.length);
    System.out.println(i.getClass() + " " + i.length);
    System.out.println(b.getClass() + " " + b.length);

    System.out.println(HEADER_LEN);
    System.out.println(I_OFF + " " + F_OFF);

    b[3] = 0x12;
    b[2] = 0x34;
    b[1] = 0x56;
    b[0] = 0x78;

    System.out.println(i[I_OFF] == 0x12345678);

    f[F_OFF] = 1.f;

    System.out.println(Integer.toHexString(i[I_OFF]));

  }
}
