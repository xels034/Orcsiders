package src;

import gui.Hexfield;
import gui.Sandbox;

import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeSet;

public class EntityManager {
  
  private static boolean pause = false;
  
  private static LinkedList<Entity> entities = new LinkedList<>();
  private static LinkedList<Entity> remove = new LinkedList<>();
  private static LinkedList<Entity> add = new LinkedList<>();
  
  private static Sandbox sb = null;
  private static ProxyHandler listener = new ProxyHandler(EntityManager.class);
  
  public static void registerSB(Sandbox s){
    unregisterSB();
    sb=s;
    LinkedList<Message.M_TYPE> ll = new LinkedList<>();
    ll.add(Message.M_TYPE.Game_Pause);
    Messenger.subscribe(listener, ll);

  }
  
  public static void unregisterSB(){
    sb=null;
    entities = new LinkedList<>();
    add = new LinkedList<>();
    Messenger.unsubscribe(listener);
    pause = false;
  }
  
  public static void add(Entity e){
    add.add(e);
  }
  
  public static void add(Collection<Entity> c){
    for(Entity e : c){
      add(e);
    }
  }
  
  public static void remove(Entity u){
    remove.add(u);
  }
  
  public static void remove(Collection<Entity> c){
    for(Entity u : c){
      remove(u);
    }
  }
  
  public static void update(double dt){
    if(!pause && sb != null){
      
      for(Entity e : entities){
        if(e.life <= 0) {
          remove.add(e);
          if(e.layer != -1){
            Messenger.send(new Message(Message.M_TYPE.Game_Event,
                               new Message.GEventParam(Message.G_EVENT_TYPE.Unit_Died, (Unit)e, null, null)));
          }
        }
      }
      
      entities.removeAll(remove);
      entities.addAll(add);
      remove.clear();
      add.clear();
      
      for(Entity e : entities){
        e.update(dt);
      }
      
      if(sb != null) gridding();
    }
  }
  
  private static void gridding(){
    for(int y=0; y<sb.playfield[0].length;y++){
      for(int x=0;x<sb.playfield.length;x++){
        sb.playfield[x][y].entities.clear();
      }
    }
    
    Vector2I ugp;
    for(Entity e : entities){
      if(e.griddable){
        ugp = sb.relativeToGrid(e.pos.add(sb.imgW/2));
        sb.playfield[ugp.x][ugp.y].entities.add(e);
      }
    }
  }
  
  public static void applyBuff(Vector2I v, Hexfield.BuffState s){ buffing(v, s, 1);}
  public static void removeBuff(Vector2I v, Hexfield.BuffState s) { buffing(v, s, -1);}
  
  private static void buffing(Vector2I v, Hexfield.BuffState s, int i){
    //find out where to go for the neighbours, depending if even or uneven column
    int signum = 1;
    if(v.x % 2 == 0) signum = -1;
    int ymin = v.y-1;
    int ymax = v.y+1;
    int xmin = v.x-1;
    int xmax = v.x+1;
    
    if(i == 1){
      sb.playfield[v.x][v.y].buff(s);//center
      
      if(ymin >= 0)  sb.playfield[v.x][v.y-1].buff(s);//vertical
      if(ymax <= 13) sb.playfield[v.x][v.y+1].buff(s);
      
      if(xmin >= 0) sb.playfield[v.x-1][v.y].buff(s);//default horizontal
      if(xmax <= 15) sb.playfield[v.x+1][v.y].buff(s);
      
      if(xmin >= 0 && v.y+signum <= 14 && v.y+signum >= 0) sb.playfield[v.x-1][v.y+signum].buff(s);//conditional horizontal
      if(xmax <= 15&& v.y+signum <= 14 && v.y+signum >= 0) sb.playfield[v.x+1][v.y+signum].buff(s);//conditional horizontal
    }else{
      sb.playfield[v.x][v.y].debuff(s);//center
      
      if(ymin >= 0)  sb.playfield[v.x][v.y-1].debuff(s);//vertical
      if(ymax <= 13) sb.playfield[v.x][v.y+1].debuff(s);
      
      if(xmin >= 0) sb.playfield[v.x-1][v.y].debuff(s);//default horizontal
      if(xmax <= 15) sb.playfield[v.x+1][v.y].debuff(s);
      
      if(xmin >= 0 && v.y+signum <= 14 && v.y+signum >= 0) sb.playfield[v.x-1][v.y+signum].debuff(s);//conditional horizontal
      if(xmax <= 15&& v.y+signum <= 14 && v.y+signum >= 0) sb.playfield[v.x+1][v.y+signum].debuff(s);//conditional horizontal
    }
    
  }
  
  public static int getHexDistance(Vector2I a, Vector2I b){
    int xDiff = a.x - b.x;
    int yDiff = a.y - b.y;
    if(xDiff < 0) xDiff = -xDiff;
    if(yDiff < 0) yDiff = -yDiff;
    
    int distance = xDiff + (yDiff - xDiff/2);

    return distance;
  }
  
  @SuppressWarnings("deprecation")
  public static boolean checkDistance(Unit s, Unit t, int r){
    Vector2I si = sb.relativeToGridUNCLAMPED(s.pos);
    Vector2I ti = sb.relativeToGridUNCLAMPED(t.pos);
    
    if(si.x < 0) si.x=0;
    if(si.y < 0) si.y=0;
    if(ti.x < 0) ti.x=0;
    if(ti.y < 0) ti.y=0;

    System.out.println("si: "+si + " ti: "+ti);
    System.out.println("s="+s);
    System.out.println("t="+t);
    
    return ( r <= getHexDistance(si,ti));
  }
  
  public static Unit getNearest(Unit u, int r){

    Vector2D cheat = u.pos.add(sb.imgW/2);
    Vector2I gridPos = sb.relativeToGrid(cheat);

    LinkedList<Entity> tmpList = new LinkedList<>();

    int gsx = Math.max(0, gridPos.x-r);
    int gsy = Math.max(0, gridPos.y-r);
    int gex = Math.min(15, gridPos.x+r+1);
    int gey = Math.min(13, gridPos.y+r+1);
    
    for(int y=gsy; y < gey; y++){
      for(int x = gsx; x < gex; x++){
        if(getHexDistance(new Vector2I(x,y), gridPos) <= r){
          tmpList.addAll(sb.playfield[x][y].entities);
        }
      }
    }
    
    LinkedList<Unit> toAdd = new LinkedList<>();

    for(Entity en : tmpList){
      if(en.layer != u.layer && en.layer != -1) toAdd.add((Unit)en);
    }
    TreeSet<Unit> ts = new TreeSet<>(new Entity.EntityRLDistanceComparator(u.pos));
    ts.addAll(toAdd);

    if(!ts.isEmpty()) return ts.first();
    else              return null;
  }
  
  public static LinkedList<Unit> getHexInhabitants(Unit u){
    LinkedList<Unit> ll = new LinkedList<>();
    
    Vector2D cheat = u.pos.add(sb.imgW/2);
    Vector2I gridPos = sb.relativeToGrid(cheat);
    
    for(Entity e : sb.playfield[gridPos.x][gridPos.y].entities){
      if(e.layer == u.layer && e.layer != -1) ll.add((Unit)e);
    }
    
    return ll;
  }
  
  public static TreeSet<Entity> getEntityState(){
    TreeSet<Entity> ts = new TreeSet<>(new Entity.EntityHeightComparator());
    ts.addAll(entities);
    return ts;
  }
  
  public static LinkedList<Unit> getAllUnits(int layer){
    LinkedList<Unit> returner = new LinkedList<>();
    
    for(Entity e : entities){
      if(e instanceof Unit){
        if(((Unit)e).layer == layer) returner.add((Unit)e);
      }
    }
    
    return returner;
  }
  
  public static boolean exists(Entity e){
    return entities.contains(e);
  }
  
  public static void proxyHandleMessage(Message m){
    if(m.getMsgType() == Message.M_TYPE.Game_Pause){
      pause = (m.getParams() == Message.VIEW.Pause_ON);
    }
  }
  
}
