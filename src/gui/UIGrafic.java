package gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.Message;
import src.Vector2D;

public class UIGrafic extends UIElement{

  private Image image;

  public UIGrafic(Vector2D pos, Vector2D ref, float width, float height, Image image) {
    super(pos, width, height);
    this.image = image;
    this.image = image.getScaledCopy((int)(ref.x*width),(int)(ref.y*height));
    // TODO Auto-generated constructor stub
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    int W = gc.getWidth();
    int H = gc.getHeight();
    
    g.drawImage(image, (float)pos.x*W, (float)pos.y*H);
    //AssetManager.defaultFont.drawString(textX,textY,text, color);
  }

  @Override
  public void update(int delta) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void handleMessage(Message m){
    switch(m.getMsgType()){
    case HW_Input:
      break;
    default:
      break;
    }
  }
}
