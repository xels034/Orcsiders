package src;

import gui.Hexfield;

import java.util.Comparator;

import org.newdawn.slick.Image;

public abstract class Entity {
  public final int layer;
  public Vector2D pos;
  public boolean griddable;
  public double life;
  public Hexfield.BuffState type;
  public float rotation;
  
  public static class EntityHeightComparator implements Comparator<Entity>{
    @Override
    public int compare(Entity arg0, Entity arg1) {
      //bolts, layer -1, should always be on top:
      if(arg0.layer == -1) return 1;
      if(arg1.layer == -1) return -1;
      
      int signum = (int)Math.signum(arg0.pos.y - arg1.pos.y);
      //just because sets don't like doubles, but units on the same height should still be rendered
      if(signum == 0) return -1;
      return signum;
    }
  }
  
  public static class EntityRLDistanceComparator implements Comparator<Entity>{
    private Vector2D ref;
    
    public EntityRLDistanceComparator(Vector2D r){
      ref=r;
    }
    
    @Override
    public int compare(Entity a, Entity b){      
      double aDist = ref.sub(a.pos).length2();
      double bDist = ref.sub(b.pos).length2();
      int signum = (int)Math.signum(aDist - bDist);
      if(signum == 0) return -1;
      else            return signum;
    }
  }
  
  public Entity(int i, Vector2D p, Hexfield.BuffState s){
    type = s;
    layer = i;
    pos = p.copy();
    life = 1;
  }
  
  public abstract Image getImage();
  public abstract void update(double dt);
}
