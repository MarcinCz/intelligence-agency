package pl.edu.pw.wsd.agency.visualization;

import jade.core.AID;
import javafx.geometry.Point2D;

import javax.swing.*;
import java.util.Map;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class LocationFrame extends JFrame {

    private LocationMainPanel jPanel;

    public void updateAgentsLocations() {
        jPanel.validate();
        jPanel.repaint();
    }


    public LocationFrame(Map<AID, Point2D> agentsLocation) {
        super("Agency");
        // tricky
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jPanel = new LocationMainPanel(agentsLocation);
        add(jPanel);
        pack();
        setVisible(true);

    }

}
