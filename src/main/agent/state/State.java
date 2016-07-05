package main.agent.state;

import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class State {
	private String id;
	private Vector<Template> templates;
	public State(String id,Vector<Template> templates) {
		this.id = id;
		this.templates = templates;
	}
	
	public static State fromXML(Node stateNode){
		if(stateNode.getNodeType() == Node.ELEMENT_NODE){
			Element e = (Element) stateNode;
			String id = e.getAttribute("name");
			Vector<Template> templates = new Vector<Template>();
			for(int i = 0; i < stateNode.getChildNodes().getLength(); i++){
				Node n = stateNode.getChildNodes().item(i);
				if(n.getNodeName().equals("template")){
					templates.add(Template.fromXML(n));
				}
			}
			return new State(id, templates);
		}
		return null;
	}
	public static Element toXML(State state, Document doc){
		Element e = doc.createElement("state");
		Attr id = doc.createAttribute("name");
		id.setValue(state.getId());
		e.setAttributeNode(id);
		for(Template t: state.getTemplates()){
			e.appendChild(Template.toXML(t,doc));
		}
		return e;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Vector<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(Vector<Template> templates) {
		this.templates = templates;
	}
	
	@Override
	public String toString() {
		
		return id;
	}


}
