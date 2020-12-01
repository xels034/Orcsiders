package src;

public class Message {

  public enum M_TYPE{None,Hallo,
             Place_Unit,
             Confirm_Placement,
             Unbuild_Unit,
             Remove_Unit,
             Select_Unit_To_Build,
             Release_Selection,
             Cannot_Move,
             Destination_Reached,
             Move_selection,
             New_Wave,
             Pause,
             HW_Input,
             Game_Pause,
             View_Msg,
             Level_Diff,
             Level_Select,
             Menu_Msg,
             Quit_Msg,
             Option_Select,
             Game_Event,
             UI_Sound,
             Update_Texts,
             Next_Wave,
             Display_Select,
             Resolution_Select
  }
  
  public enum HW_EVENT_TYPE{
    KP, //KeyPressed
    KR, //KeyReleased
    MP, //MousePressed
    MR, //MouseReleased
    MM, //MouseMoved
    MD, //MouseDragged
    MS  //MouseScrolled
  }
  
  public enum VIEW{
      Main_Menu,
      Pause_ON,
      Pause_OFF,
      Level_View,
      Credits,
      Load,
      Save,
      Quit,
      Options,
      Quit_ON,
      Quit_OFF,
      Level_Selection,
      Game_Over,
      Game_Won
  }
  
  public enum LVL_DIFFICULTY{
    Faceroll,
    Easy,
    Medium,
    Hard,
    Masochist
  }
  
  public enum OPTION{
      Resolution,
      DisplayMode
  }
  
  public enum DISPLAYMODE{
    Fullscreen,
    Window
  }
  
  public enum RESOLUTION{
    P360,
    P720,
    P1080,
    P2160,
    P4320
  }
  
  public enum G_EVENT_TYPE{
    Unit_Spawned,
    Unit_Died,
    Unit_Escaped,
    Fire,
    Impact
  }
  
  public enum UI_SOUND_TYPE{
    hover,
    press,
    select,
    Not_Enough_Money,
    SoundOn,
    SoundOff
  }
  
  public static class ViewParam{
     public int lvl;
     public int difficulty;
     public VIEW view;
     
     public ViewParam(int lvl, int difficulty, VIEW view){
       this.lvl = lvl;
       this.difficulty = difficulty;
       this.view = view;
     }
  }
  
  public static class UnitParam{
    public Vector2D pos;
    public Unit unit;
    public int animIdx;
    
    public UnitParam(Vector2D p, Unit u, int idx){
      pos = p.copy();
      unit=u;
      animIdx = idx;
    }
  }
  
  public static class HWParam{
    public HW_EVENT_TYPE evnt;
    public float oldx, oldy, newx, newy, custom;
    public char c;
  }
  
  public static class MoveParam{
    public Vector2I start, end;
    public MoveParam(){
      start = new Vector2I(0,0);
      end = new Vector2I(0,0);
    }
    public MoveParam(Vector2I a, Vector2I b){
      start  =a;
      end = b;
    }
  }
  
  public static class GEventParam{
    public Unit source,target;
    public G_EVENT_TYPE get;
    public Object custom;
    public GEventParam(G_EVENT_TYPE g){
      get=g;
    }
    public GEventParam(G_EVENT_TYPE g, Unit s, Unit t, Object o){
      get=g;
      source=s;
      target=t;
      custom = o;
    }
  }
  
  private M_TYPE msgType;
  private Object params;
  
  public Message(M_TYPE m){
    msgType=m;
  }
  
  public Message(M_TYPE m, Object o){
    msgType = m;
    params = o;
  }
  
  public M_TYPE getMsgType(){
    return msgType;
  }
  
  public Object getParams(){
    return params;
  }
  
  public void setParams(Object o){
    params = o;
  }
  
  public Message copy(){
    return new Message(msgType, params);
  }
}
