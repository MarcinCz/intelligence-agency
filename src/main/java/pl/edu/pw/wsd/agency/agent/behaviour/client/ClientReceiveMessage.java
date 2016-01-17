package pl.edu.pw.wsd.agency.agent.behaviour.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.io.IOException;

/**
 * @author apapros
 */
public class ClientReceiveMessage extends TickerBehaviour {

    private static final long serialVersionUID = -4865095272921712993L;

    private static final Logger log = LogManager.getLogger();

    private ClientAgent clientAgent;

    private final ObjectMapper objectMapper = Configuration.getInstance().getObjectMapper();

    public ClientReceiveMessage(ClientAgent clientAgent, long period) {
        super(clientAgent, period);
        this.clientAgent = clientAgent;
    }


    @Override
    public void onTick() {
        MessageTemplate tm = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE), MessageTemplate.MatchReceiver(new AID[]{clientAgent.getAID()}));
        ACLMessage receivedMessage = clientAgent.receive(tm);

        if (receivedMessage != null) {
            try {
                ClientMessage clientMessage = objectMapper.readValue(receivedMessage.getContent(), ClientMessage.class);
                log.info("Received message, {}", clientMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            block();
        }
    }
}