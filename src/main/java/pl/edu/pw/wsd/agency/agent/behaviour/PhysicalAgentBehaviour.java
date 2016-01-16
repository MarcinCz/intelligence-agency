package pl.edu.pw.wsd.agency.agent.behaviour;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.LocationRegistryAgent;
import pl.edu.pw.wsd.agency.agent.PhysicalAgent;
import pl.edu.pw.wsd.agency.agent.ViewAgent;
import javafx.geometry.Point2D;
import pl.edu.pw.wsd.agency.agent.EntityLocationAgent;
import pl.edu.pw.wsd.agency.agent.MovingAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.PhysicalDeviceLocation;
import pl.edu.pw.wsd.agency.location.ViewEntity;

/**
 * Simulates Agents moving.
 *
 * @author Adrian Sidor
 */
public class PhysicalAgentBehaviour extends TickerBehaviour {
    @Getter
    private PhysicalAgent physicalAgent;

    public PhysicalAgentBehaviour(PhysicalAgent physicalAgent, long period, boolean save) {
        super(physicalAgent, period);
        Preconditions.checkNotNull(physicalAgent);
        this.physicalAgent = physicalAgent;
    }

    private static final long serialVersionUID = -8221711531932126745L;

    private static final Logger log = LogManager.getLogger();

    @Override
    protected void onTick() {
        PhysicalAgent agent = physicalAgent;
        updatePosition(agent);
        sendInfoToLocationRegistry(agent);

        sendInfoToEntityLocationRegistry(agent);
        log.trace("Agent moved:" + agent.getLocation());
        log.trace("Agent target: " + agent.getCurrentTarget());
    }

    /**
     * Updates Agents Position.
     * Agent is moving.
     *
     * @param agent
     */
    private void updatePosition(PhysicalAgent agent) {
        agent.updatePosition();
    }

    /**
     * Agent sends information to LocationRegistry Agent about its new Position.
     *
     * @param agent
     */
    private void sendInfoToLocationRegistry(PhysicalAgent agent) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(LocationRegistryAgent.SERVICE_TYPE);
        sd.setName(LocationRegistryAgent.SERVICE_NAME);
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
            msg.setConversationId(LocationRegistryAgent.LOCATION_CONVERSATION_ID);
            PhysicalDeviceLocation location = agent.getLocation();
            ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
            String content;
            try {
                content = mapper.writeValueAsString(location);
                msg.setContent(content);
                msg.addReceiver(locationRegistry);
                agent.send(msg);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendInfoToEntityLocationRegistry(PhysicalAgent agent) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(ViewAgent.SERVICE_TYPE);
        sd.setName(ViewAgent.SERVICE_NAME);
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
            msg.setConversationId(ViewAgent.CONVERSATION_ID);

            // create viewEntity
            ViewEntity viewEntity = new ViewEntity(agent.getLocation());
            viewEntity.setMessageIdList(agent.getStoredMessageId());

            ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
            String content;
            try {
                content = mapper.writeValueAsString(viewEntity);
                msg.setContent(content);
                msg.addReceiver(locationRegistry);
                agent.send(msg);
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


}
