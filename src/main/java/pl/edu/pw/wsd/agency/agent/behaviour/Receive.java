package pl.edu.pw.wsd.agency.agent.behaviour;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pw.wsd.agency.agent.TransmitterAgent;
import pl.edu.pw.wsd.agency.message.content.PropagateMyMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class Receive extends Behaviour{

    private static final long serialVersionUID = 4463025011000946515L;
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean done = false;
    @Override
    public void action() {
        LOGGER.info("Czekam na wiadomosc.");
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
          LOGGER.info("Otrzymalem wiadomosc.");
          String content = msg.getContent();
          ObjectMapper mapper = new ObjectMapper();
          try {
            PropagateMyMessage pmm = mapper.readValue(content, PropagateMyMessage.class);
            TransmitterAgent agent = (TransmitterAgent)getAgent();
            agent.addPropagateMyMessage(pmm);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          done = false;
        }
        else {
          block();
        }
    }

    @Override
    public boolean done() {
        // TODO Auto-generated method stub
        return done;
    }

}
