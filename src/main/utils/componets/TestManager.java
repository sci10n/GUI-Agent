package main.utils.componets;

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

import main.Run;
import main.SnippingTool;
import main.agent.Match;
import main.agent.Matcher;
import main.agent.state.Template;

public class TestManager extends JPanel{

	
	private Matcher matcher;
	private JLabel templateName;
	private JLabel threshold;
	private JLabel mode;
	private JLabel score;
	public TestManager(){
		matcher = new Matcher(Imgproc.TM_CCOEFF_NORMED, 0.97);
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
		matcher.setMethod(template.getMethod());
		matcher.setThreshold(template.getThreshold());
		Mat templateMat = 	Run.cvtMat(template.getImage(),CvType.CV_8UC3);
		JOptionPane.showConfirmDialog(null, new JLabel(new ImageIcon(Run.cvtBufferedImage(templateMat, BufferedImage.TYPE_3BYTE_BGR))));

		Mat screenshotMat = Run.cvtMat(screenshot,CvType.CV_8UC3);
		System.out.println(templateMat);
		Match m = matcher.match(screenshotMat,templateMat);
		screenshotMat.release();
		
		templateMat.release();
		templateName.setText("Template: " + template.getId());
		threshold .setText("Threshold: " + matcher.getThreshold());
		mode.setText("Mode: " + matcher.getMethod());
		score.setText("Score: " + m.getScore());
		return m;
	}
}
