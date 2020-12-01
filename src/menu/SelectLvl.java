package menu;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.AssetManager;
import src.Message;
import src.Message.LVL_DIFFICULTY;
import src.Message.ViewParam;
import src.Messenger;
import src.Vector2D;
import gui.UIButton;
import gui.UIElement;

public class SelectLvl extends UIElement{

  private Vector2D ref;
  private Image buttonUp, buttonDown, facerollTxt, easyTxt, mediumTxt, hardTxt, masochistTxt, lvl1, lvl2, lvl3, lvl4, lvl5, hoverMenu;
  private UIButton lvl1Button, lvl2Button, lvl3Button, lvl4Button, lvl5Button;
  private Message lvl1Msg, lvl2Msg, lvl3Msg, lvl4Msg, lvl5Msg;
  private Vector2D lvl1Pos, lvl2Pos, lvl3Pos, lvl4Pos, lvl5Pos;
  public int difficulty = 0;

  public SelectLvl(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    
    //System.out.println("Create SelectLvl");
    
    this.ref = ref;
    //quitGame = new SureToQuit(new Vector2D(Ref.X545, Ref.Y395), ref, Ref.X830, Ref.Y290);
    
    LinkedList<Message.M_TYPE> ll = new LinkedList<>();
    ll.add(Message.M_TYPE.Level_Select);
    Messenger.subscribe(this, ll);
    
    buttonUp = AssetManager.getCachedImage("assets/generic/buttons/buttonUpLvlSelectImg.png");
    buttonDown = AssetManager.getCachedImage("assets/generic/buttons/buttonDownLvlSelectImg.png");
    facerollTxt = AssetManager.getCachedImage("assets/generic/buttons/facerollImg.png");
    easyTxt = AssetManager.getCachedImage("assets/generic/buttons/easyImg.png");
    mediumTxt = AssetManager.getCachedImage("assets/generic/buttons/mediumImg.png");
    hardTxt = AssetManager.getCachedImage("assets/generic/buttons/hardImg.png");
    masochistTxt = AssetManager.getCachedImage("assets/generic/buttons/masochistImg.png");
    hoverMenu = AssetManager.getCachedImage("assets/generic/buttons/hoveredLvl.png");
    
    Vector2D facerollPos = new Vector2D(Ref.X770, Ref.Y242);
    Vector2D easyPos = new Vector2D(Ref.X770, Ref.Y409);
    Vector2D mediumPos = new Vector2D(Ref.X770, Ref.Y576);
    Vector2D hardPos = new Vector2D(Ref.X770, Ref.Y743);
    Vector2D masochistPos = new Vector2D(Ref.X770, Ref.Y910);
    
    Message facerollMsg = new Message(Message.M_TYPE.Level_Select, Message.LVL_DIFFICULTY.Faceroll);
    Message easyMsg = new Message(Message.M_TYPE.Level_Select, Message.LVL_DIFFICULTY.Easy);
    Message mediumMsg = new Message(Message.M_TYPE.Level_Select, Message.LVL_DIFFICULTY.Medium);
    Message hardMsg = new Message(Message.M_TYPE.Level_Select, Message.LVL_DIFFICULTY.Hard);
    Message masochistMsg = new Message(Message.M_TYPE.Level_Select, Message.LVL_DIFFICULTY.Masochist);
    
    UIButton facerollButton = new UIButton(facerollPos, ref, Ref.X380, Ref.Y150, facerollTxt, buttonUp, buttonDown, hoverMenu, facerollMsg);
    UIButton easyButton = new UIButton(easyPos, ref, Ref.X380, Ref.Y150, easyTxt, buttonUp, buttonDown, hoverMenu, easyMsg);
    UIButton mediumButton = new UIButton(mediumPos, ref, Ref.X380, Ref.Y150, mediumTxt, buttonUp, buttonDown, hoverMenu, mediumMsg);
    UIButton hardButton = new UIButton(hardPos, ref, Ref.X380, Ref.Y150, hardTxt, buttonUp, buttonDown, hoverMenu, hardMsg);
    UIButton masochistButton = new UIButton(masochistPos, ref, Ref.X380, Ref.Y150, masochistTxt, buttonUp, buttonDown, hoverMenu, masochistMsg);
    
    children.add(facerollButton);
    children.add(easyButton);
    children.add(mediumButton);
    children.add(hardButton);
    children.add(masochistButton);
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
    case Level_Select:
      switch((LVL_DIFFICULTY)m.getParams()) {
      case Faceroll:
        switch(difficulty){
        case 0:
          difficulty = 1;
          createLvlButtons();
          break;
        case 1:
          break;
        default:
          difficulty = 1;
          deleteLvlButtons();
          createLvlButtons();
          break;
        }
        break;
      case Easy:
        switch(difficulty){
        case 0:
          difficulty = 2;
          createLvlButtons();
          break;
        case 2:
          break;
        default:
          difficulty = 2;
          deleteLvlButtons();
          createLvlButtons();
          break;
        }
        break;
      case Medium:
        switch(difficulty){
        case 0:
          difficulty = 3;
          createLvlButtons();
          break;
        case 3:
          break;
        default:
          difficulty = 3;
          deleteLvlButtons();
          createLvlButtons();
          break;
        }
        break;
      case Hard:
        switch(difficulty){
        case 0:
          difficulty = 4;
          createLvlButtons();
          break;
        case 4:
          break;
        default:
          difficulty = 4;
          deleteLvlButtons();
          createLvlButtons();
          break;
        }
        break;
      case Masochist:
        switch(difficulty){
        case 0:
          difficulty = 5;
          createLvlButtons();
          break;
        case 5:
          break;
        default:
          difficulty = 5;
          deleteLvlButtons();
          createLvlButtons();
          break;
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
  
  private void hw_handling(Message m){
    for(UIElement uie : children){
      uie.handleMessage(m);
    }
  }
  
  public void deleteLvlButtons(){
    //System.out.println("childern size before: " + children.size());
    if (children.contains(lvl1Button)) children.remove(lvl1Button);
    if (children.contains(lvl2Button)) children.remove(lvl2Button);
    if (children.contains(lvl3Button)) children.remove(lvl3Button);
    if (children.contains(lvl4Button)) children.remove(lvl4Button);
    if (children.contains(lvl5Button)) children.remove(lvl5Button);
    //System.out.println("children size: " + children.size());
  }
  
  private void createLvlButtons(){
    
    lvl1Msg = new Message(Message.M_TYPE.View_Msg, new ViewParam(1, difficulty, Message.VIEW.Level_View));
    lvl2Msg = new Message(Message.M_TYPE.View_Msg, new ViewParam(1, difficulty, Message.VIEW.Level_View));
    lvl3Msg = new Message(Message.M_TYPE.View_Msg, new ViewParam(1, difficulty, Message.VIEW.Level_View));
    lvl4Msg = new Message(Message.M_TYPE.View_Msg, new ViewParam(1, difficulty, Message.VIEW.Level_View));
    lvl5Msg = new Message(Message.M_TYPE.View_Msg, new ViewParam(1, difficulty, Message.VIEW.Level_View));
    
    lvl1 = AssetManager.getCachedImage("assets/generic/buttons/lvl1Img.png");
    lvl2 = AssetManager.getCachedImage("assets/generic/buttons/lvl2Img.png");
    lvl3 = AssetManager.getCachedImage("assets/generic/buttons/lvl3Img.png");
    lvl4 = AssetManager.getCachedImage("assets/generic/buttons/lvl4Img.png");
    lvl5 = AssetManager.getCachedImage("assets/generic/buttons/lvl5Img.png");
    
    lvl1Pos = new Vector2D(Ref.X1525, Ref.Y242);
    lvl2Pos = new Vector2D(Ref.X1525, Ref.Y409);
    lvl3Pos = new Vector2D(Ref.X1525, Ref.Y576);
    lvl4Pos = new Vector2D(Ref.X1525, Ref.Y743);
    lvl5Pos = new Vector2D(Ref.X1525, Ref.Y910);
    
    lvl1Button = new UIButton(lvl1Pos, ref, Ref.X380, Ref.Y150, lvl1, buttonUp, buttonDown, hoverMenu, lvl1Msg);
    lvl2Button = new UIButton(lvl2Pos, ref, Ref.X380, Ref.Y150, lvl2, buttonUp, buttonDown, hoverMenu, lvl2Msg);
    lvl3Button = new UIButton(lvl3Pos, ref, Ref.X380, Ref.Y150, lvl3, buttonUp, buttonDown, hoverMenu, lvl3Msg);
    lvl4Button = new UIButton(lvl4Pos, ref, Ref.X380, Ref.Y150, lvl4, buttonUp, buttonDown, hoverMenu, lvl4Msg);
    lvl5Button = new UIButton(lvl5Pos, ref, Ref.X380, Ref.Y150, lvl5, buttonUp, buttonDown, hoverMenu, lvl5Msg);
    
    children.add(lvl1Button);
    children.add(lvl2Button);
    children.add(lvl3Button);
    children.add(lvl4Button);
    children.add(lvl5Button);
  }

}
