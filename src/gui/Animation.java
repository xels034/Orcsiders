package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.Image;

import src.AssetManager;

public class Animation {

  private LinkedList<Image> frames;
  private LinkedList<Integer> timings;
  
  private long duration;
  private long start;
  private boolean run;
  
  public Animation(){
    frames = new LinkedList<>();
    timings = new LinkedList<>();
    run = false;
  }
  
  public Animation(String path){
    frames = new LinkedList<>();
    timings = new LinkedList<>();
    run = false;
    loadAnimation(path);
  }
  
  public void loadAnimation(String path){
    try (BufferedReader br = new BufferedReader(new FileReader(path+"/timings.txt"));){
      File f = new File(path);
      
      File lookAt;
      for(String s : f.list()){
        lookAt = new File(s);
        if(!lookAt.isDirectory()){
          String[] name = s.split("\\.");
          String type = name[name.length-1];
          if(type.equals("png")){
            int time = Integer.parseInt(br.readLine());
            addFrame(AssetManager.getCachedImage(path+"/"+s),time);
          }
        }
      }
      br.close();
    }catch (IOException x){
      x.printStackTrace();
    }
  }
  
  public void addFrame(Image i, Integer ts){
    if(ts <= 0) throw new IllegalArgumentException("Durations must be greater than 0ms!");
    
    frames.add(i);
    timings.add(ts);
    duration += ts;
  }
  
  public void start(){
    start = System.currentTimeMillis();
    run = true;
  }
  
  public long getDuration(){
    return duration;
  }
  
  public Image getImage(){
    if(!run) throw new IllegalStateException("Start the animation before retrieving images");
    if(frames.isEmpty()) return null;
    
    long timeSpent = System.currentTimeMillis() - start;
    return getImageAtTime(timeSpent);
  }
  
  public Image getImageAtIndex(int idx){
    return frames.get(idx);
  }
  
  public Image getImageAtTime(long time){
    if(duration == 0) throw new IllegalStateException("No Frames added yet to retrieve");
    time = time %= duration;
    
    Image i = frames.getFirst();
    long timeSum = 0;
    
    Iterator<Image> it =  frames.listIterator();
    Iterator<Integer> iti = timings.listIterator();
    
    while(timeSum < time){
      i = it.next();
      timeSum += iti.next();
    }
  
    return i;
  }
}
