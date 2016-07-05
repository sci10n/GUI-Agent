package main;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import main.agent.Agent;
import main.agent.state.State;

public class Run {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public static Mat cvtMat(BufferedImage raw, int type) {
	
		Mat screenMat = new Mat(raw.getHeight(), raw.getWidth(),type);
		byte[] pixels = ((DataBufferByte) raw.getRaster().getDataBuffer()).getData();
		screenMat.put(0, 0, pixels);
		return screenMat;
	}

	public static BufferedImage cvtBufferedImage(Mat raw, int type) {
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
	public static void main(String[] args) {
		System.out.println("Welcome to OpenCV " + Core.VERSION);
		
		Agent agent = new Agent();
		agent.init();

		try {
			File file = new File("assets/States/Windows10_StateSpace.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder buidler;
			buidler = factory.newDocumentBuilder();
			Document doc = buidler.parse(file);
			
			agent.loadFromCML(doc.getElementsByTagName("task").item(0));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true){
			System.out.println("HELLO WORLD");
			for(State s:agent.run()){
				System.out.println(s);
			}
		}
	}
	


	
}
