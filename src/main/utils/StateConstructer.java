package main.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.spec.ECField;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.Run;
import main.SnippingTool;
import main.agent.Match;
import main.agent.Matcher;
import main.agent.state.State;
import main.agent.state.Template;
import main.utils.componets.TemplateManager;

@SuppressWarnings("serial")
public class StateConstructer extends JFrame {

    private State targetState = null;
    private JTree tree;
    private DefaultMutableTreeNode targetNode = null;
    private DefaultMutableTreeNode root;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    TemplateManager templateManager = new TemplateManager();

    public static void main(String[] args) {
	StateConstructer constructer = new StateConstructer();
	constructer.init();
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
	for (int i = startingIndex; i < rowCount; ++i) {
	    tree.expandRow(i);
	}

	if (tree.getRowCount() != rowCount) {
	    expandAllNodes(tree, rowCount, tree.getRowCount());
	}
    }

    private void loadFromXML(String path) {
	root.removeAllChildren();
	Document doc = Run.parseFromXML(path);
	if (doc == null)
	    JOptionPane.showMessageDialog(this, "Cant load file");
	Node taskNode = doc.getElementsByTagName("task").item(0);
	NodeList stateNodes = taskNode.getChildNodes();
	for (int i = 0; i < stateNodes.getLength(); i++) {
	    Node n = stateNodes.item(i);
	    if (n.getNodeType() != Node.ELEMENT_NODE)
		continue;
	    State s = State.fromXML(n);
	    DefaultMutableTreeNode stn = new DefaultMutableTreeNode(s);
	    for (Template t : s.getTemplates()) {
		DefaultMutableTreeNode ttn = new DefaultMutableTreeNode(t);
		stn.add(ttn);
	    }
	    root.add(stn);
	}
	expandAllNodes(tree, 0, tree.getRowCount());
    }

    private void saveToXML(String path) {
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder;
	try {
	    docBuilder = docFactory.newDocumentBuilder();
	    Document doc = docBuilder.newDocument();
	    Element rootElement = doc.createElement("task");
	    doc.appendChild(rootElement);
	    for (int i = 0; i < root.getChildCount(); i++) {
		DefaultMutableTreeNode n = (DefaultMutableTreeNode) root.getChildAt(i);
		if (n.getUserObject() instanceof State) {
		    State s = (State) n.getUserObject();
		    rootElement.appendChild(State.toXML(s, doc));
		}
	    }
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    DOMSource source = new DOMSource(doc);
	    StreamResult result = new StreamResult(new File(path));
	    transformer.transform(source, result);
	    System.out.println("File saved!");
	} catch (ParserConfigurationException | TransformerException e) {
	    e.printStackTrace();
	}

    }

    public void init() {

	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
		| UnsupportedLookAndFeelException e1) {
	    e1.printStackTrace();
	}
	
	templateManager.init();
	JPanel treePanel = new JPanel(new BorderLayout());

	root = new DefaultMutableTreeNode("States");

	DefaultTreeModel treeModel = new DefaultTreeModel(root);

	tree = new JTree(treeModel);
	tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.setShowsRootHandles(true);
	tree.addTreeSelectionListener(new TreeSelectionListener() {
	    @Override
	    public void valueChanged(TreeSelectionEvent e) {
		try {
		    DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
		    targetNode = dtn;
		    if (dtn.getUserObject().getClass().equals(State.class)) {
			State s = (State) dtn.getUserObject();
			System.out.println("Selceted a STATE");
		    } else if (dtn.getUserObject().getClass().equals(Template.class)) {
			templateManager.setTargetTemplate((Template) dtn.getUserObject());
			System.out.println("Selceted a TEMPLATE");

		    } else {
			templateManager.setTargetTemplate(null);
			targetState = null;
			System.out.println("Selceted root");

		    }

		} catch (ClassCastException ex) {
		}
	    }
	});
	treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);

	JPanel treeControlPanel = new JPanel(new GridLayout(0, 3));
	JButton add = new JButton("New");
	add.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (targetNode == null)
		    return;
		if (targetState != null) {
		    DefaultMutableTreeNode parentNode = targetNode;
		    State parent = (State) parentNode.getUserObject();
		    String name = JOptionPane.showInputDialog("Please type name");
		    if (name == null)
			name = "Undefined";
		    Template template = new Template(name, Imgproc.TM_CCOEFF_NORMED, 0.97, "", null);
		    parent.getTemplates().add(template);

		    treeModel.insertNodeInto(new DefaultMutableTreeNode(template), parentNode,
			    parentNode.getChildCount());

		} else if (templateManager.getTargetTemplate() != null) {
		    return;
		} else {
		    String name = JOptionPane.showInputDialog("Please type name");
		    if (name == null)
			name = "Undefined";
		    State s = new State(name, new Vector<Template>());

		    treeModel.insertNodeInto(new DefaultMutableTreeNode(s), targetNode, targetNode.getChildCount());

		}
	    }
	});
	JButton remove = new JButton("Remove");
	remove.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (targetNode == null)
		    return;
		if (targetNode.getParent() == null)
		    return;
		if (targetState != null) {
		    treeModel.removeNodeFromParent(targetNode);
		}

		else if (templateManager.getTargetTemplate() != null) {
		    treeModel.removeNodeFromParent(targetNode);
		}
	    }
	});
	treeControlPanel.add(add);
	treeControlPanel.add(remove);

	treePanel.add(treeControlPanel, BorderLayout.PAGE_END);
	add(treePanel, BorderLayout.LINE_START);
	add(templateManager, BorderLayout.CENTER);
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
		    loadFromXML(chooser.getSelectedFile().getAbsolutePath());
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
		    saveToXML(chooser.getSelectedFile().getAbsolutePath());
		} catch (Exception ec) {
		    ec.printStackTrace();
		}
	    }
	});

	fileMenu.add(importItem);
	fileMenu.add(exportItem);

	JMenu testMenu = new JMenu("Test");
	JMenuItem selectTestImage = new JMenuItem("Select Test Image");

	testMenu.add(selectTestImage);
	menuBar.add(testMenu);
	add(menuBar, BorderLayout.PAGE_START);
	
	setPreferredSize(new Dimension(900, 800));
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setTitle("State Constructer");
	pack();
	setVisible(true);
    }
}
