package pl.edu.pw.wsd.agency.location;

import com.google.common.cache.Cache;
import jade.core.AID;

import javax.swing.*;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class LocationFrame extends JFrame {

    private LocationMainPanel jPanel;

    public void updateAgentsLocations() {
        jPanel.validate();
        jPanel.repaint();
    }


    public LocationFrame(Cache<AID, Point> agentsLocation) {
        super("Agency");
        // tricky
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jPanel = new LocationMainPanel(agentsLocation);
        add(jPanel);
        pack();
        setVisible(true);

    }

}
