package pl.edu.pw.wsd.agency.agent;


import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pw.wsd.agency.agent.behaviour.physical.PhysicalAgentBehaviour;
import pl.edu.pw.wsd.agency.common.PhysicalAgentId;
import pl.edu.pw.wsd.agency.config.MovingAgentConfiguration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.location.message.content.LocationRegistryData;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;
import pl.edu.pw.wsd.agency.message.content.AgentStatus.Location;

@Getter
@Setter
public abstract class PhysicalAgent extends BaseAgent {

	private static final long serialVersionUID = 851946783328690212L;

	private static final Logger log = LogManager.getLogger();

	/**
	 * List of messages identifiers that are held by agent (Client or transmitter)
	 */
	public abstract Set<MessageId> getStoredMessageId();

	// FIXME :: lepsza nazwa!
	private LocationRegistryData location;

	/**
	 * List of Agents in range of this Agent
	 */
	@Getter
	@Setter
	private Set<PhysicalAgentId> transmittersInRange = new HashSet<>();

	@Getter
	@Setter
	private Set<PhysicalAgentId> clientsInRange = new HashSet<>();

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

	// FIXME redundant to location.isClient
	private boolean isClient;

	private boolean sendAgentLocationToRegistry = true; //by default every physical agent sends it's location to the LocationRegistry

	/**
	 * Constructor
	 */
	protected PhysicalAgent(MovingAgentConfiguration config, boolean isClient) {
		super();
		loadConfiguration(config);
		this.isClient = isClient;
	}

	@Override
	protected void setup() {
		super.setup();
		log.info("Agent starting position: " + location);
		addBehaviour(new PhysicalAgentBehaviour(this, moveBehaviourPeriod, location.getIsClient()));

	}

	protected void loadConfiguration(MovingAgentConfiguration cfg) {
		moveBehaviourPeriod = cfg.getMoveBehaviourPeriod();
		path = cfg.getPath();
		speed = cfg.getSpeed();
		agentDirection = cfg.getAgentDirection();
		int spi = cfg.getStartingPositionIndex();
		Point2D startingPoint = path[spi];
		location = new LocationRegistryData(startingPoint.getX(), startingPoint.getY(), cfg.getSignalRange(), this.isClient);
		if(path.length > 1) {
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
		LocationRegistryData location = getLocation();
		status.setLocation(new Location(location.getX(), location.getY()));
		status.setSenderId(getLocalName());
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
		if (distance < speed && path.length > 1) {
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
