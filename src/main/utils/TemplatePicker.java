package main.utils;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.security.auth.callback.Callback;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class TemplatePicker extends Canvas implements  MouseListener, MouseMotionListener{

	
	enum State{
		DEFAULT, MARKED, PICKED
	}
	private BufferStrategy bs;
	private double scaleX = 1.0;
	private double scaleY = 1.0;
	private State sampling = State.DEFAULT;
	private int startXPos = -32;
	private int startYPos = -32;
	private int endXPos = -32;
	private int endYPos = -32;
	private BufferedImage offscreen;
	private BufferedImage template;
	private Graphics2D graphics;
	private BufferedImage screenshot;
	private Vector<PickerListener> listeners;
	
	public TemplatePicker() {
		listeners = new Vector<PickerListener>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void addListener(PickerListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(PickerListener listener){
		listeners.remove(listener);
	}
	@Override
	public void paint(Graphics g) {
		offscreen = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		render();
	}
	public void render(){

		if(offscreen == null)
			return;
		
		graphics = (Graphics2D) offscreen.createGraphics();
		bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}
		if(screenshot == null)
			return;
		scaleX = (double)getWidth() / (double)screenshot.getWidth();
		scaleY = (double)getHeight() / (double)screenshot.getHeight();
		graphics.clearRect(0, 0, getWidth(), getHeight());
		graphics.setColor(Color.GREEN);
		if(sampling == State.MARKED){
			graphics.drawImage(screenshot, 0, 0, getWidth(), getHeight(), null);
			graphics.setColor(Color.GRAY);
			graphics.drawOval(startXPos-16, startYPos-16, 32, 32);
			graphics.drawOval(endXPos-16, endYPos-16, 32, 32);
			graphics.drawRect(Math.min(startXPos, endXPos), Math.min(startYPos, endYPos), Math.abs(endXPos-startXPos), Math.abs(endYPos-startYPos));
		}
		else if(sampling == State.DEFAULT){
			graphics.drawImage(screenshot, 0, 0,getWidth(), getHeight(), null);
			graphics.setColor(Color.GREEN);
			graphics.drawOval(startXPos-16, startYPos-16, 32, 32);
			graphics.drawOval(endXPos-16, endYPos-16, 32, 32);
			graphics.drawRect(Math.min(startXPos, endXPos), Math.min(startYPos, endYPos), Math.abs(endXPos-startXPos), Math.abs(endYPos-startYPos));
		}
		else if(sampling == State.PICKED){
			graphics.drawImage(screenshot, 0, 0, getWidth(), getHeight(), null);
			graphics.setColor(Color.GREEN);
			graphics.drawOval(startXPos-16, startYPos-16, 32, 32);
			graphics.drawOval(endXPos-16, endYPos-16, 32, 32);
			graphics.drawRect(Math.min(startXPos, endXPos), Math.min(startYPos, endYPos), Math.abs(endXPos-startXPos), Math.abs(endYPos-startYPos));
		}
		bs.getDrawGraphics().drawImage(offscreen, 0, 0, getWidth(), getHeight(),null);
		graphics.dispose();
		bs.show();
	}
	
	public void setScreenshot(BufferedImage screenshot) {
		this.screenshot = screenshot;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(!isVisible() || screenshot == null)
			return;
		sampling = State.MARKED;
		startXPos = e.getX();
		startYPos = e.getY();
		endXPos = e.getX();
		endYPos = e.getY();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(!isVisible() || screenshot == null)
			return;
		template = screenshot.getSubimage((int)(Math.min(startXPos, endXPos)), (int)(Math.min(startYPos, endYPos)), (int)(Math.abs(endXPos-startXPos)), (int)(Math.abs(endYPos-startYPos)));
		sampling = State.PICKED;
		for(PickerListener l: listeners){
			l.process(template);
		}
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(!isVisible() || screenshot == null)
			return;
		if(sampling == State.MARKED){
			endXPos = e.getX();
			endYPos = e.getY();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!isVisible())
			return;
	}

	public BufferedImage getScreenshot() {
		return screenshot;
	}

	public BufferedImage getTemplate() {
		return template;
	}

	/**
	 * Will lock the thread
	 * @param screenshot
	 * @return
	 */
	public BufferedImage pickTemplate(BufferedImage screenshot) {
		sampling = State.DEFAULT;
		template = null;
		setScreenshot(screenshot);
		while(template == null){
			requestFocus();
			render();
		}
			
		return template;
			
	}
	
}
