package gui;

import java.util.ArrayList;
import java.util.LinkedList;

import menu.Pause;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import src.AppWindow;
import src.EntityManager;
import src.Message;
import src.Message.UnitParam;
import src.AssetManager;
import src.Messenger;
import src.Unit;
import src.Vector2D;
import src.Vector2I;

public class LevelView extends UIElement{
  
  private Unit selection;
  private Sandbox sb;
  private Pause gamePause;
  private GameMenu gm;
  private Image bg;
  
  private ArrayList<String> unitAnims;
  
  private Vector2I curr;

  
  private UIButton nextWaveButton;
  
  public LevelView(Vector2D pos, Vector2D ref, float width, float height){
    super(pos, width, height);
    
    curr = new Vector2I(20,0);

    gm = new GameMenu(new Vector2D(0.0078, 0.0139), ref, 0.2907f, 0.9722f);
    Messenger.send(new Message(Message.M_TYPE.Update_Texts, curr));
    children.add(gm);
    sb = new Sandbox(new Vector2D(0.2109, 0.0139), ref, 0.7813f, 0.9722f, 1);
    children.add(sb);
    
    EntityManager.registerSB(sb);
    
    gamePause = new Pause(new Vector2D(0.3945, 0.2986), ref, 0.2109f, 0.4028f);
    LinkedList<Message.M_TYPE> ll = new LinkedList<>();
    ll.add(Message.M_TYPE.Select_Unit_To_Build);
    ll.add(Message.M_TYPE.Release_Selection);
    ll.add(Message.M_TYPE.Game_Pause);
    ll.add(Message.M_TYPE.Game_Event);
    Messenger.subscribe(this, ll);
    
    bg = AssetManager.getCachedImage("assets/generic/GameScreen.png");
    
    unitAnims = new ArrayList<>();
    
    unitAnims.add("assets/generic/playerUnits/fire");
    unitAnims.add("assets/generic/playerUnits/ice");
    unitAnims.add("assets/generic/playerUnits/earth");
    unitAnims.add("assets/generic/playerUnits/slow");
    unitAnims.add("assets/generic/playerUnits/fast");

                                      //to get same ration as the 151x96px texture
    nextWaveButton = new UIButton(new Vector2D(.22,.91), ref, .06f, .06f*1.7777f*(95f/151f), AssetManager.getCachedImage("assets/generic/buttons/transparent.png"),
                AssetManager.getCachedImage("assets/generic/buttons/newWaveUp.png"),
                AssetManager.getCachedImage("assets/generic/buttons/newWaveDown.png"),
                AssetManager.getCachedImage("assets/generic/buttons/newWaveHover.png"),
                new Message(Message.M_TYPE.Next_Wave));
    
    children.add(nextWaveButton);
  }
  
  @Override
  public void render(Graphics g, GameContainer gc) {
    sb.render(g, gc);
    
    bg = bg.getScaledCopy(gc.getWidth(), gc.getHeight());
    g.drawImage(bg, 0, 0);
    
    gm.render(g, gc);
    nextWaveButton.render(g, gc);
    if(children.contains(gamePause)){
      g.setColor(new Color(0,0,0,0.75f));
      g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
      AppWindow.addBlur(g);
      
      gamePause.render(g, gc);
    }
    
    
  }

  @Override
  public void update(int delta) {
    // TODO Auto-generated method stub
    sb.update(delta);
  }

  
  @Override
  public void handleMessage(Message m){
    switch(m.getMsgType()){
    case HW_Input:
      hw_handling(m);
      break;
    case Game_Event:
      Message.GEventParam gep = (Message.GEventParam)m.getParams();
      switch(gep.get){
      case Unit_Died:
        if(gep.source.layer == 1){// enemies  
          curr = curr.add(gep.source.curr);
          Messenger.send(new Message(Message.M_TYPE.Update_Texts, curr));
        }
        break;

      case Unit_Spawned:
        if(gep.source.layer == 0){ //own unit
          curr = curr.sub(gep.source.curr);
          Messenger.send(new Message(Message.M_TYPE.Update_Texts, curr));
        }
        break;
      default:
        break;
      }
      break;
    case Select_Unit_To_Build:
      UnitParam up = (UnitParam)m.getParams();
      Messenger.fire(new Message(Message.M_TYPE.Release_Selection));
      selection = up.unit.getInstance(up.pos, new AnimationSet(unitAnims.get(up.animIdx)));
      if(selection.curr.x <= curr.x && selection.curr.y <= curr.y){
        selection.setAnimStartNow();
        EntityManager.add(selection);
      }else{
        selection = null;
        Messenger.send(new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.Not_Enough_Money));
      }
      break;
    case Release_Selection:
      EntityManager.remove(selection);
      selection = null;
      break;
    case Game_Pause:
      if(m.getParams() == Message.VIEW.Pause_OFF) children.remove(gamePause);
      if(m.getParams() == Message.VIEW.Pause_ON) children.add(gamePause);
    default:
      break;
    }
  }

  private void hw_handling(Message m){
    Message.HWParam hwp = (Message.HWParam)m.getParams();
    if(!children.contains(gamePause)){
      //float md_offset = 0; //needed because stupid switch variable allocation, for shift painting in draging mode
      switch(hwp.evnt){ 
      case KP:
        switch((int)(hwp.custom)){
        case Input.KEY_ESCAPE:
          if(selection != null) {
            EntityManager.remove(selection);
            selection = null;
          }
          else Messenger.send(new Message(Message.M_TYPE.Game_Pause, Message.VIEW.Pause_ON));
          break;
        }
        break;
      case MP:
      case MD:
        //md_offset = 5.0f/1920.0f;
      case MM:
        sb.selected_Unit = selection;
        if(selection != null){
          selection.pos = new Vector2D(hwp.newx-sb.imgW/2, hwp.newy-sb.imgW/2 - sb.imgW*0.5); //-imgW so its centered
        }
        break;
      case MR:
        switch((int)hwp.custom){
        case Input.MOUSE_LEFT_BUTTON:
          if(selection != null && sb.relativeToGrid(selection.pos) != null){
            //hack 
            sb.selectionPos = null;
            Messenger.send(new Message(Message.M_TYPE.Place_Unit,
                new UnitParam(new Vector2D(hwp.newx, hwp.newy), selection, 0)));
          }
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
          Messenger.send(new Message(Message.M_TYPE.Game_Pause, Message.VIEW.Pause_OFF));
          break;
        }
      default:
        break;
      }
      gamePause.handleMessage(m);
    }
  }

}
