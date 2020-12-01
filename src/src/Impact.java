package src;

public abstract class Impact {
  public long duration = 5000;
  public abstract void fire(Unit u, double dmg);
  public abstract void release(Unit u);
}
