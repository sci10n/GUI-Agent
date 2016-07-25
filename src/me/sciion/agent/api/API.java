package me.sciion.agent.api;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.text.Document;

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
  
    public static final String VERSION = "Version: 0.1";
    private static Matcher matcher = new Matcher(Imgproc.TM_CCOEFF_NORMED);
    public static Template loadTemplate(String path){
	
	try {
	    BufferedImage templateImage = ImageIO.read(new File(path));
	    return new Template("Undefined template id",Imgproc.TM_CCOEFF_NORMED, 0.97, path,templateImage);
	} catch (IOException e) {
	    try{
		Node doc = Run.parseFromXML(path);
		if(doc == null)
		    throw new NullPointerException();
		   return Template.fromXML(doc);
	    }catch(NullPointerException ee){
		   return null;
	    }
	}
    }
    
    public static boolean exists(Template template){
	Match m = matcher.match(Run.cvtMat(Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR),CvType.CV_8UC3), template);
	return m.getScore() >= template.getThreshold();
    }
    
    public static Location locate(Template template){
	Match m = matcher.match(Run.cvtMat(Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR),CvType.CV_8UC3), template);
	return new Location(m.getX(),m.getY());
    }
    
    public static void click(int button){
	try {
	    new Robot().mousePress(button);
	    Thread.sleep(1);
	    new Robot().mouseRelease(button); 
	} catch (AWTException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public static void move(Location location){
	try {
	    new Robot().mouseMove(location.x, location.y);
	} catch (AWTException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public static void type(KeySequence sequence){
	
    }
    
    public static String getVersion() {
	return VERSION;
    }
}
