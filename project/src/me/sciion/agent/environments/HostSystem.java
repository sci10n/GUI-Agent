package me.sciion.agent.environments;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import me.sciion.agent.utils.KeySequence;
import me.sciion.agent.utils.Location;

public class HostSystem implements VirtualEnvironment{

    private Robot robot;
    
    public HostSystem() {
	try {
	    robot = new Robot();
	} catch (AWTException e) {
	    e.printStackTrace();
	}
    }
    
    @Override
    public void rightClick() {
	robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	robot.mouseRelease(InputEvent.BUTTON1_MASK); 
    }

    @Override
    public void leftClick() {
	robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
	robot.mouseRelease(InputEvent.BUTTON3_MASK); 
    }

    @Override
    public void type(KeySequence sequence) {
	
    }

    @Override
    public void move(Location location) {
	// TODO Auto-generated method stub
	
    }

}
