package pl.edu.pw.wsd.agency.agent;

import java.util.List;

import javafx.geometry.Point2D;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.Agent;

public class BaseAgent extends Agent {

    private static final long serialVersionUID = 851946783328690212L;

    private static final Logger log = LogManager.getLogger();
    
    /**
     * List of Agents in range of this Agent
     */
    private List<AID> agentsInRange;

    protected AbstractConfiguration CFG;

    private static final String COORDINATE_SEPARATOR = ";";
    
    // MoveBehaviour period
    protected int mbp;

    // agent x position coordinate
    protected double posX;

    // agent y position coordinate
    protected double posY;

    // agent moving speed
    private double speed;

    // agent direction
    private boolean ad;

    // target point index
    // index of point in the path agent is moving to
    private int tpi;

    // agent path
    // agent will move along this path
    private Point2D[] path;

    public double getX() {
        return posX;
    }

    public void setX(double posx) {
        this.posX = posx;
    }

    public double getY() {
        return posY;
    }

    public void setY(double posy) {
        this.posY = posy;
    }

    public Point2D getPosition() {
        return new Point2D(posX, posY);
    }

    @Override
    protected void setup() {
        log.info("Agent starting.");
        log.info("Loading configuration.");
        Object[] args = getArguments();
        if (args != null && args.length == 1) {
            String propertiesFileName = (String) args[0];
            try {
                loadConfiguration(propertiesFileName);
            } catch (ConfigurationException e) {
                log.info("Could not load configuration. Agent terminating");
                doDelete();
            }
        } else {
            log.info("No properties file passed to agent. Agent terminating.");
            doDelete();
        }

        log.info("Agent starting position: " + getPosition());
    }

    private void loadConfiguration(String propertiesFielName) throws ConfigurationException {
        CFG = new PropertiesConfiguration(propertiesFielName);
        // set MoveBehaviour tick period
        mbp = CFG.getInt("mbp");
        // set agent path
        List<Object> points = CFG.getList("path");
        path = loadPath(points);
        // set agent moving speed
        speed = CFG.getDouble("speed");
        // set agent path direction
        ad = CFG.getBoolean("ad");
        // set agent starting position
        int spi = CFG.getInt("spi");
        Point2D startingPoint = path[spi];
        setX(startingPoint.getX());
        setY(startingPoint.getY());
        // set current target point
        if (ad) {
            if (spi == path.length) {
                ad = false;
                tpi = spi - 1;
            } else {
                tpi = spi + 1;
            }
        } else {
            if (spi == 0) {
                ad = true;
                tpi = spi + 1;
            } else {
                tpi = spi - 1;
            }
        }
    }

    private Point2D[] loadPath(List<Object> points) {
        Point2D[] path = new Point2D[points.size()];
        for (int i = 0; i < points.size(); i++) {
            String s = (String) points.get(i);
            String[] s2 = s.split(COORDINATE_SEPARATOR);
            Point2D _point = new Point2D(Double.valueOf(s2[0]), Double.valueOf(s2[1]));
            path[i] = _point;
        }

        return path;
    }

    public Point2D[] getPath() {
        return path;
    }

    public Point2D getCurrentTarget() {
        return path[tpi];
    }

    public double getSpeed() {
        return speed;
    }

    public boolean getDirection() {
        return ad;
    }

    public void setDirection(boolean ad) {
        this.ad = ad;
    }

    public int getTargetPointNumber() {
        return tpi;
    }

    public void incrementTargetPointNumber() {
        ++tpi;
    }

    public void decrementTargetPointNumber() {
        --tpi;
    }

    public List<AID> getAgentsInRange() {
        return agentsInRange;
    }

    public void setAgentsInRange(List<AID> agentsInRange) {
        this.agentsInRange = agentsInRange;
    }

}
