package main;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Display extends Canvas implements Runnable{

	private BufferStrategy bs;
	private int width; 
	private int height;
	
	private BufferedImage offscreen;
	private Graphics2D graphics;
	
	private BufferedImage screenshot;
	
	public Display(int width, int height) {
		this.width = width;
		this.height = height;
		offscreen = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		
	}
	public void init(){
		new Thread(this).start();
	}
	
	public void render(){
		
		graphics = (Graphics2D) offscreen.createGraphics();
		bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}
		graphics.clearRect(0, 0, width, height);
		graphics.drawImage(screenshot, 0, 0, width, height, null);
		
		bs.getDrawGraphics().drawImage(offscreen, 0, 0, width,height,null);
		graphics.dispose();
		bs.show();
	}
	
	public void setScreenshot(BufferedImage screenshot) {
		this.screenshot = screenshot;
	}
	@Override
	public void run() {
		while(true){
			render();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
