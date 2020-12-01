package src;

public class Line {
  public Vector2D start;
  public Vector2D end;
  
  public Line(Vector2D s, Vector2D e){
    start=s;
    end=e;
  }
  
  public static double[] getUV(Line v1, Line v2){
    //uses parametric representation: A +x*(B-A)
    double ax = v1.start.x;
    double ay = v1.start.y;//origin
    double bx = v1.end.x-v1.start.x;//vector
    double by = v1.end.y-v1.start.y;
    
    double cx = v2.start.x;
    double cy = v2.start.y;
    double dx = v2.end.x-v2.start.x;
    double dy = v2.end.y-v2.start.y;
    
    // a + u*b = c + v*d
    // u,v=?
    double u,v;
    
    u =  (-cx*dy+ax*dy+(cy-ay)*dx)/(by*dx-bx*dy);
    v =  -(bx*(ay-cy)+by*cx-ax*by)/(by*dx-bx*dy);
    
    double[] arr = {u,v};
    return arr;
  }
  
  public static boolean isIntersecting(Line v1, Line v2){
    //treats parallel as not intersecting
    double[] uv=getUV(v1,v2);
    return (uv[0]>=0 && uv[0]<=1 && uv[1]>=0 && uv[1]<=1);
  }
}
