package main.utils.componets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.Run;
import main.agent.Agent;
import main.utils.StateConstructer;

public class AgentManager extends JPanel {

    private JLabel isRunningLabel;
    private JButton runAgent;
    private JTextArea agentOutput;
    private JTextField stateSpaceFile;
    public Agent agent;
    private boolean isRunning = false;
    public AgentManager() {
	agent = new Agent();
	agent.init();
    }

    public void init() {
	setLayout(new BorderLayout());
	isRunningLabel = new JLabel("Agent is not running");
	agentOutput = new JTextArea();
	agentOutput.setEditable(false);
	stateSpaceFile = new JTextField();
	runAgent = new JButton("Start");

	JPanel loadAndRunPanel = new JPanel(new FlowLayout());
	loadAndRunPanel.add(runAgent);
	loadAndRunPanel.add(stateSpaceFile);
	loadAndRunPanel.add(isRunningLabel);
	add(loadAndRunPanel, BorderLayout.PAGE_START);
	add(agentOutput, BorderLayout.CENTER);

	runAgent.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		isRunning = !isRunning;
		if (isRunning) {
		    isRunningLabel.setText("Agent is running...");
		    runAgent.setText("Stop");
		} else {
		    isRunningLabel.setText("Agent is not running...");

		    runAgent.setText("Start");
		}
	    }
	});
	new Thread() {
	    public void run() {
		while (true) {
		    
		    if (isRunning) {
			Vector<main.agent.state.State> result = agent.run();
			if (result.isEmpty()) {
			    agentOutput.setText("No States identified");
			} else {
			agentOutput.setText("Running...");
			    for (main.agent.state.State s : result) {
				agentOutput.append(s.toString());
			    }
			}
			try {
			    Thread.sleep(100);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    } else {
			try {
			    agentOutput.setText("Stopped...");
			    Thread.sleep(100);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    }
		}
	    };
	}.start();
    }
}
