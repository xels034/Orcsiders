package src;

public class Vector2D {
  public double x;
  public double y;
  
  /**
   * CONSTRUCTOR
   * @param x x-direction (start point)
   * @param y y-direction (start point)
   */
  public Vector2D(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  /**
   * 
   * @param v2 the vvector you will add up
   * @return the result of the addition
   */
  public Vector2D add(Vector2D v2){
    return new Vector2D(this.x + v2.x, this.y + v2.y);
  }
  
  public Vector2D add(double d){
    return new Vector2D(x+d, y+d);
  }
  
  /**
   * 
   * @param v2 the vector you will subtract
   * @return the result of the subtraction
   */
  public Vector2D sub(Vector2D v2){
    return new Vector2D(this.x - v2.x, this.y - v2.y);
  }
  
  /**
   * 
   * @param scalar the scalar for scale
   * @return the scaled vector
   */
  public Vector2D mult(double scalar){
    return new Vector2D(this.x * scalar, this.y * scalar);
  }
  
  public Vector2D unify(){
    double l = length();
    if(l != 0) return new Vector2D(x/l, y/l);
    else return new Vector2D(0, 0);
  }
  
  /**
   * 
   * @param v2 the second factor
   * @return the scalar product about 2 vectors
   */
  public double skalarProduct(Vector2D v2){
    return (this.x * v2.x + this.y * v2.y);
  }
  
  /**
   * 
   * @return the length of a vector (pow 2)
   */
  public double length2() {
    return (this.x * this.x + this.y * this.y);
  }
  
  /**
   * 
   * @return the length of a vector
   */
  public double length() {
    double l2 = length2();
    if(l2 == 0) return 0;
    else return Math.sqrt(l2);
  }
  
  /**
   * 
   * @param v1 first Vector (Point)
   * @param v2 second Vector (Point)
   * @return  the length between the two points (pow 2)
   */
   public static double length2(Vector2D v1, Vector2D v2) {
    Vector2D v3 = new Vector2D(v1.x - v2.x, v1.y - v2.y);
    return v3.length2();
  }
  
   /**
    * 
    * @param v1 first Vector (Point)
    * @param v2 second Vector (Point)
    * @return the length between the two points (squared)
    */
  public static double length(Vector2D v1, Vector2D v2) {
    Vector2D v3 = new Vector2D(v1.x - v2.x, v1.y - v2.y);
    return v3.length();
  }
  
  public Vector2I toVec2I(){
    return new Vector2I((int)x, (int)y);
  }
  
  public Vector2D copy() {
    return new Vector2D(x,y);
  }
  
  @Override
  public String toString(){
    return "Vec2D["+x+"]["+y+"]";
  }
}
