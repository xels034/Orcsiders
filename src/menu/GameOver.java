package menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.AssetManager;
import src.Message;
import src.Message.ViewParam;
import src.Messenger;
import src.Vector2D;
import gui.UIElement;
import gui.UIGrafic;

public class GameOver extends UIElement{
  
  private Image   gameOverImg;
  //private int selected = -1;
  Vector2D ref;
  
  public GameOver(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    
    this.ref = ref;
    
    //Messenger.subscribe(this, Message.M_TYPE.Menu_Msg);
    gameOverImg = AssetManager.getCachedImage("assets/generic/buttons/gameOverImg.png");
    
    UIGrafic gameOver = new UIGrafic(new Vector2D(0,0), ref, 1, 1, gameOverImg);
    
    children.add(gameOver);
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    for(UIElement ui : children){
      ui.render(g, gc);
    }
  }

  @Override
  public void update(int delta) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void handleMessage(Message m) {
    // TODO Auto-generated method stub
    switch(m.getMsgType()){
    case HW_Input:
      hw_handling(m);
      break;
    default:
      break;
    }
  }
  
  private void hw_handling(Message m){
    Message.HWParam hwp = (Message.HWParam)m.getParams();
    switch(hwp.evnt){
      case MR:
        Messenger.send(new Message(Message.M_TYPE.View_Msg, new ViewParam(0, 0, Message.VIEW.Main_Menu)));
        break;
      default:
        break;
    }
      
    for(UIElement uie : children){
      uie.handleMessage(m);
    }
  }
}
