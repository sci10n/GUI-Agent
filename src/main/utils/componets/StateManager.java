package main.utils.componets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.agent.state.State;
import main.agent.state.Template;

public class StateManager extends JPanel {

    
    private State state;
    private JLabel targetStateLabel;
    private Vector<JLabel> templateLabels;
    private JPanel templateLabelPanel;
    public void setTargetState(State targetState){
    	if(targetState == null){
    		setEnabled(false);
        	targetStateLabel.setText("No State Selected");
        	for(JLabel l: templateLabels){
        		templateLabelPanel.remove(l);
        	}
        	templateLabels.clear();
    	}
    	else{
    		setEnabled(true);
        	targetStateLabel.setText(targetState.getId());
        	for(JLabel l: templateLabels){
        		templateLabelPanel.remove(l);
        	}
        	templateLabels.clear();
        	if(!targetState.getTemplates().isEmpty()){
        		for(Template t: targetState.getTemplates()){
            		templateLabels.add(new JLabel(new ImageIcon(t.getImage().getScaledInstance(128, 128, BufferedImage.TYPE_3BYTE_BGR))));
            	}
            	for(JLabel l: templateLabels){
            		l.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
            		templateLabelPanel.add(l);
            	}
        	}	
        }
        
    	this.state = targetState;
    }
    public StateManager(){
    	templateLabels = new Vector<JLabel>();
    }
    
    public void init(){
    	setLayout(new BorderLayout());
    	targetStateLabel = new JLabel("No State Selected");
    	templateLabelPanel = new JPanel(new FlowLayout());
    	add(templateLabelPanel,BorderLayout.CENTER);
    	add(targetStateLabel,BorderLayout.PAGE_START);
    }
    
    public State getState(){
    	return state;
    }
    
}
