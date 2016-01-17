package pl.edu.pw.wsd.agency.agent;


import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import jade.core.AID;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.config.MovingAgentConfiguration;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

public class MovingAgent extends BaseAgent {

	private static final long serialVersionUID = 851946783328690212L;
    private static final Logger log = LogManager.getLogger();

    /**
     * List of Agents in range of this Agent
     */
    private List<AID> agentsInRange = new ArrayList<AID>();;

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
    
    public MovingAgent(MovingAgentConfiguration config) {
		super();
		loadConfiguration(config);
	}
    
    @Override
    protected void setup() {
        super.setup();
        log.info("Agent starting position: " + getPosition());
    }
    
    protected void loadConfiguration(MovingAgentConfiguration cfg) {
        mbp = cfg.getMoveBehaviourPeriod();
        path = cfg.getPath();
        speed = cfg.getSpeed();
        ad = cfg.getAgentDirection();
        int spi = cfg.getStartingPositionIndex();
        Point2D startingPoint = cfg.getStartingPosition();
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
    
    public AgentStatus getAgentStatus() {
    	AgentStatus status = new AgentStatus();
    	status.setLocation(getPosition());
    	status.setSenderId(getLocalName());
    	status.setTimestamp(DateTime.now());
    	status.setStatistics(getAgentStatistics());
    	return status;
    }

}
