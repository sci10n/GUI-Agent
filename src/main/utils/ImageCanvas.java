package main.utils;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class ImageCanvas extends Canvas{

	private BufferStrategy bs;
	private int width; 
	private int height;
	private BufferedImage offscreen;
	private BufferedImage image;
	public ImageCanvas(BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(250,250));

	}
	
	@Override
	public void paint(Graphics g) {
		offscreen = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		render();
	}
	public void render(){
		
		width = getWidth();
		height = getHeight();
		Graphics2D graphics = (Graphics2D) offscreen.createGraphics();
		bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}
		graphics.clearRect(0, 0, width, height);
		graphics.drawRect(0, 0, width, height);
		if(image != null){
			graphics.drawImage(image, 0, 0, width, height, null);
		}
			
		
		bs.getDrawGraphics().drawImage(offscreen, 0, 0, width,height,null);
		graphics.dispose();
		bs.show();
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		if(image != null){
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		}
		else{
			setPreferredSize(new Dimension(250,250));
		}
		
		paint(getGraphics());
	}
	

	
}
