package src;

import org.newdawn.slick.Image;

import gui.Animation;
import gui.Hexfield;

public class Bolt extends Entity{
  
  private static Impact fireFunc = new Impact(){
    @Override
    public void fire(Unit u, double dmg){
      u.life -=  dmg*u.intakeDmgMod;
      u.intakeDmgMod = 1.5f;
      
      if(u.affection.containsKey(this)) u.affection.remove(this);
      u.affection.put(this, System.currentTimeMillis());
      Messenger.send(new Message(Message.M_TYPE.Game_Event,
                         new Message.GEventParam(Message.G_EVENT_TYPE.Impact, null, u, Hexfield.BuffState.Fire)));
    }
    
    @Override
    public void release(Unit u){
      u.intakeDmgMod = 1f;
    }
  };
  
  private static Impact frostFunc = new Impact(){
    @Override
    public void fire(Unit u, double dmg){
      u.life -=  dmg*u.intakeDmgMod;
      u.speed = u.maxSpeed*0.2;
      
      if(u.affection.containsKey(this)) u.affection.remove(this);
      u.affection.put(this, System.currentTimeMillis());
      Messenger.send(new Message(Message.M_TYPE.Game_Event,
                   new Message.GEventParam(Message.G_EVENT_TYPE.Impact, null, u, Hexfield.BuffState.Ice)));
    }
    
    @Override
    public void release(Unit u){
      u.speed = u.maxSpeed;
    }
  };
  
  private static Impact earthFunc = new Impact(){
    @Override
    public void fire(Unit u, double dmg){
      u.life -=  dmg*u.intakeDmgMod;
      
      for(Unit un : EntityManager.getHexInhabitants(u)){
        un.life -=  dmg*un.intakeDmgMod;
      }
      Messenger.send(new Message(Message.M_TYPE.Game_Event,
                   new Message.GEventParam(Message.G_EVENT_TYPE.Impact, null, u, Hexfield.BuffState.Earth)));
    }
    
    @Override
    public void release(Unit u){}
  };
  
  private static Impact defaultFunc = new Impact(){
    @Override
    public void fire(Unit u, double dmg){
      u.life -= dmg*u.intakeDmgMod;
      Messenger.send(new Message(Message.M_TYPE.Game_Event,
                   new Message.GEventParam(Message.G_EVENT_TYPE.Impact, null, u, Hexfield.BuffState.Neutral)));
    }
    
    @Override
    public void release(Unit u){}
  };
  
  private long lifeTime = 300;
  
  
  public Vector2D birthPos, targetPos;
  private long birthTime;
  private Animation anim;
  private Impact impactFunc;
  private Unit target;
  private double dmg;
  
  public Bolt(Vector2D p, Unit t, Hexfield.BuffState s, float dmgMod){
    super(-1, p, s);
    birthTime = System.currentTimeMillis();
    birthPos = p.copy();
    Animation a=null;
    Impact i=null;
    switch(type){
    case Fire:
      a = new Animation("assets/generic/bolts/fire");
      i = fireFunc;
      dmg = 10;
      break;
    case Ice:
      a = new Animation("assets/generic/bolts/frost");
      i = frostFunc;
      dmg = 25;
      break;
    case Earth:
      a = new Animation("assets/generic/bolts/earth");
      i = earthFunc;
      dmg = 50;
      break;
    default:
      a = new Animation("assets/generic/bolts/earth");
      i = defaultFunc;
      lifeTime = 25;
      dmg=25 * dmgMod;
      break;
    }
    
    anim = a;
    impactFunc = i;
    a.start();
    target = t;
  }
  
  @Override
  public Image getImage(){
    return anim.getImage();
  }
  
  @Override
  public void update(double dt){
    long lived = System.currentTimeMillis()-birthTime;
    if(lived > lifeTime) {
      impactFunc.fire(target, dmg);
      target = null;
      life = 0;
      EntityManager.add(new Explosion(-1, pos, type));
    }
    if(target != null) targetPos = target.pos.copy();
     
    double lifeFac = (double)(System.currentTimeMillis()-birthTime) / (double)lifeTime;
    if(targetPos != null){
      pos.x = birthPos.x*(1-lifeFac) + targetPos.x*lifeFac;
      pos.y = birthPos.y*(1-lifeFac) + targetPos.y*lifeFac;
      
      Vector2D direction = pos.sub(targetPos).unify();
      
      rotation = (float)Math.atan2(direction.x, -direction.y);
      rotation += Math.PI/2;
    }

  }
}
