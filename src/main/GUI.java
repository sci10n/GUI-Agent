package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import main.utils.StateConstructer;
import main.utils.componets.AgentManager;

public class GUI extends JFrame {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private JTabbedPane tabbs;

    public static void main(String[] args) {
	GUI gui = new GUI();
	gui.init();
    }

    public void init() {
	tabbs = new JTabbedPane();
	AgentManager agent = new AgentManager();
	StateConstructer constructer = new StateConstructer(agent);
	
	constructer.init();
	agent.init();

	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	fileMenu.getAccessibleContext().setAccessibleDescription("Import and export from file");
	menuBar.add(fileMenu);

	JMenuItem importItem = new JMenuItem("Import");
	importItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);
		chooser.showOpenDialog(null);
		try {
		    if (chooser.getSelectedFile() == null)
			return;

		    constructer.loadFromXML(chooser.getSelectedFile().getCanonicalPath());
		} catch (Exception ec) {
		    ec.printStackTrace();
		}
	    }
	});
	JMenuItem exportItem = new JMenuItem("Export");
	exportItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);
		chooser.showOpenDialog(null);
		try {
		    if (chooser.getSelectedFile() == null)
			return;
		    constructer.saveToXML(chooser.getSelectedFile().getCanonicalPath());
		    constructer.saveTemplateImages("");
		} catch (Exception ec) {
		    ec.printStackTrace();
		}
	    }
	});

	fileMenu.add(importItem);
	fileMenu.add(exportItem);

	JMenu testMenu = new JMenu("Test");
	JMenuItem selectTestImage = new JMenuItem("Select Test Image");

	selectTestImage.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);
		chooser.showOpenDialog(null);
		try {
		    if (chooser.getSelectedFile() == null)
			return;
		    constructer.testManager.setTestImage(ImageIO.read(chooser.getSelectedFile().getCanonicalFile()));
		} catch (Exception ec) {
		    ec.printStackTrace();
		}
	    }
	});

	testMenu.add(selectTestImage);
	menuBar.add(testMenu);
	add(menuBar, BorderLayout.PAGE_START);

	tabbs.add("States", constructer);
	tabbs.addTab("Agent", agent);
	add(tabbs);
	setSize(WIDTH, HEIGHT);
	setVisible(true);
	setResizable(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
