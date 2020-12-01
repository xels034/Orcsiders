package src;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Messenger {

  private static HashMap<Message.M_TYPE,LinkedList<Handler>> handleMap = new HashMap<>();
  private static LinkedList<Message> mQueue = new LinkedList<>();
  private static Object monitor = new Object();
  
  public static void subscribe(Handler h, Message.M_TYPE msgT){
    synchronized(monitor){
      if(!handleMap.containsKey(msgT)){
        handleMap.put(msgT, new LinkedList<Handler>());
      }
      handleMap.get(msgT).add(h);
    }
  }
  
  public static void subscribe(Handler h, Collection<Message.M_TYPE> msgT){
    for(Message.M_TYPE i : msgT){
      subscribe(h, i);
    }
  }
  
  public static void unsubscribe(Handler h, Message.M_TYPE msgT){
    synchronized(monitor){
      if(handleMap.containsKey(msgT)){
        handleMap.get(msgT).remove(h);
      }
    }
  }
  
  public static void unsubscribe(Handler h){
    synchronized(monitor){
      for(Message.M_TYPE msgT : handleMap.keySet()){
        handleMap.get(msgT).remove(h);
      }
    }
  }
  
  public static void fire(Message m){
    if(handleMap.containsKey(m.getMsgType())){
      for(Handler h : handleMap.get(m.getMsgType())){
        synchronized(h){
          h.handleMessage(m);
        }
      }
    }
  }
  
  public static void send(Message m){
    //System.out.println("[MESSENGER] Msg of type "+m.getMsgType()+" added to Queue");
    mQueue.add(m);
  }
  
  @SuppressWarnings("unchecked")
  public static void update(){
    LinkedList<Message> workList = (LinkedList<Message>)mQueue.clone();
    mQueue.clear();
    for(Message m : workList){
      fire(m);
    }
  }
}
