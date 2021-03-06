package me.sciion.agent.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import me.sciion.agent.Run;
import me.sciion.agent.state.State;
import me.sciion.agent.state.Template;

@SuppressWarnings("serial")
public class StateConstructer extends JPanel {

    private StateManager stateManager = new StateManager();
    private JTree tree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode targetNode = null;
    private DefaultMutableTreeNode root;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private JButton add;
    private JButton remove;
    private AgentManager agentManager;
    TemplateManager templateManager = new TemplateManager(this);
    public TestManager testManager = new TestManager();

    public StateConstructer(AgentManager agentManager) {
	this.agentManager = agentManager;
    }

    public TestManager getTestManager() {
	return testManager;
    }

    public void setTestManager(TestManager testManager) {
	this.testManager = testManager;
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
	for (int i = startingIndex; i < rowCount; ++i) {
	    tree.expandRow(i);
	}

	if (tree.getRowCount() != rowCount) {
	    expandAllNodes(tree, rowCount, tree.getRowCount());
	}
    }

    public void loadFromXML(String path) {

	for (int i = 0; i < root.getChildCount(); i++) {
	    DefaultMutableTreeNode dt = (DefaultMutableTreeNode) root.getChildAt(i);
	    dt.removeAllChildren();
	    dt.removeFromParent();
	}
	if (targetNode != null) {
	    targetNode.removeAllChildren();
	    targetNode.removeFromParent();
	}
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
	    agentManager.agent.getStateSpace().addNode(s);
	    DefaultMutableTreeNode stn = new DefaultMutableTreeNode(s);
	    for (Template t : s.getTemplates()) {
		DefaultMutableTreeNode ttn = new DefaultMutableTreeNode(t);
		stn.add(ttn);
	    }
	    root.add(stn);
	    treeModel.setRoot(root);
	    tree.setModel(treeModel);
	    expandAllNodes(tree, 0, tree.getRowCount());

	}
    }

    public void saveToXML(String path) {
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

    public void saveTemplateImages(String rootPath) {
	for (int i = 0; i < root.getChildCount(); i++) {
	    DefaultMutableTreeNode n = (DefaultMutableTreeNode) root.getChildAt(i);
	    if (n.getUserObject() instanceof State) {
		State s = (State) n.getUserObject();
		for (Template t : s.getTemplates()) {
		    try {
			System.out.println("Writing Template: " + rootPath + " " + t.getPath());
			if (t.getImage() == null)
			    System.out.println("No image!");
			else
			    ImageIO.write(t.getImage(), "png", new File(rootPath + t.getPath()));
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }

	}
    }

    public void init() {
	setLayout(new BorderLayout());
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
		| UnsupportedLookAndFeelException e1) {
	    e1.printStackTrace();
	}
	testManager.init();

	stateManager.init();
	templateManager.init();
	stateManager.setVisible(false);
	templateManager.setVisible(false);
	JPanel treePanel = new JPanel(new BorderLayout());

	root = new DefaultMutableTreeNode("States");
	treeModel = new DefaultTreeModel(root);
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
			stateManager.setTargetState(s);
			add.setText("New Template");
			add.setEnabled(true);
			remove.setText("Remove State");
			remove.setEnabled(true);
			templateManager.setVisible(false);
			stateManager.setVisible(true);
		    } else if (dtn.getUserObject().getClass().equals(Template.class)) {
			templateManager.setTargetTemplate((Template) dtn.getUserObject());
			add.setText("New");
			add.setEnabled(false);
			remove.setText("Remove Template");
			remove.setEnabled(true);
			templateManager.setVisible(true);
			stateManager.setVisible(false);

		    } else {
			templateManager.setTargetTemplate(null);
			stateManager.setTargetState(null);
			add.setText("New State");
			add.setEnabled(true);
			remove.setText("Delete");
			remove.setEnabled(false);
			templateManager.setVisible(false);
			stateManager.setVisible(false);
		    }
		    expandAllNodes(tree, 0, 100);
		    templateManager.revalidate();
		    stateManager.revalidate();

		} catch (ClassCastException ex) {
		}
	    }
	});
	treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);

	JPanel treeControlPanel = new JPanel(new GridLayout(0, 3));
	add = new JButton("New");
	add.setEnabled(false);
	add.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (targetNode == null)
		    return;
		if (stateManager.getState() != null) {
		    DefaultMutableTreeNode parentNode = targetNode;
		    State parent = (State) parentNode.getUserObject();
		    String name = JOptionPane.showInputDialog("Please type name");
		    if (name == null)
			name = "Undefined";
		    Template template = new Template(name, Imgproc.TM_CCOEFF_NORMED, 0.97, "", new BufferedImage(128,128,BufferedImage.TYPE_3BYTE_BGR));
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
		    agentManager.agent.getStateSpace().addNode(s);

		    treeModel.insertNodeInto(new DefaultMutableTreeNode(s), targetNode, targetNode.getChildCount());

		}
		expandAllNodes(tree, 0, 100);
	    }
	});
	remove = new JButton("Remove");
	remove.setEnabled(false);
	remove.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (targetNode == null)
		    return;
		if (targetNode.getParent() == null)
		    return;
		if (stateManager.getState() != null) {
		    treeModel.removeNodeFromParent(targetNode);
		    agentManager.agent.getStateSpace().deleteNode((stateManager.getState()));
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
	JPanel managerPanel = new JPanel(new FlowLayout());
	managerPanel.add(templateManager, BorderLayout.CENTER);
	managerPanel.add(stateManager, BorderLayout.CENTER);
	add(managerPanel);

    }
}
