package gui;

import gui.Hexfield.BuffState;
import gui.Hexfield.HexState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;

import src.AssetManager;
import src.CurrencyText;
import src.Entity;
import src.EntityManager;
import src.Explosion;
import src.Line;
import src.Message;
import src.Message.GEventParam;
import src.Message.UnitParam;
import src.Message.ViewParam;
import src.Messenger;
import src.Unit;
import src.Vector2D;
import src.Vector2I;
import src.Wave;

public class Sandbox extends UIElement{

  private static final int xMax = 16;
  private static final int yMax = 14;
  
  Polygon pgy;
  
  public Hexfield[][] playfield = new Hexfield[xMax][yMax];

  private LinkedList<Line> hexCollider;
  private LinkedList<Wave> waves;
  private LinkedList<CurrencyText> cts;
  private Image bg,fg, waveTimeIcon, timeMarker, timeLine, alcIcn, ressIcn;
  private long jumpGap, endTime, startTime;
  private boolean isPaused;
  private long pauseStamp, shift;
  
  //needed for drawing and selecting, so they're global
  public final float imgW, offset, wFac, hFac, inset;
  private Vector2I hoverPos;
  public Vector2I selectionPos;
  public Unit selected_Unit;
  private int lives = 1;
  private boolean statusSend = false;
  
  public Sandbox(Vector2D pos, Vector2D ref, float width, float height, int lvlnbr) {
    super(pos, width, height);
    Image i = AssetManager.getCachedImage("assets/generic/hexfield_2.png");
    for(int y=0;y<yMax;y++){
      for(int x=0;x<xMax;x++){
        playfield[x][y] = new Hexfield(new Vector2I(x,y),i, Hexfield.HexState.Free);
      }
    }
    
    cts = new LinkedList<>();
    isPaused = false;
    
    try(BufferedReader br = new BufferedReader(new FileReader("assets/generic/levels/lvl"+lvlnbr+".txt"));){
      
      colliding(br);
      br.readLine();
      while(wayPointing(br)) {/**/}
      br.close();
      waves = Wave.loadLevel(lvlnbr);
      endTime = waves.getLast().arrival;
      startTime = System.currentTimeMillis();
    }catch (IOException x){
      x.printStackTrace();
    }
    
      
    bg = AssetManager.getCachedImage("assets/generic/sandboxPattern.png");
    fg = AssetManager.getCachedImage("assets/generic/baumecken.png");
    waveTimeIcon = AssetManager.getCachedImage("assets/generic/buttons/waveTimeIcon.png");
    timeMarker = AssetManager.getCachedImage("assets/generic/buttons/timeMarker.png");
    timeLine = AssetManager.getCachedImage("assets/generic/buttons/timeLine.png");
    alcIcn = AssetManager.getCachedImage("assets/generic/alc_icon.png");
    ressIcn = AssetManager.getCachedImage("assets/generic/ress_icon.png");
    
    //uh ... don't ... don't even ask how I got them, I don't know either
    imgW   = width * 0.08f;   //width of the actual bitmap image
    offset = width * 0.022f;  //y-offset for the uneven columns
    inset  = width * 0.007f;  //inset of the border ... I think
    
                         //the hex is stored in a quadratic image ...
    wFac   = 0.74f;        //wFac describes the width of the x-aligned edges in respect to total imgWidth
    hFac   = 0.55f;           //height of the hexagon in respect to total imgHeight
    
    hexCollider = new LinkedList<>();
    Vector2D p1 = new Vector2D(0, 0.5);
    Vector2D p2 = new Vector2D((wFac/2), 0); //those numbers are "Magic" ... don't know how I got those :D
    Vector2D p3 = new Vector2D(1, 0);
    Vector2D p4 = new Vector2D(1+wFac/2, 0.5f);
    Vector2D p5 = new Vector2D(1, 1);
    Vector2D p6 = new Vector2D((wFac/2), 1);
    
    hexCollider.add(new Line(p1,p2));
    hexCollider.add(new Line(p2,p3));
    hexCollider.add(new Line(p3,p4));
    hexCollider.add(new Line(p4,p5));
    hexCollider.add(new Line(p5,p6));
    hexCollider.add(new Line(p6,p1));
    
    hoverPos = null;

    
    LinkedList<Message.M_TYPE> ll = new LinkedList<>();
    ll.add(Message.M_TYPE.Place_Unit);
    ll.add(Message.M_TYPE.Move_selection);
    ll.add(Message.M_TYPE.Release_Selection);
    ll.add(Message.M_TYPE.Destination_Reached);
    ll.add(Message.M_TYPE.Next_Wave);
    ll.add(Message.M_TYPE.Game_Event);
    ll.add(Message.M_TYPE.Game_Pause);
    
    Messenger.subscribe(this, ll);
  }
  
  private void colliding(BufferedReader br) throws IOException{
    for(int y=0;y<14;y++){
      String[] s = br.readLine().split(" ");
      for(int x=0;x<s.length;x++){
        if(s[x].equals("0")) playfield[x][y].state = Hexfield.HexState.Free;
        else                 playfield[x][y].state = Hexfield.HexState.Unpassable;
      }
    }
  }
  
  private boolean wayPointing(BufferedReader br) throws IOException{
    String s=br.readLine();
    String[] snbr;
    Vector2I wypO, wypN;
    wypO = wypN = null;
    snbr = s.split(" ");
    wypN = new Vector2I(Integer.parseInt(snbr[0]), Integer.parseInt(snbr[1]));
    
    s=br.readLine();
    
    while(s != null && !s.equals("s")){
      wypO = wypN;
      snbr = s.split(" ");
      wypN = new Vector2I(Integer.parseInt(snbr[0]), Integer.parseInt(snbr[1]));
      playfield[wypO.x][wypO.y].waypoints.add(wypN.copy());
      
      s = br.readLine();
    }
    return (s != null);
  }

  @Override
  public void render(Graphics g, GameContainer gc) {
    
    int bgw = (int)(width*gc.getWidth());
    int bgh = (int)(height*gc.getHeight());
    
    Image i = bg.getScaledCopy(bgw, bgh);
    
    g.drawImage(i, (float)(pos.x*gc.getWidth()), (float)(pos.y*gc.getHeight()));
    
    for(int y=0;y<yMax;y++){
      for(int x=0;x<xMax;x++){
        if(hoverPos != null && x == hoverPos.x && y == hoverPos.y) {
          if(selected_Unit != null) playfield[x][y].selection_hovered = selected_Unit;
          else                      playfield[x][y].hovered = true;
        }
        else {
          playfield[x][y].hovered = false;
          playfield[x][y].selection_hovered = null;
        }
        
        if(selectionPos != null && x == selectionPos.x && y == selectionPos.y) playfield[x][y].selected = true;
        else                                   playfield[x][y].selected = false;

        playfield[x][y].render(g, gc, this);
      }
    }
    
    for(Entity e : EntityManager.getEntityState()){
      drawEntity(e,g,gc);
    }
    
    LinkedList<CurrencyText> toDel = new LinkedList<>();
    for(CurrencyText ct : cts){
      ct.render(gc, g);
      if(!ct.alive) toDel.add(ct);
    }
    cts.removeAll(toDel);
    
    i = fg.getScaledCopy(bgw, bgh);
    g.drawImage(i, (float)(pos.x*gc.getWidth()), (float)(pos.y*gc.getHeight()));
    
    
    renderWaveTimer(g, gc);
  }
  
  private void drawEntity(Entity e, Graphics g, GameContainer gc){
    Image uImg;
    int W = gc.getWidth();
    int H = gc.getHeight();
    int uSize = (int)(imgW*W);
    
    float ancX =(float)( e.pos.x*W);
    float ancY = (float)(e.pos.y- 0.0185)*H;
    uImg = e.getImage().getScaledCopy(uSize, uSize);
    uImg.setRotation((float)(e.rotation*(180/Math.PI)));
    if(e.layer == -1){
      //uImg.setAlpha(0.5f);
      BlendWrapper.setMode(BlendWrapper.Mode.MIX);
    }
    
    g.drawImage(uImg, ancX, ancY);
    BlendWrapper.setMode(BlendWrapper.Mode.MIX);
    
    if(e instanceof Unit && e.layer != 0){
      Unit u = (Unit) e;
      
      uImg = u.getAura();
      
      if(uImg != null){
        BlendWrapper.setMode(BlendWrapper.Mode.ADD);
        g.drawImage(uImg.getScaledCopy(uSize, uSize), ancX, ancY);
        BlendWrapper.setMode(BlendWrapper.Mode.MIX);
      }

      float lbH = (float)(0.005*W);
      float lifeWidth = imgW*W;
      lifeWidth *= 0.5;
      
      float relLifeWidth = (float)(lifeWidth*(u.life / u.maxLife));
      g.setLineWidth(2f);
      
      g.setColor(new Color(.2f,.2f,.2f,.5f));

      g.fillRect(ancX+(lifeWidth/2), ancY, lifeWidth, lbH);
      
      g.setColor(new Color(1f,0f,0f));
      g.fillRect(ancX+(lifeWidth/2), ancY, relLifeWidth, lbH);
      
      g.setColor(new Color(.2f,.2f,.2f));
      g.drawRect(ancX+(lifeWidth/2), ancY, lifeWidth, lbH);

    }
    
    
    /*if(e.layer == 0){
      Unit u = (Unit)e;
      if(u.target != null){
        float tAncX = (float)(u.target.pos.x*W);
        float tAncY = (float)(u.target.pos.y - 0.0185)*H;
        g.drawLine(ancX, ancY, tAncX, tAncY);
      }
    }*/
  }
  
  
  @Deprecated
  public void drawDebugPoly(Graphics g, GameContainer gc){
    int W = gc.getWidth();
    //int H = gc.getHeight();
    
    g.setColor(new Color(1f,0f,0f));

    float ancX = (float)(pos.x+inset)*W;
    float ancY = (float)(pos.y+inset+offset-inset)*W;
    float facX = imgW*wFac*W;
    float facY = imgW*hFac*W;
    
    int b;

    if(hoverPos.x%2 == 1) b = 1;
    else b = 0;
    
    ancX +=(facX*hoverPos.x);
    ancY +=(facY*hoverPos.y) + offset*b*W;

    pgy = new Polygon();
    pgy.addPoint(ancX+(float)hexCollider.get(0).start.x*facX, ancY+(float)hexCollider.get(0).start.y*facY);
    pgy.addPoint(ancX+(float)hexCollider.get(1).start.x*facX, ancY+(float)hexCollider.get(1).start.y*facY);
    pgy.addPoint(ancX+(float)hexCollider.get(2).start.x*facX, ancY+(float)hexCollider.get(2).start.y*facY);
    pgy.addPoint(ancX+(float)hexCollider.get(3).start.x*facX, ancY+(float)hexCollider.get(3).start.y*facY);
    pgy.addPoint(ancX+(float)hexCollider.get(4).start.x*facX, ancY+(float)hexCollider.get(4).start.y*facY);
    pgy.addPoint(ancX+(float)hexCollider.get(5).start.x*facX, ancY+(float)hexCollider.get(5).start.y*facY);
    
    g.fill(pgy);
  }
  
  private void renderWaveTimer(Graphics g, GameContainer gc){
    
    Vector2D ref = new Vector2D(gc.getWidth(), gc.getHeight());
    Vector2D anch = new Vector2D((pos.x+.05)*ref.x, (pos.y+0.93)*ref.y);
    float iconSize = 0.0125f;
    float timeW = (float)(ref.x * .69);
    long now = System.currentTimeMillis() - shift;
    
    long duration = endTime - startTime;
    
    float ratio;
    long gap;

    
    
    
    timeLine = timeLine.getScaledCopy((int)(ref.x * .69), (int)(ref.y * iconSize));
    waveTimeIcon = waveTimeIcon.getScaledCopy((int)(ref.x*iconSize*3), (int)(ref.x*iconSize*3));
    timeMarker = timeMarker.getScaledCopy((int)(ref.x*iconSize*2), (int)(ref.x*iconSize*2));
    
    g.drawImage(timeLine, (float)(anch.x), (float)(anch.y));
    
    for(int i=0;i<waves.size();i++){
      gap = waves.get(i).rootArrival - startTime;
      ratio = (float)gap/(float)duration;
      g.drawImage(waveTimeIcon, (float)(anch.x + ((timeW * ratio))-(ref.x*iconSize)), (float)(anch.y - ref.y*0.03));
    }

    gap = (now+jumpGap) - startTime;
    ratio = (float)Math.min(1.0, (float)gap/(float)duration);
    
    g.drawImage(timeMarker, (float)(anch.x + (timeW * ratio)), (float)(anch.y - ref.y*0.02));
  }

  @Override
  public void update(int delta) {
    if(!isPaused){
      if(pauseStamp > 0) {
        shift += System.currentTimeMillis() - pauseStamp;
        pauseStamp = 0;
      }
      LinkedList<Unit> news = new LinkedList<>();
      int remainder = 0;
      for(Wave w : waves){
        remainder += w.getRemainers();
        news.addAll(w.getPendingUnits(shift));
      }

      if(lives == 0){
        Messenger.send(new Message(Message.M_TYPE.View_Msg, new Message.ViewParam(0, 0, Message.VIEW.Game_Over)));
        statusSend = true;
      }
      if(remainder > 0){
        Vector2D rp = gridToRelative(new Vector2I(0,0));
        for(Unit u : news){
          u.pos = rp;
          
          u.griddable = true;
          EntityManager.add(u);
          Messenger.send(new Message(Message.M_TYPE.Destination_Reached, u));
          Messenger.send(new Message(Message.M_TYPE.Game_Event, new Message.GEventParam(Message.G_EVENT_TYPE.Unit_Spawned, u, null, null)));
        }
        //layer 1, list size 0
      }else if(EntityManager.getAllUnits(1).size() == 0 && !statusSend){
        if(lives > 0 ) Messenger.send(new Message(Message.M_TYPE.View_Msg, new ViewParam(0,0,Message.VIEW.Game_Won)));       
        statusSend = true;
      }
    }else{
      if(pauseStamp == 0) pauseStamp = System.currentTimeMillis();
    }
  }
  
  @SuppressWarnings("hiding")
    public void accWaves(){
    long now = System.currentTimeMillis();
    Wave w = waves.getFirst();
    Iterator<Wave> iter = waves.listIterator();

    while(iter.hasNext() &&  w.arrival < now){
      w = iter.next();
    }
    
    if(w != null){
      long shift = w.arrival - now;
      if(shift > 0 ) jumpGap += shift;
      for(Wave ww: waves){
        if(ww.arrival > now){
          ww.arrival -= shift;
        }
      }
    }
  }
  
  public Vector2I relativeToGrid(Vector2D relative){
    //x&y must be the same. our ratio is 1.7777 tough. still don't knwo why its needed
    float x = (float)relative.x;
    float y = (float)relative.y;
    y /= 1.7777;
    
    //don't know why its inset and then offset ... but it seems to work only this way :I
    x -= (pos.x+inset);
    y -= (pos.y+offset);
    
    //divide the x by the real with of the hexagon
    float tX = x/(imgW*wFac);
    //apply vertical offset if its an uneven column
    if((int)(tX)%2 == 1) y-=offset;
    //dividing ...
    float tY = y/(imgW*hFac);
    
    //get the remainder for poly checking in that grid tile
    float inHexX = tX%1;
    float inHexY = tY%1;
    
    //depending on where the cursor is outside of the poly, different shifting is needed
    if(!polyContains(new Vector2D(inHexX, inHexY))){
      if((int)tX%2==0) tY-=1;
      if(inHexY > 0.5) tY+=1;
      tX-=1;
    }
    
    if((int)tX < 0 || (int)tY < 0 || tX > xMax || tY > yMax) return null;
    else                            return new Vector2I((int)tX,(int)tY);                     
  }
  
  public Vector2D gridToRelative(Vector2I r){
    int b;

    if(r.x%2 == 1) b = 1;
    else b = 0;
    
    float xPos = (float)  (pos.x + imgW*r.x*wFac) + inset;
    float yPos = (float)  (pos.y + imgW*r.y*hFac) + inset + offset*b;
    yPos *= 1.7777;

    return new Vector2D(xPos, yPos);
  }
  
  private boolean polyContains(Vector2D p){
    int intersects = 0;
    Line intersector = new Line(new Vector2D(-1,-1), p);
    for(Line l: hexCollider){
      if(Line.isIntersecting(l, intersector)) intersects++;
    }
    return (intersects %2 == 1);
  }
  
  @Override
  public void handleMessage(Message m){
    Unit u; //to prevent duplicate variabel error msg. stupid java
    switch(m.getMsgType()){
    case HW_Input:
      HW_handle(m);
      break;
    case Game_Pause:
      isPaused = (m.getParams() == Message.VIEW.Pause_ON);
      break;
    case Place_Unit:
      UnitParam p = (UnitParam)m.getParams();
      Vector2I posI = relativeToGrid(p.pos);
      
      if(posI != null){
        if(playfield[posI.x][posI.y].state == Hexfield.HexState.Free){
          
          //clamp to grid coordinates, then place it in the game
          p.unit.pos = gridToRelative(posI);
          p.unit.griddable = true;
          
          playfield[posI.x][posI.y].build(p.unit);
          EntityManager.applyBuff(posI, p.unit.type);
          
          Messenger.fire(new Message(Message.M_TYPE.Release_Selection));
          EntityManager.add(p.unit);
          Messenger.send(new Message(Message.M_TYPE.Game_Event,
                             new Message.GEventParam(Message.G_EVENT_TYPE.Unit_Spawned, p.unit, null, null)));
          Messenger.send(new Message(Message.M_TYPE.Confirm_Placement));
          
        }else{
          Messenger.send(new Message(Message.M_TYPE.Cannot_Move));
        }
      }
      break;
    case Move_selection:
      Message.MoveParam mp = (Message.MoveParam)m.getParams();
      u = playfield[mp.start.x][mp.start.y].getBuilding();
      if(u.speed > 0){
        playfield[mp.start.x][mp.start.y].removeBuilding();
        EntityManager.removeBuff(mp.start, u.type);
        playfield[mp.end.x][mp.end.y].state = HexState.Unpassable;
        u.issueMove(gridToRelative(mp.end));
      }
      
      break;
    case Release_Selection:
      selectionPos = null;
      break;
    case Destination_Reached:
      u = (Unit)m.getParams();
      Vector2I gridPos = relativeToGrid(u.pos.add(imgW/2));
      if(u.layer == 0){
        playfield[gridPos.x][gridPos.y].build(u);
      }else{
        if(playfield[gridPos.x][gridPos.y].waypoints.size() != 0){
          u.issueMove(gridToRelative(playfield[gridPos.x][gridPos.y].waypoints.get(
              (int)(Math.random()*playfield[gridPos.x][gridPos.y].waypoints.size()))));
        }else{
          Messenger.send(new Message(Message.M_TYPE.Game_Event,
                   new Message.GEventParam(Message.G_EVENT_TYPE.Unit_Escaped, u, null, null)));
          EntityManager.remove(u);
          if(u.type == BuffState.Neutral) lives --;
        }
      }
      break;
    case Next_Wave:
      accWaves();
      break;
    case Game_Event:
      GEventParam gep = (GEventParam)m.getParams();
      if(gep.get == Message.G_EVENT_TYPE.Unit_Died && gep.source.layer == 1){
        cts.add(new CurrencyText(gep.source.pos.add(imgW/2), ressIcn, gep.source.curr.x));
        cts.add(new CurrencyText(gep.source.pos.add(new Vector2D(0,0.025)).add(imgW/2), alcIcn, gep.source.curr.y));
      }
      break;
    default:
      break;
    }
  }
  
  private void HW_handle(Message m){
    Message.HWParam hwp = (Message.HWParam)m.getParams();
    switch(hwp.evnt){
    case KP:
      switch((int)hwp.custom){
      case Input.KEY_DELETE:
        if(selectionPos != null){
          Unit b = playfield[selectionPos.x][selectionPos.y].getBuilding();
          if(b != null){
            playfield[selectionPos.x][selectionPos.y].removeBuilding();
            EntityManager.removeBuff(selectionPos, b.type);
            EntityManager.remove(b);
            EntityManager.add(new Explosion(-1, b.pos, b.type));
            
            Messenger.send(new Message(Message.M_TYPE.Remove_Unit, b));
            Messenger.send(new Message(Message.M_TYPE.Game_Event,
                               new Message.GEventParam(Message.G_EVENT_TYPE.Impact, null, b, b.type)));
          }
        }
        break;
      default:
        break;
      }
    case MD:
    case MM:
      hoverPos = relativeToGrid(new Vector2D(hwp.newx,hwp.newy));
      break;
    case MR:
      Vector2D actPos = new Vector2D(hwp.newx, hwp.newy);
      Vector2I gridActPos = relativeToGrid(actPos);
      switch((int)hwp.custom){

      case Input.MOUSE_LEFT_BUTTON:

        selectionPos = relativeToGrid(actPos);
        if(selectionPos != null && playfield[selectionPos.x][selectionPos.y].getBuilding() != null){
          Messenger.send(new Message(Message.M_TYPE.UI_Sound, Message.UI_SOUND_TYPE.select));
        }
        break;
      case Input.MOUSE_RIGHT_BUTTON:

        
        if(selectionPos != null &&
               gridActPos != null &&
               playfield[selectionPos.x][selectionPos.y].getBuilding() != null && playfield[selectionPos.x][selectionPos.y].getBuilding().speed > 0){
               if(playfield[gridActPos.x][gridActPos.y].state == HexState.Free){
              
                Messenger.send(new Message(Message.M_TYPE.Move_selection,
                                       new Message.MoveParam(selectionPos, gridActPos)));
               }else{
                 Messenger.send(new Message(Message.M_TYPE.Cannot_Move));
               }
        }
        
        Messenger.send(new Message(Message.M_TYPE.Release_Selection));
        break;
      }
      break;
    default:
      break;
    }
  }
  
  @Override
  public void unsubscribeAll(){
    super.unsubscribeAll();
    EntityManager.unregisterSB();
    waves.clear();
  }
  
  //###################
  //###################
  
  
  @Deprecated
  public Vector2I relativeToGridUNCLAMPED(Vector2D relative){
    //x&y must be the same. our ratio is 1.7777 tough. still don't knwo why its needed
    float x = (float)relative.x;
    float y = (float)relative.y;
    y /= 1.7777;
    
    //don't know why its inset and then offset ... but it seems to work only this way :I
    x -= (pos.x+inset);
    y -= (pos.y+offset);
    
    //divide the x by the real with of the hexagon
    float tX = x/(imgW*wFac);
    //apply vertical offset if its an uneven column
    if((int)(tX)%2 == 1) y-=offset;
    //dividing ...
    float tY = y/(imgW*hFac);
    
    //get the remainder for poly checking in that grid tile
    float inHexX = tX%1;
    float inHexY = tY%1;
    
    //depending on where the cursor is outside of the poly, different shifting is needed
    if(!polyContains(new Vector2D(inHexX, inHexY))){
      if((int)tX%2==0) tY-=1;
      if(inHexY > 0.5) tY+=1;
      tX-=1;
    }
    
    return new Vector2I((int)tX,(int)tY);        
  }
}
