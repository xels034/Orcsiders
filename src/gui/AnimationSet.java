package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import src.Vector2D;

public class AnimationSet {
  private Animation idle;
  private Animation attack;
  private ArrayList<Animation> running;
  
  public AnimationSet(String path){
    File look;
    Animation i;
    Animation a = null;
    LinkedList<Animation> run = null;
    
    i = new Animation(path+"/idle");
    
    look = new File(path+"/attack");
    if(look.exists()) a = new Animation(path+"/attack");
    
    look = new File(path+"/running");
    if(look.exists()){
      run = new LinkedList<>();
      for(String s : look.list()){
        run.add(new Animation(path+"/running/"+s));
      }
    }
    
    idle = i;
    running = new ArrayList<>();
    
    if(run == null) running.add(i);
    else            running.addAll(run);
    
    if(a == null) attack = i;
    else          attack = a;
  }
  
  public void start(){
    idle.start();
    attack.start();
    for(Animation a : running){
      a.start();
    }
  }
  
  public void setAttackAnim(Animation a){
    attack = a;
  }
  
  public Animation getIdle(){
    return idle;
  }
  
  public Animation getAttack(){
    return attack;
  }
  
  public Animation getRunning(Vector2D direction){
    double phi = Math.atan2(direction.x, direction.y / 1.7777); //because apparent movement is squished. would have 45°
                                                                //edge-cases instead
    
    phi+=Math.PI*1.5; //bc atan2 is -PI to Pi. but 0-2PI is better.
                      //also atan2 seems to be shifted -90°

    phi += ((Math.PI*2)/running.size())/2d; //shift so its in the center of the first slice. so (2pi / size) /2
    phi %= (Math.PI*2); if(phi<0) phi+= Math.PI*2; //always positive modulo

    int idx = (int)(phi/(Math.PI*2)*running.size()); //derive weighting, multiply by size, get index, win
    return running.get(idx);
  }
  
  public ArrayList<Animation> getRunningSet(){
    ArrayList<Animation> ret = new ArrayList<>();
    ret.addAll(running);
    return ret;
  }
}
