package me.sciion.agent.gui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SnippingTool {

    public static BufferedImage screenshot;

    public static BufferedImage snip() {
	BufferedImage template = null;

	try {
	    // Setting up screenshot
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Robot robot = new Robot();
	    screenshot = robot.createScreenCapture(
		    new Rectangle(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight()));
	    BufferedImage converter = new BufferedImage(screenshot.getWidth(), screenshot.getHeight(),
		    BufferedImage.TYPE_3BYTE_BGR);
	    Graphics2D g = (Graphics2D) converter.getGraphics();
	    g.clearRect(0, 0, converter.getWidth(), converter.getHeight());
	    g.drawImage(screenshot, 0, 0, converter.getWidth(), converter.getHeight(), null);
	    screenshot = converter;
	    // Setting up window
	    JFrame frame = new JFrame();
	    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setUndecorated(true);
	    frame.setFocusable(true);
	    TemplatePicker picker = new TemplatePicker();
	    frame.add(picker);
	    frame.setVisible(true);
	    // Setting up template extraction
	    int result;

	    do {
		template = picker.pickTemplate(screenshot);
		result = JOptionPane.showConfirmDialog(frame, new JLabel(new ImageIcon(template)));
		if (result == JOptionPane.CANCEL_OPTION)
		    System.exit(0);

	    } while (result == JOptionPane.NO_OPTION);

	    // Cleaning
	    frame.setVisible(false);
	} catch (AWTException e) {
	    e.printStackTrace();
	}
	return template;
    }
}
