package src;

import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.Image;

import gui.Animation;
import gui.AnimationSet;
import gui.Hexfield;
import gui.Hexfield.BuffState;

public class Unit extends Entity{
  
  public static final Unit fire = new Unit(0, BuffState.Fire, null, 0, 100, 400, 2, new Vector2I(10,0));
  public static final Unit ice = new Unit(0, BuffState.Ice, null, 0, 100, 1300, 2, new Vector2I(15,0));
  public static final Unit earth = new Unit(0, BuffState.Earth, null, 0, 100, 3000, 2, new Vector2I(30,0));
  public static final Unit pSlow = new Unit(0, BuffState.Neutral, null, 0.025f, 100, 1000, 1, new Vector2I(10,20));
  public static final Unit pFast = new Unit(0, BuffState.Neutral, null, 0.05f, 100, 500, 1, new Vector2I(20,40));
  //
  public static final Unit eSlow = new Unit(1, BuffState.Fire, null, 0.025f, 180, 1, 1, new Vector2I(1,1)); 
  public static final Unit eFast = new Unit(1, BuffState.Earth, null, 0.05f, 130, 1, 1, new Vector2I(2,1));
  public static final Unit eHero = new Unit(1, BuffState.Neutral, null, 0.05f, 8000, 1, 1, new Vector2I(10,5));
  //
  private static LinkedList<BuffState> jt;
  //###########
  //###########
  

  
  private long cd;
  private long lastFired = 0;
  
  public float maxSpeed;
  public float maxLife;
  public double speed;
  public int range;
  
  public float intakeDmgMod = 1;
  public float speedMod = 1;
  
  public Vector2I curr;

  private AnimationSet effigy;
  private Animation aura;
  private Vector2D moveTarget;
  public Unit target;
  public Hexfield base;
  
  public HashMap<Impact, Long> affection;
  
  private boolean attacking = false;

  private boolean usable;
  

  
  //###########################
  //NON-STATIC TIME BITCHES :D#
  //###########################
  
  
  public Unit(int i, Hexfield.BuffState s, AnimationSet as, float sp, float l, long cd, int r, Vector2I price){
    super(i, new Vector2D(0,0), s);
    if(jt == null){
      jt = new LinkedList<>();
      jt.add(BuffState.Fire);
      jt.add(BuffState.Ice);
      jt.add(BuffState.Earth);
    }
    curr = price.copy();
    
    affection = new HashMap<>();
    type = s;
    effigy = as;
    pos = new Vector2D(0,0);
    range = r;
    this.cd=cd;
    
    maxSpeed = sp;
    maxLife=l;
    resetMalus();
    
    finalizeEffigy();
  }
  
  public Unit getInstance(@SuppressWarnings("hiding") Vector2D pos, AnimationSet as){
    Unit u = new Unit(layer, type, as, maxSpeed, maxLife, cd, range, curr);
    u.pos = pos.copy();
    finalizeEffigy();
    u.setAnimStartNow();
    return u;
  }
  
  public void resetMalus(){
    life=maxLife;
    speed=maxSpeed;
  }
  
  public void finalizeEffigy(){
    usable = true;
    switch (type){
    case Fire:
      aura = new Animation("assets/generic/auras/fire");
      break;
    case Ice:
      aura = new Animation("assets/generic/auras/frost");
      break;
    case Earth:
      aura = new Animation("assets/generic/auras/earth");
      break;
    default:
      aura = new Animation();
      break;
    }
    aura.start();
  }
  
  
  public Unit copy(){
    return getInstance(pos, effigy);
  }
  
  public void setAnimStartNow(){
    if(effigy!= null) effigy.start();
    if(aura != null) aura.start();
  }
  
  @Override
  public Image getImage(){
    if(moveTarget != null){
      Vector2D dir = moveTarget.sub(pos).unify();
      return effigy.getRunning(dir).getImage();
    }else if(attacking){
      return effigy.getAttack().getImage();
    }else{
      return effigy.getIdle().getImage();
    }
  }
  
  public Image getAura(){
    return aura.getImage();
  }
  
  public void issueMove(Vector2D issuePos){
    moveTarget = issuePos;
  }
  
  @Override
    public void update(double dt){
    if(usable && griddable){
      move(dt);
      if(moveTarget == null) attack();
      releaseAffection();
    }
  }
  
  private void move(double dt){
    if(moveTarget != null){
      if(maxSpeed > 0){
        Vector2D dir = moveTarget.sub(pos);
        if(dir.length2() > 0.00001){
          pos=pos.add(dir.unify().mult(speed*dt));
        }else{
          moveTarget = null;
          Messenger.send(new Message(Message.M_TYPE.Destination_Reached, this));
        }
      }else{
        moveTarget = null;
      }
    }
  }
  
  private void attack(){
    //0 == player
    if(layer == 0){
      if(target != null &&
         (target.life <= 0 ||
         EntityManager.checkDistance(this, target, range))) target = null;  

      if(target == null) target = EntityManager.getNearest(this, range);

      if(target != null){
        long now = System.currentTimeMillis();
        if(lastFired + cd < now){
          
          float dmgMod = 1;
          if(type == BuffState.Neutral){                                     //Only for units
            BuffState vuln = target.type;                                  //get type of target
            if(vuln != BuffState.Neutral){                  //No bonus für neutral units
              int vIdx = jt.indexOf(vuln);                                 //get idx of it in jt
              vIdx++; if(vIdx >= jt.size()) vIdx-= jt.size();              //shift idx to find whats its vuln
              if(base != null){
                if(base.getBuffs().contains(jt.get(vIdx))) dmgMod = 1.25f;   //if the base has the vuln type, raise dmg
              }
            }
          }
          
          EntityManager.add(new Bolt(pos,target, type, dmgMod));
          //System.out.println("[UNIT] firing, dmgMod="+dmgMod);
          Messenger.send(new Message(Message.M_TYPE.Game_Event,
                             new Message.GEventParam(Message.G_EVENT_TYPE.Fire, this, target, null)));
          
          lastFired = now;
        }
      }
    }
  }
  
  private void releaseAffection(){
    long now = System.currentTimeMillis();
    LinkedList<Impact> released = new LinkedList<>();
    for(Impact i : affection.keySet()){
      if(affection.get(i) + i.duration < now){
        i.release(this);
        released.add(i);
      }
    }
    for(Impact i : released){
      affection.remove(i);
    }
  }
  
  public boolean isUsable(){
    return usable;
  }
  
  @Override
  public String toString(){
    return "Unit@["+pos.x+"]["+pos.y+"]";
  }
}
