package menu;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import src.AssetManager;
import src.AudioManager;
import src.Message;
import src.Message.OPTION;
import src.Message.UI_SOUND_TYPE;
import src.Messenger;
import src.Vector2D;
import gui.UIButton;
import gui.UIElement;

public class SelectOptions extends UIElement{
  
  private Image buttonUp, buttonDown, soundOnTxt, soundOffTxt, resolutionTxt, hoverMenu, displayModeTxt;
  private UIButton soundButton, windowButton, fullscreenButton, r720Button, r1080Button, r360Button, r2160Button, r4320Button;
  private Vector2D ref;

  public SelectOptions(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    //System.out.println("Create SelectOptions");
    this.ref = ref;
    
    LinkedList<Message.M_TYPE> ll = new LinkedList<>();
    ll.add(Message.M_TYPE.Option_Select);
    ll.add(Message.M_TYPE.UI_Sound);
    Messenger.subscribe(this, ll);
    
    buttonUp = AssetManager.getCachedImage("assets/generic/buttons/buttonUpOptionSelectImg.png");
    buttonDown = AssetManager.getCachedImage("assets/generic/buttons/buttonDownOptionSelectImg.png");
    soundOnTxt = AssetManager.getCachedImage("assets/generic/buttons/soundOnImg.png");
    soundOffTxt = AssetManager.getCachedImage("assets/generic/buttons/soundOffImg.png");
    resolutionTxt = AssetManager.getCachedImage("assets/generic/buttons/resolutionImg.png");
    hoverMenu = AssetManager.getCachedImage("assets/generic/buttons/hoveredLvl.png");
    displayModeTxt = AssetManager.getCachedImage("assets/generic/buttons/displayModeTxt.png");
    
    Vector2D soundPos = new Vector2D(Ref.X770, Ref.Y242);
    Vector2D displayModePos = new Vector2D(Ref.X770, Ref.Y409);
    Vector2D resolutionPos = new Vector2D(Ref.X770, Ref.Y576);
    
    Message resolutionMsg = new Message(Message.M_TYPE.Option_Select, Message.OPTION.Resolution);
    Message displayModeMsg = new Message(Message.M_TYPE.Option_Select, Message.OPTION.DisplayMode);
    
    if (AudioManager.sound) soundButton = new UIButton(soundPos, ref, Ref.X380, Ref.Y150, soundOffTxt, buttonUp, buttonDown, hoverMenu, new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.SoundOff));
    else soundButton = new UIButton(soundPos, ref, Ref.X380, Ref.Y150, soundOnTxt, buttonUp, buttonDown, hoverMenu, new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.SoundOn));
    
    UIButton displayModeButton = new UIButton(displayModePos, ref, Ref.X380, Ref.Y150, displayModeTxt, buttonUp, buttonDown, hoverMenu, displayModeMsg);
    UIButton resolutionButton = new UIButton(resolutionPos, ref, Ref.X380, Ref.Y150, resolutionTxt, buttonUp, buttonDown, hoverMenu, resolutionMsg);
    
    children.add(soundButton);
    children.add(displayModeButton);
    children.add(resolutionButton);
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
    case UI_Sound:
      switch((UI_SOUND_TYPE)m.getParams()){
      case SoundOff:
        AudioManager.sound = false;
        soundButton.unsubscribeAll();
        children.remove(soundButton);
        soundButton = new UIButton(new Vector2D(Ref.X770, Ref.Y242), ref, Ref.X380, Ref.Y150, soundOnTxt, buttonUp, buttonDown, hoverMenu, new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.SoundOn));
        children.add(soundButton);
        break;
      case SoundOn:
        AudioManager.sound = true;
        soundButton.unsubscribeAll();
        children.remove(soundButton);
        soundButton = new UIButton(new Vector2D(Ref.X770, Ref.Y242), ref, Ref.X380, Ref.Y150, soundOffTxt, buttonUp, buttonDown, hoverMenu, new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.SoundOff));
        children.add(soundButton);
        break;
      default:
        break;
      }
      break;
    case Option_Select:
      switch((OPTION)m.getParams()){
      case DisplayMode:
        if(children.contains(r360Button)) delResolutions();
        if(children.contains(windowButton)) delDisplayMode();
        else createDisplayMode();
        break;
      case Resolution:
        if(children.contains(windowButton)) delDisplayMode();
        if(!children.contains(r360Button)) createResolution();
        else delResolutions();
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
  
  private void createDisplayMode(){
    
    Image fullscreenTxt = AssetManager.getCachedImage("assets/generic/buttons/fullscreenTxt.png");
    Image windowTxt = AssetManager.getCachedImage("assets/generic/buttons/windowTxt.png");
    
    Vector2D windowPos = new Vector2D(Ref.X1525, Ref.Y242);
    Vector2D fullscreenPos = new Vector2D(Ref.X1525, Ref.Y409);
    
    Message windowMsg = new Message(Message.M_TYPE.Display_Select, Message.DISPLAYMODE.Fullscreen);
    Message fullscreenMsg = new Message(Message.M_TYPE.Display_Select, Message.DISPLAYMODE.Window);
    
    windowButton = new UIButton(windowPos, ref, Ref.X380, Ref.Y150, windowTxt, buttonUp, buttonDown, hoverMenu, windowMsg);
    fullscreenButton = new UIButton(fullscreenPos, ref, Ref.X380, Ref.Y150, fullscreenTxt, buttonUp, buttonDown, hoverMenu, fullscreenMsg);
    
    children.add(windowButton);
    children.add(fullscreenButton);
  }
  
  private void createResolution() {
    Image r1080Txt = AssetManager.getCachedImage("assets/generic/buttons/r1080Txt.png");
    Image r720Txt = AssetManager.getCachedImage("assets/generic/buttons/r720Txt.png");
    Image r360Txt = AssetManager.getCachedImage("assets/generic/buttons/r360Txt.png");
    Image r2160Txt = AssetManager.getCachedImage("assets/generic/buttons/r2160Txt.png");
    Image r4320Txt = AssetManager.getCachedImage("assets/generic/buttons/r4320Txt.png");
    
    Vector2D r360Pos = new Vector2D(Ref.X1525, Ref.Y242);
    Vector2D r720Pos = new Vector2D(Ref.X1525, Ref.Y409);
    Vector2D r1080Pos = new Vector2D(Ref.X1525, Ref.Y576);
    Vector2D r2160Pos = new Vector2D(Ref.X1525, Ref.Y743);
    Vector2D r4320Pos = new Vector2D(Ref.X1525, Ref.Y910);
    
    Message r360Msg = new Message(Message.M_TYPE.Resolution_Select, Message.RESOLUTION.P360);
    Message r720Msg = new Message(Message.M_TYPE.Resolution_Select, Message.RESOLUTION.P720);
    Message r1080Msg = new Message(Message.M_TYPE.Resolution_Select, Message.RESOLUTION.P1080);
    Message r2160Msg = new Message(Message.M_TYPE.Resolution_Select, Message.RESOLUTION.P2160);
    Message r4320Msg = new Message(Message.M_TYPE.Resolution_Select, Message.RESOLUTION.P4320);
    
    r360Button = new UIButton(r360Pos, ref, Ref.X380, Ref.Y150, r360Txt, buttonUp, buttonDown, hoverMenu, r360Msg);
    r720Button = new UIButton(r720Pos, ref, Ref.X380, Ref.Y150, r720Txt, buttonUp, buttonDown, hoverMenu, r720Msg);
    r1080Button = new UIButton(r1080Pos, ref, Ref.X380, Ref.Y150, r1080Txt, buttonUp, buttonDown, hoverMenu, r1080Msg);
    r2160Button = new UIButton(r2160Pos, ref, Ref.X380, Ref.Y150, r2160Txt, buttonUp, buttonDown, hoverMenu, r2160Msg);
    r4320Button = new UIButton(r4320Pos, ref, Ref.X380, Ref.Y150, r4320Txt, buttonUp, buttonDown, hoverMenu, r4320Msg);
    
    children.add(r360Button);
    children.add(r720Button);
    children.add(r1080Button);
    children.add(r2160Button);
    children.add(r4320Button);
  }
  
  public void delDisplayMode(){
    if (children.contains(windowButton)) children.remove(windowButton);
    if (children.contains(fullscreenButton)) children.remove(fullscreenButton);
  }
  
  public void delResolutions(){
    if (children.contains(r360Button)) children.remove(r360Button);
    if (children.contains(r720Button)) children.remove(r720Button);
    if (children.contains(r1080Button)) children.remove(r1080Button);
    if (children.contains(r2160Button)) children.remove(r2160Button);
    if (children.contains(r4320Button)) children.remove(r4320Button);
  }
}
