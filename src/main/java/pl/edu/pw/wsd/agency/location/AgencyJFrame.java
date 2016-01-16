package pl.edu.pw.wsd.agency.location;

import com.google.common.cache.Cache;
import pl.edu.pw.wsd.agency.common.TransmitterId;

import javax.swing.*;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class AgencyJFrame extends JFrame {

    private LocationMainPanel mainPanel;

    public void updateAgentsLocations() {
        mainPanel.validate();
        mainPanel.repaint();
    }


    public AgencyJFrame(Cache<TransmitterId, ViewEntity> agentsLocation) {
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
