package roquen.wiki.dvar;

/**
 * Minimal example of implementing a limited form of prototype based variables.
 */

abstract class UserVar
{
  /** */
  public enum TYPE
  {
    // these are here mostly to allow the holding container to perform
    // in-place updates, which the example container "GameObject" does
    // not.
    NONE,
    INTEGER,
    FLOAT,
    STRING,
    ENTITY
    ;
  }
  
  /** */
  abstract int    getInt();
  
  /** */
  abstract float  getFloat();
  
  /** */
  abstract String getString();
  
  /** */
  abstract Entity getEntity();
  
  /** */
  abstract TYPE   getType();

  // Sentinel for non-existent variables
  public static final UserVar NILL = new UserVar()
  {
    @Override final int    getInt()    { return 0; }
    @Override final float  getFloat()  { return 0; }
    @Override final String getString() { return ""; }
    @Override final Entity getEntity() { return Entity.NILL; }
    @Override final TYPE   getType()   { return TYPE.NONE; }
  };
  
  // NOTE: Could easily be a double instead. Given small object size padding
  // it's likely wouldn't even increase memory footprint.
  static final class FLOAT extends UserVar
  {
    float value;

    FLOAT(float v) { value = v; }

    @Override final int    getInt()    { return (int)value; }
    @Override final float  getFloat()  { return value; }
    @Override final String getString() { return String.valueOf(value); }
    @Override final Entity getEntity() { return Entity.NILL; }
    @Override final TYPE   getType()   { return TYPE.FLOAT; }
  }

  // NOTE: Could easily be a long instead. Given small object size padding
  // it's likely wouldn't even increase memory footprint.
  static final class INT extends UserVar
  {
    int value;

    INT(int v) { value = v; }

    @Override final int    getInt()    { return value; }
    @Override final float  getFloat()  { return value; }
    @Override final String getString() { return String.valueOf(value); }
    @Override final Entity getEntity() { return Entity.NILL; } // could lookup UID if used
    @Override final TYPE   getType()   { return TYPE.INTEGER; }
  }

  static final class STRING extends UserVar
  {
    String value;

    STRING(String v) { value = v; }

    @Override final int    getInt()    { return 0; }  // parsing would be better
    @Override final float  getFloat()  { return 0; }  // paring would be better
    @Override final String getString() { return value; }
    @Override final Entity getEntity() { return Entity.NILL; } // could look up tag
    @Override final TYPE   getType()   { return TYPE.STRING; }
  }

  static final class ENTITY extends UserVar
  {
    Entity value;

    ENTITY(Entity v) { value = v; }

    @Override final int    getInt()    { return 0; }  // or UID if used
    @Override final float  getFloat()  { return 0; }  // ditto as int
    @Override final String getString() { return ""; } // or something else
    @Override final Entity getEntity() { return value; }
    @Override final TYPE   getType()   { return TYPE.ENTITY; }
  }
}