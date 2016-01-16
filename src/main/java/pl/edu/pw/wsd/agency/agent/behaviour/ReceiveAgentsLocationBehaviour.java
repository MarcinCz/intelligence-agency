package pl.edu.pw.wsd.agency.agent.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.PhysicalAgent;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.PhysicalDeviceLocation;
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

    private PhysicalAgent physicalAgent;

    public ReceiveAgentsLocationBehaviour(PhysicalAgent physicalAgent) {
        super(physicalAgent);
        this.physicalAgent = physicalAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(LocationRegistryAgent.LOCATION_CONVERSATION_ID);
        PhysicalAgent agent = physicalAgent;
        ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
        if (msg != null) {
            try {
                String content = msg.getContent();
                ObjectMapper mapper = Configuration.getInstance().getObjectMapper();

                AgentsLocationMessage alm = mapper.readValue(content, AgentsLocationMessage.class);
                Map<TransmitterId, PhysicalDeviceLocation> al = alm.getAgentsLocation();

                List<TransmitterId> agentsInRange = new ArrayList<>();
                for (Entry<TransmitterId, PhysicalDeviceLocation> entry : al.entrySet()) {
                    PhysicalDeviceLocation location = entry.getValue();
                    if (amIInRange(location)) {
                        // create transmitter id with local nam
                        // FIXME
                        agentsInRange.add(new TransmitterId(entry.getKey().getLocalName()));
                    }
                }
                agent.setAgentsInRange(agentsInRange);
                if (log.isDebugEnabled()) {
                    log.debug(agentsInRange.size() + " agents in range: " + agentsInRange);
                }
            } catch (IOException e) {
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
    private boolean amIInRange(PhysicalDeviceLocation location) {
        double distance = physicalAgent.getLocation().distance(location);
        return distance <= location.getSignalRange();
    }

}
