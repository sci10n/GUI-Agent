package main.agent;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Node;

import com.CMS.Si10n.Graphlib.Graph;

import main.Run;
import main.agent.state.State;
import main.agent.state.Template;
import main.utils.Grabber;

public class Agent {

	private Matcher matcher;

	private Graph<State, String> stateSpace;

	public Agent() {

	}

	public void init() {
		matcher = new Matcher(Imgproc.TM_CCOEFF_NORMED, 0.97);
		stateSpace = new Graph<State, String>();

	}

	// private int running = 0;
	private AtomicInteger running = new AtomicInteger(0);
	AtomicInteger templateCounter = new AtomicInteger(0);
	private Vector<State> current = new Vector<State>();

	public Vector<State> run() {
		long time = System.nanoTime();
		BufferedImage screenshot = Grabber.getScreenshot(BufferedImage.TYPE_3BYTE_BGR);
		Mat screen = Run.cvtMat(screenshot, CvType.CV_8SC3);
		synchronized (current) {
			current.clear();
		}

		stateSpace.forEachNode((State s) -> {
			new Thread() {
				public void run() {
					running.incrementAndGet();
					AtomicInteger count = new AtomicInteger(0);
					AtomicInteger total = new AtomicInteger(0);
					synchronized (s.getTemplates()) {
						for (Template t : s.getTemplates()) {
							new Thread() {
								public void run() {
									Matcher matcher = new Matcher(t.getMethod(), t.getThreshold());
									templateCounter.incrementAndGet();
									Mat mat = Run.cvtMat(t.getImage(),CvType.CV_8UC3);
									Match m = matcher.match(screen,mat);
									mat.release();
									if (m.score >= t.getThreshold()) {
										count.incrementAndGet();
									}
									total.incrementAndGet();
									templateCounter.decrementAndGet();
								}
							}.start();
						}
					}
					while (templateCounter.get() != 0) {
						//System.out.println(templateCounter.get());
					}
					if ((double) count.get() / (double) total.get() >= 0.8) {
						synchronized (current) {
							current.add(s);
						}

					}
					running.decrementAndGet();
				}
			}.start();
			while (running.get() != 0) {
				try {
					// System.out.println(running);
					// Thread.sleep(16);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return s;

		});
		double now = (System.nanoTime() - time) / 1000000000.0;
		System.out.println(now);
		synchronized (current) {

			return current;
		}
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
		System.out.println(stateSpace);
	}

}
