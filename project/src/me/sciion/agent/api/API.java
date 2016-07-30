package me.sciion.agent.api;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;

import me.sciion.agent.Run;
import me.sciion.agent.environments.HostSystem;
import me.sciion.agent.environments.VirtualEnvironment;
import me.sciion.agent.matching.Match;
import me.sciion.agent.matching.Matcher;
import me.sciion.agent.state.Template;
import me.sciion.agent.utils.Location;

public class API {

    static {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	setEnvironment(new HostSystem());
    }
    public static final String VERSION = "Version: 0.1";
    private static Matcher matcher = new Matcher(Imgproc.TM_CCOEFF_NORMED);

    private static VirtualEnvironment environment;

    public static void setEnvironment(VirtualEnvironment env) {
	environment = env;
    }

    public static Template loadTemplate(String path) {

	BufferedImage templateImage = Run.getImageResource(path);
	return new Template("Undefined template id", Imgproc.TM_CCOEFF_NORMED, 0.97, path, templateImage);
    }

    public static boolean exists(Template template) {
	Match m = matcher.match(Run.cvtMat(environment.getScreenshot(), CvType.CV_8UC3), template);
	return m.getScore() >= template.getThreshold();
    }

    public static void waitForChange() {
	int hash = 0;
	int hash2 = -1;
	BufferedImage first = environment.getScreenshot();
	do {
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    BufferedImage second = environment.getScreenshot();
	    hash = Arrays.hashCode(((DataBufferByte) first.getRaster().getDataBuffer()).getData());
	    hash2 = Arrays.hashCode(((DataBufferByte) second.getRaster().getDataBuffer()).getData());
	} while (hash == hash2);
	return;

    }

    public static boolean waitForTemplate(Template template) {
	Match m;
	do {
	    m = matcher.match(Run.cvtMat(environment.getScreenshot(), CvType.CV_8UC3), template);
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	} while (m.getScore() < template.getThreshold());
	return true;
    }

    public static Location locate(Template template) {
	Match m = matcher.match(Run.cvtMat(environment.getScreenshot(), CvType.CV_8UC3), template);
	return new Location(m.getX() + template.getImage().getWidth() / 2,
		m.getY() + template.getImage().getHeight() / 2);
    }

    public static void click() {
	environment.rightClick();
    }

    public static void move(Location location) {
	environment.move(location);
    }

    public static void type(String line) {
	environment.type(line);
    }

    public static String getVersion() {
	return VERSION;
    }
    public static void main(String [] args){
	
    }
}
