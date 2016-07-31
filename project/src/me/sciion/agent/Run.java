package me.sciion.agent;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import me.sciion.agent.matching.Agent;
import me.sciion.agent.state.State;

public class Run {

    static {
   	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
       }
	public static Mat cvtMat(BufferedImage raw, int type) {
		
		Mat screenMat = new Mat();
		screenMat.setTo(new Scalar(0.0));
		screenMat.create(raw.getHeight(), raw.getWidth(), type);
		byte[] pixels = ((DataBufferByte) raw.getRaster().getDataBuffer()).getData();
		screenMat.put(0, 0, pixels);
		return screenMat;
	}
	
	public static BufferedImage cvtBufferedImage(Mat rawIn, int type) {
		Mat raw = new Mat();
		rawIn.copyTo(raw);
		BufferedImage image = new BufferedImage(raw.width(), raw.height(), type);
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		raw.get(0, 0, data);
		return image;
	}
	
	public static Document parseFromXML(String path){
		try {
			File file = new File(path);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder buidler;
			buidler = factory.newDocumentBuilder();
			Document doc = buidler.parse(file);
			return doc;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage getScreenshot(int type){
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage raw = new BufferedImage(screenRect.width, screenRect.height, type);
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
	
	public static BufferedImage getImageResource(String path){
		BufferedImage image = null;
			try {
				BufferedImage raw= ImageIO.read(new File(path));
				image = new BufferedImage(raw.getWidth(), raw.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
				image.getGraphics().drawImage(raw, 0, 0, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return image;
	}
	
}
