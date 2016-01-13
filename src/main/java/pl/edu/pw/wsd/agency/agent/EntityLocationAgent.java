package pl.edu.pw.wsd.agency.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.geometry.Point2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.visualization.LocationFrame;
import pl.edu.pw.wsd.agency.config.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Store location of ALL entities in system ( transmitters and clients0
 *
 * @author <a href="mailto:adam.papros@gmail.com">Adam Papros</a>
 */
public class EntityLocationAgent extends Agent {

    private Map<AID, Point2D> entityLocationMap;

    private LocationFrame locationFrame;

    public static final String CONVERSATION_ID = "Entity-Location";
    public static final String SERVICE_TYPE = "EntityRegistry";
    public static final String SERVICE_NAME = "EntityLocationRegistry";

    private static final Logger log = LogManager.getLogger();

    @Override
    protected void setup() {

        entityLocationMap = new HashMap<>();
        locationFrame = new LocationFrame(entityLocationMap);

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
        addBehaviour(new EntitiesLocationServiceBehaviour());

    }


    public void updateEntityLocation(AID aid, Point2D location) {
        entityLocationMap.put(aid, location);
        locationFrame.updateAgentsLocations();
    }

    private class EntitiesLocationServiceBehaviour extends Behaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchConversationId(CONVERSATION_ID);
            log.debug("Czekam na wiadomosc.");
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                log.debug("Nowa wiadomość o lokalizacji");
                EntityLocationAgent agent = (EntityLocationAgent) getAgent();

                if (msg.getPerformative() == ACLMessage.INFORM) {
                    String content = msg.getContent();
                    ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
                    try {
                        Point2D position = mapper.readValue(content, Point2D.class);
                        AID sender = msg.getSender();
                        agent.updateEntityLocation(sender, position);
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
