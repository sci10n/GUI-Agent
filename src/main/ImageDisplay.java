package main;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ImageDisplay  extends JFrame implements Runnable, WindowListener{
	private boolean running = false;
		private Canvas canvas;
		private BufferStrategy bs;
		private int width; 
		private int height;
		
		private BufferedImage offscreen;
		private Graphics2D graphics;
		
		private BufferedImage image;
		
		public ImageDisplay(BufferedImage image) {
			canvas = new Canvas();
			this.image = image;
			this.width = image.getWidth();
			this.height = image.getHeight();
			canvas.setSize(width, height);
			offscreen = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			add(canvas);
			setSize(width,height);
			setResizable(false);
			setVisible(true);
			running = true;
			this.addWindowListener(this);
			new Thread(this).start();
			
		}
		
		@Override
		public void paint(Graphics g) {
			 render();
			
		}
		public void render(){
			
			graphics = (Graphics2D) offscreen.createGraphics();
			bs = canvas.getBufferStrategy();
			if (bs == null){
				canvas.createBufferStrategy(3);
				return;
			}
			graphics.clearRect(0, 0, width, height);
			graphics.drawImage(image, 0, 0, width, height, null);
			bs.getDrawGraphics().drawImage(offscreen, 0, 0, width,height,null);
			graphics.dispose();
			bs.show();
		}
		
		public void setImage(BufferedImage image) {
			this.image = image;
		}

		@Override
		public void run() {
			while(running){
				render();
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					running = false;
					e.printStackTrace();
				}
			}
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			running = false;
		}

		@Override
		public void windowClosing(WindowEvent e) {
			running = false;
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}


}
