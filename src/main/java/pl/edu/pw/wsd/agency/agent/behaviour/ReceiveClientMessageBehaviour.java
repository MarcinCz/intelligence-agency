package pl.edu.pw.wsd.agency.agent.behaviour;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.config.Configuration;
import pl.edu.pw.wsd.agency.message.content.ClientMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveClientMessageBehaviour extends Behaviour {

    private static final long serialVersionUID = -4355343485797591490L;

    private static final Logger log = LogManager.getLogger();

    private static final String LANGUAGE = "JSON";

    private static final int PERFORMATIVE = ACLMessage.PROPAGATE;

    private static final String CONVERSATION_ID = "client-message";

    @Override
    public void action() {
        MessageTemplate mt2 = MessageTemplate.and(
                MessageTemplate.MatchPerformative(PERFORMATIVE), 
                MessageTemplate.MatchLanguage(LANGUAGE));
        /*MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId(CONVERSATION_ID), 
                mt2);*/
        ACLMessage msg = myAgent.receive(mt2);
        if (msg != null) {
            ObjectMapper mapper = Configuration.getInstance().getObjectMapper();
            String content = msg.getContent();
            try {
                ClientMessage cm = mapper.readValue(content, ClientMessage.class);
                TransmitterAgent agent = (TransmitterAgent) myAgent;
                agent.addClientMessage(cm);
                log.info("Odebralem wiadomosc ClientMessage");
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

}
