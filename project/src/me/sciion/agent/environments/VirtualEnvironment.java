package me.sciion.agent.environments;

import java.awt.image.BufferedImage;

import me.sciion.agent.utils.Location;

public interface VirtualEnvironment {

    
   public void rightClick();
   public void leftClick();
   public void type(String line);
   public void move(Location location);
   public BufferedImage getScreenshot();
}
