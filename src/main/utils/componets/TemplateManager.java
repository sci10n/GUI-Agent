package main.utils.componets;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStreamImpl;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.imgproc.Imgproc;

import main.SnippingTool;
import main.agent.state.Template;
import main.utils.StateConstructer;

public class TemplateManager extends JPanel {

	private Template targetTemplate;

	private JButton selectImagePath;
	private JButton snippTemplate;
	private JTextField templateImagePath;

	private JLabel targetTemplateLabelIcon;

	private JRadioButton ccoeffButton;
	private JRadioButton ccorrButton;
	private JRadioButton sqdiffButton;

	private JSlider acceptThreshold;
	private JLabel acceptThresholdLabel;
	
	private StateConstructer constructer;
	public void setTargetTemplate(Template targetTemplate) {
		this.targetTemplate = targetTemplate;
		if (this.targetTemplate != null) {
			setEnabled(true);
			if (targetTemplate.getImage() == null) {
				targetTemplateLabelIcon
						.setIcon(new ImageIcon(new BufferedImage(400, 400, BufferedImage.TYPE_3BYTE_BGR)));
				templateImagePath.setText(targetTemplate.getPath());
			} else {
				targetTemplateLabelIcon.setIcon(new ImageIcon(
						targetTemplate.getImage().getScaledInstance(400, 400, BufferedImage.TYPE_3BYTE_BGR)));
				templateImagePath.setText(targetTemplate.getPath());
			}

			if (targetTemplate.getMethod() == Imgproc.TM_CCOEFF_NORMED) {
				ccoeffButton.setSelected(true);
			} else if (targetTemplate.getMethod() == Imgproc.TM_SQDIFF_NORMED) {
				sqdiffButton.setSelected(true);
			} else if (targetTemplate.getMethod() == Imgproc.TM_CCORR_NORMED) {
				ccorrButton.setSelected(true);
			}

			acceptThreshold.setValue((int) (targetTemplate.getThreshold() * 100));
			acceptThresholdLabel.setText("Template Accept Threshold: " + acceptThreshold.getValue());
			

		} else {
			targetTemplateLabelIcon.setIcon(new ImageIcon(new BufferedImage(256, 256, BufferedImage.TYPE_3BYTE_BGR)));
			templateImagePath.setText("");
		}

	}

	public TemplateManager(StateConstructer constructer) {
		this.targetTemplate = null;
		this.constructer = constructer;
	}

	public void init() {
		JPanel templatePreview = new JPanel(new BorderLayout());
		JPanel settingsPanel = new JPanel(new GridLayout(11, 0));
		JPanel templatePathPanel = new JPanel(new BorderLayout());
		JPanel templateModePanel = new JPanel(new GridLayout(2, 3));
		JPanel templateThresholdPanel = new JPanel(new BorderLayout());
		setLayout(new BorderLayout());

		selectImagePath = new JButton("Select File");
		snippTemplate = new JButton("Snip");
		templateImagePath = new JTextField("No file selected");
		targetTemplateLabelIcon = new JLabel(new ImageIcon());

		templatePathPanel.add(snippTemplate, BorderLayout.LINE_START);
		templatePathPanel.add(selectImagePath, BorderLayout.LINE_END);
		templatePathPanel.add(templateImagePath, BorderLayout.CENTER);
		templatePathPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
		sqdiffButton = new JRadioButton();
		ccoeffButton = new JRadioButton();
		ccorrButton = new JRadioButton();

		ButtonGroup group = new ButtonGroup();
		group.add(sqdiffButton);
		group.add(ccoeffButton);
		group.add(ccorrButton);

		acceptThreshold = new JSlider(JSlider.HORIZONTAL, 0, 100, 97);

		acceptThresholdLabel = new JLabel("Template Accept Threshold: " + acceptThreshold.getValue());
		templateThresholdPanel.add(acceptThreshold, BorderLayout.CENTER);
		templateThresholdPanel.add(acceptThresholdLabel, BorderLayout.PAGE_START);
		templateThresholdPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

		templateModePanel.add(new JLabel("TM_SQDIFF_NORMED"));
		templateModePanel.add(new JLabel("TM_CCOEFF_NORMED"));
		templateModePanel.add(new JLabel("TM_CCORR_NORMED"));
		templateModePanel.add(sqdiffButton);
		templateModePanel.add(ccoeffButton);
		templateModePanel.add(ccorrButton);

		settingsPanel.add(templatePathPanel);

		settingsPanel.add(new JSeparator());

		settingsPanel.add(templateModePanel);
		settingsPanel.add(templateThresholdPanel);
		
		settingsPanel.add(constructer.getTestManager());
		
		targetTemplateLabelIcon.setBorder(BorderFactory.createLoweredBevelBorder());
		templatePreview.add(targetTemplateLabelIcon, BorderLayout.CENTER);
		templatePreview.add(settingsPanel, BorderLayout.PAGE_END);
		add(templatePreview, BorderLayout.CENTER);

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
					File f = chooser.getSelectedFile().getCanonicalFile();

	
					String path = f.getAbsolutePath().substring(new File(".").getCanonicalPath().toString().length()+1);
					BufferedImage templateImage = null;
					try {
						templateImage = ImageIO.read(chooser.getSelectedFile().getCanonicalFile());
					} catch (Exception ec) {
					}
					if (templateImage != null) {
						targetTemplate.setImage(templateImage);
						targetTemplateLabelIcon.setIcon(new ImageIcon(targetTemplate.getImage()));
					}
					targetTemplate.setPath(path);
					templateImagePath.setText(targetTemplate.getPath());
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
								File f = File.createTempFile("tmpImage","png");
								ImageIO.write(templateImage, "png", f);
								templateImage = ImageIO.read(f);
								if (templateImage == null)
									throw new IOException();
								targetTemplate.setPath("");
								targetTemplate.setImage(templateImage);
								targetTemplateLabelIcon.setIcon(new ImageIcon(templateImage));
								double score = constructer.getTestManager().performTest(SnippingTool.screenshot, targetTemplate).getScore();
								targetTemplate.setThreshold(score >= 0.97 ? 0.97 : score);
							} catch (Exception ec) {
								ec.printStackTrace();
							}
						} catch (Exception ec) {
							ec.printStackTrace();
						}
					}
				}.start();

			}
		});

		templateImagePath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BufferedImage templateImage = null;
				try {
					templateImage = ImageIO.read(new File(templateImagePath.getText()));
				} catch (Exception ec) {
				}
				if (templateImage != null) {
					targetTemplate.setImage(templateImage);
					targetTemplateLabelIcon.setIcon(new ImageIcon(templateImage));
				}
				targetTemplate.setPath(templateImagePath.getText());
			}
		});

		sqdiffButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sqdiffButton.isSelected())
					targetTemplate.setMethod(Imgproc.TM_SQDIFF_NORMED);
				if(SnippingTool.screenshot != null)
				    constructer.getTestManager().performTest(SnippingTool.screenshot, targetTemplate);

			}
		});
		ccoeffButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ccoeffButton.isSelected())
					targetTemplate.setMethod(Imgproc.TM_CCOEFF_NORMED);
				if(SnippingTool.screenshot != null)
				    constructer.getTestManager().performTest(SnippingTool.screenshot, targetTemplate);

			}
		});
		ccorrButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ccorrButton.isSelected())
					targetTemplate.setMethod(Imgproc.TM_CCORR_NORMED);
				if(SnippingTool.screenshot != null)
				    constructer.getTestManager().performTest(SnippingTool.screenshot, targetTemplate);

			}
		});

		acceptThreshold.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (targetTemplate != null) {
					targetTemplate.setThreshold((double) acceptThreshold.getValue() / 100.0);
					acceptThresholdLabel.setText("Template Accept Threshold: " + acceptThreshold.getValue());
				}
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
