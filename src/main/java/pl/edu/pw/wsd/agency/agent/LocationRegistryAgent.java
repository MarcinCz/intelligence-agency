package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.common.TransmitterId;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.PhysicalDeviceLocation;
import pl.edu.pw.wsd.agency.message.content.AgentsLocationMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LocationRegistryAgent extends Agent {

    public static final String SERVICE_TYPE = "Registry";
    public static final String SERVICE_NAME = "LocationRegistry";

    public static final String LOCATION_CONVERSATION_ID = "Agents-Location";

    private static final long serialVersionUID = 6818961843348892572L;

    private static final Logger log = LogManager.getLogger();

    private Cache<TransmitterId, PhysicalDeviceLocation> agentsLocation;

    @Override
    protected void setup() {
        this.agentsLocation = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.SECONDS).build();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(SERVICE_TYPE);
        sd.setName(SERVICE_NAME);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
            log.error("Could not register agent. Agent terminating");
            doDelete();
        }

        addBehaviour(new AgentsLocationServiceBehaviour(this));
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Returns Agents Location information without information about Agent it ask for.
     * Agent doesn't need information about its  own location.
     *
     * @param aid
     * @return
     */
    public Map<TransmitterId, PhysicalDeviceLocation> getAgentsLocationWithout(AID aid) {
        Map<TransmitterId, PhysicalDeviceLocation> agentsLocation = new HashMap<>();
        agentsLocation.putAll(this.agentsLocation.asMap());
        agentsLocation.remove(aid);

        return agentsLocation;
    }

    public void updateAgentLocation(TransmitterId aid, PhysicalDeviceLocation location) {
        agentsLocation.put(aid, location);
    }


    public static class AgentsLocationServiceBehaviour extends Behaviour {

        private static final long serialVersionUID = 6320047876997878496L;

        private static final Logger log = LogManager.getLogger();

        private final LocationRegistryAgent locationRegistryAgent;

        AgentsLocationServiceBehaviour(LocationRegistryAgent agent) {
            this.locationRegistryAgent = agent;
        }

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchConversationId(LOCATION_CONVERSATION_ID);
            log.trace("Czekam na wiadomosc");
            ACLMessage msg = myAgent.receive(mt);
            if (msg == null) {
                block();
            } else {

                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    ACLMessage reply = msg.createReply();
                    ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                    AgentsLocationMessage alm = new AgentsLocationMessage(locationRegistryAgent.getAgentsLocationWithout(msg.getSender()));
                    try {
                        String content = mapper.writeValueAsString(alm);
                        reply.setContent(content);

                    } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    locationRegistryAgent.send(reply);
                } else if (msg.getPerformative() == ACLMessage.INFORM) {
                    String content = msg.getContent();
                    ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                    try {
                        PhysicalDeviceLocation position = mapper.readValue(content, PhysicalDeviceLocation.class);
                        AID sender = msg.getSender();
                        locationRegistryAgent.updateAgentLocation(new TransmitterId(sender), position);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public boolean done() {
            return false;
        }

    }
}
