package src;

import java.lang.reflect.InvocationTargetException;

public class ProxyHandler implements Handler{

  @SuppressWarnings("rawtypes")
  java.lang.Class staticHandler;
  
  @SuppressWarnings("rawtypes")
  public ProxyHandler(java.lang.Class c){
    staticHandler=c;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void handleMessage(Message m) {
    try {
      staticHandler.getMethod("proxyHandleMessage", m.getClass()).invoke(null, m);
    } catch (IllegalAccessException    |
         IllegalArgumentException  |
         InvocationTargetException |
         NoSuchMethodException     |
         SecurityException        e) {
      
      e.printStackTrace();
    }
  }

}
