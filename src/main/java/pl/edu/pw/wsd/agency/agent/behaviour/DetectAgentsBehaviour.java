package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.geometry.Point2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.BaseAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * Detects Agents that are in range.
 * 
 * @author Adrian Sidor
 *
 */
public class DetectAgentsBehaviour extends TickerBehaviour {

    public DetectAgentsBehaviour(Agent a, long period) {
        super(a, period);
    }

    private static final long serialVersionUID = 4463025011000946515L;

    private static final Logger log = LogManager.getLogger();

    private double range = 3;

    @Override
    public void onTick() {
        Configuration conf = Configuration.getInstance();
        Map<AID, Point2D> al = conf.getAgentsLocationWithout(myAgent.getAID());
        List<AID> agentsInRange = new ArrayList<AID>();
        for (Entry<AID, Point2D> entry : al.entrySet()) {
            Point2D location = entry.getValue();
            if (isInRange(location)) {
                agentsInRange.add(entry.getKey());
            }
        }
        BaseAgent agent = (BaseAgent) getAgent();
        agent.setAgentsInRange(agentsInRange);
        log.debug("Agents in range: " + agent.getAgentsInRange());
    }

    /**
     * Checks if two points are in range.
     * 
     * @param location
     * @return
     */
    private boolean isInRange(Point2D location) {
        BaseAgent agent = (BaseAgent) this.getAgent();
        Point2D agentPosition = agent.getPosition();
        double distance = agentPosition.distance(location);
        if (distance > range) {
            return false;
        } else {
            return true;
        }
    }

}
