package pl.edu.pw.wsd.agency.location;

import com.google.common.cache.Cache;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;

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
    protected final Cache<PhysicalAgentId, ViewEntity> pointsMap;


    public LocationMainPanel(Cache<PhysicalAgentId, ViewEntity> agentsLocation) {
        this.pointsMap = agentsLocation;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setPreferredSize(new Dimension(700, 700));

    }


    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Dimension size = getSize();

//        g.translate((int)size.getWidth()/2, (int)size.getHeight()/2);

        ConcurrentMap<PhysicalAgentId, ViewEntity> aidPointConcurrentMap = pointsMap.asMap();
        aidPointConcurrentMap.forEach((transmitterId, point) -> {
            final int x = mapX(size, point);
            final int y = mapY(size, point);
            final int signalRangeX = 2 * mapSignalRangeX(size, point.getSignalRange());
            final int signalRangeY = 2 * mapSignalRangeY(size, point.getSignalRange());


            // FIXME :: dirty hack
            Color pointColor = null;
            if (point.getSignalRange() <= 0) {
                pointColor = Color.BLUE;
            } else {
                pointColor = Color.RED;
            }
            String name = transmitterId.getLocalName();
            // print name
            g.drawString(name, x - 20, y - 5);

            // print dot
            g.setColor(pointColor);
            g.fillOval(x - 2, y - 2, 4, 4);

            // print signal range
            g.setColor(Color.BLACK);

            g.drawOval(x - signalRangeX / 2 - 2, y - signalRangeX / 2 - 2, signalRangeX, signalRangeY);

            // print stored messages
            int i = 0;
            for (MessageId messageId : point.getMessageIdList()) {
                g.drawString(messageId.prettyToString(), x + 10, y + (11 * i));
                i++;

            }
        });

    }

    private static final int GRID = 1000;


    public static int mapX(Dimension size, ViewEntity viewEntity2D) {
        Double x = viewEntity2D.getX() / GRID * size.getWidth() + size.getWidth() / 2;
        return x.intValue();
    }

    public static int mapY(Dimension size, ViewEntity viewEntity2D) {
        Double y = (-1) * viewEntity2D.getY() / GRID * size.getHeight() + size.getHeight() / 2;
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
