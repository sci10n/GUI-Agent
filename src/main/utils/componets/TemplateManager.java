package main.utils.componets;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.opencv.core.CvType;

import main.Run;
import main.SnippingTool;
import main.agent.state.Template;

public class TemplateManager extends JPanel {

    private Template targetTemplate;

    private JButton selectImagePath;
    private JButton snippTemplate;
    private JTextField templateImagePath;

    private JLabel targetTemplateLabelIcon;
    public void setTargetTemplate(Template targetTemplate){
	this.targetTemplate = targetTemplate;
	if(this.targetTemplate != null){
	    setEnabled(true);
	    targetTemplateLabelIcon.setIcon(new ImageIcon(Run.cvtBufferedImage(targetTemplate.getImage(), BufferedImage.TYPE_3BYTE_BGR)));
	}else{
	    targetTemplateLabelIcon.setIcon(new ImageIcon(new BufferedImage(256,256, BufferedImage.TYPE_3BYTE_BGR)));
	}

    }
    public TemplateManager() {
	this.targetTemplate = null;
    }

    public void init() {
	JPanel settingsPanel = new JPanel(new GridLayout(4, 0));
	JPanel templatePathPanel = new JPanel(new BorderLayout());
	setLayout(new BorderLayout());
	
	selectImagePath = new JButton("Select File");
	snippTemplate = new JButton("Snip");
	templateImagePath = new JTextField("No file selected");
	targetTemplateLabelIcon = new JLabel(new ImageIcon());
	settingsPanel.add(targetTemplateLabelIcon);
	templatePathPanel.add(snippTemplate, BorderLayout.LINE_START);
	templatePathPanel.add(selectImagePath, BorderLayout.LINE_END);
	templatePathPanel.add(templateImagePath, BorderLayout.CENTER);
	
	settingsPanel.add(templatePathPanel);
	add(settingsPanel,BorderLayout.CENTER);
	snippTemplate.setEnabled(false);
	selectImagePath.setEnabled(false);
	templateImagePath.setEnabled(false);

	selectImagePath.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);
		chooser.showOpenDialog(null);
		try {
		    String path = chooser.getSelectedFile().getAbsolutePath();
		    BufferedImage templateImage = null;
		    try {
			templateImage = ImageIO.read(chooser.getSelectedFile());
			if (templateImage == null)
			    throw new IOException();
		    } catch (Exception ec) {
			ec.printStackTrace();
		    }
		    targetTemplate.setPath(path);
		    targetTemplate.setImage(Run.cvtMat(templateImage, CvType.CV_8SC3));
		    targetTemplateLabelIcon.setIcon(new ImageIcon(Run.cvtBufferedImage(targetTemplate.getImage(), BufferedImage.TYPE_3BYTE_BGR)));
		} catch (Exception ec) {
		    ec.printStackTrace();
		}
	    }
	});

	snippTemplate.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		
		new Thread() {
		    public void run() {
			try {
			    BufferedImage templateImage = null;
			    try {
				templateImage = SnippingTool.snip();

				if (templateImage == null)
				    throw new IOException();
			    } catch (Exception ec) {
				ec.printStackTrace();
			    }
			    targetTemplate.setPath("");
			    targetTemplate.setImage(Run.cvtMat(templateImage, CvType.CV_8SC3));
			   
			} catch (Exception ec) {
			    ec.printStackTrace();
			}
		    }
		}.start();
		 targetTemplateLabelIcon.setIcon(new ImageIcon(Run.cvtBufferedImage(targetTemplate.getImage(),BufferedImage.TYPE_3BYTE_BGR)));

			JFrame frame2 = new JFrame("After");
			frame2.add(new JLabel(new ImageIcon(Run.cvtBufferedImage(targetTemplate.getImage(), BufferedImage.TYPE_3BYTE_BGR))));
			frame2.pack();
			frame2.setVisible(true);
	    }
	});

	templateImagePath.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		BufferedImage templateImage = null;
		try {
		    templateImage = ImageIO.read(new File(templateImagePath.getText()));
		    if (templateImage == null)
			throw new IOException();
		    targetTemplate.setPath(templateImagePath.getText());
		    targetTemplate.setImage(Run.cvtMat(templateImage, CvType.CV_8SC3));
		    targetTemplateLabelIcon.setIcon(new ImageIcon(templateImage));
		} catch (Exception ec) {
		    ec.printStackTrace();
		}
		repaint();
	    }
	});
	
	
    }
    
    @Override
    public void setEnabled(boolean enabled) {
	selectImagePath.setEnabled(enabled);
	snippTemplate.setEnabled(enabled);
	templateImagePath.setEnabled(enabled);
	super.setEnabled(enabled);
    }
    public Template getTargetTemplate() {
        return targetTemplate;
    }
}
