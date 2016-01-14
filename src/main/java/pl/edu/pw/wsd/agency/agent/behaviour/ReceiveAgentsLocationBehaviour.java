package pl.edu.pw.wsd.agency.agent.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.geometry.Point2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.MovingAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.AgentsLocationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Behaviour that receive massage from LocationRegistry Agent about all Agents Positions.
 * Based on that information checks what Agents are in range.
 *
 * @author Adrian Sidor
 */
public class ReceiveAgentsLocationBehaviour extends Behaviour {

    private static final long serialVersionUID = 4463025011000946515L;

    private static final Logger log = LogManager.getLogger();

    private MovingAgent movingAgent;

    public ReceiveAgentsLocationBehaviour(MovingAgent movingAgent) {
        super(movingAgent);
        this.movingAgent = movingAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId("Agents-Location");
        MovingAgent agent = (MovingAgent) getAgent();
        ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
        if (msg != null) {
            String content = msg.getContent();
            ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
            try {
                AgentsLocationMessage alm = mapper.readValue(content, AgentsLocationMessage.class);
                Map<AID, Point2D> al = alm.getAgentsLocation();
                List<AID> agentsInRange = new ArrayList<AID>();
                for (Entry<AID, Point2D> entry : al.entrySet()) {
                    Point2D location = entry.getValue();
                    if (isInRange(location)) {
                        agentsInRange.add(entry.getKey());
                    }
                }
                agent.setAgentsInRange(agentsInRange);
                log.debug("Agents in range: " + agent.getAgentsInRange());
            } catch (IOException e) {
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
        if (distance > movingAgent.getSignalRange()) {
            return false;
        } else {
            return true;
        }
    }

}
