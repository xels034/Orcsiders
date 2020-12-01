package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import src.AppWindow;
import src.AssetManager;
import src.Message;
import src.Message.VIEW;
import src.Messenger;
import src.Vector2D;
import gui.UIButton;
import gui.UIElement;
import gui.UIGrafic;

public class MainMenu extends UIElement{
  
  private Image   backgroundImg, workingTitleImg, buttonUpMainMenuImg, buttonDownMainMenuImg, hoverMenu,
          workingTitleTxt, lvlTxt, optionTxt, loadTxt, creditsTxt, quitTxt, creditsImg;
  //private int selected = -1;
  Vector2D ref;
  private UIGrafic credits;
  
  
  private SureToQuit quitGame;
  public SelectLvl selectLvl;
  public SelectOptions selectOptions;
  
  public MainMenu(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    Messenger.subscribe(this, Message.M_TYPE.Menu_Msg);
    
    //System.out.println("Create MainMenu");
    
    this.ref = ref;
    
    //Messenger.subscribe(this, Message.M_TYPE.Menu_Msg);
    
    quitGame = new SureToQuit(new Vector2D(Ref.X545, Ref.Y395), ref, Ref.X830, Ref.Y290);
    selectLvl = new SelectLvl(new Vector2D(Ref.X770, Ref.Y242), ref, Ref.X380, Ref.Y818);
    selectOptions = new SelectOptions(new Vector2D(Ref.X770, Ref.Y242), ref, Ref.X380, Ref.Y818);
    
    backgroundImg = AssetManager.getCachedImage("assets/generic/buttons/backgroundImg.png");
    workingTitleImg = AssetManager.getCachedImage("assets/generic/buttons/workingTitleImg.png");
    buttonUpMainMenuImg = AssetManager.getCachedImage("assets/generic/buttons/buttonUpMainMenuImg.png");
    buttonDownMainMenuImg = AssetManager.getCachedImage("assets/generic/buttons/buttonDownMainMenuImg.png");
    
    workingTitleTxt = AssetManager.getCachedImage("assets/generic/buttons/blank.png");
    lvlTxt = AssetManager.getCachedImage("assets/generic/buttons/lvlTxt.png");
    optionTxt = AssetManager.getCachedImage("assets/generic/buttons/optionTxt.png");
    loadTxt = AssetManager.getCachedImage("assets/generic/buttons/loadTxt.png");
    creditsTxt = AssetManager.getCachedImage("assets/generic/buttons/creditsTxt.png");
    quitTxt = AssetManager.getCachedImage("assets/generic/buttons/quitTxt.png");
    hoverMenu = AssetManager.getCachedImage("assets/generic/buttons/hoveredMainMenuButtons.png");
    
    Vector2D backgroundPos = new Vector2D(0, 0);
    Vector2D workingTitlePos = new Vector2D(Ref.X15, Ref.Y15);
    Vector2D lvlPos = new Vector2D(Ref.X15, Ref.Y242);
    Vector2D optionPos = new Vector2D(Ref.X15, Ref.Y409);
    Vector2D loadPos = new Vector2D(Ref.X15, Ref.Y576);
    Vector2D creditsPos = new Vector2D(Ref.X15, Ref.Y743);
    Vector2D quitPos = new Vector2D(Ref.X15, Ref.Y910);
    
    Message lvlMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Level_Selection);
    Message optionMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Options);
    Message loadMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Load);
    Message creditsMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Credits);
    Message quitMsg = new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Quit_ON);
    
    UIGrafic backgroundGrafic = new UIGrafic(backgroundPos, ref, 1, 1, backgroundImg);
    UIGrafic workingTitleGrafic = new UIGrafic(workingTitlePos, ref, Ref.X1890, Ref.Y200, workingTitleImg);
    UIGrafic workingTitleText = new UIGrafic(workingTitlePos, ref, Ref.X1890, Ref.Y200, workingTitleTxt);
    UIButton lvlButton = new UIButton(lvlPos, ref, Ref.X380, Ref.Y150, lvlTxt, buttonUpMainMenuImg, buttonDownMainMenuImg, hoverMenu, lvlMsg);
    UIButton optionButton = new UIButton(optionPos, ref, Ref.X380, Ref.Y150, optionTxt, buttonUpMainMenuImg, buttonDownMainMenuImg, hoverMenu, optionMsg);
    UIButton loadButton = new UIButton(loadPos, ref, Ref.X380, Ref.Y150, loadTxt, buttonUpMainMenuImg, buttonDownMainMenuImg, hoverMenu, loadMsg);
    UIButton creditsButton = new UIButton(creditsPos, ref, Ref.X380, Ref.Y150, creditsTxt, buttonUpMainMenuImg, buttonDownMainMenuImg, hoverMenu, creditsMsg);
    UIButton quitButton = new UIButton(quitPos, ref, Ref.X380, Ref.Y150, quitTxt, buttonUpMainMenuImg, buttonDownMainMenuImg, hoverMenu, quitMsg);
    
    children.add(backgroundGrafic);
    children.add(workingTitleGrafic);
    children.add(workingTitleText);
    children.add(lvlButton);
    children.add(optionButton);
    children.add(loadButton);
    children.add(creditsButton);
    children.add(quitButton);
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    // TODO Auto-generated method stub
    for(UIElement ui : children){
      if(ui == quitGame){
        g.setColor(new Color(0,0,0,0.75f));
        g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
        AppWindow.addBlur(g);
      }
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
    case Menu_Msg:
      switch((VIEW)m.getParams()) {
      case Quit_OFF:
        children.remove(quitGame);
        break;
      case Quit_ON:
        children.add(quitGame);
        break;
      case Level_Selection:
        deleteCredits();
        if(children.contains(selectLvl)) {
          selectLvl.deleteLvlButtons();
          selectLvl.difficulty = 0;
          children.remove(selectLvl);
        } else if(children.contains(selectOptions)) {
          children.remove(selectOptions);
          selectOptions.delDisplayMode();
          selectOptions.delResolutions();
          children.add(selectLvl);
        }
        else children.add(selectLvl);
        break;
      case Options:
        deleteCredits();
        if(children.contains(selectOptions)) {
          children.remove(selectOptions);
          selectOptions.delDisplayMode();
          selectOptions.delResolutions();
        } else if(children.contains(selectLvl)) {
          selectLvl.deleteLvlButtons();
          children.remove(selectLvl);
          children.add(selectOptions);
        }
        else children.add(selectOptions);
        break;
      case Credits:
        if(!children.contains(credits)) createCredits();
        else deleteCredits();
        break;
      case Load:
        deleteCredits();
        selectOptions.delDisplayMode();
        selectOptions.delResolutions();
        selectLvl.deleteLvlButtons();
        if(children.contains(selectOptions)) children.remove(selectOptions);
        if(children.contains(selectLvl)) children.remove(selectLvl);
        break;
      default:
        break;
      }
    default:
      break;
    }
  }
  
  private void hw_handling(Message m){
    Message.HWParam hwp = (Message.HWParam)m.getParams();
    if(!children.contains(quitGame)){
      switch(hwp.evnt){
      case KP:
        switch((int)(hwp.custom)){
        case Input.KEY_ESCAPE:
          Messenger.send(new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Quit_ON));
          break;
        }
        break;
      default:
        break;
      }
      
      for(UIElement uie : children){
        uie.handleMessage(m);
      }
    }
    else{
      switch(hwp.evnt){
      case KP:
        switch((int)(hwp.custom)){
        case Input.KEY_ESCAPE:
          Messenger.send(new Message(Message.M_TYPE.Menu_Msg, Message.VIEW.Quit_OFF));
          break;
        }
      default:
        break;
      }
      
      quitGame.handleMessage(m);
    }
  }
  
  public void createCredits() {
    creditsImg = AssetManager.getCachedImage("assets/generic/buttons/creditsImg.png");
    credits = new UIGrafic(new Vector2D(Ref.X410, Ref.Y242), ref, Ref.X1495, Ref.Y823, creditsImg);
    children.add(credits);
  }
  
  public void deleteCredits() {
    if(children.contains(credits)) children.remove(credits);
  }

}
