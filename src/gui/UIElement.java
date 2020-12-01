package gui;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import src.Handler;
import src.Message;
import src.Messenger;
import src.Vector2D;

public abstract class UIElement implements Handler{
  
  public Vector2D pos = new Vector2D(0,0);
  public float width;
  public float height;
  public boolean hovered = false;
  public boolean isClicked = false;
  
  public boolean isPressed = false;
  
  public LinkedList<UIElement> children;
  
  public UIElement(Vector2D pos, float width, float height) {
    this.pos = pos;
    this.width = width;
    this.height = height;
    
    this.children = new LinkedList<>();
  }

  public boolean clickedIn(float x, float y){
    if(((this.pos.x + width) < x) ||
      ((this.pos.y + height) < y) ||
      (this.pos.x > x) ||
      (this.pos.y > y)) {
      return false;
    }
    return true;
  }
  
  abstract public void render(Graphics g, GameContainer gc);
  
  abstract public void update(int delta);
  
  public int mouseOver(Message.HWParam hwp){
    if(this.pos.x <= hwp.newx &&
     ((this.pos.x + this.width) >= hwp.newx) &&
       this.pos.y <= hwp.newy &&
     ((this.pos.y + this.height) >= hwp.newy)) {
      if(this.hovered){
        return 0;
        
      }
      else if(!this.hovered){
        this.hovered = true;
        Messenger.send(new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.hover));
        return 1;
      }
    }
    else if(hovered){
      hovered = false;
      return -1;
    }
    return -2;
  }
  
  public void clearR(){
    for(UIElement uie : children){
      uie.clearR();
    }
    children.clear();
  }
  
  public void unsubscribeAll(){
    for(UIElement uie : children){
      Messenger.unsubscribe(uie);
    }
    Messenger.unsubscribe(this);
  }
}
