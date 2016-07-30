package me.sciion.agent.environments;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.sciion.agent.utils.Key;
import me.sciion.agent.utils.Location;
import me.sciion.agent.utils.parser.KeyParser;

public class HostSystem implements VirtualEnvironment{

    private Robot robot;
    
    public HostSystem() {
	try {
	    robot = new Robot();
	    robot.setAutoDelay(50);
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
    public void type(String line) {
	KeyParser parser = new KeyParser();
	for(Key k: parser.parse(line)){
	    if(k.isPress())
		robot.keyPress(k.getCode());
	    else
		robot.keyRelease(k.getCode());
	}
	robot.keyPress(KeyEvent.VK_ENTER);
	robot.keyRelease(KeyEvent.VK_ENTER);

}

    @Override
    public void move(Location location) {
	robot.mouseMove(location.x, location.y);
    }
    
    @Override
    public BufferedImage getScreenshot() {
	Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	BufferedImage raw = new BufferedImage(screenRect.width, screenRect.height, BufferedImage.TYPE_3BYTE_BGR);
	try {
		BufferedImage tmp = new Robot().createScreenCapture(screenRect);
		File f = File.createTempFile("tmpImage","png");
		ImageIO.write(tmp, "png", f);
		tmp = ImageIO.read(f);
		raw.getGraphics().drawImage(tmp, 0, 0, null);
	} catch (AWTException e) {
		e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
       return raw;
    }
}
