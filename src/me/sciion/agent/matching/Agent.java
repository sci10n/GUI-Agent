package me.sciion.agent.matching;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Node;

import com.CMS.Si10n.Graphlib.Graph;

import me.sciion.agent.Run;
import me.sciion.agent.state.State;
import me.sciion.agent.state.Template;

public class Agent {

    private Matcher matcher;

    private Graph<State, String> stateSpace;

    public Agent() {

    }

    public void init() {
	matcher = new Matcher(Imgproc.TM_CCOEFF_NORMED);
	stateSpace = new Graph<State, String>();

    }

    // private int running = 0;
    private AtomicInteger timestamp = new AtomicInteger(0);
    private AtomicInteger running = new AtomicInteger(0);
    AtomicInteger templateCounter = new AtomicInteger(0);
    private Vector<State> current = new Vector<State>();
    private List<String> log = Collections.synchronizedList(new ArrayList<String>());
    public Vector<State> run() {
	long time = System.nanoTime();
	BufferedImage screenshot = Run.getScreenshot(BufferedImage.TYPE_3BYTE_BGR);
	Mat screen = Run.cvtMat(screenshot, CvType.CV_8UC3);
	current.clear();
	stateSpace.forEachNode((State s) -> {
	    
	    running.incrementAndGet();
	    AtomicInteger count = new AtomicInteger(0);
	    AtomicInteger total = new AtomicInteger(0);
	    for (Template t : s.getTemplates()) {
		Match m;
		Matcher matcher = new Matcher(t.getMethod());
		templateCounter.incrementAndGet();
		if (t.getImage() == null) {
		    templateCounter.decrementAndGet();
		    return s;
		}

		m = matcher.match(screen, t);
		if (m.score >= t.getThreshold()) {
		    count.incrementAndGet();
		}
		total.incrementAndGet();
		templateCounter.decrementAndGet();
		log.add(timestamp.get() + "," + m.getX() + "," + m.getY() + "," + m.getScore() + "," + t + "," + (m.score >= t.getThreshold() ? "found" : "not found"));
	    }
	    if ((double) count.get() / (double) total.get() >= 0.8) {
		current.add(s);

	    }
	    running.decrementAndGet();

	    return s;
	});
	double now = (System.nanoTime() - time) / 1000000000.0;
	timestamp.incrementAndGet();
	return current;
    }

    public void loadFromCML(Node root) {
	HashMap<String, State> states = new HashMap<String, State>();
	if (root.getNodeName().equals("task")) {
	    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
		Node n = root.getChildNodes().item(i);
		if (n.getNodeName().equals("state")) {
		    State s = State.fromXML(n);
		    states.put(s.getId(), s);
		    stateSpace.addNode(s);
		} else if (n.getNodeName().equals("action")) {
		    String state1 = n.getAttributes().getNamedItem("start").getNodeValue();
		    String state2 = n.getAttributes().getNamedItem("target").getNodeValue();
		    stateSpace.addEdge(states.get(state1), states.get(state2),
			    n.getAttributes().getNamedItem("action").getNodeValue());
		}
	    }
	}
    }

    public Graph<State, String> getStateSpace() {
	return stateSpace;
    }

    public void setStateSpace(Graph<State, String> stateSpace) {
	this.stateSpace = stateSpace;
    }
    public List<String> getLog(){
	return log;
    }
}
