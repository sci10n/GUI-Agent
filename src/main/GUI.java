package main;

import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GUI extends JFrame{

	private Display display;
	private TextArea console;
	private JLabel lbl;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public void init(){
		display = new Display(WIDTH, HEIGHT);
		console = new TextArea();
		setSize(WIDTH,HEIGHT);
		//add(console);
		add(display);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.init();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		display.render();
	}
	
	
	public void setScreenshot(BufferedImage image){
		display.setScreenshot(image);
		//display.render();

	}
}
