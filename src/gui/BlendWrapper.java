package gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class BlendWrapper {

  public enum Mode {MIX, ADD, SUBTRACT, MULTIPLY, SCREEN, ERASE}
  
  public static void setMode(Mode m){
    switch (m){
    case MIX:
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL14.glBlendEquation(GL14.GL_FUNC_ADD);
      break;
    case ADD:
      GL11.glEnable(GL11.GL_BLEND);
          GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
          GL14.glBlendEquation(GL14.GL_FUNC_ADD);
      break;
    case SUBTRACT:
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_COLOR);
      GL14.glBlendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
      break;
    case MULTIPLY:
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL14.glBlendEquation(GL14.GL_FUNC_ADD);
      break;
    case SCREEN:
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR);
      GL14.glBlendEquation(GL14.GL_FUNC_ADD);
      break;
    case ERASE:
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ZERO);
      GL14.glBlendEquation(GL14.GL_FUNC_ADD);
      break;
    }
  }
  
}
