package src;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class CurrencyText {

  private static final long lifeTime = 3000;
  
  public Vector2D root;
  public Image type;
  public int amount;
  public boolean alive;
  
  private long birth;
  
  public CurrencyText(Vector2D p,Image t,int a){
    root = p.copy();
    type = t;
    amount = a;
    alive = true;
    birth = System.currentTimeMillis();
  }
  
  
  public void render(GameContainer gc, Graphics g){
    int W = gc.getWidth();
    int H = gc.getHeight();
    long gap = System.currentTimeMillis()-birth;
    
    float iconSize = 0.0125f;
    
    type = type.getScaledCopy((int)(iconSize*W), (int)(iconSize*W));
    
    if(gap > lifeTime) alive = false;
    float alpha = 1-((float)gap/(float)lifeTime);
    Color c = new Color(1f,1f,0f,alpha);
    
    float yHeight = (float)((root.y-(gap/30000f))*H);

    AssetManager.smallFont.drawString((float)(root.x*W), yHeight, String.valueOf(amount),c);
    type.setAlpha(alpha);
    g.drawImage(type, (float)(root.x-0.015)*W, (float)(yHeight+(0.003*W)));
  }
}
