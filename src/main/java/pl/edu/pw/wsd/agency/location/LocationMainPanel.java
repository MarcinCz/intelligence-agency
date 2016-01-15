package pl.edu.pw.wsd.agency.location;

import com.google.common.cache.Cache;
import jade.core.AID;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
class LocationMainPanel extends JPanel {

    /**
     * Map of entities that holds its coordinates.
     */
    protected final Cache<AID, Point> pointsMap;


    public LocationMainPanel(Cache<AID, Point> agentsLocation) {
        this.pointsMap = agentsLocation;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setPreferredSize(new Dimension(600, 600));

    }


    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Dimension size = getSize();

//        g.translate((int)size.getWidth()/2, (int)size.getHeight()/2);

        ConcurrentMap<AID, Point> aidPointConcurrentMap = pointsMap.asMap();
        aidPointConcurrentMap.forEach((aid, point) -> {
            final int x = mapX(size, point);
            final int y = mapY(size, point);
            final int signalRangeX = mapSignalRangeX(size, point.getSignalRange());
            final int signalRangeY = mapSignalRangeY(size, point.getSignalRange());


            // FIXME :: dirty hack
            String name = null;
            Color pointColor = null;
            if (point.getSignalRange() < 0) {
                pointColor = Color.BLUE;
                name = "Client";
            } else {
                pointColor = Color.RED;
                name = "Transmitter";
            }
            // print name
            g.drawString(name, x - 20, y - 5);

            // print dot
            g.setColor(pointColor);
            g.fillOval(x - 2, y - 2, 4, 4);

            // print signal range
            g.setColor(Color.BLACK);
            // FIXME :: scale signal RANGE!!!!
            g.drawOval(x - signalRangeX / 2 - 2, y - signalRangeX / 2 - 2, signalRangeX, signalRangeY);

            // print stored messages
            String join = StringUtils.join(point.getMessageIdList().toArray(), "\n");
            g.drawString(join, x + 10, y);
        });

    }

    private static final int GRID = 1000;


    public static int mapX(Dimension size, Point point2D) {
        Double x = point2D.getX() / GRID * size.getWidth() + size.getWidth() / 2;
        return x.intValue();
    }

    public static int mapY(Dimension size, Point point2D) {
        Double y = (-1) * point2D.getY() / GRID * size.getHeight() + size.getHeight() / 2;
        return y.intValue();
    }

    public static int mapSignalRangeX(Dimension size, Double signalRange) {
        Double mapped = signalRange / GRID * size.getWidth();
        return mapped.intValue();
    }

    public static int mapSignalRangeY(Dimension size, Double signalRange) {
        Double mapped = signalRange / GRID * size.getHeight();
        return mapped.intValue();
    }
}
