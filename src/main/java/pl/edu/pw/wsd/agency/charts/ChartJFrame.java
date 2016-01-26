package pl.edu.pw.wsd.agency.charts;
import com.google.common.cache.Cache;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.location.ViewEntity;

import javax.swing.*;

public class ChartJFrame extends JFrame{

	    private ChartMainPanel mainPanel;

	    public void updateAgentsLocations() {
	        mainPanel.validate();
	        mainPanel.repaint();
	    }


	    public ChartJFrame(Cache<PhysicalAgentId, ViewEntity> agentsLocation) {
	        super("Agency");
	        // tricky
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        mainPanel = new ChartMainPanel(agentsLocation);
	        add(mainPanel);
	        pack();
	        setVisible(true);
	        setResizable(false);

	    }

	}
