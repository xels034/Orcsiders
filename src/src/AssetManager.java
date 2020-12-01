package src;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class AssetManager {

  //Reference to the main Thread. Only this thread can access glMethods needed for loading Images
  private static Thread glThread;
  //Thrue if the reference to the glThread is set
  public static boolean useable=false;
  
  //The cache ... duh. List of SoftReferences. Java decides when its time to remove those references, even
  //if a valid reference still exists. Its memory friendlier than to hold onto the references forever
  private static ArrayList<SoftReference<Image>> cache;
  
  //Assets are referenced by indices in the cache ArrayList. This ArrayList has the corresponding paths to the assets
  private static ArrayList<String> addresses;
  
  //External Thread put their orders into this list. Key is Type of order "IMG_L" for load, "IMG_N" for new image.
  //Value has the path for loading or the dimensions for new Image. "xSize:ySize" in case of dimensions
  private static ConcurrentHashMap<String, Object> orders;
  
  //Loaded images for the external Thread.s Key is the same as above, Value is the requested image
  private static ConcurrentHashMap<String, Object> deliveries;
  
  //Dummy object to act as java Monitor.
  private static Object monitor;
  
  
  public static TrueTypeFont defaultFont;
  public static TrueTypeFont smallFont;
  
  
  //Setup all the needed references for the AssetManager to work
  public static void setUp() throws FontFormatException, IOException{
    //call only once. Better not to mess things up
    if(!useable){
      glThread = Thread.currentThread();
      //System.out.println("setup: glThread=["+Thread.currentThread()+"]");
      if(glThread.getId()!=1){
        //Typically, the main thread (the one with gl capabilities, has the id 1, but that may vary.
        //Still, i thoung a warning is apropriate in this case
        System.out.println("Warning: glThread for AssetManager may not be the main thread!");
      }
      
      //Initialize all the thingies
      cache = new ArrayList<>();
      addresses = new ArrayList<>();
      orders = new ConcurrentHashMap<>();
      deliveries = new ConcurrentHashMap<>();
      monitor = new Object();
      
      //load hardcoded asset paths. Maybe this will be a txt or something in the future
      //loadDefaultPaths();
      loadDebugPaths();
      
      //font loading
      Font awtFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("assets/fonts/rocker.ttf"));
      awtFont = awtFont.deriveFont(28f);
      defaultFont = new TrueTypeFont(awtFont, true);
      awtFont = awtFont.deriveFont(18f);
      smallFont = new TrueTypeFont(awtFont, true);
      //so that all the methods run and setup can't be called again
      useable=true;
    }else{
      System.out.println("Warning: AssetManager already setup.");
    }
  }
  
  //see method below
  public static Image getCachedImage(String name){
    return getCachedImage(resolveResource(name));
  }
  
  //callable from external threads aswell as from the glThread
  //getImage works with cache, and only registered resources (in addresses list) may be requested
  public static Image getCachedImage(int pos){
    if(useable){
      if(pos == -1) throw new IllegalArgumentException("Index is -1. Did resolveResource fail to find an index?");
      //get the image from the cache&return. if the cache is empty at this position, put a new order
      //to load that bitch :D
      SoftReference<Image> sr = cache.get(pos);
      if(sr!= null && sr.get() != null){
        return sr.get();
      }else{
        Image im = getImage(addresses.get(pos));
        
        //after loading, fill the cache with the image
        cache.set(pos, new SoftReference<>(im));
        return im;
      }
    }else{
      throw new IllegalStateException("AssetManager is not set up");
    }
  }
  
  //new image, not cached
  public static Image newImage(int x, int y){
    if(useable){
      //the glThread doesn't have to put a order, it can make a new Image itself
      if(Thread.currentThread()==glThread){
        try {
          return new Image(x,y);
        } catch (SlickException e) {
          System.out.println("Error: new Image in AssetManager by glThread");
          return null;
        }
      }else{
        synchronized(monitor){
          //external threads have to put a order. after that they wait. aufter being notified
          //they check the id an take it if it's their image
          String id = "IMG_N:"+Thread.currentThread().getId()+System.currentTimeMillis();
          orders.put(id, x+":"+y);
          while(!deliveries.containsKey(id)){
            try {
              monitor.wait();
            } catch (InterruptedException e) {
              System.out.println("Error: Waiting for newImage with ID="+id);
            }
          }
          return (Image)deliveries.remove(id);
        }
      }
    }else{
      throw new IllegalStateException("AssetManager is not set up");
    }
  }
  
  //loads any image, no caching, all paths allowed
  public static Image getImage(String pth){
    if(useable){
      //putOrder can be called by erxternal thread aswell as the glThread
      if(Thread.currentThread()==glThread){
        //System.out.println("glThread["+Thread.currentThread()+"] doing stuff");
        try {
          return new Image(pth);
        } catch (SlickException e) {
          System.out.println("Error: in new Image of putOrder of glThread");
          return null;
        }
      }else{
        //System.out.println("loadBimbo should go here");
        //external thread go here
        synchronized(monitor){
          //again synchronized for putting orders into the map, wait and get it after if it's their's
          String id = "IMG_L:"+Thread.currentThread().getId()+System.currentTimeMillis();
          orders.put(id, pth);
          while(!deliveries.containsKey(id)){
            try {
              monitor.wait();
            } catch (InterruptedException e) {
              System.out.println("Error: putOrder wait on image on ThreadID="+Thread.currentThread().getId());
            }
          }
          return (Image)deliveries.remove(id);
        }
      }
    }else{
      throw new IllegalStateException("AssetManager is not set up");
    }
  }
  
  //once a while the glThread works through all the orders
  public static void update() throws SlickException{
    if(useable){
      if(Thread.currentThread() == glThread){
        synchronized(monitor){
          if(orders.size()>0){
            //can only be called by the glThread, it loads all the external orders
            System.out.println("LoadChunk loading ...");
            //gets the first random entry of the map
            String key = orders.keySet().iterator().next();
            //first part of the key is the image order type, second is the unique id
            String type = key.split(":")[0];
            Image i = null;
            if(type.equals("IMG_L")){
              i = new Image((String)orders.get(key));
            }else if(type.equals("IMG_N")){
              String order = (String)orders.get(key);
              //dimensions are like this "xDim:yDim" eg "128:128"
              int x = Integer.parseInt(order.split(":")[0]);
              int y = Integer.parseInt(order.split(":")[1]);
              i = new Image(x,y);
            }
            //System.out.println("AssetManager.loadChunck: Added Image für external Thread.");
            deliveries.put(key, i);
            orders.remove(key);
            monitor.notifyAll();
          }
        }
    }else{
        throw new IllegalStateException("Chunck Loading only by glThread permitted!");
      }
    }else{
      throw new IllegalStateException("AssetManager is not set up");
    }
  }
  
  //get the index of a resource name in the addresses list
  public static int resolveResource(String name){
    if(useable){
      for(int i=0;i<addresses.size();i++){
        if(addresses.get(i).equals(name)) return i;
      }
    }
    return -1;
  }
  
  private static void loadDebugPaths(){
    addresses.addAll(recursivePNGIndexing("assets/generic"));
    for(int i=0;i<addresses.size();i++){
      cache.add(null);
    }
  }
  
  private static ArrayList<String> recursivePNGIndexing(String path){
    ArrayList<String> returner = new ArrayList<>();
    File root = new File(path);
    File lookAt;//needed to check if directory
    for(String s : root.list()){
      //because recursive, the path "stack" has to be appended. s = "file.ext" without the path
      lookAt = new File(path+"/"+s);
      if(lookAt.isDirectory()) {
        returner.addAll(recursivePNGIndexing(path+"/"+s));
      }
      else{
        //filter out only to index pngs. regex "\\." filters for dots .. don't know why 
        String[] name = s.split("\\.");
        String type = name[name.length-1];
        if(type.equals("png")){
          returner.add(path+"/"+s);
        }
      }
    }
    return returner;
  }
}
