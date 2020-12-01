package gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import src.AssetManager;
import src.Message;
import src.Vector2D;

public class UIText extends UIElement{

  private String text;
  private Color color;

  public UIText(Vector2D pos, Vector2D ref, float width, float height, String text, Color color) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    this.text = text;
    this.color = color;
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    int W = gc.getWidth();
    int H = gc.getHeight();
    
    double textLength = AssetManager.defaultFont.getWidth(text);
    
    AssetManager.defaultFont.drawString((float)(((float)pos.x * W) - textLength), (float)pos.y * H, text, color);
  }

  @Override
  public void update(int delta) {
    // TODO Auto-generated method stub
    text = getText();
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
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
