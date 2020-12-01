package src;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import menu.MainView;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Shader.Shader;

public class AppWindow extends BasicGame{

  private static AppGameContainer appgc;
  private static float actX, actY;
  
  private static Shader blurH, blurV;
  private static Image offscreen;
  
  public static void main(String[] args) {
    try {
      //offscreen = new Image(1280, 720)
      
      appgc = new AppGameContainer(new AppWindow("Hexagon"));
      //String[] ref = {"assets/generic/logo_t32.tga","assets/generic/logo_t24.tga","assets/generic/logo_t16.tga"};
      //appgc.setIcons(ref);
      appgc.setDisplayMode(1280, 720, false);
      appgc.setAlwaysRender(true);
      appgc.setVerbose(false);
      appgc.setSmoothDeltas(true);
      appgc.start();
    } catch (SlickException ex) {
      Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  //#########
  //NON MAIN TIME BITCHES :D
  //########
  
  MainView mainView;
  
  //#####
  //some debug shit
  //#####
  
  //UIButton b;
  //Image img;
  
  
  public AppWindow(String title) {
    super(title);
  }
  
  @Override
  public void init(GameContainer gc) throws SlickException {
    
    gc.setTargetFrameRate(60);
    gc.setShowFPS(false);
    gc.setSmoothDeltas(true);
    //gc.setMouseCursor(new Image("assets/generic/cursor.png"), 8, 7);
    

    
    Input.disableControllers();
    try {
      AssetManager.setUp();
      AudioManager.setUp();
      blurH = Shader.makeShader("assets/shader/blur/basicVert.vrt", "assets/shader/blur/blurHorizontal.frg" );
      blurV = Shader.makeShader("assets/shader/blur/basicVert.vrt", "assets/shader/blur/blurVertical.frg" );
      offscreen = AssetManager.newImage(1280, 720);
    } catch (FontFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    mainView = new MainView(new Vector2D(0,0), new Vector2D(gc.getWidth(), gc.getHeight()),1,1);
    mainView.gc = gc;
  }

  static float x = 800;
  static float y = 600;
  
  @Override
  public void render(GameContainer gc, Graphics g) throws SlickException {
    g.setAntiAlias(false);    
    //AssetManager.defaultFont.drawString(100,200,"Hallo das bin ich, der font :D", Color.yellow);
    

    
    //b.render(g);
    mainView.render(g, gc);
    //g.drawImage(img,100, 100);
    //appgc.setDisplayMode((int)x, (int)y, false);
    x-=0.1;
    y-=0.1;
  }


  @Override
  public void update(GameContainer gc, int delta) throws SlickException {
    double dt = delta/1000d;
    AssetManager.update();
    AudioManager.update();
    Messenger.update();
    EntityManager.update(dt);
    mainView.update(delta);
  }
  
  public static void addBlur(Graphics g){
    if(offscreen != null){
      g.copyArea(offscreen, 0, 0);
      g.flush();
       
      blurH.startShader();
      g.drawImage(offscreen, 0, 0);
      Shader.forceFixedShader();
        
      g.copyArea(offscreen, 0, 0);
      g.flush();
      
      blurV.startShader();
      g.drawImage(offscreen, 0, 0);
      Shader.forceFixedShader();
    }
  }
  
  //###########
  //CALLBACK TIME MOTHERFUCKERS!
  //###########
  
  @Override
  public void keyPressed(int key, char c){

    Message.HWParam hwp = new Message.HWParam();
    hwp.evnt = Message.HW_EVENT_TYPE.KP;
    hwp.oldx = actX;
    hwp.oldy = actY;
    hwp.newx = actX;
    hwp.newy = actY;
    hwp.custom = key;
    hwp.c = c;
    Messenger.send(new Message(Message.M_TYPE.HW_Input, hwp));
  }

  @Override
  public void keyReleased(int key, char c){
    //float[] a = {key, actX, actY};
    //response = mainView.handle(Event.KR, a);
    Message.HWParam hwp = new Message.HWParam();
    hwp.evnt = Message.HW_EVENT_TYPE.KR;
    hwp.oldx = actX;
    hwp.oldy = actY;
    hwp.newx = actX;
    hwp.newy = actY;
    hwp.custom = key;
    hwp.c = c;
    Messenger.send(new Message(Message.M_TYPE.HW_Input, hwp));
  }
  
  @SuppressWarnings("hiding")
    @Override
  public void mousePressed(int button, int x, int y){
    float W = appgc.getWidth();
    float H = appgc.getHeight();
    
    //float[] a = {button,x/W,y/H};
    //response = mainView.handle(Event.MP, a);
    
    Message.HWParam hwp = new Message.HWParam();
    hwp.evnt = Message.HW_EVENT_TYPE.MP;
    hwp.oldx = x/W;
    hwp.oldy = y/H;
    hwp.newx = x/W;
    hwp.newy = y/H;
    hwp.custom = button;
    //hwp.c = c;
    Messenger.send(new Message(Message.M_TYPE.HW_Input, hwp));
  }
  
  @SuppressWarnings("hiding")
    @Override
  public void mouseReleased(int button, int x, int y){
    float W = appgc.getWidth();
    float H = appgc.getHeight();
    
    //float[] a = {button,x/W,y/H};
    //response = mainView.handle(Event.MR, a);
    
    Message.HWParam hwp = new Message.HWParam();
    hwp.evnt = Message.HW_EVENT_TYPE.MR;
    hwp.oldx = x/W;
    hwp.oldy = y/H;
    hwp.newx = x/W;
    hwp.newy = y/H;
    hwp.custom = button;
    //hwp.c = c;
    Messenger.send(new Message(Message.M_TYPE.HW_Input, hwp));
  }
  
  @Override
  public void mouseMoved(int oldx, int oldy, int newx, int newy){
    float W = appgc.getWidth();
    float H = appgc.getHeight();
    
    actX = newx/W;
    actY = newy/H;
    
    //float[] a = {oldx/W,oldy/H,newx/W,newy/H};
    //response = mainView.handle(Event.MM, a);
    
    Message.HWParam hwp = new Message.HWParam();
    hwp.evnt = Message.HW_EVENT_TYPE.MM;
    hwp.oldx = oldx/W;
    hwp.oldy = oldy/H;
    hwp.newx = newx/W;
    hwp.newy = newy/H;
    //hwp.custom = button;
    //hwp.c = c;
    Messenger.send(new Message(Message.M_TYPE.HW_Input, hwp));
  }
  
  @Override
  public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    float W = appgc.getWidth();
    float H = appgc.getHeight();
    
    actX = newx/W;
    actY = newy/H;
    
    //float[] a = {oldx/W,oldy/H,newx/W,newy/H};
    //response = mainView.handle(Event.MD, a);
    
    Message.HWParam hwp = new Message.HWParam();
    hwp.evnt = Message.HW_EVENT_TYPE.MD;
    hwp.oldx = oldx/W;
    hwp.oldy = oldy/H;
    hwp.newx = newx/W;
    hwp.newy = newy/H;
    //hwp.custom = button;
    //hwp.c = c;
    Messenger.send(new Message(Message.M_TYPE.HW_Input, hwp));
  }
  
  @Override
  public void mouseWheelMoved(int change){
    //float[] a = {change, actX, actY};
    //response = mainView.handle(Event.MS, a);
    
    Message.HWParam hwp = new Message.HWParam();
    hwp.evnt = Message.HW_EVENT_TYPE.MS;
    hwp.oldx = actX;
    hwp.oldy = actY;
    hwp.newx = actX;
    hwp.newy = actY;
    hwp.custom = change;
    //hwp.c = c;
    Messenger.send(new Message(Message.M_TYPE.HW_Input, hwp));
  }
}
