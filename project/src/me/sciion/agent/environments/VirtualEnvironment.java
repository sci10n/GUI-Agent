package me.sciion.agent.environments;

import me.sciion.agent.utils.KeySequence;
import me.sciion.agent.utils.Location;

public interface VirtualEnvironment {

    
   public void rightClick();
   public void leftClick();
   public void type(KeySequence sequence);
   public void move(Location location);
   
}
