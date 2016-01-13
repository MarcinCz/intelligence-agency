package pl.edu.pw.wsd.agency.visualization;

import jade.core.AID;
import javafx.geometry.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
class LocationMainPanel extends JPanel {

    /**
     * Map of entities that holds its coordinates.
     */
    protected final Map<AID, Point2D> pointsMap;


    public LocationMainPanel(Map<AID, Point2D> agentsLocation) {
        this.pointsMap = agentsLocation;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setPreferredSize(new Dimension(500, 500));
    }


    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Dimension size = getSize();
        pointsMap.forEach((aid, point) -> {
            final int x = mapX(size, point);
            final int y = mapY(size, point);

            g.drawString(aid.getLocalName(), x - 50, y);
            g.drawOval(x, y, 10, 10);
        });

    }

    private static final int MAX_X = 1000;
    private static final int MAX_Y = 1000;

    public static int mapX(Dimension size, Point2D point2D) {
        Double x = point2D.getX() / MAX_X * size.getWidth() + size.getWidth() / 2;
        return x.intValue();
    }

    public static int mapY(Dimension size, Point2D point2D) {
        Double y = (-1) * point2D.getY() / MAX_Y * size.getHeight() + size.getHeight() / 2;
        return y.intValue();
    }
}
