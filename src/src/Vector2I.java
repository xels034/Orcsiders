package src;

import java.util.Objects;

public class Vector2I {
  public int x;
  public int y;
  

  public Vector2I(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  public Vector2I add(Vector2I v2){
    return new Vector2I(this.x + v2.x, this.y + v2.y);
  }
  
  public Vector2I add(int i){
    return new Vector2I(x+i, y+i);
  }

  public Vector2I sub(Vector2I v2){
    return new Vector2I(this.x - v2.x, this.y - v2.y);
  }

  public Vector2I mult(int scalar){
    return new Vector2I(this.x * scalar, this.y * scalar);
  }

  public double skalarProduct(Vector2I v2){
    return (this.x * v2.x + this.y * v2.y);
  }

  public double length2() {
    return (this.x * this.x + this.y * this.y);
  }
  
  public double length() {
    return (Math.sqrt(this.length2()));
  }

   public static double length2(Vector2I v1, Vector2I v2) {
    Vector2D v3 = new Vector2D(v1.x - v2.x, v1.y - v2.y);
    return v3.length2();
  }

  public static double length(Vector2I v1, Vector2I v2) {
    Vector2D v3 = new Vector2D(v1.x - v2.x, v1.y - v2.y);
    return v3.length();
  }
  
  public Vector2D toVec2D(){
    return new Vector2D(x,y);
  }
  
  
  public Vector2I copy() {
    return new Vector2I(x,y);
  }
  
  @Override
  public String toString(){
    return "Vec2D["+x+"]["+y+"]";
  }
  
  @Override
  public boolean equals(Object o){
    if(!( o instanceof Vector2I)) return false;
    Vector2I vi = (Vector2I)o;
    return (vi.x == x && vi.y == y);
  }

  @Override
  public int hashCode(){
    return Objects.hash(x,y);
  }
}
