package me.sciion.agent.api;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.text.Document;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Node;

import me.sciion.agent.Run;
import me.sciion.agent.matching.Match;
import me.sciion.agent.matching.Matcher;
import me.sciion.agent.state.Template;
import me.sciion.agent.utils.KeySequence;
import me.sciion.agent.utils.Location;

public class API {
  
    static {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static final String VERSION = "Version: 0.1";
    private static Matcher matcher = new Matcher(Imgproc.TM_CCOEFF_NORMED);
    
    public static Template loadTemplate(String path){
	
	BufferedImage templateImage = Run.getImageResource(path);
	return new Template("Undefined template id",Imgproc.TM_CCOEFF_NORMED, 0.97, path,templateImage);
    }
    
    public static boolean exists(Template template){
	Match m = matcher.match(Run.cvtMat(Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR),CvType.CV_8UC3), template);
	return m.getScore() >= template.getThreshold();
    }
    
    public static void waitForChange(){
	 int hash = 0;
	 int hash2 = -1;
	   BufferedImage first = Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR);
	do{
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    BufferedImage second = Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR);
	    hash = Arrays.hashCode(((DataBufferByte) first.getRaster().getDataBuffer()).getData());
	    hash2 = Arrays.hashCode(((DataBufferByte) second.getRaster().getDataBuffer()).getData());
	}while(hash == hash2);
	return;

    }
    
    public static boolean waitForTemplate(Template template){
	Match m;
	do{
	    m = matcher.match(Run.cvtMat(Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR),CvType.CV_8UC3), template);
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}while(m.getScore() < template.getThreshold());
	return true;
    }
    
    public static Location locate(Template template){
	Match m = matcher.match(Run.cvtMat(Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR),CvType.CV_8UC3), template);
	return new Location(m.getX()+template.getImage().getWidth()/2,m.getY()+template.getImage().getHeight()/2);
    }
    
    public static void click(){
	try {
	    
	    new Robot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    Thread.sleep(1);
	    new Robot().mouseRelease(InputEvent.BUTTON1_MASK); 
	} catch (AWTException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
    
    public static void move(Location location){
	try {
	    new Robot().mouseMove(location.x, location.y);
	} catch (AWTException e) {
	    e.printStackTrace();
	}
    }
    
    public static void type(KeySequence sequence){
	
    }
    
    public static String getVersion() {
	return VERSION;
    }
    
    public static void main(String[] args){
    }
}
