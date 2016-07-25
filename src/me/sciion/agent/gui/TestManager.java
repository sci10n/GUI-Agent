package me.sciion.agent.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import me.sciion.agent.Run;
import me.sciion.agent.matching.Match;
import me.sciion.agent.matching.Matcher;
import me.sciion.agent.state.Template;

public class TestManager extends JPanel{

	
	private Matcher matcher;
	private JLabel templateName;
	private JLabel threshold;
	private JLabel mode;
	private JLabel score;
	private BufferedImage testImage;
	public TestManager(){
		matcher = new Matcher(Imgproc.TM_CCOEFF_NORMED);
		testImage = new BufferedImage(250, 250, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	public void init(){
		setLayout(new BorderLayout());
		templateName = new JLabel("Template: ");
		threshold = new JLabel("Threshold: " );
		mode = new JLabel("Mode: ");
		score = new JLabel("Score: ");
		
		JPanel resultPanel = new JPanel(new FlowLayout());
		resultPanel.add(templateName);
		resultPanel.add(threshold);
		resultPanel.add(mode);
		resultPanel.add(score);
		add(resultPanel,BorderLayout.CENTER);
	}
	
	public Match performTest(BufferedImage screenshot, Template template){
		setTestImage(screenshot);
		matcher.setMethod(template.getMethod());
		Mat screenshotMat = Run.cvtMat(screenshot,CvType.CV_8UC3);
		Match m = matcher.match(screenshotMat,template);
		screenshotMat.release();
		
		templateName.setText("Template: " + template.getId());
		threshold .setText("Threshold: " + template.getThreshold());
		mode.setText("Mode: " + matcher.getMethod());
		score.setText("Score: " + m.getScore());
		return m;
	}
	
	public Match performTest( Template template){
		matcher.setMethod(template.getMethod());

		Mat screenshotMat = Run.cvtMat(testImage,CvType.CV_8UC3);
		Match m = matcher.match(screenshotMat,template);
		screenshotMat.release();
		
		templateName.setText("Template: " + template.getId());
		threshold .setText("Threshold: " + template.getThreshold());
		mode.setText("Mode: " + matcher.getMethod());
		score.setText("Score: " + m.getScore());
		return m;
	}
	
	public BufferedImage getTestImage(){
		return testImage;
	}
	public void setTestImage(BufferedImage image){
		this.testImage = image;
	}
}
