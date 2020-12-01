package src;

import org.newdawn.slick.Image;

import gui.Animation;
import gui.Hexfield.BuffState;

public class Explosion extends Entity{

  private Animation anim;
  private long birth;
  private long lifeTime;
  
  public Explosion(int i, Vector2D p, BuffState s) {
    super(i, p, s);
    switch(s){
    case Fire:
      anim = new Animation("assets/generic/effects/impact1");
      break;
    case Ice:
      anim = new Animation("assets/generic/effects/impact2");
      break;
    case Earth:
      anim = new Animation("assets/generic/effects/impact3");
      break;
    case Neutral:
      anim = new Animation("assets/generic/effects/impact1");
      break;
    }
    anim.start();
    birth = System.currentTimeMillis();
    lifeTime = anim.getDuration();
  }

  @Override
  public Image getImage() {
    return anim.getImage();
  }

  @Override
  public void update(double dt) {
    if(birth+lifeTime < System.currentTimeMillis()) life = 0;
  }

}
