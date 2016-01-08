package pl.edu.pw.wsd.agency.agent.behaviour;

import javafx.geometry.Point2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.MovingAgent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.edu.pw.wsd.agency.config.Configuration;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

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
            sendInfoToLocationRegistry(agent);
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
     * Agent sends information to LocationRegistry Agent about its new Position.
     * 
     * @param agent
     */
    private void sendInfoToLocationRegistry(MovingAgent agent) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Registry");
        sd.setName("LocationRegistry");
        template.addServices(sd);
        AID locationRegistry = null;
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            if (result.length == 1) {
                locationRegistry = result[0].getName();
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        if (locationRegistry != null) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setConversationId("Agents-Location");
            Point2D position = agent.getPosition();
            ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
            String content;
            try {
                content = mapper.writeValueAsString(position);
                msg.setContent(content);
                msg.addReceiver(locationRegistry);
                agent.send(msg);
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

    private double getDirection(MovingAgent agent) {
        Point2D target = agent.getCurrentTarget();
        double deltaX = target.getX() - agent.getX();
        double deltaY = target.getY() - agent.getY();

        return Math.atan2(deltaY, deltaX);
    }

}
