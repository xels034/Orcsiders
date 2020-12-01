package gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import src.Message;
import src.Message.UnitParam;
import src.AssetManager;
import src.Messenger;
import src.Vector2D;

public class UIButton extends UIElement{
  public Image textureUp, textureHovered, textureDown, font, hoverTexture;
  
  //static private float offsetX4 = 0.002f;
  //static private float offsetY4 = 0.0037f;
  //static private float scale5 = 0.05f;
  private float mpX;
  private float mpY;

  private Object responseParameter;
  

  public UIButton(Vector2D pos, Vector2D ref, float width, float height,Image font, Image textureUp, Image textureDown, Image hoverTexture, Object responseParameter) {
    super(pos, width, height);
    this.textureUp = textureUp;
    this.textureUp = textureUp.getScaledCopy((int)(ref.x*width),(int)(ref.y*height));
    
    this.textureDown = textureDown;
    this.textureDown = textureDown.getScaledCopy((int)(ref.x*width),(int)(ref.y*height));
    
    if(font == null){
      this.font = AssetManager.getCachedImage("assets/generic/buttons/blank.png");
      this.font = this.font.getScaledCopy((int)(ref.x*width),(int)(ref.y*height));
    }
    
    else{
      this.font = font;
      this.font = font.getScaledCopy((int)(ref.x*width),(int)(ref.y*height));
    }
    
    
    this.hoverTexture = hoverTexture;
    this.hoverTexture = hoverTexture.getScaledCopy((int)(ref.x*width),(int)(ref.y*height));
    
    this.responseParameter = responseParameter;
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    
    int W = gc.getWidth();
    int H = gc.getHeight();
    
    if(isClicked) {
      g.drawImage(textureDown, (float)pos.x*W, (float)pos.y*H);
    }
    
    else {
      g.drawImage(textureUp, (float)pos.x*W, (float)pos.y*H);
    }
    
    if(hovered) {
      BlendWrapper.setMode(BlendWrapper.Mode.ADD);
      g.drawImage(hoverTexture, (float)pos.x*W, (float)pos.y*H);
      BlendWrapper.setMode(BlendWrapper.Mode.MIX);
    }
    
    g.drawImage(font, (float)pos.x*W, (float)pos.y*H);
  }

  @Override
  public void update(int delta) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void handleMessage(Message m){
    //int diff = (int)((textureUp.getHeight()*1.09) - textureUp.getHeight());
    
    switch(m.getMsgType()){
    case HW_Input:
      Message.HWParam hwp = (Message.HWParam)m.getParams();
      switch(hwp.evnt){
      case MD:
      case MM:
        mouseOver(hwp);
        break;
      case MP:
        if(hwp.custom == Input.MOUSE_LEFT_BUTTON){
          mpX = hwp.newx;
          mpY = hwp.newy;
          
          if(clickedIn(mpX, mpY)){
            isClicked = true;
            Messenger.send(new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.press));
          }
          else{
            isClicked = false;
          }
        }
        break;
        
      case MR:
        if(hwp.custom == Input.MOUSE_LEFT_BUTTON){
          if (isClicked == true && clickedIn(hwp.newx, hwp.newy)){
            isPressed = true;
            
            Message response = (Message)responseParameter;
            if(response.getMsgType() == Message.M_TYPE.Select_Unit_To_Build){
              response = response.copy(); //don't fiddle with the original man :D
              //Float f = (Float)response.getParams();
              UnitParam up = (UnitParam)response.getParams();
              up.pos = new Vector2D(hwp.newx, hwp.newy);
              //response.setParams(new Float[]{f,hwp.newx,hwp.newy});
            }
            Messenger.send(response);
            isClicked = false;
          }
          else {
            isPressed = false;
            isClicked = false;
          }
        }
        break;  
      default:
        break;
      }
      break;
    default:
      break;
    }
  }
}