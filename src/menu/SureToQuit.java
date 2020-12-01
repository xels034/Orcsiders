package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.AssetManager;
import src.Message;
import src.Vector2D;

import gui.UIButton;
import gui.UIElement;
import gui.UIGrafic;

public class SureToQuit extends UIElement{

  private Image backgroundSureToQuitImg, buttonUpSureToQuitImg, buttonDownSureToQuitImg, yesTxt, noTxt, sureToQuitTxt, hoverMenu;

  public SureToQuit(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    
    backgroundSureToQuitImg = AssetManager.getCachedImage("assets/generic/buttons/backgroundSureToQuitImg.png");
    buttonUpSureToQuitImg = AssetManager.getCachedImage("assets/generic/buttons/buttonUpSureToQuitImg.png");
    buttonDownSureToQuitImg = AssetManager.getCachedImage("assets/generic/buttons/buttonDownSureToQuitImg.png");
    yesTxt = AssetManager.getCachedImage("assets/generic/buttons/yesTxt.png");
    noTxt = AssetManager.getCachedImage("assets/generic/buttons/noTxt.png");
    sureToQuitTxt = AssetManager.getCachedImage("assets/generic/buttons/sureToQuitTxt.png");
    hoverMenu = AssetManager.getCachedImage("assets/generic/buttons/hoveredLvl.png");
    
    Vector2D backgroundSureToQuitPos = new Vector2D(Ref.X545, Ref.Y395);
    Vector2D yesPos = new Vector2D(Ref.X560, Ref.Y520);
    Vector2D noPos = new Vector2D(Ref.X980, Ref.Y520);
    
    Message yesMsg = new Message(Message.M_TYPE.Quit_Msg, Message.VIEW.Quit);
    Message noMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Quit_OFF);
    
    UIGrafic backgroundSureToQuitGrafic = new UIGrafic(backgroundSureToQuitPos, ref, Ref.X830, Ref.Y290, backgroundSureToQuitImg);
    UIGrafic sureToQuitGrafic = new UIGrafic(backgroundSureToQuitPos, ref, Ref.X830, Ref.Y200, sureToQuitTxt);
    UIButton yesButton = new UIButton(yesPos, ref, Ref.X380, Ref.Y150, yesTxt, buttonUpSureToQuitImg, buttonDownSureToQuitImg, hoverMenu, yesMsg);
    UIButton noButton = new UIButton(noPos, ref, Ref.X380, Ref.Y150, noTxt, buttonUpSureToQuitImg, buttonDownSureToQuitImg, hoverMenu, noMsg);
    
    children.add(backgroundSureToQuitGrafic);
    children.add(sureToQuitGrafic);
    children.add(yesButton);
    children.add(noButton);
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    int W = gc.getWidth();
    int H = gc.getHeight();
    
    int WIDTH = (int)(W * width);
    int HEIGHT = (int)(H * height);
    
    //some debug BG
    g.setColor(new Color(0.5f,0.5f,0.5f));
    g.fillRect((float)pos.x*W, (float)pos.y*H, WIDTH, HEIGHT);
    
    for(UIElement ui : children){
      ui.render(g, gc);
    }
  }

  @Override
  public void update(int delta) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void handleMessage(Message m){
    if(m.getMsgType() == Message.M_TYPE.HW_Input){
      for(UIElement ui : children){
        ui.handleMessage(m);
      }
    }
  }

}
