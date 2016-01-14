package pl.edu.pw.wsd.agency.agent.behaviour;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.pw.wsd.agency.agent.ClientAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

/**
 * User can send to Client messages which will be propagated by Client as Client message.
 * This behaviour reads that User messages.
 * This can be assumed as User input.
 * @author Adrian Sidor
 *
 */
public class UserInputMessageBehaviour extends CyclicBehaviour{

    private static final long serialVersionUID = -565817641845670437L;

    private static final Logger log = LogManager.getLogger();
    
    private static final String CONVERSATION_ID = "user-input";
    
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(CONVERSATION_ID);
        log.trace("Czekam na wiadomosc od Uzytkownika.");
        ClientAgent agent = (ClientAgent)myAgent;
        ACLMessage msg = agent.receiveAndUpdateStatistics(mt);
        if (msg != null) {
            ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
            String content = msg.getContent();
            try {
                ClientMessage cm = mapper.readValue(content, ClientMessage.class);
                agent.queueClientMessage(cm);
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
