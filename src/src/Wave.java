package src;

import gui.AnimationSet;
import gui.Hexfield.BuffState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Wave {

  public static final int unitGap = 2000;
  public static final int waveGap = 30000; //30.000
  
  private LinkedList<Unit> contents;
  private boolean virgin;
  
  int lastIdx = -1;
  public long arrival;
  public long rootArrival;
  
  public Wave(){
    contents = new LinkedList<>();
    arrival = Long.MAX_VALUE;
    virgin = true;
  }
  
  public void addUnits(Unit u, int cnt){
    for(int i=0;i<cnt;i++){
      contents.add(u.copy());
    }
  }
  
  public int getRemainers(){
    return contents.size() - (lastIdx+1);
  }
  
  public LinkedList<Unit> getPendingUnits(long shift){
    long now = System.currentTimeMillis() - shift;
    LinkedList<Unit> units = new LinkedList<>();
    long gap = now - arrival;
    
    if(gap > 0 && lastIdx < contents.size() - 1){
      int idx = Math.min(contents.size(), (int)(gap/unitGap));
      for(int i = lastIdx+1; i<idx;i++){
        units.add(contents.get(i).copy());
        lastIdx++;
        if(virgin){
          virgin = false;
          Messenger.send(new Message(Message.M_TYPE.New_Wave));
        }
      }
    }

    return units;
  }
  
  public static LinkedList<Wave> loadLevel(int lvl) throws IOException{
      try(BufferedReader br = new BufferedReader(new FileReader("assets/generic/levels/waves"+lvl+".txt"));){
        LinkedList<Wave> contents = new LinkedList<>();
        Wave tmpW = null;
        String line;
        long now = System.currentTimeMillis();
        long sTime = now;
        int unitSum = 0;
        int waveCounter = 0;
        Unit unitPattern=null;
        
        AnimationSet[] as = {new AnimationSet("assets/generic/enemyUnits/ass"),
                         new AnimationSet("assets/generic/enemyUnits/yak"),
                         new AnimationSet("assets/generic/enemyUnits/hero")};
        int asCnt=0;
        
        line = br.readLine();
        while(line != null){
          if(line.equals("w")){
            if(tmpW != null)contents.add(tmpW);
            
            waveCounter ++;
            sTime = now;
            
            sTime = sTime + (unitSum*unitGap)+(waveGap*waveCounter);
            tmpW = new Wave();
            tmpW.arrival = sTime;
            tmpW.rootArrival = sTime;
          }else{
            String[] entry = line.split(" ");
            BuffState type = BuffState.Neutral;
            if(entry[0].equals("ass")){
              unitPattern = Unit.eFast;
              asCnt = 0;
            }else if(entry[0].equals("yak")){
              unitPattern = Unit.eSlow;
              asCnt = 1;
            }else if(entry[0].equals("hero")){
              unitPattern = Unit.eHero;
              asCnt = 2;
              //System.out.println("[WAVE]herooo");
            }
            
            if(entry[2].equals("fire")){
              type = BuffState.Fire;
            }else if(entry[2].equals("earth")){
              type = BuffState.Earth;
            }else if(entry[2].equals("ice")){
              type = BuffState.Ice;
            }
            
            unitPattern = unitPattern.getInstance(new Vector2D(0,0), as[asCnt]);
            unitPattern.type = type;
            unitSum += Integer.parseInt(entry[1]);
            tmpW.addUnits(unitPattern, Integer.parseInt(entry[1]));
          }
          line = br.readLine();
        }
        if(tmpW != null) contents.add(tmpW);
        return contents;
      }
  }
}
