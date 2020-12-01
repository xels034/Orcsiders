package gui;

import menu.Ref;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.AssetManager;
import src.Message;
import src.Message.UnitParam;
import src.Messenger;
import src.Unit;
import src.Vector2D;
import src.Vector2I;

public class GameMenu extends UIElement{
  
  private Image   fastSquad, menue, slowSquad, buttonDown, buttonUp, hoverUnit, hoverMenu, buttonUpMenu, buttonDownMenu,
          fireUp, fireDown, fireHover, earthUp, earthDown, earthHover, iceUp, iceDown, iceHover, ring, resourcesImg /*hexagon*/;
  //private int selected = -1;
  Vector2D ref;
  //static private float offsetX4 = 0.002f;
  //static private float offsetY4 = 0.0037f;
  static private float offsetY30 = 0.0278f;
  static private float offsetX15 = 0.0078f;
  static private float offsetY6 = 0.0056f;

  UIText currency1Text, currency2Text;
  
  public GameMenu(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    float offsetY = 0.0164f;
    float offsetX = 0.0078f;
    float currencyH = 0.0556f;
    float gameMenuW = 0.1953f;
    float unitH = 0.1306f;
    float menuH = 0.1111f;
    this.ref = ref;

      fastSquad = AssetManager.getCachedImage("assets/generic/buttons/fastSquad.png");
      menue = AssetManager.getCachedImage("assets/generic/buttons/menue.png");
      slowSquad = AssetManager.getCachedImage("assets/generic/buttons/slowSquad.png");
      buttonDown = AssetManager.getCachedImage("assets/generic/buttons/buttonDown.png");
      buttonUp = AssetManager.getCachedImage("assets/generic/buttons/buttonUp.png");
      buttonUpMenu = AssetManager.getCachedImage("assets/generic/buttons/buttonUpMenu.png");
      buttonDownMenu = AssetManager.getCachedImage("assets/generic/buttons/buttonDownMenu.png");
      hoverUnit = AssetManager.getCachedImage("assets/generic/buttons/hoveredGameMenu.png");
      hoverMenu = AssetManager.getCachedImage("assets/generic/buttons/hoveredGameMainMenu.png");
      ring = AssetManager.getCachedImage("assets/generic/buttons/F_Ring.png");
      resourcesImg = AssetManager.getCachedImage("assets/generic/buttons/Resources.png");
      
      fireUp = AssetManager.getCachedImage("assets/generic/buttons/fireUp.png");
      fireDown = AssetManager.getCachedImage("assets/generic/buttons/fireDown.png");
      fireHover = AssetManager.getCachedImage("assets/generic/buttons/fireHover.png");
      
      earthUp = AssetManager.getCachedImage("assets/generic/buttons/earthUp.png");
      earthDown = AssetManager.getCachedImage("assets/generic/buttons/earthDown.png");
      earthHover = AssetManager.getCachedImage("assets/generic/buttons/earthHover.png");
      
      iceUp = AssetManager.getCachedImage("assets/generic/buttons/iceUp.png");
      iceDown = AssetManager.getCachedImage("assets/generic/buttons/iceDown.png");
      iceHover = AssetManager.getCachedImage("assets/generic/buttons/iceHover.png");
      
      //hexagon = AssetManager.getCachedImage("assets/generic/buttons/hexagon.png");
    
    float curH = 0;
    Color currencyTextColor = Color.yellow;
    
    curH += offsetY;
    Vector2D currency1Pos = new Vector2D(offsetX, curH);
    Vector2D currency1textPos = new Vector2D(Ref.X170, offsetY + Ref.Y37);
    
    curH += (currencyH + offsetX15);
    Vector2D currency2textPos = new Vector2D(Ref.X300, offsetY + Ref.Y37);
    
    Vector2D ringPos = new Vector2D(0.055f, 0.265f);
    
    curH += (currencyH + offsetY30);
    Vector2D firetowerPos = new Vector2D(Ref.X143, Ref.Y200);
    
    curH += (unitH + offsetY6);
    Vector2D earthtowerPos = new Vector2D(Ref.X227, Ref.Y200+Ref.Y176);
    
    curH += (unitH + offsetY6);
    Vector2D icetowerPos = new Vector2D(Ref.X60, Ref.Y200+Ref.Y176);
    
    curH += (unitH + offsetY6);
    Vector2D slowSquadPos = new Vector2D(offsetX, curH);
    
    curH += (unitH + offsetY6);
    Vector2D fastSquadPos = new Vector2D(offsetX, curH);
    
    curH += (unitH + offsetY30 + offsetY6);
    Vector2D menuePos = new Vector2D(offsetX, curH);
    Vector2D asd = new Vector2D(0,0);

    Message m1 = new Message(Message.M_TYPE.Select_Unit_To_Build, new UnitParam(asd,Unit.fire,0));
    Message m2 = new Message(Message.M_TYPE.Select_Unit_To_Build, new UnitParam(asd,Unit.ice,1));
    Message m3 = new Message(Message.M_TYPE.Select_Unit_To_Build, new UnitParam(asd,Unit.earth,2));
    Message m4 = new Message(Message.M_TYPE.Select_Unit_To_Build, new UnitParam(asd,Unit.pSlow,3));
    Message m5 = new Message(Message.M_TYPE.Select_Unit_To_Build, new UnitParam(asd,Unit.pFast,4));
    Message m6 = new Message(Message.M_TYPE.Game_Pause, Message.VIEW.Pause_ON);
    
    //UIGrafic currency1grafic = new UIGrafic(currency1Pos, ref, gameMenuW, currencyH, currency1);
    //UIGrafic currency2grafic = new UIGrafic(currency2Pos, ref, gameMenuW, currencyH, currency2);
    UIGrafic resourcesGrafic = new UIGrafic(currency1Pos, ref, gameMenuW, 0.125f, resourcesImg);
    UIButton firetowerbutton = new UIButton(firetowerPos, ref, Ref.X120, Ref.Y120, null, fireUp, fireDown, fireHover, m1);
    UIButton earthtowerbutton = new UIButton(icetowerPos, ref, Ref.X120, Ref.Y120, null, iceUp, iceDown, iceHover, m2);
    UIButton icetowerbutton = new UIButton(earthtowerPos, ref, Ref.X120, Ref.Y120, null, earthUp, earthDown, earthHover, m3);
    UIButton slowSquadbutton = new UIButton(slowSquadPos, ref, gameMenuW, unitH, slowSquad, buttonUp, buttonDown, hoverUnit, m4);
    UIButton fastSquadbutton = new UIButton(fastSquadPos, ref, gameMenuW, unitH, fastSquad, buttonUp, buttonDown,hoverUnit, m5);
    UIButton menu = new UIButton(menuePos, ref, gameMenuW, menuH, menue, buttonUpMenu, buttonDownMenu, hoverMenu, m6);
    currency1Text = new UIText(currency1textPos, ref, 0, 0, "0", currencyTextColor);
    currency2Text = new UIText(currency2textPos, ref, 0, 0, "1231", currencyTextColor);
    UIGrafic ringGrafic = new UIGrafic(ringPos, ref, Ref.X195, Ref.Y184, ring);
    
    this.children.add(ringGrafic);
    //this.children.add(currency1grafic);
    //this.children.add(currency2grafic);
    this.children.add(resourcesGrafic);
    this.children.add(firetowerbutton);
    this.children.add(earthtowerbutton);
    this.children.add(icetowerbutton);
    this.children.add(slowSquadbutton);
    this.children.add(fastSquadbutton);
    this.children.add(menu);
    this.children.add(currency1Text);
    this.children.add(currency2Text);
    
    Messenger.subscribe(this, Message.M_TYPE.Update_Texts);
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
  public void handleMessage(Message m){
    if(m.getMsgType() == Message.M_TYPE.HW_Input){
      for(UIElement ui : children){
        ui.handleMessage(m);
      }
    }else if(m.getMsgType() == Message.M_TYPE.Update_Texts){
      Vector2I texts = (Vector2I)m.getParams();
      currency1Text.setText(texts.x+"");
      currency2Text.setText(texts.y+"");
    }
  }
}
