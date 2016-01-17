package pl.edu.pw.wsd.agency.agent.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.location.MessageId;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import java.io.IOException;

/**
 * User can send to Client messages which will be propagated by Client as Client message.
 * This behaviour reads that User messages.
 * This can be assumed as User input.
 *
 * @author Adrian Sidor
 */
public class UserInputMessageBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = -565817641845670437L;

    private static final Logger log = LogManager.getLogger();

    private static final String CONVERSATION_ID = "user-input";

    // for test purpose
    private boolean test = false;
    // for test purpose
    private long lastSentTime = System.currentTimeMillis();
    // for test purpose
    private long nextMessageId = 1;

    ClientAgent clientAgent;

    public UserInputMessageBehaviour(ClientAgent clientAgent) {
        this.clientAgent = clientAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(CONVERSATION_ID);
        log.trace("Czekam na wiadomosc od Uzytkownika.");
        ClientAgent agent = clientAgent;
        ACLMessage msg = agent.receiveAndUpdateStatistics(mt);

        ObjectMapper mapper = Configuration.getInstance().getObjectMapper();

        if (test && lastSentTime + 5000 < System.currentTimeMillis()) {
            if (msg == null) {
                try {
                    msg = new ACLMessage(ACLMessage.PROPAGATE);
                    ClientMessage testMessage = new ClientMessage(new MessageId(clientAgent.getLocalName(), Long.toString(nextMessageId++)), "client_2", "Hello WSD", System.currentTimeMillis() + 100 * 1000l);
                    msg.setContent(mapper.writeValueAsString(testMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (msg != null) {
            try {
                String content = msg.getContent();

                ClientMessage cm = mapper.readValue(content, ClientMessage.class);
                agent.queueClientMessage(cm);
                lastSentTime = System.currentTimeMillis();
                log.info("Odebralem wiadomosc od uzytkownika");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            block();
        }

    }

}
