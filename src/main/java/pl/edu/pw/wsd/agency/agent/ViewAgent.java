package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
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
import pl.edu.pw.wsd.agency.location.AgencyJFrame;
import pl.edu.pw.wsd.agency.location.ViewEntity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Store location of ALL entities in system ( transmitters and clients0
 *
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class ViewAgent extends Agent {

    private Cache<TransmitterId, ViewEntity> entityLocationCache;
    private AgencyJFrame agencyJFrame;

    public static final String CONVERSATION_ID = "Entity-Location";
    public static final String SERVICE_TYPE = "EntityRegistry";
    public static final String SERVICE_NAME = "EntityLocationRegistry";

    private static final Logger log = LogManager.getLogger();

    @Override
    protected void setup() {
        entityLocationCache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.SECONDS).removalListener(new RemovalListener<TransmitterId, ViewEntity>() {
            @Override
            public void onRemoval(RemovalNotification<TransmitterId, ViewEntity> removalNotification) {
                agencyJFrame.updateAgentsLocations();
            }
        }).build();

        agencyJFrame = new AgencyJFrame(entityLocationCache);

        // create agent description and service description
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
        addBehaviour(new RefreshingViewBehaviour(this));
    }

    public void updateEntityLocation(TransmitterId transmitterId, ViewEntity location) {
        entityLocationCache.put(transmitterId, location);
        agencyJFrame.updateAgentsLocations();
    }

    private class RefreshingViewBehaviour extends Behaviour {

        private final ViewAgent viewAgent;

        RefreshingViewBehaviour(ViewAgent viewAgent) {
            this.viewAgent = viewAgent;
        }

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchConversationId(CONVERSATION_ID);
            log.trace("Czekam na wiadomosc.");
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                log.trace("Nowa wiadomość o lokalizacji");
                if (msg.getPerformative() == ACLMessage.INFORM) {
                    String content = msg.getContent();
                    ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                    try {
                        ViewEntity viewEntity = mapper.readValue(content, ViewEntity.class);
                        AID sender = msg.getSender();
                        viewAgent.updateEntityLocation(new TransmitterId(sender), viewEntity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
