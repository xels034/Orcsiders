package menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.AssetManager;
import src.Message;
import src.Message.ViewParam;
import src.Vector2D;
import gui.UIButton;
import gui.UIElement;
import gui.UIGrafic;

public class Pause extends UIElement{

  private Image continueImg, saveImg, loadImg, quitImg, buttonUp, buttonDown, hoverMenu, backgroundSureToQuitImg;
  //private int selected = -1;
  Vector2D ref;
  static private float offsetY = 0.3125f;
  static private float offsetX = 0.4023f;
  static private float offsetY90 = 0.0833f;
  static private float buttonWidth = 0.1953f;
  
  public Pause(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    
    //System.out.println("Create Pause");
    
    this.ref = ref;
    
    backgroundSureToQuitImg = AssetManager.getCachedImage("assets/generic/buttons/backgroundSureToQuitImg.png");
    continueImg = AssetManager.getCachedImage("assets/generic/buttons/continueButton.png");
    saveImg = AssetManager.getCachedImage("assets/generic/buttons/saveButton.png");
    loadImg = AssetManager.getCachedImage("assets/generic/buttons/loadButton.png");
    quitImg = AssetManager.getCachedImage("assets/generic/buttons/quitButton.png");
    buttonUp = AssetManager.getCachedImage("assets/generic/buttons/pauseButtonUp.png");
    buttonDown = AssetManager.getCachedImage("assets/generic/buttons/pauseButtonDown.png");
    hoverMenu = AssetManager.getCachedImage("assets/generic/buttons/hoveredGameMainMenu.png");
    
    float curH = 0;
    
    curH += offsetY;
    Vector2D continuePos = new Vector2D(offsetX, curH);
    
    curH += (offsetY90 + Ref.Y15);
    Vector2D savePos = new Vector2D(offsetX, curH);
    
    curH += (offsetY90 + Ref.Y15);
    Vector2D loadPos = new Vector2D(offsetX, curH);
    
    curH += (offsetY90 + Ref.Y15);
    Vector2D quitPos = new Vector2D(offsetX, curH);
    
    Vector2D backgroundSureToQuitPos = new Vector2D(0.3945, 0.2986);
    
    Message continueMsg = new Message(Message.M_TYPE.Game_Pause, Message.VIEW.Pause_OFF);
    Message saveMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Save);
    Message loadMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Load);
    Message quitMsg = new Message(Message.M_TYPE.View_Msg, new ViewParam(0, 0, Message.VIEW.Main_Menu));
    
    UIGrafic backgroundSureToQuitGrafic = new UIGrafic(backgroundSureToQuitPos, ref, 0.2109f, 0.4028f, backgroundSureToQuitImg);
    UIButton continueButton = new UIButton(continuePos, ref, buttonWidth, offsetY90, continueImg, buttonUp, buttonDown, hoverMenu, continueMsg);
    UIButton saveButton = new UIButton(savePos, ref, buttonWidth, offsetY90, saveImg, buttonUp, buttonDown, hoverMenu, saveMsg);
    UIButton loadButton = new UIButton(loadPos, ref, buttonWidth, offsetY90, loadImg, buttonUp, buttonDown, hoverMenu, loadMsg);
    UIButton quitButton = new UIButton(quitPos, ref, buttonWidth, offsetY90, quitImg, buttonUp, buttonDown, hoverMenu, quitMsg);
    
    children.add(backgroundSureToQuitGrafic);
    children.add(continueButton);
    children.add(saveButton);
    children.add(loadButton);
    children.add(quitButton);
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    /*int W = gc.getWidth();
    int H = gc.getHeight();
    
    int WIDTH = (int)(W * width);
    int HEIGHT = (int)(H * height);
    
    //some debug BG
    g.setColor(new Color(0.5f,0.5f,0.5f));
    g.fillRect((float)pos.x*W, (float)pos.y*H, WIDTH, HEIGHT);
    */
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
