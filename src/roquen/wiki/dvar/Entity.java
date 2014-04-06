package roquen.wiki.dvar;

// fake stub base class
public class Entity extends GameObject<Entity>
{  
  // the 'class' of this entity
  final ArchType type;
  
  Entity(ArchType def)
  {
    type = def;
  }
  
  public void runAI() {
    // put any preconditions that apply to everyone here
  
    // invoke the open class method
    type.aiMethod.run(this);
    
    // put any postconditions that apply to everyone here
  }
  
  // no entity proxy
  public static final Entity NILL = new Entity(ArchType.NILL);
}
