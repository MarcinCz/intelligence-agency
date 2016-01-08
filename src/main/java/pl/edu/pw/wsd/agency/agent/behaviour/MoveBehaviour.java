package pl.edu.pw.wsd.agency.agent.behaviour;

import javafx.geometry.Point2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.MovingAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * Simulates Agents moving.
 * 
 * @author Adrian Sidor
 *
 */
public class MoveBehaviour extends TickerBehaviour {

    public MoveBehaviour(Agent a, long period, boolean save) {
        super(a, period);
        this.save = save;
    }

    private static final long serialVersionUID = -8221711531932126745L;

    private static final Logger log = LogManager.getLogger();

    private boolean save;

    @Override
    protected void onTick() {
        MovingAgent agent = (MovingAgent) getAgent();
        updatePosition(agent);
        if (save) {
            savePosition(agent);
        }
        log.debug("Agent moved:" + agent.getPosition());
        log.debug("Agent target: " + agent.getCurrentTarget());
    }

    /**
     * Updates Agents Position.
     * Agent is moving.
     * 
     * @param agent
     */
    private void updatePosition(MovingAgent agent) {
        double direction = getDirection(agent);
        log.debug("Direction: " + direction);
        double speed = agent.getSpeed();
        agent.setX(agent.getX() + (speed * Math.cos(direction)));
        agent.setY(agent.getY() + (speed * Math.sin(direction)));
        Point2D target = agent.getCurrentTarget();
        double distance = target.distance(agent.getPosition());
        log.debug("Distance: " + distance);
        if (distance < speed) {
            Point2D[] path = agent.getPath();
            int tpi = agent.getTargetPointNumber();
            if (agent.getDirection()) {
                if (tpi == path.length - 1) {
                    agent.decrementTargetPointNumber();
                    agent.setDirection(false);
                } else {
                    agent.incrementTargetPointNumber();
                }
            } else {
                if (tpi == 0) {
                    agent.incrementTargetPointNumber();
                    agent.setDirection(true);
                } else {
                    agent.decrementTargetPointNumber();
                }
            }
            log.debug("Moving to next target: " + agent.getCurrentTarget());
        }
    }

    /**
     * Agent updates its position in Configuration.
     * 
     * @param agent
     */
    private void savePosition(MovingAgent agent) {
        Configuration conf = Configuration.getInstance();
        conf.updateAgentLocation(agent.getAID(), agent.getPosition());
    }

    private double getDirection(MovingAgent agent) {
        Point2D target = agent.getCurrentTarget();
        double deltaX = target.getX() - agent.getX();
        double deltaY = target.getY() - agent.getY();

        return Math.atan2(deltaY, deltaX);
    }

}
