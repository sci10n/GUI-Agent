package main.utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Grabber {

	public static BufferedImage getScreenshot(int type){
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage raw = new BufferedImage(screenRect.width, screenRect.height, type);
		try {
			BufferedImage tmp = new Robot().createScreenCapture(screenRect);
			raw.getGraphics().drawImage(tmp, 0, 0, null);
		} catch (AWTException e) {
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
