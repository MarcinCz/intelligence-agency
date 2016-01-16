package pl.edu.pw.wsd.agency.agent;


import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.config.MovingAgentConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.location.PhysicalDeviceLocation;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public abstract class PhysicalAgent extends BaseAgent {

    private static final long serialVersionUID = 851946783328690212L;

    private static final Logger log = LogManager.getLogger();

    /**
     * List of messages identifiers that are held by agent (Client or transmitter)
     */
    public abstract Set<MessageId> getStoredMessageId();

    private PhysicalDeviceLocation location;

    /**
     * List of Agents in range of this Agent
     */
    @Getter
    @Setter
    private List<TransmitterId> agentsInRange;

    /**
     * PhysicalAgentBehaviour period
     */
    protected int moveBehaviourPeriod;

    /**
     * agent moving speed
     */
    private double speed;

    /**
     * agent direction
     */
    private boolean agentDirection;

    /**
     * target point index
     * index of point in the path agent is moving to
     */
    private int targetPointIndex;

    /**
     * agent path
     * agent will move along this path
     */
    private Point2D[] path;

    /**
     * Constructor
     */
    public PhysicalAgent(String propertiesFileName) {
        super(propertiesFileName);
    }


    @Override
    protected void setup() {
        super.setup();
        log.info("Agent starting position: " + location);
    }

    @Override
    protected void loadConfiguration(String propertiesFileName) throws ConfigurationException {
        agentsInRange = new ArrayList<>();
        MovingAgentConfiguration cfg = configProvider.getMovingAgentConfiguration(propertiesFileName);
        moveBehaviourPeriod = cfg.getMoveBehaviourPeriod();
        path = cfg.getPath();
        speed = cfg.getSpeed();
        agentDirection = cfg.getAgentDirection();
        int spi = cfg.getStartingPositionIndex();
        Point2D startingPoint = cfg.getStartingPosition();
        location = new PhysicalDeviceLocation(startingPoint.getX(), startingPoint.getY(), cfg.getSignalRange());
        // set current target point
        if (agentDirection) {
            if (spi == path.length) {
                agentDirection = false;
                targetPointIndex = spi - 1;
            } else {
                targetPointIndex = spi + 1;
            }
        } else {
            if (spi == 0) {
                agentDirection = true;
                targetPointIndex = spi + 1;
            } else {
                targetPointIndex = spi - 1;
            }
        }


    }

    public Point2D getCurrentTarget() {
        return path[targetPointIndex];
    }

    public void incrementTargetPointNumber() {
        ++targetPointIndex;
    }

    public void decrementTargetPointNumber() {
        --targetPointIndex;
    }

    public AgentStatus getAgentStatus() {
        AgentStatus status = new AgentStatus();
        // fixme point2D
        status.setLocation(null);
        status.setSenderId(getAgentState().getName());
        status.setTimestamp(DateTime.now());
        status.setStatistics(getAgentStatistics());
        return status;
    }

    public void updatePosition() {
        double direction = getDirection();

        log.trace("Direction: " + direction);

        double speed = getSpeed();
        location.setX(location.getX() + (speed * Math.cos(direction)));
        location.setY(location.getY() + (speed * Math.sin(direction)));
        Point2D target = getCurrentTarget();
        double distance = target.distance(location.getX(), location.getY());
        log.trace("Distance: " + distance);
        if (distance < speed) {
            Point2D[] path = getPath();
            int targetPointIndex = getTargetPointIndex();
            if (agentDirection) {
                if (targetPointIndex == path.length - 1) {
                    decrementTargetPointNumber();
                    setAgentDirection(false);
                } else {
                    incrementTargetPointNumber();
                }
            } else {
                if (targetPointIndex == 0) {
                    incrementTargetPointNumber();
                    setAgentDirection(true);
                } else {
                    decrementTargetPointNumber();
                }
            }
            log.trace("Moving to next target: " + getCurrentTarget());
        }

    }

    private double getDirection() {
        Point2D target = getCurrentTarget();
        double deltaX = target.getX() - location.getX();
        double deltaY = target.getY() - location.getY();

        return Math.atan2(deltaY, deltaX);
    }
}