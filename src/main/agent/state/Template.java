package main.agent.state;
import java.util.HashMap;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import main.Run;
import main.utils.Grabber;
public class Template {

	private String id;
	private int method;
	private double threshold;
	private Mat image;
	private String path;
	
	
	private static HashMap<String,Integer> methodTranslation;
	private static HashMap<Integer,String> methodInvTranslation;

	static {
		methodTranslation = new HashMap<String,Integer>();
		methodTranslation.put("TM_CCOEFF_NORMED", Imgproc.TM_CCOEFF_NORMED);
		methodTranslation.put("TM_CCORR_NORMED", Imgproc.TM_CCORR_NORMED);
		methodTranslation.put("TM_SQDIFF_NORMED", Imgproc.TM_SQDIFF_NORMED);
		methodInvTranslation = new HashMap<Integer,String>();
		methodInvTranslation.put(Imgproc.TM_CCOEFF_NORMED,"TM_CCOEFF_NORMED");
		methodInvTranslation.put(Imgproc.TM_CCORR_NORMED,"TM_CCORR_NORMED");
		methodInvTranslation.put(Imgproc.TM_SQDIFF_NORMED,"TM_SQDIFF_NORMED");
	}
	public Template(String id, int method, double threshold,String path, Mat image) {
		super();
		this.id = id;
		this.method = method;
		this.threshold = threshold;
		this.image = image;
		this.path = path;


	}
	
	public static Template fromXML(Node node){
		if(node.getNodeType() == Node.ELEMENT_NODE){
			Element e = (Element) node;
			String id  = e.getAttribute("id");
			int method = methodTranslation.get(e.getAttribute("method"));
			double threshold = Double.parseDouble(e.getAttribute("threshold"));
			String path = e.getAttribute("path");
			Mat image = null;
			if(!path.isEmpty())
				image = Run.cvtMat(Grabber.getImageResource(path), CvType.CV_8SC3);
			Template t = new Template(id, method, threshold,path, image);
			return t;
		}
		return null;
	}
	
	public static Element toXML(Template template, Document doc){
		Element e = doc.createElement("template");
		Attr id = doc.createAttribute("id");
		Attr method = doc.createAttribute("method");
		Attr threhold = doc.createAttribute("threshold");
		Attr path = doc.createAttribute("path");
		id.setValue(template.getId());
		method.setValue(methodInvTranslation.get(template.getMethod()));
		threhold.setValue(Double.toString(template.getThreshold()));
		path.setValue(template.getPath());
		e.setAttributeNode(id);
		e.setAttributeNode(method);
		e.setAttributeNode(threhold);
		e.setAttributeNode(path);
		return e;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getMethod() {
		return method;
	}
	public void setMethod(int method) {
		this.method = method;
	}
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public Mat getImage() {
		return image;
	}
	public void setImage(Mat image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		
		return id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
