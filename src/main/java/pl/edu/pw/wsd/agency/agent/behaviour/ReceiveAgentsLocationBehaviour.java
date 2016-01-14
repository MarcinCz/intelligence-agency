package pl.edu.pw.wsd.agency.agent.behaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.agent.MovingAgent;

/**
 * Behaviour that receive massage from LocationRegistry Agent about all Agents Positions.
 * Based on that information checks what Agents are in range.
 * 
 * @author Adrian Sidor
 *
 */
public class ReceiveAgentsLocationBehaviour extends Behaviour {

    private static final long serialVersionUID = 4463025011000946515L;

    private static final Logger log = LogManager.getLogger();

    private double range = 3;

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId("Agents-Location");
        MovingAgent agent = (MovingAgent) getAgent();
        ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
        if (msg != null) {
            try {
                Map<AID, java.awt.geom.Point2D> al = (Map<AID, java.awt.geom.Point2D>) msg.getContentObject();
                List<AID> agentsInRange = new ArrayList<AID>();
                for (Entry<AID, java.awt.geom.Point2D> entry : al.entrySet()) {
                    java.awt.geom.Point2D value = entry.getValue();
					Point2D location = new Point2D(value.getX(), value.getY());
                    if (isInRange(location)) {
                        agentsInRange.add(entry.getKey());
                    }
                }
                agent.setAgentsInRange(agentsInRange);
                log.debug("Agents in range: " + agent.getAgentsInRange());
            } catch (UnreadableException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Checks if two points are in range.
     * 
     * @param location
     * @return
     */
    private boolean isInRange(Point2D location) {
        MovingAgent agent = (MovingAgent) this.getAgent();
        Point2D agentPosition = agent.getPosition();
        double distance = agentPosition.distance(location);
        if (distance > range) {
            return false;
        } else {
            return true;
        }
    }

}
