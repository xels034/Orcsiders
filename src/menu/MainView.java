package menu;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import src.Message;
import src.Message.VIEW;
import src.Message.ViewParam;
import src.Messenger;
import src.Vector2D;
import gui.LevelView;
import gui.UIElement;

public class MainView extends UIElement{
  
  private Vector2D ref;
  public GameContainer gc;

  public MainView(Vector2D pos, Vector2D ref, float width, float height) {
    super(pos, width, height);
    // TODO Auto-generated constructor stub
    
    //System.out.println("Create MainView");
    
    this.ref = ref;
    
    LinkedList<Message.M_TYPE> ll = new LinkedList<>();
    
    ll.add(Message.M_TYPE.HW_Input);
    ll.add(Message.M_TYPE.View_Msg);
    ll.add(Message.M_TYPE.Quit_Msg);
    
    //Messenger.send(new Message(Message.M_TYPE.View_Msg, new Message.ViewParam(0, 0, Message.VIEW.Game_Over)));
    
    Messenger.subscribe(this, ll); 
    
    children.add(new MainMenu(new Vector2D(0,0), ref, 1, 1));
  }

  @SuppressWarnings("hiding")
    @Override
  public void render(Graphics g, GameContainer gc) {
    for(UIElement ui : children){
      ui.render(g, gc);
    }
  }

  @Override
  public void update(int delta) {
    for(UIElement uie : children){
      uie.update(delta);
    }
    
  }

  @Override
  public void handleMessage(Message m) {
    // TODO Auto-generated method stub
    switch(m.getMsgType()){
      case View_Msg:
        //Messenger.fire(new Message(Message.M_TYPE.Pause, Message.VIEW.Pause_OFF));
        switch(((ViewParam)m.getParams()).view){
        case Level_View:
          //clearR();
          children.pop().unsubscribeAll();
          //children.removeLast();
          children.add(new LevelView(new Vector2D(0,0), ref, 1, 1));
          //Messenger.send(new Message(Message.M_TYPE.Pause, Message.VIEW.Pause_ON));
          
          break;
        case Main_Menu:
          //clearR();
          children.pop().unsubscribeAll();
          //children.removeLast();
          children.add(new MainMenu(new Vector2D(0,0), ref, 1, 1));
          //Messenger.send(new Message(Message.M_TYPE.Pause, Message.VIEW.Pause_OFF));
          break;
        case Game_Over:
          //if(!loose){
            children.pop().unsubscribeAll();
            children.add(new GameOver(new Vector2D(0,0), ref, 1, 1));
            System.out.println("Game_Over");
          //}
          break;
        case Game_Won:
          //if(!won){
            System.out.println("Game_Won");
            children.pop().unsubscribeAll();
            children.add(new WinTheGame(new Vector2D(0,0), ref, 1, 1));
          //}
          break;
        default:
          break;
        }
        break;
      case Quit_Msg:
        switch((VIEW)m.getParams()){
        case Quit:
          gc.exit();
          break;
        default:
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

}
