package pl.edu.pw.wsd.agency.location;

import com.google.common.cache.Cache;
import jade.core.AID;

import javax.swing.*;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class LocationFrame extends JFrame {

    private LocationMainPanel mainPanel;

    public void updateAgentsLocations() {
        mainPanel.validate();
        mainPanel.repaint();
    }


    public LocationFrame(Cache<AID, Point> agentsLocation) {
        super("Agency");
        // tricky
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainPanel = new LocationMainPanel(agentsLocation);
        add(mainPanel);
        pack();
        setVisible(true);
        setResizable(false);

    }

}
