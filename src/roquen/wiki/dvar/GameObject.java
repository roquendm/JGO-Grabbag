package roquen.wiki.dvar;

import java.util.*;


/**
 * <h2>User Variables</h2>
 * <p>
 * User variables allow designers to assign additional data to the various
 * exposed {@link GameObject}. This allows designing scripts which are
 * data-driven and building of functionality of which the code base is not
 * specifically aware.
 * <p>
 * These <i>user variables</i> are logically <i>typeless</i>, that is to 
 * say that no specific type associated with a given variable. At any point
 * in time the active type will be that of the most recent assignment. Any
 * unassigned variable query will return a default value of that type and
 * any existing variable which is assigned this default value will automatically
 * be deleted.
 * <p>
 *
 */
abstract class GameObject<T extends GameObject<T>>
{ 
  //***
  static final String EMPTY_STRING = "";
  
  static final String stringFilter(String s)
  {
    if (s != null) return s;
    return EMPTY_STRING;
  }
  
  
  //*** dynamic variable support stuff
  
  HashMap<String, UserVar> dvars = null;
  
  final UserVar getVar(String name)
  {
    if (dvars != null) {
      UserVar v = dvars.get(name);
      
      if (v != null)
        return v;
    }
    
    return UserVar.NILL;
  }
  
  /**
   * Returns <i>true</i> if there is a dynamic variable of the
   * specified name defined.
   */
  public final boolean isVarDefined(String name)
  {
    return getVar(name) != UserVar.NILL;
  }
  
  /**
   * Returns the value of the variable as an integer.
   * <p>
   * If the name is currently assigned to a <tt>float</tt> then
   * the result is the same as: <tt>(int)getFloat(name)</tt>.  In
   * all other cases, including undefined, the result is zero.
   */
  public final int getInt(String name)
  {
    return getVar(name).getInt();
  }
  
  /**
   * Returns the value of the variable as a float.
   * <p>
   * If the name is currently assigned to a <tt>int</tt> then
   * the result is the same as: <tt>getInt(name)</tt>.  In
   * all other cases, including undefined, the result is zero.
   */
  public final float getFloat(String name)
  {
    return getVar(name).getFloat();
  }
  
  /**
   * Return the value of the variable as a {@link Entity}.
   * <p>
   * If the variable is question is not entity or is
   * undefined, then the result is {@link Entity.NILL}.
   */
  public final Entity getEntity(String name)
  {
    return getVar(name).getEntity();
  }
  
  /**
   * Returns the value of the variable as a {@link String}.
   * <p>
   * If the variable is currently assigned an integer or a float, then
   * the result is that value converted into a string.  In all other
   * cases, including undefined, the result is an empty string.
   */
  public final String getString(String name)
  {
    return  getVar(name).getString();
  }

  private final void deleteVar(String name)
  {
    if (dvars != null) {
      dvars.remove(name);
      
      if (dvars.size() != 0)
        return;
      
      dvars = null;
    }
  }

  private final void setVar(String name, UserVar value)
  {
    if (dvars == null) dvars = new HashMap<>();
    dvars.put(name, value);
  }

  // NOTE: All the sets could be optimized. Such as not creating
  // a new UserVar.TYPE object if one already exists and is of
  // the same type. Additionally if the new value if a different
  // type in int vs. float from current but is perfectly
  // representable in current, then current type could be used.
  
  /**
   * Sets or creates a dynamic variable with the specified name and value.
   * <p>
   * <tt>setInt(0)</tt> automatically deletes.
   */
  public final void setInt(String name, int value)
  {
    if (value != 0)
      setVar(name, new UserVar.INT(value));
    else
      deleteVar(name);
  }
  
  /**
   * Sets or creates a dynamic variable with the specified name and value.
   * <p>
   * <tt>setFloat(0)</tt> automatically deletes.
   */
  public final void setFloat(String name, float value)
  {
    if (value != 0)
      setVar(name, new UserVar.FLOAT(value));
    else
      deleteVar(name);
  }
  
  /**
   * Sets or creates a dynamic variable with the specified name and value.
   * <p>
   * <tt>setEntity(null)</tt> automatically deletes.
   */
  public final void setEntity(String name, Entity value)
  {
    if (value != null && value != Entity.NILL)
      setVar(name, new UserVar.ENTITY(value));
    else
      deleteVar(name);
  }
  
  /**
   * Sets or creates a dynamic variable with the specified name and value.
   * <p>
   * <tt>setString(null)</tt> automatically deletes.
   */
  public final void setString(String name, String value)
  {
    if (value != null && value.length() != 0)
      setVar(name, new UserVar.STRING(value));
    else
      deleteVar(name);
  }
}
