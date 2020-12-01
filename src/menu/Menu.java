package menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import src.Message;
import src.Vector2D;
import gui.UIElement;

public class Menu extends UIElement{

  public Menu(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    
    children.add(new Pause(new Vector2D(0.375, 0.1), ref, 0.25f, 0.8f));
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    for(UIElement uie : children){
      uie.render(g, gc);
    }
  }

  @Override
  public void update(int delta) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void handleMessage(Message m){
    switch(m.getMsgType()){
    case HW_Input:
      for(UIElement uie : children){
        uie.handleMessage(m);
      }
      break;
    default:
      break;
    }
  }

}
