package src;

import gui.Hexfield.BuffState;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import src.Message.ViewParam;

public class AudioManager {

  public static boolean sound;
  
  private static ProxyHandler listener;
  
  private static Audio alc_drop, ress_drop, press, hover, fire_t_f, fire_t_i, fire_t_e, fire_u,
                       impact, death, escape, newWave, place,
                       no_move, no_ress, acc_move, game_over, hero_spawn;
  
  private static Audio music;
  
  public static void setUp(){
    listener = new ProxyHandler(AudioManager.class);
    Messenger.subscribe(listener, Message.M_TYPE.UI_Sound);
    Messenger.subscribe(listener, Message.M_TYPE.Game_Event);
    Messenger.subscribe(listener, Message.M_TYPE.Cannot_Move);
    Messenger.subscribe(listener, Message.M_TYPE.Confirm_Placement);
    Messenger.subscribe(listener, Message.M_TYPE.New_Wave);
    Messenger.subscribe(listener, Message.M_TYPE.View_Msg);
    Messenger.subscribe(listener, Message.M_TYPE.Move_selection);
    
    
    try {
      alc_drop = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/alcohol_dropped.wav"));
      ress_drop = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/ressource_dropped.wav"));
      press = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/button_press.wav"));
      hover = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/button_hover.wav"));
      place = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/place_tower.wav"));
      fire_t_f = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/tower_attack_fire.wav"));
      fire_t_i = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/tower_attack_ice.wav"));
      fire_t_e = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/tower_attack_earth.wav"));
      fire_u = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/unit_attack.wav"));
      impact = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/projectile_hit.wav"));
      death = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/unit_dies.wav"));
      escape = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/unit_escapes.wav"));
      newWave = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/new_wave.wav"));
      no_move = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/cant_move_or_place_unit.wav"));
      no_ress = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/not_enough_ressources.wav"));
      acc_move = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/movement_confirmation.wav"));
      game_over = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/gameover.wav"));
      hero_spawn = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/hero_spawned.wav"));
      
      music = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("assets/sounds/music.ogg"));
      //music = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/sounds/hero_spawned.wav"));
      music.playAsMusic(1, .01f, true);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    SoundStore.get().setSoundVolume(.1f);
  }
  
  public static void update(){
    SoundStore.get().poll(0);
  }
  
  public static void proxyHandleMessage(Message m){
    switch(m.getMsgType()){
    case UI_Sound:
      switch((Message.UI_SOUND_TYPE)m.getParams()){
      case hover:
        hover.playAsSoundEffect(1, .3f, false);
        break;
      case press:
        press.playAsSoundEffect(1, 1, false);
        break;
      case select:
        break;
      case Not_Enough_Money:
        no_ress.playAsSoundEffect(1, 1, false);
        break;
      case SoundOn:
        SoundStore.get().setSoundVolume(0f);
        SoundStore.get().setMusicVolume(0f);
        break;
      case SoundOff:
        SoundStore.get().setSoundVolume(.1f);
        SoundStore.get().setMusicVolume(1f);
        break;
      }
      break;
    case Game_Event:
      Message.GEventParam gep = (Message.GEventParam)m.getParams();
      switch(gep.get){
      case Fire:
        if(gep.source.maxSpeed == 0) {
          //fire_t.playAsSoundEffect(1, 1, false);
          if(gep.source.type == BuffState.Fire) fire_t_f.playAsSoundEffect(1, .15f, false);
          else if(gep.source.type == BuffState.Ice) fire_t_i.playAsSoundEffect(1, .4f, false);
          else if(gep.source.type == BuffState.Earth) fire_t_e.playAsSoundEffect(1, .4f, false);
        }
        else {
          fire_u.playAsSoundEffect(1, 1, false);
        }
        break;
      case Impact:
        impact.playAsSoundEffect(1, .4f, false);
        break;
      case Unit_Died:
        death.playAsSoundEffect(1, 1, false);
        if(gep.source.layer == 1){
          Vector2I cost = gep.source.curr.copy();
          if(cost.x > 0) ress_drop.playAsSoundEffect(1, 1, false);
          if(cost.y > 0) alc_drop.playAsSoundEffect(1, .2f, false);
        }
        break;
      case Unit_Escaped:
        escape.playAsSoundEffect(1, 1, false);
        break;
      case Unit_Spawned:
        //applies only to hero
        if(gep.source.layer == 1 && gep.source.type == BuffState.Neutral) {
          hero_spawn.playAsSoundEffect(1, 1, false);
        }
        break;
      }
      break;
    case Confirm_Placement:
      place.playAsSoundEffect(1, 1, false);
      break;
    case New_Wave:
      newWave.playAsSoundEffect(1, 1, false);
      break;
    case View_Msg:
      ViewParam v = (ViewParam)m.getParams();
      if(v.view == Message.VIEW.Game_Over) game_over.playAsSoundEffect(1, 1, false);
      break;
    case Cannot_Move:
      no_move.playAsSoundEffect(1, .4f, false);
      break;
    case Move_selection:
      acc_move.playAsSoundEffect(1, 1f, false);
      break;
    default:
      break;
    }
    
  }
}
