package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.AssetManager;
import src.Entity;
import src.Unit;
import src.Vector2D;
import src.Vector2I;

public class Hexfield {

  public enum HexState {Free, Passable, Unpassable}
  public enum BuffState {Ice, Fire, Earth, Neutral}
  
  public LinkedList<Entity> entities;
  private HashMap<BuffState, Integer> buffs;
  private static Image fireBuff = AssetManager.getCachedImage("assets/generic/stati/fireStat.png");
  private static Image frostBuff = AssetManager.getCachedImage("assets/generic/stati/frostStat.png");
  private static Image earthBuff = AssetManager.getCachedImage("assets/generic/stati/earthStat.png");
  
  private Image img;
  private Unit building;
  public HexState state;
  private Vector2I gridPos;
  public ArrayList<Vector2I> waypoints;
  
  public boolean hovered, selected;
  public Unit selection_hovered;
  
  public Hexfield(Vector2I vi, Image i, HexState s){
    gridPos = vi.copy();
    img = i;
    state = s;
    entities = new LinkedList<>();
    //buffs = new LinkedList<BuffState>();
    buffs = new HashMap<>();
    waypoints = new ArrayList<>();
  }
  
  public void render(Graphics g, GameContainer gc, Sandbox sb){
    final int W = gc.getWidth();
    final int H = gc.getHeight();
    int x = gridPos.x;
    int y = gridPos.y;
    int imgScale = (int)(sb.imgW*W);
    img = img.getScaledCopy(imgScale, imgScale);
    
    Vector2D relPos = sb.gridToRelative(new Vector2I(x,y));
    
    float xPos = (float)(relPos.x*W);
    float yPos = (float)(relPos.y*H);
    
    Image tmpImg;
    //dirty but whatever ... boolean decides if hexfield s drawn, or the status. latter as an overlay later in the pipelinegg
    BlendWrapper.setMode(BlendWrapper.Mode.MIX);
    for(BuffState s : buffs.keySet()){
      switch(s){
      case Fire:
        tmpImg = fireBuff.getScaledCopy(imgScale,imgScale);
        break;
      case Ice:
        tmpImg = frostBuff.getScaledCopy(imgScale,imgScale);
        break;
      case Earth:
        tmpImg = earthBuff.getScaledCopy(imgScale,imgScale);
        break;
      default:
        tmpImg = fireBuff.getScaledCopy(imgScale,imgScale);
        break;
      }
      g.drawImage(tmpImg, xPos, yPos);
    }
    if(selection_hovered != null){
      int rangeScale = imgScale * (1+selection_hovered.range*2);
      tmpImg = AssetManager.getCachedImage("assets/generic/range"+selection_hovered.range+".png").getScaledCopy(rangeScale, rangeScale);
      float shift = (sb.imgW*selection_hovered.range)*gc.getWidth();
      BlendWrapper.setMode(BlendWrapper.Mode.ADD);
      tmpImg.setAlpha(.35f);
      g.drawImage(tmpImg, xPos-shift, yPos-shift);
    }else if(hovered){
      tmpImg = AssetManager.getCachedImage("assets/generic/lowlight.png").getScaledCopy(imgScale, imgScale);
      
      tmpImg.setAlpha(.35f);
      BlendWrapper.setMode(BlendWrapper.Mode.ADD);
      g.drawImage(tmpImg, xPos, yPos);
      BlendWrapper.setMode(BlendWrapper.Mode.MIX);
    }
    if(selected){
      //tmpImg = AssetManager.getCachedImage("assets/generic/selection_tex.png").getScaledCopy(imgScale, imgScale);
      
      //BlendWrapper.setMode(BlendWrapper.Mode.ADD);
      //g.drawImage(tmpImg, xPos, yPos);
      
      if(building != null){
        int rangeScale = imgScale * (1+building.range*2);
        tmpImg = AssetManager.getCachedImage("assets/generic/range"+building.range+".png").getScaledCopy(rangeScale, rangeScale);
        float shift = (sb.imgW*building.range)*gc.getWidth();
        tmpImg.setAlpha(.35f);
        BlendWrapper.setMode(BlendWrapper.Mode.ADD);
        g.drawImage(tmpImg, xPos-shift, yPos-shift);
      }
        
      BlendWrapper.setMode(BlendWrapper.Mode.MIX);
    }
    
    
  }
  
  public Image getImage(){
    return img;
  }
  
  public void build(Unit u){
    building = u;
    state = HexState.Unpassable;
    building.base = this;
    u.base = this;
    //Messenger.send(new Message(Message.M_TYPE.Place_Unit));
  }
  
  public void destroy(){
    entities.remove(building);
    building = null;
    state = HexState.Free;
  }
  
  public Unit removeBuilding(){
    state = HexState.Free;
    Unit e = building;
    building = null;
    e.base = null;
    return e;
  }
  
  public void buff(BuffState bs){
    if(bs != BuffState.Neutral){
      int i = 1;
      if(buffs.containsKey(bs)) i += buffs.get(bs);
      
      buffs.put(bs,i);
    }
  }
  
  public void debuff(BuffState bs){
    int i = -1;
    if(buffs.containsKey(bs)) i += buffs.get(bs);
    if(i <= 0) buffs.remove(bs);
    else buffs.put(bs,  i);
  }
  
  public LinkedList<BuffState> getBuffs(){
    LinkedList<BuffState> ret = new LinkedList<>();
    ret.addAll(buffs.keySet());
    return ret;
  }
  
  public Unit getBuilding(){
    return building;
  }
}
